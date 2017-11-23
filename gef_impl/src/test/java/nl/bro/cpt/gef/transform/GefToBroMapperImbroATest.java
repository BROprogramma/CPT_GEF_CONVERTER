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
package nl.bro.cpt.gef.transform;

import static nl.bro.cpt.gef.transform.MappingConstants.IMBROA;
import static nl.bro.cpt.gef.transform.MappingConstants.TRANSACTION_TYPE_REGISTRATION;
import static nl.bro.dto.gef.testutil.reflectionassert.MyReflectionAssert.assertReflectionEquals;
import static org.fest.assertions.Assertions.assertThat;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import org.junit.Test;

import nl.bro.cpt.gef.dto.GefCptFile;
import nl.bro.cpt.gef.dto.GefCptSurvey;
import nl.bro.cpt.gef.dto.GefFile;
import nl.bro.cpt.gef.dto.SpecimenVar;
import nl.bro.cpt.gef.logic.GefSurveySet;
import nl.bro.cpt.gef.logic.GefUnmarshaller;
import nl.bro.cpt.gef.logic.impl.GefParserImpl;
import nl.bro.dto.gef.testutil.TestUtil;
import nl.broservices.xsd.brocommon.v_3_0.IndicationYesNoUnknownEnumeration;
import nl.broservices.xsd.cptcommon.v_1_1.CPTMethodType;
import nl.broservices.xsd.cptcommon.v_1_1.CPTStandardType;
import nl.broservices.xsd.cptcommon.v_1_1.ProcessingType;
import nl.broservices.xsd.cptcommon.v_1_1.QualityClassType;
import nl.broservices.xsd.iscpt.v_1_1.GeotechnicalCPTSurveyType;

public class GefToBroMapperImbroATest {

    @Test
    public void testBroYNUnknownMapping() {

        IndicationYesNoUnknownEnumeration value = GefToBroMapperImbroA.getInstance().getYNValueWithUnknown( "Nee" );
        assertThat( value ).isEqualTo( IndicationYesNoUnknownEnumeration.NEE );

        value = GefToBroMapperImbroA.getInstance().getYNValueWithUnknown( "NEE" );
        assertThat( value ).isEqualTo( IndicationYesNoUnknownEnumeration.NEE );

        value = GefToBroMapperImbroA.getInstance().getYNValueWithUnknown( "nee" );
        assertThat( value ).isEqualTo( IndicationYesNoUnknownEnumeration.NEE );

        value = GefToBroMapperImbroA.getInstance().getYNValueWithUnknown( "ja" );
        assertThat( value ).isEqualTo( IndicationYesNoUnknownEnumeration.JA );

        value = GefToBroMapperImbroA.getInstance().getYNValueWithUnknown( "Ja" );
        assertThat( value ).isEqualTo( IndicationYesNoUnknownEnumeration.JA );

        value = GefToBroMapperImbroA.getInstance().getYNValueWithUnknown( "JA" );
        assertThat( value ).isEqualTo( IndicationYesNoUnknownEnumeration.JA );

        value = GefToBroMapperImbroA.getInstance().getYNValueWithUnknown( "onbekend" );
        assertThat( value ).isEqualTo( IndicationYesNoUnknownEnumeration.ONBEKEND );

        value = GefToBroMapperImbroA.getInstance().getYNValueWithUnknown( "ONBEKEND" );
        assertThat( value ).isEqualTo( IndicationYesNoUnknownEnumeration.ONBEKEND );

        value = GefToBroMapperImbroA.getInstance().getYNValueWithUnknown( "Rexicoricofallipatorious" );
        assertThat( value ).isEqualTo( IndicationYesNoUnknownEnumeration.JA );
    }

