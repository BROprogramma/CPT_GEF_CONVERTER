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
package nl.bro.cpt.gef.main;

import static nl.bro.cpt.gef.transform.MappingConstants.IMBRO;
import static nl.bro.cpt.gef.transform.MappingConstants.IMBROA;
import static nl.bro.cpt.gef.transform.MappingConstants.TRANSACTION_TYPE_CORRECTION;
import static nl.bro.cpt.gef.transform.MappingConstants.TRANSACTION_TYPE_REGISTRATION;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.LogManager;
import java.util.logging.Logger;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBElement;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;

import nl.bro.cpt.gef.dto.GefCptSurvey;
import nl.bro.cpt.gef.dto.GefFile;
import nl.bro.cpt.gef.dto.IfcDisFile;
import nl.bro.cpt.gef.logic.GefSurveySet;
import nl.bro.cpt.gef.logic.GefUnmarshaller;
import nl.bro.cpt.gef.logic.impl.GefParserImpl;
import nl.bro.cpt.gef.transform.GefToBroMapperImbro;
import nl.bro.cpt.gef.transform.GefToBroMapperImbroA;
import nl.broservices.xsd.iscpt.v_1_1.CorrectionRequestType;
import nl.broservices.xsd.iscpt.v_1_1.ObjectFactory;
import nl.broservices.xsd.iscpt.v_1_1.RegistrationRequestType;

public class ConvertGef {

    private static final Logger LOG = Logger.getLogger( ConvertGef.class.getName() );
    private static final ObjectFactory CPT_IS_OF = new ObjectFactory();
    private static final net.opengis.sa.v_2_0.ObjectFactory SAM_OF = new net.opengis.sa.v_2_0.ObjectFactory();
    private static final JAXBContext CPT_JAXB_CTX = getJaxbContext();

    private static JAXBContext getJaxbContext() {
        try {
            return JAXBContext.newInstance( new Class[]{ CPT_IS_OF.getClass(), SAM_OF.getClass() } );
        }
        catch ( JAXBException ex ) {
            throw new RuntimeException( ex );
        }
    }

    public static void main(String[] args) {

        // re-init logging
        try {
            InputStream stream = ConvertGef.class.getResourceAsStream( "/convertgeflogging.properties" );
            LogManager.getLogManager().readConfiguration( stream );
        }
        catch ( IOException | SecurityException ex ) {
            Logger.getLogger( ConvertGef.class.getName() ).log( Level.SEVERE, null, ex );
        }

        // now business
        try {
            Cli cli = new Cli( args );
            cli.parse();
            if ( cli.isShouldExit() ) {
                return;
            }

            List<String> filenames = new ArrayList<>();
            Files.newDirectoryStream( Paths.get( cli.getInputDir() ), path -> isValidFile( path ) )
                .forEach( path2 -> filenames.add( path2.toFile().getName() )   );

            if ( filenames.isEmpty() ) {
               LOG.log(Level.SEVERE, "Er kunnen geen GEF files gevonden worden in deze directory." );
               return;

            }

            List<byte[]> fileContents = new ArrayList<>();
            Files.newDirectoryStream( Paths.get( cli.getInputDir() ), path -> isValidFile( path ) )
                .forEach( path2 -> fileContents.add( getFileContents( path2.toFile() )  )   );


            processGefFilesToXml( fileContents, filenames, cli );
        }
        catch ( IllegalArgumentException | IOException ex ) {
            Logger.getLogger( ConvertGef.class.getName() ).log( Level.SEVERE, null, ex );
        }

    }

    private static boolean isValidFile( Path path ) {
        return path.toFile().isFile() && ( path.toString().endsWith( ".GEF" ) || path.toString().endsWith( ".gef" ) );
    }

    private static byte[] getFileContents(File file) throws IllegalArgumentException {

        LOG.log( Level.INFO, "Reading file: {0}", file );

        if ( !file.exists() ) {
            LOG.log(Level.SEVERE, "[{0}] kan niet gevonden worden", file );
            throw new IllegalArgumentException();
        }

        try ( InputStream is = new FileInputStream( file ) ) {

            List<Byte> bytes = new ArrayList<>();
            int b;
            while ( -1 != ( b = is.read() ) ) {
                bytes.add( ( byte ) b );
            }
            is.close();

            byte[] result = new byte[ bytes.size() ];
            for ( int i = 0; i < bytes.size(); ++i ) {
                result[ i ] = bytes.get( i );
            }

            return result;
        }
        catch ( IOException ex ) {
            throw new IllegalArgumentException( ex );
        }

    }

    public static void processGefFilesToXml(List<byte[]> fileContents, List<String> filenames, Cli cli) throws IllegalArgumentException {

        List<GefCptSurvey> surveyList = read( fileContents, filenames, cli );

        boolean foundErrors = printParseErrors( surveyList.get( 0 ) );
        foundErrors = foundErrors || printValidationErrors( surveyList.get( 0 ) );

        if ( foundErrors ) {
            LOG.log( Level.SEVERE, "Er zijn parse of validatie fouten gevonden - GEF verwerking wordt afgebroken." );
            return;
        }

        writeOutputToFile( surveyList, cli );

    }

