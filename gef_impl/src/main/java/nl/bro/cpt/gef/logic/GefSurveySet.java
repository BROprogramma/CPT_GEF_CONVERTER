/**
 *
 * Copyright 2012-2017 TNO Geologische Dienst Nederland
 *
 * Licensed under the EUPL, Version 1.2 or - as soon they will be approved by
 * the European Commission - subsequent versions of the EUPL (the "Licence");
 * You may not use this work except in compliance with the Licence.
 * You may obtain a copy of the Licence at:
 *
 * https://joinup.ec.europa.eu/software/page/eupl
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the Licence is distributed on an "AS IS" basis,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the Licence for the specific language governing permissions and
 * limitations under the Licence.
 *
 * This work was sponsored by the Dutch Rijksoverheid, Basisregistratie
 * Ondergrond (BRO) Programme (https://bro.pleio.nl/)
 */
package nl.bro.cpt.gef.logic;

import static nl.bro.cpt.gef.transform.MappingConstants.IMBRO;
import static nl.bro.cpt.gef.transform.MappingConstants.IMBROA;
import static nl.bro.cpt.gef.transform.MappingConstants.TRANSACTION_TYPE_CORRECTION;
import static nl.bro.cpt.gef.transform.MappingConstants.TRANSACTION_TYPE_REGISTRATION;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;
import java.util.Set;

import javax.validation.ConstraintViolation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;
import javax.validation.groups.Default;

import org.apache.log4j.Logger;

import nl.bro.cpt.gef.dto.GefCptSurvey;
import nl.bro.cpt.gef.dto.GefFile;
import nl.bro.cpt.gef.dto.IfcCptFile;
import nl.bro.cpt.gef.dto.IfcDisFile;
import nl.bro.cpt.gef.dto.IfcGefFile;
import nl.bro.cpt.gef.transform.GefToBroMapperCommon;
import nl.bro.dto.gef.groups.Correction;
import nl.bro.dto.gef.groups.Imbro;
import nl.bro.dto.gef.groups.ImbroA;
import nl.bro.dto.gef.groups.Registration;
import nl.bro.dto.gef.validation.support.GefValidation;

/**
 * @author derksenjpam
 */
public class GefSurveySet implements Serializable {

    private static final long serialVersionUID = -8979765909214941732L;
    private static final Logger LOG = Logger.getLogger( GefSurveySet.class );
    private static final ResourceBundle RB = ResourceBundle.getBundle( "SurveySetMessages" );

    private final List<IfcCptFile> cptFiles = new ArrayList<>();
    private final Map<String, List<IfcDisFile>> dissFiles = new HashMap<>();

    private List<GefCptSurvey> cptSuveys = new ArrayList<>();

    public GefSurveySet() {
    }

    public void addFile(GefFile file) {

        // Voeg GEF file toe aan te groeperen lijst:
        if ( file != null ) {

            switch ( file.getFileType() ) {
                case DISS:
                    IfcDisFile disFile = (IfcDisFile) file;
                    addDisFileToGroup( disFile );
                    break;
                case CPT:
                    IfcCptFile cptFile = (IfcCptFile) file;
                    cptFiles.add( cptFile );
                    break;
                case UNKNOWN: // fallthrough intended!
                default:
                    String message = String.format( "Unknown file type for uploaded file: %s", file.getFileName() );
                    LOG.error( message );
                    throw new IllegalStateException( message );
            }
        }
    }

    public boolean deleteFile(String name) {

        return deleteCptFile( name ) || deleteDissFile( name );
    }

    public void clear() {
        cptFiles.clear();
        dissFiles.clear();
        cptSuveys.clear();
    }

    private boolean deleteCptFile(String name) {

        int index = 0;
        boolean found = false;
        for ( ; index < cptFiles.size() && !found; index++ ) {
            if ( cptFiles.get( index ).getFileName().equals( name ) ) {
                found = true;
            }
        }
        if ( found ) {
            cptFiles.remove( index );
        }
        return found;
    }

    private boolean deleteDissFile(String name) {

        int index = 0;
        String key = "";
        for ( Map.Entry<String, List<IfcDisFile>> entry : dissFiles.entrySet() ) {
            index = 0;
            for ( ; index < entry.getValue().size() && key.isEmpty(); index++ ) {
                if ( entry.getValue().get( index ).getFileName().equals( name ) ) {
                    key = entry.getKey();
                }
            }
        }
        if ( !key.isEmpty() ) {
            List<IfcDisFile> dissFilesForCpt = dissFiles.get( key );
            dissFilesForCpt.remove( index );
            if ( dissFilesForCpt.isEmpty() ) {
                dissFiles.remove( key );
            }
        }

        return !key.isEmpty();
    }