    // artf-51903/51904
    @Test
    public void testProcessingMapping() {

        GefCptFile cptFile = new GefCptFile();

        // expert correction (wordt niet gemapped in ImbroA!)
        cptFile.setMeasurementText110( "ja" );
        // signal processing
        cptFile.setMeasurementText20( "ja" );
        // process interuptions
        cptFile.setMeasurementText21( "ja" );

        ProcessingType processing = GefToBroMapperImbroA.getInstance().gefToProcessing( cptFile );

        assertThat( processing ).isNotNull();
        assertThat( processing.getExpertCorrectionPerformed() ).isEqualTo( IndicationYesNoUnknownEnumeration.ONBEKEND );
        assertThat( processing.getInterruptionProcessingPerformed() ).isEqualTo( IndicationYesNoUnknownEnumeration.JA );
        assertThat( processing.getSignalProcessingPerformed() ).isEqualTo( IndicationYesNoUnknownEnumeration.JA );

        processing = null;

        // expert correction (wordt niet gemapped in ImbroA!)
        cptFile.setMeasurementText110( "nee" );
        // signal processing
        cptFile.setMeasurementText20( "nee" );
        // process interuptions
        cptFile.setMeasurementText21( "nee" );

        processing = GefToBroMapperImbroA.getInstance().gefToProcessing( cptFile );

        assertThat( processing ).isNotNull();
        assertThat( processing.getExpertCorrectionPerformed() ).isEqualTo( IndicationYesNoUnknownEnumeration.ONBEKEND );
        assertThat( processing.getInterruptionProcessingPerformed() ).isEqualTo( IndicationYesNoUnknownEnumeration.NEE );
        assertThat( processing.getSignalProcessingPerformed() ).isEqualTo( IndicationYesNoUnknownEnumeration.NEE );
    }

    // artf-51903/51904
    @Test
    public void testReadGefWithInvalidProcessingMappingResults() throws IOException {
        // MEASUREMENTTEXT 20 = nee (signaalbewerking)
        // MEASUREMENTTEXT 21 = nee (bewerking onderbreking)
        // MEASUREMENTTEXT 110 = ja (expert correctie)
        String cptFileName = "CPT-F3b-i15-00001-registration-IMBRO-completion-CPT.gef";
        byte[] cptFileContent = TestUtil.getFileBytes( "/" + cptFileName );

        GefUnmarshaller unmarshaller = new GefParserImpl();
        GefCptFile cptFile = (GefCptFile) unmarshaller.unmarshall( cptFileName, cptFileContent, TRANSACTION_TYPE_REGISTRATION );

        // signaal bewerking
        assertThat( cptFile.getMeasurementText20() ).isEqualTo( "nee" );
        // bewerking onderbreking
        assertThat( cptFile.getMeasurementText21() ).isEqualTo( "nee" );
        // expert correctie
        assertThat( cptFile.getMeasurementText110() ).isEqualTo( "ja" );

        ProcessingType processing = GefToBroMapperImbro.getInstance().gefToProcessing( cptFile );

        assertThat( processing ).isNotNull();
        assertThat( processing.getSignalProcessingPerformed() ).isEqualTo( IndicationYesNoUnknownEnumeration.NEE );
        assertThat( processing.getInterruptionProcessingPerformed() ).isEqualTo( IndicationYesNoUnknownEnumeration.NEE );
        assertThat( processing.getExpertCorrectionPerformed() ).isEqualTo( IndicationYesNoUnknownEnumeration.JA );
    }