    private static List<GefCptSurvey> read(List<byte[]> fileDataList, List<String> filenameList, Cli cli) {

        GefSurveySet surveySet = new GefSurveySet();
        for ( int i = 0; i < fileDataList.size(); i++ ) {
            GefUnmarshaller unmarshaller = new GefParserImpl();
            GefFile gf = unmarshaller.unmarshall( filenameList.get( i ), fileDataList.get( i ), cli.getTransactionType() );
            surveySet.addFile( gf );
        }
        List<GefCptSurvey> surveys = surveySet.getGefSurveyList( cli.getQualityRegime(), cli.getTransactionType() );
        GefSurveySet.validateConsistency( surveys );
        GefSurveySet.validateContent( surveys, cli.getTransactionType() );
        return surveys;
    }

    private static void writeOutputToFile(List<GefCptSurvey> surveyList, Cli cli) {

        for ( GefCptSurvey survey : surveyList ) {

            String fileName = survey.getCptFileName();
            int extensionIndex = fileName.lastIndexOf( '.' );
            if ( extensionIndex != -1 ) {
                fileName = fileName.substring( 0, extensionIndex);
            }

            File out = new File( cli.getOutputDir() + File.separator + fileName + ".xml"  );

            if ( IMBRO.equals( survey.getQualityRegime() ) && TRANSACTION_TYPE_REGISTRATION.equals(  cli.getTransactionType() ) )   {
                RegistrationRequestType result = GefToBroMapperImbro.getInstance().gefToRegistrationRequest( survey, cli.getRequestReference() );
                writeOutputToFile( CPT_IS_OF.createRegistrationRequest( result ), out );
            }
            else if ( IMBRO.equals( survey.getQualityRegime() ) && TRANSACTION_TYPE_CORRECTION.equals(  cli.getTransactionType() ) )   {
                CorrectionRequestType result =  GefToBroMapperImbro.getInstance().gefToCorrectionRequest( survey, cli.getRequestReference(), cli.getCorrectionReason() );
                result.getSourceDocument().getCPT().setObjectIdAccountableParty( cli.getObjectIdAccountableParty() );
                writeOutputToFile( CPT_IS_OF.createCorrectionRequest( result ), out );
            }
            else if ( IMBROA.equals( survey.getQualityRegime() ) && TRANSACTION_TYPE_REGISTRATION.equals(  cli.getTransactionType() ) )  {
                RegistrationRequestType result = GefToBroMapperImbroA.getInstance().gefToRegistrationRequest( survey, cli.getRequestReference() );
                writeOutputToFile( CPT_IS_OF.createRegistrationRequest( result ), out );
            }
            else {
                CorrectionRequestType result =  GefToBroMapperImbroA.getInstance().gefToCorrectionRequest( survey, cli.getRequestReference(), cli.getCorrectionReason() );
                result.getSourceDocument().getCPT().setObjectIdAccountableParty( cli.getObjectIdAccountableParty() );
                writeOutputToFile( CPT_IS_OF.createCorrectionRequest( result ), out );
            }
        }
    }

    private static void writeOutputToFile( JAXBElement request, File out ) {
        try {
            Marshaller marshaller = CPT_JAXB_CTX.createMarshaller();
            marshaller.setProperty( Marshaller.JAXB_FORMATTED_OUTPUT, true );
            marshaller.marshal( request, out );
        }
        catch ( JAXBException ex ) {
            Logger.getLogger( ConvertGef.class.getName() ).log( Level.SEVERE, null, ex );
        }

    }

    private static boolean printParseErrors(GefCptSurvey gefCptSurvey) {

        boolean returnValue = false;
        List<String> parseErrors = gefCptSurvey.getGefCptFile().getParseErrors();
        if ( !parseErrors.isEmpty() ) {
            returnValue = true;
            LOG.log( Level.INFO, "Parse errors (GEF-CPT = %s):", gefCptSurvey.getGefCptFile().getFileName() );
            for ( String error : parseErrors ) {
                LOG.log( Level.INFO, "  - %s", error );
            }
        }

        for ( IfcDisFile disFile : gefCptSurvey.getGefDisFiles() ) {
            parseErrors = disFile.getParseErrors();
            if ( !parseErrors.isEmpty() ) {
                returnValue = true;
                LOG.log( Level.INFO, String.format( "Parse errors (GEF-DISS = %s):", gefCptSurvey.getGefCptFile().getFileName() ) );
                for ( String error : parseErrors ) {
                    LOG.log( Level.INFO, String.format( "  - %s", error ) );
                }
            }
        }
        return returnValue;
    }

    private static boolean printValidationErrors(GefCptSurvey gefCptSurvey) {

        boolean returnValue = false;
        List<String> validationErrors = gefCptSurvey.getGefCptFile().getValidationErrors();
        if ( !validationErrors.isEmpty() ) {
            returnValue = true;
            LOG.log( Level.INFO, String.format( "Validation errors (GEF-CPT = %s):", gefCptSurvey.getGefCptFile().getFileName() ) );
            for ( String error : validationErrors ) {
                LOG.log( Level.INFO, String.format( "  - %s", error ) );
            }
        }

        for ( IfcDisFile disFile : gefCptSurvey.getGefDisFiles() ) {
            validationErrors = disFile.getValidationErrors();
            if ( !validationErrors.isEmpty() ) {
                returnValue = true;
                LOG.log( Level.INFO, String.format( "Validation errors (GEF-DISS = %s):", gefCptSurvey.getGefCptFile().getFileName() ) );
                for ( String error : validationErrors ) {
                    LOG.log( Level.INFO, String.format( "  - %s", error ) );
                }
            }
        }
        return returnValue;
    }

}