    private void addDisFileToGroup(IfcDisFile disFile) {
        List<IfcDisFile> groupedDisFiles = dissFiles.get( disFile.getParentFileName() );
        if ( groupedDisFiles == null ) {
            groupedDisFiles = new ArrayList<>();
        }
        groupedDisFiles.add( disFile );
        dissFiles.put( disFile.getParentFileName(), groupedDisFiles );
    }

    public List<GefCptSurvey> getGefSurveyList(String qualityRegime, String transactionType) {

        if ( cptSuveys.isEmpty() ) {
            cptSuveys.addAll( constructGefSurveys( qualityRegime, transactionType ) );
            cptSuveys.addAll( getOrphanedDissFiles( qualityRegime ) );
        }

        return cptSuveys;
    }

    public static void validateContent(List<GefCptSurvey> surveyList, String transactionType) {

        // controle 11
        for ( GefCptSurvey survey : surveyList ) {

            // valideer alleen op inhoud, als er geen fouten anderszins zijn opgetreden.
            if ( isValid( survey ) ) {
                String qualityRegime = survey.getQualityRegime();
                validate( survey.getGefCptFile(), transactionType, qualityRegime );
                for ( IfcDisFile disFile : survey.getGefDisFiles() ) {
                    // set validation data.
                    disFile.setCptStartDate( survey.getGefCptFile().getStartDate() );
                    disFile.setCptStartTime( survey.getGefCptFile().getStartTime() );
                    validate( disFile, transactionType, qualityRegime );
                }
            }
        }
    }

    private List<GefCptSurvey> constructGefSurveys(String qualityRegime, String transactionType) {
        List<GefCptSurvey> surveyList = new ArrayList<>();

        // Stel de surveys samen uit een parent CPT GEF en 0 of meer DISS GEF bestanden:
        for ( IfcCptFile parentCpt : cptFiles ) {

            GefCptSurvey gefCptSurvey = new GefCptSurvey();
            gefCptSurvey.setCptFileName( parentCpt.getFileName() );
            gefCptSurvey.setQualityRegime( qualityRegime );
            gefCptSurvey.setGefDisFiles( dissFiles.get( parentCpt.getFileName() ) );
            dissFiles.remove( parentCpt.getFileName() );
            gefCptSurvey.setGefCptFile( parentCpt );
            if ( TRANSACTION_TYPE_REGISTRATION.equals( transactionType ) ) {
                gefCptSurvey.setObjectName( GefToBroMapperCommon.getInstance().getObjectIdAccountableParty( parentCpt ) );
            }
            else if ( TRANSACTION_TYPE_CORRECTION.equals( transactionType ) ) {
                gefCptSurvey.setObjectName( parentCpt.getTestId() );
            }
            surveyList.add( gefCptSurvey );

        }
        return surveyList;
    }

    private List<GefCptSurvey> getOrphanedDissFiles(String qualityRegime) {
        // De lijst met dissipatie files zou nu leeg moeten zijn, alle dissipatie files zijn gekoppeld aan een CPT file.
        // Wanneer dat niet zo is: foutmelding aanmaken!

        List<GefCptSurvey> orphanedDissFiles = new ArrayList<>();

        if ( !dissFiles.isEmpty() ) {
            for ( Map.Entry<String, List<IfcDisFile>> entry : dissFiles.entrySet() ) {

                GefCptSurvey dummyCptSurvey = new GefCptSurvey();
                dummyCptSurvey.setCptFileName( entry.getKey() );
                dummyCptSurvey.setQualityRegime( qualityRegime );
                dummyCptSurvey.setOrphanaged( true );
                dummyCptSurvey.getGefDisFiles().addAll( entry.getValue() );
                orphanedDissFiles.add( dummyCptSurvey );
            }
        }

        return orphanedDissFiles;
    }

    public static void validateConsistency(List<GefCptSurvey> surveyList) {

        // controleer de GEF file groepering:
        for ( GefCptSurvey survey : surveyList ) {

            if ( survey.isOrphanaged() ) {
                // controle 9 (ex 10a)
                for ( IfcDisFile disFile : survey.getGefDisFiles() ) {
                    String message = String.format( RB.getString( "surveyset.nonmatchingdissipationtest" ), disFile.getFileName() );
                    disFile.getValidationErrors().add( message );
                    LOG.debug( message );
                }
            }
            else {

                // controle 10
                validateCptSurveyComposition( survey );
            }
        }
    }

