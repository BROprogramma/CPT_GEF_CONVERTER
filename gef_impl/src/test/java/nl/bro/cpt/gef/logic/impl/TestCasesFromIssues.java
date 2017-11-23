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
package nl.bro.cpt.gef.logic.impl;

import static nl.bro.cpt.gef.transform.MappingConstants.IMBRO;
import static nl.bro.cpt.gef.transform.MappingConstants.IMBROA;
import static nl.bro.cpt.gef.transform.MappingConstants.REGISTRATION_STATUS_REGISTRATION;
import static nl.bro.cpt.gef.transform.MappingConstants.TRANSACTION_TYPE_CORRECTION;
import static nl.bro.cpt.gef.transform.MappingConstants.TRANSACTION_TYPE_REGISTRATION;
import static org.fest.assertions.Assertions.assertThat;

import java.io.IOException;
import java.util.List;

import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.runners.MockitoJUnitRunner;

import nl.bro.cpt.gef.dto.DataRowCpt;
import nl.bro.cpt.gef.dto.GefCptFile;
import nl.bro.cpt.gef.dto.GefCptSurvey;
import nl.bro.cpt.gef.dto.GefFile;
import nl.bro.cpt.gef.dto.IfcCptFile;
import nl.bro.cpt.gef.logic.GefSurveySet;
import nl.bro.cpt.gef.logic.GefUnmarshaller;
import nl.bro.dto.gef.testutil.TestUtil;

@RunWith( MockitoJUnitRunner.class )
public class TestCasesFromIssues {

    // no issue in TF yet: Column void value voor laatste column wordt niet herkend.
    @Test
    public void testColumnVoidInterpretation() throws IOException {
        // check whether the last column is filled when no additional column seperator at the end of the line.
        String filename = "test-columnvoid.gef";
        byte[] fileContent = TestUtil.getFileBytes( "/" + filename );

        GefUnmarshaller unmarshaller = new GefParserImpl();
        GefFile gf = unmarshaller.unmarshall( filename, fileContent, TRANSACTION_TYPE_REGISTRATION );

        IfcCptFile cptFile = (IfcCptFile) gf;
        assertThat( cptFile.getParseErrors().size() ).isEqualTo( 0 );
        assertThat( cptFile.getDataBlock() ).isNotEmpty();
        assertThat( cptFile.getDataBlock().get( 0 ) ).isNotNull();
        DataRowCpt row = cptFile.getDataBlock().get( 0 );
        // Dit lijkt raar - maar de waarde is wel gezet (filled entry is true omdat de waarde naar null is gezet):
        assertThat( row.getFilledEntry( "39" ) ).isTrue();
        assertThat( row.getDatablockci39() ).isNull();
    }

    // artf-52092
    @Test
    public void testNrOfReadColumns() throws IOException {

        // check whether the last column is filled when no additional column seperator at the end of the line.
        String filename = "test-artf52092.gef";
        byte[] fileContent = TestUtil.getFileBytes( "/" + filename );

        GefUnmarshaller unmarshaller = new GefParserImpl();
        GefFile gf = unmarshaller.unmarshall( filename, fileContent, TRANSACTION_TYPE_REGISTRATION );

        IfcCptFile cptFile = (IfcCptFile) gf;
        assertThat( cptFile.getParseErrors().size() ).isEqualTo( 0 );
        assertThat( cptFile.getDataBlock() ).isNotEmpty();
        assertThat( cptFile.getDataBlock().get( 0 ) ).isNotNull();
        DataRowCpt row = cptFile.getDataBlock().get( 0 );
        assertThat( row.getFilledEntry( "39" ) ).isTrue();
        // check whether the last column is filled when no additional column seperator at the end of the line.

        filename = "test-artf51852.gef";
        fileContent = TestUtil.getFileBytes( "/" + filename );

        unmarshaller = new GefParserImpl();
        gf = unmarshaller.unmarshall( filename, fileContent, TRANSACTION_TYPE_REGISTRATION );

        cptFile = (IfcCptFile) gf;
        assertThat( cptFile.getParseErrors().size() ).isEqualTo( 0 );
        assertThat( cptFile.getDataBlock() ).isNotEmpty();
        assertThat( cptFile.getDataBlock().get( 0 ) ).isNotNull();
        row = cptFile.getDataBlock().get( 0 );
        assertThat( row.getFilledEntry( "39" ) ).isTrue();
    }