    @Test
    public void testMeasurementText6kwaliteitsklasseMapper() {
        GefCptSurvey survey = new GefCptSurvey();
        GefCptFile cptFile = new GefCptFile();
        survey.setGefCptFile( cptFile );

        cptFile.setMeasurementText6kwaliteitsklasse( "1" );
        QualityClassType qc = GefToBroMapperImbroA.getInstance().getQualityClass( survey );
        assertThat( qc.getValue() ).isEqualTo( "klasse1" );

        cptFile.setMeasurementText6kwaliteitsklasse( "2" );
        qc = GefToBroMapperImbroA.getInstance().getQualityClass( survey );
        assertThat( qc.getValue() ).isEqualTo( "klasse2" );

        cptFile.setMeasurementText6kwaliteitsklasse( "3" );
        qc = GefToBroMapperImbroA.getInstance().getQualityClass( survey );
        assertThat( qc.getValue() ).isEqualTo( "klasse3" );

        cptFile.setMeasurementText6kwaliteitsklasse( "4" );
        qc = GefToBroMapperImbroA.getInstance().getQualityClass( survey );
        assertThat( qc.getValue() ).isEqualTo( "klasse4" );

        cptFile.setMeasurementText6kwaliteitsklasse( "5" );
        qc = GefToBroMapperImbroA.getInstance().getQualityClass( survey );
        assertThat( qc.getValue() ).isEqualTo( "klasse5" );

        cptFile.setMeasurementText6kwaliteitsklasse( "6" );
        qc = GefToBroMapperImbroA.getInstance().getQualityClass( survey );
        assertThat( qc.getValue() ).isEqualTo( "klasse6" );

        cptFile.setMeasurementText6kwaliteitsklasse( "" );
        qc = GefToBroMapperImbroA.getInstance().getQualityClass( survey );
        assertThat( qc.getValue() ).isEqualTo( "onbekend" );

        cptFile.setMeasurementText6kwaliteitsklasse( "Raxacoricofallipatorius" );
        qc = GefToBroMapperImbroA.getInstance().getQualityClass( survey );
        assertThat( qc.getValue() ).isEqualTo( "onbekend" );

        cptFile.setMeasurementText6sondeernorm( "3680" );
        cptFile.setMeasurementText6kwaliteitsklasse( "1" );
        qc = GefToBroMapperImbroA.getInstance().getQualityClass( survey );
        assertThat( qc.getValue() ).isEqualTo( "nvt" );

        cptFile.setMeasurementText6kwaliteitsklasse( "2" );
        qc = GefToBroMapperImbroA.getInstance().getQualityClass( survey );
        assertThat( qc.getValue() ).isEqualTo( "nvt" );
    }

    @Test
    public void testMeasurementText6SondeernormMapper() {

        CPTStandardType standard = GefToBroMapperImbroA.getInstance().getStandard( "3680" );
        assertThat( standard.getValue() ).isEqualTo( "NEN3680" );

        standard = GefToBroMapperImbroA.getInstance().getStandard( "5140" );
        assertThat( standard.getValue() ).isEqualTo( "NEN5140" );

        standard = GefToBroMapperImbroA.getInstance().getStandard( "22476-1" );
        assertThat( standard.getValue() ).isEqualTo( "ISO22476D1" );

        standard = GefToBroMapperImbroA.getInstance().getStandard( "22476-12" );
        assertThat( standard.getValue() ).isEqualTo( "ISO22476D12" );

        standard = GefToBroMapperImbroA.getInstance().getStandard( "Raxacoricofallipatorius" );
        assertThat( standard.getValue() ).isEqualTo( "onbekend" );

        standard = GefToBroMapperImbroA.getInstance().getStandard( "onbekend" );
        assertThat( standard.getValue() ).isEqualTo( "onbekend" );

        standard = GefToBroMapperImbroA.getInstance().getStandard( "" );
        assertThat( standard.getValue() ).isEqualTo( "onbekend" );

        standard = GefToBroMapperImbroA.getInstance().getStandard( null );
        assertThat( standard.getValue() ).isEqualTo( "onbekend" );
    }

    @Test
    public void testGetMethod() {

        CPTMethodType method = GefToBroMapperImbroA.getInstance().getMethod( "0" );
        assertThat( method.getValue() ).isEqualTo( "elektrisch" );

        method = GefToBroMapperImbroA.getInstance().getMethod( "1" );
        assertThat( method.getValue() ).isEqualTo( "mechanischDiscontinu" );

        method = GefToBroMapperImbroA.getInstance().getMethod( "2" );
        assertThat( method.getValue() ).isEqualTo( "mechanischContinu" );

        method = GefToBroMapperImbroA.getInstance().getMethod( "3" );
        assertThat( method.getValue() ).isEqualTo( "elektrischDiscontinu" );

        method = GefToBroMapperImbroA.getInstance().getMethod( "4" );
        assertThat( method.getValue() ).isEqualTo( "elektrischContinu" );

        method = GefToBroMapperImbroA.getInstance().getMethod( "5" );
        assertThat( method.getValue() ).isEqualTo( "mechanisch" );

        method = GefToBroMapperImbroA.getInstance().getMethod( "" );
        assertThat( method.getValue() ).isEqualTo( "onbekend" );

        method = GefToBroMapperImbroA.getInstance().getMethod( null );
        assertThat( method.getValue() ).isEqualTo( "onbekend" );

    }