    private static void validateCptSurveyComposition(GefCptSurvey survey) {

        // controle 10
        List<String> expectedDisFileNames = survey.getGefCptFile().getExpectedChildFileNameList();

        Set<String> remainingFoundDisFileNames = new HashSet<>();
        for ( IfcDisFile dissFileName : survey.getGefDisFiles() ) {
            remainingFoundDisFileNames.add( dissFileName.getFileName().trim() );
        }

        // Alle verwachtte dissfiles zouden in de lijst met meegelinkte dissfiles moeten staan.
        List<String> errors = survey.getGefCptFile().getValidationErrors();
        for ( String expectedFileName : expectedDisFileNames ) {
            if ( !remainingFoundDisFileNames.remove( expectedFileName ) ) {
                // foundDisFileNames does not contain expectedFileName, so dissipation test missing
                String cptFileName = survey.getGefCptFile().getFileName();
                String message = String.format( RB.getString( "surveyset.missingdissipationtests" ), cptFileName );
                if ( !errors.contains( message ) ) {
                    // report missing dissipation test only once
                    errors.add( message );
                    LOG.debug( message );
                }
            }
        }

        // Controle 9 revisited, Nu kunnen er dus nog bestanden over zijn in de gevonden dissipatietesten. Dit zijn in
        // feite ook orphanaged dissipatie testen. Zij worden tenslotte niet gerefereerd in de cpt file.
        for ( String disFileName : remainingFoundDisFileNames ) {
            String message = String.format( RB.getString( "surveyset.nonmatchingdissipationtest" ), disFileName );
            for ( IfcDisFile disFile : survey.getGefDisFiles() ) {
                if ( disFile.getFileName().trim().equals( disFileName ) ) {
                    disFile.getValidationErrors().add( message );
                }
            }
        }
    }

    @SuppressWarnings( "rawtypes" )
    private static <T extends IfcGefFile> boolean validate(T file, String transactionType, String qualityRegime) {

        LOG.debug( String.format( "validate( %s, %s, %s)", file, transactionType, qualityRegime ) );

        boolean returnValue = true;

        ValidatorFactory factory = GefValidation.buildFactory( );
        Validator validator = factory.getValidator();

        Class qrClazz;
        if ( IMBRO.equals( qualityRegime ) ) {
            qrClazz = Imbro.class;
        }
        else if ( IMBROA.equals( qualityRegime ) ) {
            qrClazz = ImbroA.class;
        }
        else {
            LOG.error( String.format( "Quality regime: '%s' not recognized as a valid quality regime", qualityRegime ) );
            throw new IllegalStateException( String.format( "Ongeldig kwaliteits regime : %s", qualityRegime ) );
        }
        Class trClazz;
        if ( TRANSACTION_TYPE_REGISTRATION.equals( transactionType ) ) {
            trClazz = Registration.class;
        }
        else if ( TRANSACTION_TYPE_CORRECTION.equals( transactionType ) ) {
            trClazz = Correction.class;
        }
        else {
            LOG.error( String.format( "Transaction type: '%s' not recognized as a valid transaction type", transactionType ) );
            throw new IllegalStateException( String.format( "Ongeldig transaction type : %s", transactionType ) );
        }

        Set<ConstraintViolation<T>> cvs = validator.validate( file, qrClazz, trClazz, Default.class );

        for ( ConstraintViolation<T> cv : cvs ) {
            file.getValidationErrors().add( cv.getMessage() );
            returnValue = false;
        }

        return returnValue;
    }

    /**
     * Utility methode, stelt vast of de aangeboden survey geen fouten bevat.
     *
     * @param survey
     * @return
     */
    public static boolean isValid(GefCptSurvey survey) {
        return survey.getGefCptFile().getParseErrors().isEmpty() && survey.getGefCptFile().getValidationErrors().isEmpty() && isValid( survey.getGefDisFiles() );
    }

    private static boolean isValid(List<IfcDisFile> dissFiles) {
        boolean valid = true;
        for ( int i = 0; i < dissFiles.size() && valid; i++ ) {
            valid = dissFiles.get( i ).getParseErrors().isEmpty() && dissFiles.get( i ).getValidationErrors().isEmpty();
        }
        return valid;
    }
}