    // Artf-51852
    @Test
    public void testZidVerticalReferencePlane5861() throws IOException {
        String filename = "test-artf51852.gef";
        byte[] fileContent = TestUtil.getFileBytes( "/" + filename );

        GefUnmarshaller unmarshaller = new GefParserImpl();
        GefFile gf = unmarshaller.unmarshall( filename, fileContent, TRANSACTION_TYPE_REGISTRATION );

        IfcCptFile cptFile = (IfcCptFile) gf;
        assertThat( cptFile.getParseErrors().size() ).isEqualTo( 0 );

        GefSurveySet surveySet = new GefSurveySet();
        surveySet.addFile( gf );

        List<GefCptSurvey> surveys = surveySet.getGefSurveyList( IMBRO, TRANSACTION_TYPE_REGISTRATION );
        GefSurveySet.validateConsistency( surveys );
        GefSurveySet.validateContent( surveys, TRANSACTION_TYPE_REGISTRATION );

        assertThat( surveys.size() ).isEqualTo( 1 );
        assertThat( surveys.get( 0 ) ).isNotNull();
        assertThat( surveys.get( 0 ).getGefCptFile() ).isNotNull();
        assertThat( surveys.get( 0 ).getGefCptFile().getValidationErrors() ).isEmpty();
    }

    @Test
    public void testMeasurementVarComposition() throws IOException {

        String filename = "CPT-F3b-i3-20457-rejection-CPT.gef";
        byte[] fileContent = TestUtil.getFileBytes( "/" + filename );

        GefUnmarshaller unmarshaller = new GefParserImpl();
        GefFile gf = unmarshaller.unmarshall( filename, fileContent, TRANSACTION_TYPE_CORRECTION );

        IfcCptFile cptFile = (IfcCptFile) gf;
        assertThat( cptFile.getParseErrors().size() ).isEqualTo( 1 );
        assertThat( cptFile.getParseErrors().get( 0 ) ).startsWith( "Invalid number of datafields for #MEASUREMENTVAR" );
    }

    @Test
    public void testColumnSeperatorMissing() throws IOException {

        String filename = "CPT-F3b-i3-28717-CPT-1.gef";
        byte[] fileContent = TestUtil.getFileBytes( "/" + filename );

        GefUnmarshaller unmarshaller = new GefParserImpl();
        GefFile gf = unmarshaller.unmarshall( filename, fileContent, TRANSACTION_TYPE_CORRECTION );

        IfcCptFile cptFile = (IfcCptFile) gf;
        assertThat( cptFile.getParseErrors().size() ).isEqualTo( 1 );
        assertThat( cptFile.getParseErrors().get( 0 ) ).startsWith( "value in (row=1, column=1) is not in a valid number format" );
    }

    @Test
    public void testUnMappedEntryInFile() throws IOException {

        String filename = "CPT-F3b-i3-28753-CPT.gef";
        byte[] fileContent = TestUtil.getFileBytes( "/" + filename );

        GefUnmarshaller unmarshaller = new GefParserImpl();
        GefFile gf = unmarshaller.unmarshall( filename, fileContent, TRANSACTION_TYPE_CORRECTION );

        IfcCptFile cptFile = (IfcCptFile) gf;
        assertThat( cptFile.getParseErrors() ).isEmpty();
    }

    @Test
    public void testInvalidFormatHeaderEntries() throws IOException {

        String filename = "CPT-F3b-i3-15339b-completion-CPT.gef";
        byte[] fileContent = TestUtil.getFileBytes( "/" + filename );

        GefUnmarshaller unmarshaller = new GefParserImpl();
        GefFile gf = unmarshaller.unmarshall( filename, fileContent, TRANSACTION_TYPE_CORRECTION );

        IfcCptFile cptFile = (IfcCptFile) gf;
        assertThat( cptFile.getParseErrors().size() ).isEqualTo( 2 );
        assertThat( cptFile.getParseErrors().get( 0 ) ).contains( "Invalid number of datafields for #SPECIMENVAR 1" );
        assertThat( cptFile.getParseErrors().get( 1 ) ).contains( "Invalid number of datafields for #SPECIMENVAR 2" );
    }

    @Test
    public void testInvalidTestId() throws IOException {

        String filename = "CPT-F3b-i3-14373-acceptance-CPT.gef";
        byte[] fileContent = TestUtil.getFileBytes( "/" + filename );

        GefUnmarshaller unmarshaller = new GefParserImpl();
        GefFile gf = unmarshaller.unmarshall( filename, fileContent, REGISTRATION_STATUS_REGISTRATION );

        IfcCptFile cptFile = (IfcCptFile) gf;
        assertThat( cptFile.getParseErrors() ).isEmpty();
    }