    @Test
    public void testAdditionalInvestigationPerformedImbroA() {
        GefCptSurvey survey = new GefCptSurvey();
        GefCptFile cptFile = new GefCptFile();
        survey.setGefCptFile( cptFile );

        cptFile.setMeasurementText11( "dinges" );
        cptFile.setMeasurementVar14( BigDecimal.ONE );
        List<SpecimenVar> spaceymen = new ArrayList<>();
        spaceymen.add( new SpecimenVar() );
        cptFile.setSpecimenVar( spaceymen );

        IndicationYesNoUnknownEnumeration value = GefToBroMapperImbroA.getInstance().getAdditionalInvestigationPerformed( survey );
        assertThat( value ).isEqualTo( IndicationYesNoUnknownEnumeration.JA );

        cptFile.setMeasurementVar14( null );
        value = GefToBroMapperImbroA.getInstance().getAdditionalInvestigationPerformed( survey );
        assertThat( value ).isEqualTo( IndicationYesNoUnknownEnumeration.JA );

        cptFile.setMeasurementText11( null );
        value = GefToBroMapperImbroA.getInstance().getAdditionalInvestigationPerformed( survey );
        assertThat( value ).isEqualTo( IndicationYesNoUnknownEnumeration.JA );

        cptFile.setSpecimenVar( null );
        value = GefToBroMapperImbroA.getInstance().getAdditionalInvestigationPerformed( survey );
        assertThat( value ).isEqualTo( IndicationYesNoUnknownEnumeration.ONBEKEND );

        cptFile.setMeasurementVar14( BigDecimal.ONE );
        value = GefToBroMapperImbroA.getInstance().getAdditionalInvestigationPerformed( survey );
        assertThat( value ).isEqualTo( IndicationYesNoUnknownEnumeration.JA );

        cptFile.setMeasurementText11( "dinges" );
        value = GefToBroMapperImbroA.getInstance().getAdditionalInvestigationPerformed( survey );
        assertThat( value ).isEqualTo( IndicationYesNoUnknownEnumeration.JA );

        cptFile.setMeasurementVar14( null );
        value = GefToBroMapperImbroA.getInstance().getAdditionalInvestigationPerformed( survey );
        assertThat( value ).isEqualTo( IndicationYesNoUnknownEnumeration.JA );

    }

    // artf-51666 : transformatie
    @Test
    public void testInvalidDissFileTransformation() throws IOException {

        String disFileName = "CORR_CORR_GEF_JW_IMBROA_DISS.GEF";
        byte[] disFileContent = TestUtil.getFileBytes( "/" + disFileName );

        GefUnmarshaller unmarshaller = new GefParserImpl();
        GefFile disFile = unmarshaller.unmarshall( disFileName, disFileContent, TRANSACTION_TYPE_REGISTRATION );
        assertThat( disFile.getParseErrors() ).isEmpty();
        assertThat( disFile.getValidationErrors().isEmpty() );

        GefSurveySet surveySet = new GefSurveySet();
        surveySet.addFile( disFile );

        List<GefCptSurvey> surveys = surveySet.getGefSurveyList( IMBROA, TRANSACTION_TYPE_REGISTRATION );
        assertThat( surveys.size() ).isEqualTo( 1 );
        GeotechnicalCPTSurveyType cptSurvey = GefToBroMapperImbroA.getInstance().createSurvey( surveys.get( 0 ) );
        assertThat( cptSurvey ).isNotNull();

    }

    // artf52190
    @Test
    public void testWithWrongAdditionalInvestigation() throws IOException {

        // - prepare
        String cptFileName = "test-artf52190.gef";
        byte[] cptFileContent = TestUtil.getFileBytes( "/" + cptFileName );

        GefUnmarshaller unmarshaller = new GefParserImpl();
        GefCptFile cptFile = (GefCptFile) unmarshaller.unmarshall( cptFileName, cptFileContent, TRANSACTION_TYPE_REGISTRATION );

        GefCptSurvey survey = new GefCptSurvey();
        survey.setGefCptFile( cptFile );

        // - action
        GeotechnicalCPTSurveyType imbroASurvey = GefToBroMapperImbroA.getInstance().createSurvey( survey );

        // - verify
        assertThat( imbroASurvey.getAdditionalInvestigation() ).isNotNull();

        // -- verify entire survey
        GeotechnicalCPTSurveyType controlSurvey = TestUtil.fetchXml( "/test-artf52190.xml", GeotechnicalCPTSurveyType.class );
        controlSurvey.getConePenetrometerSurvey().getDissipationTest();
        assertReflectionEquals( controlSurvey, imbroASurvey );

    }

    // artf52193. Let op, dit is geen probleem in deze GEF Reader maar hoogstwaarschijnlijk in de GEF Writer
    // echter de toegevoegde testcase is waardevol en daarom als additionele test aanwezig gelaten.
    @Test
    public void testWitNoDisStartTime() throws IOException {

        // - prepare
        String cptFileName = "test-artf52193cpt.gef";
        byte[] cptFileContent = TestUtil.getFileBytes( "/" + cptFileName );

        String disFileName = "test-artf52193dis.gef";
        byte[] disFileContent = TestUtil.getFileBytes( "/" + disFileName );

        GefUnmarshaller unmarshaller = new GefParserImpl();

        // -action
        GefCptFile cptFile = (GefCptFile) unmarshaller.unmarshall( cptFileName, cptFileContent, TRANSACTION_TYPE_REGISTRATION );

        // -verify
        assertThat( cptFile.getParseErrors() ).isEmpty();
        assertThat( cptFile.getValidationErrors().isEmpty() );

        // -action
        GefFile disFile = unmarshaller.unmarshall( disFileName, disFileContent, TRANSACTION_TYPE_REGISTRATION );

        // -verify
        assertThat( disFile.getParseErrors() ).isEmpty();
        assertThat( disFile.getValidationErrors().isEmpty() );

        GefSurveySet surveySet = new GefSurveySet();
        surveySet.addFile( cptFile );
        surveySet.addFile( disFile );

        List<GefCptSurvey> surveys = surveySet.getGefSurveyList( IMBROA, TRANSACTION_TYPE_REGISTRATION );
        GefSurveySet.validateConsistency( surveys );
        GefSurveySet.validateContent( surveys, TRANSACTION_TYPE_REGISTRATION );
        // - action
        GeotechnicalCPTSurveyType cptSurvey = GefToBroMapperImbroA.getInstance().createSurvey( surveys.get( 0 ) );

        // - verify
        assertThat( cptSurvey.getConePenetrometerSurvey().getDissipationTest() ).hasSize( 1 );
        assertThat( cptSurvey.getConePenetrometerSurvey().getDissipationTest().get( 0 ).getResultTime() ).isNotNull();
        assertThat( cptSurvey.getConePenetrometerSurvey().getDissipationTest().get( 0 ).getPhenomenonTime() ).isNotNull();

        assertThat( surveys.get( 0 ).getGefCptFile().getValidationErrors() ).isEmpty();
        assertThat( surveys.get( 0 ).getGefDisFiles().get( 0 ).getValidationErrors() ).isEmpty();

        // -- verify entire survey
        GeotechnicalCPTSurveyType controlSurvey = TestUtil.fetchXml( "/test-artf52193.xml", GeotechnicalCPTSurveyType.class );
        controlSurvey.getConePenetrometerSurvey().getDissipationTest();
        assertReflectionEquals( controlSurvey, cptSurvey );
    }