    @Test
    public void testMeasurementVar42NotFilled() throws IOException {

        String filename = "CPT-F3b-i3-16785-rejection-CPT.gef";
        byte[] fileContent = TestUtil.getFileBytes( "/" + filename );

        GefUnmarshaller unmarshaller = new GefParserImpl();
        GefFile gf = unmarshaller.unmarshall( filename, fileContent, REGISTRATION_STATUS_REGISTRATION );

        GefCptFile cptFile = (GefCptFile) gf;
        assertThat( cptFile.getParseErrors() ).isEmpty();

        GefSurveySet surveySet = new GefSurveySet();
        surveySet.addFile( cptFile );

        List<GefCptSurvey> surveys = surveySet.getGefSurveyList( IMBRO, TRANSACTION_TYPE_REGISTRATION );
        GefSurveySet.validateConsistency( surveys );
        GefSurveySet.validateContent( surveys, TRANSACTION_TYPE_REGISTRATION );

        assertThat( surveys.size() ).isEqualTo( 1 );
        assertThat( surveys.get( 0 ) ).isNotNull();
        assertThat( surveys.get( 0 ).getGefCptFile().getValidationErrors().size() ).isEqualTo( 1 );
    }

    // artf-51116.. let op.. een IMBRO A waarde moet ook gevuld zijn.
    @Test
    public void testZidNotFilledImbroA() throws IOException {

        String filename = "CPT-F3b-i3-25083-completion-CPT.gef";
        byte[] fileContent = TestUtil.getFileBytes( "/" + filename );

        GefUnmarshaller unmarshaller = new GefParserImpl();
        GefFile gf = unmarshaller.unmarshall( filename, fileContent, TRANSACTION_TYPE_CORRECTION );

        GefCptFile cptFile = (GefCptFile) gf;
        assertThat( cptFile.getParseErrors() ).isEmpty();

        GefSurveySet surveySet = new GefSurveySet();
        surveySet.addFile( cptFile );

        List<GefCptSurvey> surveys = surveySet.getGefSurveyList( IMBROA, TRANSACTION_TYPE_CORRECTION );
        GefSurveySet.validateConsistency( surveys );
        GefSurveySet.validateContent( surveys, TRANSACTION_TYPE_CORRECTION );

        assertThat( surveys.size() ).isEqualTo( 1 );
        assertThat( surveys.get( 0 ) ).isNotNull();
        assertThat( surveys.get( 0 ).getGefCptFile().getValidationErrors() ).isEmpty();
    }

    // artf-51120
    @Test
    public void testMeasurementText6Valid() throws IOException {

        String filename = "CPT-F3b-i3-15249-completion-CPT.gef";
        byte[] fileContent = TestUtil.getFileBytes( "/" + filename );

        GefUnmarshaller unmarshaller = new GefParserImpl();
        GefFile gf = unmarshaller.unmarshall( filename, fileContent, TRANSACTION_TYPE_CORRECTION );

        GefCptFile cptFile = (GefCptFile) gf;
        assertThat( cptFile.getParseErrors() ).isEmpty();

        GefSurveySet surveySet = new GefSurveySet();
        surveySet.addFile( cptFile );

        List<GefCptSurvey> surveys = surveySet.getGefSurveyList( IMBRO, TRANSACTION_TYPE_CORRECTION );
        GefSurveySet.validateConsistency( surveys );
        GefSurveySet.validateContent( surveys, TRANSACTION_TYPE_CORRECTION );

        assertThat( surveys.size() ).isEqualTo( 1 );
        assertThat( surveys.get( 0 ) ).isNotNull();
        assertThat( surveys.get( 0 ).getGefCptFile().getValidationErrors() ).isEmpty();
    }

    // artf-51122
    @Test
    public void testMeasurementText6EmptyValue() throws IOException {

        String filename = "CPT-F3b-i3-15299-completion-CPT.gef";
        byte[] fileContent = TestUtil.getFileBytes( "/" + filename );

        GefUnmarshaller unmarshaller = new GefParserImpl();
        GefFile gf = unmarshaller.unmarshall( filename, fileContent, TRANSACTION_TYPE_CORRECTION );

        GefCptFile cptFile = (GefCptFile) gf;
        assertThat( cptFile.getParseErrors() ).isEmpty();

        GefSurveySet surveySet = new GefSurveySet();
        surveySet.addFile( cptFile );

        List<GefCptSurvey> surveys = surveySet.getGefSurveyList( IMBROA, TRANSACTION_TYPE_CORRECTION );
        GefSurveySet.validateConsistency( surveys );
        GefSurveySet.validateContent( surveys, TRANSACTION_TYPE_CORRECTION );

        assertThat( surveys.size() ).isEqualTo( 1 );
        assertThat( surveys.get( 0 ) ).isNotNull();
        assertThat( surveys.get( 0 ).getGefCptFile().getValidationErrors() ).isEmpty();
    }

    // artf-51204
    // NOTE: de record separator is een !. Echter de parser zou voldoende lenient moeten zijn
    // om ook de !/r/n te interpreteren.
    @Test
    public void testDefaultColumnSeperator() throws IOException {

        String filename = "CPT-F3b-i3-29041-CPT.gef";
        byte[] fileContent = TestUtil.getFileBytes( "/" + filename );

        GefUnmarshaller unmarshaller = new GefParserImpl();
        GefFile gf = unmarshaller.unmarshall( filename, fileContent, TRANSACTION_TYPE_CORRECTION );

        GefCptFile cptFile = (GefCptFile) gf;
        assertThat( cptFile.getParseErrors() ).isEmpty();

        GefSurveySet surveySet = new GefSurveySet();
        surveySet.addFile( cptFile );

        List<GefCptSurvey> surveys = surveySet.getGefSurveyList( IMBRO, TRANSACTION_TYPE_CORRECTION );
        GefSurveySet.validateConsistency( surveys );
        GefSurveySet.validateContent( surveys, TRANSACTION_TYPE_CORRECTION );

        assertThat( surveys.size() ).isEqualTo( 1 );
        assertThat( surveys.get( 0 ) ).isNotNull();
        assertThat( surveys.get( 0 ).getGefCptFile().getValidationErrors() ).isEmpty();
    }

    // artf-51195
    @Test
    public void testAnalysisCodeInVariable() throws IOException {

        String filename = "CPT-F3b-i3-28849-CPT.gef";
        byte[] fileContent = TestUtil.getFileBytes( "/" + filename );

        GefUnmarshaller unmarshaller = new GefParserImpl();
        GefFile gf = unmarshaller.unmarshall( filename, fileContent, TRANSACTION_TYPE_CORRECTION );

        GefCptFile cptFile = (GefCptFile) gf;
        assertThat( cptFile.getParseErrors() ).isEmpty();
    }

    // artf-51191
    @Test
    public void testCompanyIdPresentButEmptyImbroA() throws IOException {

        String filename = "CPT-F3b-i3-15403-rejection-CPT.gef";
        byte[] fileContent = TestUtil.getFileBytes( "/" + filename );

        GefUnmarshaller unmarshaller = new GefParserImpl();
        GefFile gf = unmarshaller.unmarshall( filename, fileContent, TRANSACTION_TYPE_CORRECTION );

        GefCptFile cptFile = (GefCptFile) gf;
        assertThat( cptFile.getParseErrors() ).isEmpty();

        GefSurveySet surveySet = new GefSurveySet();
        surveySet.addFile( cptFile );

        List<GefCptSurvey> surveys = surveySet.getGefSurveyList( IMBROA, TRANSACTION_TYPE_CORRECTION );
        GefSurveySet.validateConsistency( surveys );
        GefSurveySet.validateContent( surveys, TRANSACTION_TYPE_CORRECTION );

        assertThat( surveys.size() ).isEqualTo( 1 );
        assertThat( surveys.get( 0 ) ).isNotNull();
        assertThat( surveys.get( 0 ).getGefCptFile().getValidationErrors() ).isEmpty();
    }

    // artf-51119
    @Test
    public void testReportCodeInvalid() throws IOException {

        String filename = "CPT-F3b-i3-28885-CPT-extra.gef";
        byte[] fileContent = TestUtil.getFileBytes( "/" + filename );

        GefUnmarshaller unmarshaller = new GefParserImpl();
        GefFile gf = unmarshaller.unmarshall( filename, fileContent, TRANSACTION_TYPE_CORRECTION );

        GefCptFile cptFile = (GefCptFile) gf;
        assertThat( cptFile.getParseErrors() ).isEmpty();

        GefSurveySet surveySet = new GefSurveySet();
        surveySet.addFile( cptFile );

        List<GefCptSurvey> surveys = surveySet.getGefSurveyList( IMBRO, TRANSACTION_TYPE_CORRECTION );
        GefSurveySet.validateConsistency( surveys );
        GefSurveySet.validateContent( surveys, TRANSACTION_TYPE_CORRECTION );

        assertThat( surveys.size() ).isEqualTo( 1 );
        assertThat( surveys.get( 0 ) ).isNotNull();
        assertThat( surveys.get( 0 ).getGefCptFile().getValidationErrors().size() ).isEqualTo( 1 );
        String message = surveys.get( 0 ).getGefCptFile().getValidationErrors().get( 0 );
        assertThat( message ).contains( "GEF#REPORTCODE waarde is niet correct:" );
        assertThat( message ).contains( "het GEF-bestand is niet valide, het heeft niet de juiste versie." );
    }