    // artf52288
    @Test
    public void testEndDepth() throws IOException {

        // - prepare
        String cptFileName = "test-artf52288.gef";
        byte[] cptFileContent = TestUtil.getFileBytes( "/" + cptFileName );

        GefUnmarshaller unmarshaller = new GefParserImpl();
        GefCptFile cptFile = (GefCptFile) unmarshaller.unmarshall( cptFileName, cptFileContent, TRANSACTION_TYPE_REGISTRATION );

        GefCptSurvey survey = new GefCptSurvey();
        survey.setGefCptFile( cptFile );

        // - action
        GeotechnicalCPTSurveyType imbroASurvey = GefToBroMapperImbroA.getInstance().createSurvey( survey );

        // - verify
        assertThat( imbroASurvey.getConePenetrometerSurvey() ).isNotNull();
        assertThat( imbroASurvey.getConePenetrometerSurvey().getTrajectory() ).isNotNull();
        assertThat( imbroASurvey.getConePenetrometerSurvey().getTrajectory().getFinalDepth().getValue() ).isEqualTo( new BigDecimal( "54.620" ) );

        // -- verify entire survey
        GeotechnicalCPTSurveyType controlSurvey = TestUtil.fetchXml( "/test-artf52288.xml", GeotechnicalCPTSurveyType.class );
        controlSurvey.getConePenetrometerSurvey().getDissipationTest();
        assertReflectionEquals( controlSurvey, imbroASurvey );
    }

    @Test
    public void testEndDepthRowOrder() throws IOException {

        // - prepare
        String cptFileName = "test-artf52288b.gef";
        byte[] cptFileContent = TestUtil.getFileBytes( "/" + cptFileName );

        GefUnmarshaller unmarshaller = new GefParserImpl();
        GefCptFile cptFile = (GefCptFile) unmarshaller.unmarshall( cptFileName, cptFileContent, TRANSACTION_TYPE_REGISTRATION );

        GefCptSurvey survey = new GefCptSurvey();
        survey.setGefCptFile( cptFile );

        // - action
        GeotechnicalCPTSurveyType imbroASurvey = GefToBroMapperImbroA.getInstance().createSurvey( survey );

        // - verify
        assertThat( imbroASurvey.getConePenetrometerSurvey() ).isNotNull();
        assertThat( imbroASurvey.getConePenetrometerSurvey().getTrajectory() ).isNotNull();
        assertThat( imbroASurvey.getConePenetrometerSurvey().getTrajectory().getFinalDepth().getValue() ).isEqualTo( new BigDecimal( "54.620" ) );

        // -- verify entire survey
        GeotechnicalCPTSurveyType controlSurvey = TestUtil.fetchXml( "/test-artf52288b.xml", GeotechnicalCPTSurveyType.class );
        controlSurvey.getConePenetrometerSurvey().getDissipationTest();
        assertReflectionEquals( controlSurvey, imbroASurvey );
    }

    @Test
    public void testEndDepthNoDataBlockOnlyLength() throws IOException {

        // - prepare
        String cptFileName = "test-artf52288c.gef";
        byte[] cptFileContent = TestUtil.getFileBytes( "/" + cptFileName );

        GefUnmarshaller unmarshaller = new GefParserImpl();
        GefCptFile cptFile = (GefCptFile) unmarshaller.unmarshall( cptFileName, cptFileContent, TRANSACTION_TYPE_REGISTRATION );

        GefCptSurvey survey = new GefCptSurvey();
        survey.setGefCptFile( cptFile );

        // - action
        GeotechnicalCPTSurveyType imbroASurvey = GefToBroMapperImbroA.getInstance().createSurvey( survey );

        // - verify
        assertThat( imbroASurvey.getConePenetrometerSurvey() ).isNotNull();
        assertThat( imbroASurvey.getConePenetrometerSurvey().getTrajectory() ).isNotNull();
        assertThat( imbroASurvey.getConePenetrometerSurvey().getTrajectory().getFinalDepth().getValue() ).isEqualTo( new BigDecimal( "55.090" ) );

        // -- verify entire survey
        GeotechnicalCPTSurveyType controlSurvey = TestUtil.fetchXml( "/test-artf52288c.xml", GeotechnicalCPTSurveyType.class );
        controlSurvey.getConePenetrometerSurvey().getDissipationTest();
        assertReflectionEquals( controlSurvey, imbroASurvey );
    }
}