    // artf-51285
    @Test
    public void testAdditionalInvestigationPerformedMapping() throws IOException {

        String filename = "CPT-F3b-i3-00002-registration-IMBROA-completion-CPT.gef";
        byte[] fileContent = TestUtil.getFileBytes( "/" + filename );

        GefUnmarshaller unmarshaller = new GefParserImpl();
        GefFile gf = unmarshaller.unmarshall( filename, fileContent, TRANSACTION_TYPE_REGISTRATION );

        GefCptFile cptFile = (GefCptFile) gf;
        assertThat( cptFile.getParseErrors() ).isEmpty();

        GefSurveySet surveySet = new GefSurveySet();
        surveySet.addFile( cptFile );

        List<GefCptSurvey> surveys = surveySet.getGefSurveyList( IMBROA, TRANSACTION_TYPE_CORRECTION );
        GefSurveySet.validateConsistency( surveys );
        GefSurveySet.validateContent( surveys, TRANSACTION_TYPE_REGISTRATION );

        assertThat( surveys.size() ).isEqualTo( 1 );
        assertThat( surveys.get( 0 ) ).isNotNull();
        assertThat( surveys.get( 0 ).getGefCptFile().getValidationErrors() ).isEmpty();

        // TODO: om de een of andere reden vindt cobertura dit niet goed, maar surefire wel...
        // GeotechnicalCPTSurvey survey = GefToBroMapper.transform( surveys.get( 0 ) );
        // assertThat( survey.getAdditionalInvestigationPerformed() ).isEqualTo( MappingConstants.BRO_IND_YES );
    }

    // artf-51115 CPT-F3b-i3-11908-registration-IMBROA-rejection-CPT.gef
    @Test
    public void testMeasurementText20Mapping() throws IOException {
        String filename = "CPT-F3b-i3-11908-registration-IMBROA-rejection-CPT.gef";
        byte[] fileContent = TestUtil.getFileBytes( "/" + filename );

        GefUnmarshaller unmarshaller = new GefParserImpl();
        GefFile gf = unmarshaller.unmarshall( filename, fileContent, TRANSACTION_TYPE_REGISTRATION );

        GefCptFile cptFile = (GefCptFile) gf;
        assertThat( cptFile.getParseErrors() ).isEmpty();

        GefSurveySet surveySet = new GefSurveySet();
        surveySet.addFile( cptFile );

        List<GefCptSurvey> surveys = surveySet.getGefSurveyList( IMBROA, TRANSACTION_TYPE_CORRECTION );
        GefSurveySet.validateConsistency( surveys );
        GefSurveySet.validateContent( surveys, TRANSACTION_TYPE_REGISTRATION );

        assertThat( surveys.size() ).isEqualTo( 1 );
        assertThat( surveys.get( 0 ) ).isNotNull();
        assertThat( surveys.get( 0 ).getGefCptFile().getValidationErrors() ).isEmpty();

    }

    // artf-52755
    @Test
    public void testMeasurementText101Mapping() throws IOException {
        String filename = "test-artf52755.gef";
        byte[] fileContent = TestUtil.getFileBytes( "/" + filename );

        GefUnmarshaller unmarshaller = new GefParserImpl();
        GefFile gf = unmarshaller.unmarshall( filename, fileContent, TRANSACTION_TYPE_REGISTRATION );

        IfcCptFile cptFile = (IfcCptFile) gf;
        assertThat( cptFile.getParseErrors().size() ).isEqualTo( 1 );
        assertThat( cptFile.getParseErrors().get( 0 ) ).contains( "#MEASUREMENTTEXT[101] cannot be parsed - should have at least 3 elements" );
    }

    // artf-50976 nulmetingen.gef
    @Test
    public void testZeroLoadMeasurements() throws IOException {
        String filename = "nulmetingen.gef";
        byte[] fileContent = TestUtil.getFileBytes( "/" + filename );

        GefUnmarshaller unmarshaller = new GefParserImpl();
        GefFile gf = unmarshaller.unmarshall( filename, fileContent, TRANSACTION_TYPE_REGISTRATION );

        GefCptFile cptFile = (GefCptFile) gf;
        assertThat( cptFile.getParseErrors() ).isEmpty();

        GefSurveySet surveySet = new GefSurveySet();
        surveySet.addFile( cptFile );

        List<GefCptSurvey> surveys = surveySet.getGefSurveyList( IMBROA, TRANSACTION_TYPE_CORRECTION );
        GefSurveySet.validateConsistency( surveys );
        GefSurveySet.validateContent( surveys, TRANSACTION_TYPE_REGISTRATION );

        assertThat( surveys.size() ).isEqualTo( 1 );
        assertThat( surveys.get( 0 ) ).isNotNull();
        assertThat( surveys.get( 0 ).getGefCptFile().getValidationErrors() ).isNotNull();
        assertThat( surveys.get( 0 ).getGefCptFile().getValidationErrors().size() ).isEqualTo( 1 );
    }

    // artf-51333 measurementvar 13 niet aanwezig.
    @Test
    public void testMeasurementVar13NotPresent() throws IOException {
        String filename = "CPT-F3b-i6-08258-registration-IMBRO-rejection-CPT.gef";
        byte[] fileContent = TestUtil.getFileBytes( "/" + filename );

        GefUnmarshaller unmarshaller = new GefParserImpl();
        GefFile gf = unmarshaller.unmarshall( filename, fileContent, TRANSACTION_TYPE_REGISTRATION );

        GefCptFile cptFile = (GefCptFile) gf;
        assertThat( cptFile.getParseErrors() ).isEmpty();

        GefSurveySet surveySet = new GefSurveySet();
        surveySet.addFile( cptFile );

        List<GefCptSurvey> surveys = surveySet.getGefSurveyList( IMBRO, TRANSACTION_TYPE_REGISTRATION );
        GefSurveySet.validateConsistency( surveys );
        GefSurveySet.validateContent( surveys, TRANSACTION_TYPE_REGISTRATION );

        assertThat( surveys.size() ).isEqualTo( 1 );
        assertThat( surveys.get( 0 ) ).isNotNull();
        assertThat( surveys.get( 0 ).getGefCptFile().getValidationErrors().size() ).isEqualTo( 2 );
        assertThat( surveys.get( 0 ).getGefCptFile().getValidationErrors() ).contains( "GEF#SPECIMENVARx+1 (onderdiepte) waarde ontbreekt." );
        assertThat( surveys.get( 0 ).getGefCptFile().getValidationErrors() ).contains( "GEF#MEASUREMENTVAR13 (voorgeboord tot) waarde ontbreekt." );
    }

    // artf-51339
    @Test
    public void testDefaultColumnAndRecordSeparator() throws IOException {
        String filename = "CPT-F3b-i6-33265-CPT.gef";
        byte[] fileContent = TestUtil.getFileBytes( "/" + filename );

        GefUnmarshaller unmarshaller = new GefParserImpl();
        GefFile gf = unmarshaller.unmarshall( filename, fileContent, TRANSACTION_TYPE_REGISTRATION );

        GefCptFile cptFile = (GefCptFile) gf;
        assertThat( cptFile.getParseErrors() ).isEmpty();

        GefSurveySet surveySet = new GefSurveySet();
        surveySet.addFile( cptFile );

        List<GefCptSurvey> surveys = surveySet.getGefSurveyList( IMBRO, TRANSACTION_TYPE_REGISTRATION );
        GefSurveySet.validateConsistency( surveys );
        GefSurveySet.validateContent( surveys, TRANSACTION_TYPE_REGISTRATION );

        assertThat( surveys.size() ).isEqualTo( 1 );
        assertThat( surveys.get( 0 ) ).isNotNull();
        assertThat( surveys.get( 0 ).getGefCptFile().getValidationErrors() ).isEmpty();
        assertThat( surveys.get( 0 ).getGefCptFile().getParseErrors() ).isEmpty();
    }

    // artf-51329
    @Test
    public void testMeasurmentText42Conversion() throws IOException {

        String filename = "CPT-F3b-i3-13179-registration-IMBROA-completion-CPT.gef";
        byte[] fileContent = TestUtil.getFileBytes( "/" + filename );

        GefUnmarshaller unmarshaller = new GefParserImpl();
        GefFile gf = unmarshaller.unmarshall( filename, fileContent, TRANSACTION_TYPE_REGISTRATION );

        GefCptFile cptFile = (GefCptFile) gf;
        assertThat( cptFile.getParseErrors() ).isEmpty();

        GefSurveySet surveySet = new GefSurveySet();
        surveySet.addFile( cptFile );

        List<GefCptSurvey> surveys = surveySet.getGefSurveyList( IMBROA, TRANSACTION_TYPE_REGISTRATION );
        GefSurveySet.validateConsistency( surveys );
        GefSurveySet.validateContent( surveys, TRANSACTION_TYPE_REGISTRATION );

        assertThat( surveys.size() ).isEqualTo( 1 );
        assertThat( surveys.get( 0 ) ).isNotNull();
        assertThat( surveys.get( 0 ).getGefCptFile().getValidationErrors() ).isEmpty();
        assertThat( surveys.get( 0 ).getGefCptFile().getParseErrors() ).isEmpty();
    }

    // artf-51566
    @Test
    public void testInvalidParentGef() throws IOException {

        String cptFileName = "CORR_GEF_JW_IMBROA.GEF";
        byte[] cptFileContent = TestUtil.getFileBytes( "/" + cptFileName );
        String disFileName = "CORR_CORR_GEF_JW_IMBROA_DISS.GEF";
        byte[] disFileContent = TestUtil.getFileBytes( "/" + disFileName );

        GefUnmarshaller unmarshaller = new GefParserImpl();
        GefFile cptFile = unmarshaller.unmarshall( cptFileName, cptFileContent, TRANSACTION_TYPE_REGISTRATION );
        assertThat( cptFile.getParseErrors() ).isEmpty();
        assertThat( cptFile.getValidationErrors().isEmpty() );
        GefFile disFile = unmarshaller.unmarshall( disFileName, disFileContent, TRANSACTION_TYPE_REGISTRATION );
        assertThat( disFile.getParseErrors() ).isEmpty();
        assertThat( disFile.getValidationErrors().isEmpty() );

        GefSurveySet surveySet = new GefSurveySet();
        surveySet.addFile( cptFile );
        surveySet.addFile( disFile );

        List<GefCptSurvey> surveys = surveySet.getGefSurveyList( IMBROA, TRANSACTION_TYPE_REGISTRATION );
        GefSurveySet.validateConsistency( surveys );
        GefSurveySet.validateContent( surveys, TRANSACTION_TYPE_REGISTRATION );

        assertThat( surveys.size() ).isEqualTo( 2 );
        assertThat( surveys.get( 0 ) ).isNotNull();
        assertThat( surveys.get( 0 ).isOrphanaged() ).isFalse();
        assertThat( surveys.get( 0 ).getGefCptFile().getValidationErrors().size() ).isEqualTo( 1 );
        assertThat( surveys.get( 0 ).getGefCptFile().getValidationErrors().get( 0 ) ).isEqualTo( "Sondering CORR_GEF_JW_IMBROA.GEF bevat niet alle verwachte dissipatietests." );
        assertThat( surveys.get( 0 ).getGefCptFile().getParseErrors() ).isEmpty();
        assertThat( surveys.get( 1 ) ).isNotNull();
        assertThat( surveys.get( 1 ).isOrphanaged() ).isTrue();
        assertThat( surveys.get( 1 ).getGefDisFiles().size() ).isEqualTo( 1 );
        assertThat( surveys.get( 1 ).getGefDisFiles().get( 0 ).getValidationErrors().size() ).isEqualTo( 1 );
        assertThat( surveys.get( 1 ).getGefDisFiles().get( 0 ).getValidationErrors().get( 0 ) ).isEqualTo(
            "Dissipatietest CORR_CORR_GEF_JW_IMBROA_DISS.GEF hoort niet bij een eerder toegevoegde sondering." );
        assertThat( surveys.get( 1 ).getGefDisFiles().get( 0 ).getParseErrors() ).isEmpty();
    }

    // artf52280
    @Test
    public void testNotSupportedCptParameters() throws IOException {

        // - prepare
        String cptFileName = "test-artf52280.gef";
        byte[] cptFileContent = TestUtil.getFileBytes( "/" + cptFileName );

        GefUnmarshaller unmarshaller = new GefParserImpl();
        GefCptFile cptFile = (GefCptFile) unmarshaller.unmarshall( cptFileName, cptFileContent, TRANSACTION_TYPE_REGISTRATION );

        GefCptSurvey survey = new GefCptSurvey();
        survey.setGefCptFile( cptFile );

        // - verify
        assertThat( survey.getGefCptFile().getParseErrors() ).isEmpty();

    }

    // test-artf52335
    @Test
    public void testNotSupportedMeasurementVar() throws IOException {

        // - prepare
        String cptFileName = "test-artf52335.gef";
        byte[] cptFileContent = TestUtil.getFileBytes( "/" + cptFileName );

        GefUnmarshaller unmarshaller = new GefParserImpl();
        GefCptFile cptFile = (GefCptFile) unmarshaller.unmarshall( cptFileName, cptFileContent, TRANSACTION_TYPE_REGISTRATION );

        GefCptSurvey survey = new GefCptSurvey();
        survey.setGefCptFile( cptFile );

        // - verify
        assertThat( survey.getGefCptFile().getParseErrors() ).isEmpty();
    }

    // TODO. controleer trim() (enterWaarde / enterVariable in GefFIleLoader zou niet nodig moeten zijn). Multiple
    // spaties zouden moeten kunnen als value separator??
    @Ignore
    @Test
    public void testtemp() throws IOException {

        // - prepare
        String cptFileName = "cpt.gef";
        byte[] cptFileContent = TestUtil.getFileBytes( "/" + cptFileName );

        GefUnmarshaller unmarshaller = new GefParserImpl();
        GefCptFile cptFile = (GefCptFile) unmarshaller.unmarshall( cptFileName, cptFileContent, TRANSACTION_TYPE_REGISTRATION );

        GefCptSurvey survey = new GefCptSurvey();
        survey.setGefCptFile( cptFile );

        // - verify
        assertThat( survey.getGefCptFile().getParseErrors() ).isEmpty();
    }

    // test-artf52529
    @Test
    public void testRecordSeparator() throws IOException {

        // - prepare
        String cptFileName = "test-artf52529.gef";
        byte[] cptFileContent = TestUtil.getFileBytes( "/" + cptFileName );

        GefUnmarshaller unmarshaller = new GefParserImpl();
        GefCptFile cptFile = (GefCptFile) unmarshaller.unmarshall( cptFileName, cptFileContent, TRANSACTION_TYPE_REGISTRATION );

        GefCptSurvey survey = new GefCptSurvey();
        survey.setGefCptFile( cptFile );

        // - verify
        assertThat( survey.getGefCptFile().getParseErrors() ).isEmpty();

        GefSurveySet surveySet = new GefSurveySet();
        surveySet.addFile( cptFile );

        List<GefCptSurvey> surveys = surveySet.getGefSurveyList( IMBROA, TRANSACTION_TYPE_REGISTRATION );
        GefSurveySet.validateConsistency( surveys );
        GefSurveySet.validateContent( surveys, TRANSACTION_TYPE_REGISTRATION );

        assertThat( surveys.size() ).isEqualTo( 1 );
        assertThat( surveys.get( 0 ) ).isNotNull();
        assertThat( surveys.get( 0 ).getGefCptFile().getValidationErrors() ).hasSize( 751 );
        assertThat( surveys.get( 0 ).getGefCptFile().getValidationErrors().get( 0 ) ).matches( ".*GEF#COLUMNINFO diepte.*" );

    }

    // artf52528
    @Test
    public void testEscapeSequences() throws IOException {

        // - prepare
        String cptFileName = "test-artf52528.gef";
        byte[] cptFileContent = TestUtil.getFileBytes( "/" + cptFileName );

        GefUnmarshaller unmarshaller = new GefParserImpl();
        GefCptFile cptFile = (GefCptFile) unmarshaller.unmarshall( cptFileName, cptFileContent, TRANSACTION_TYPE_REGISTRATION );

        GefCptSurvey survey = new GefCptSurvey();
        survey.setGefCptFile( cptFile );

        // - verify
        assertThat( survey.getGefCptFile().getParseErrors() ).isEmpty();

    }

    // artf52619
    @Test
    public void testKwaliteitsKlasseTovSondeerNorm() throws IOException {

        // - prepare
        String cptFileName = "test-artf52619.gef";
        byte[] cptFileContent = TestUtil.getFileBytes( "/" + cptFileName );

        GefUnmarshaller unmarshaller = new GefParserImpl();
        GefCptFile cptFile = (GefCptFile) unmarshaller.unmarshall( cptFileName, cptFileContent, TRANSACTION_TYPE_REGISTRATION );

        GefCptSurvey survey = new GefCptSurvey();
        survey.setGefCptFile( cptFile );

        // - verify
        assertThat( survey.getGefCptFile().getParseErrors() ).isEmpty();

        // - prepare
        GefSurveySet surveySet = new GefSurveySet();
        surveySet.addFile( cptFile );

        // - verify
        List<GefCptSurvey> surveys = surveySet.getGefSurveyList( IMBROA, TRANSACTION_TYPE_REGISTRATION );
        GefSurveySet.validateConsistency( surveys );
        GefSurveySet.validateContent( surveys, TRANSACTION_TYPE_REGISTRATION );

        assertThat( surveys.get( 0 ).getGefCptFile().getValidationErrors() ).hasSize( 0 );

    }

}
