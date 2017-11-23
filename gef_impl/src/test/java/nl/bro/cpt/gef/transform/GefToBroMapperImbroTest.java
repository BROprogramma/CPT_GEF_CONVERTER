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

import static nl.bro.cpt.gef.transform.MappingConstants.IMBRO;
import static nl.bro.cpt.gef.transform.MappingConstants.TRANSACTION_TYPE_CORRECTION;
import static nl.bro.cpt.gef.transform.MappingConstants.TRANSACTION_TYPE_REGISTRATION;
import static nl.bro.dto.gef.testutil.reflectionassert.MyReflectionAssert.assertReflectionEquals;
import static org.fest.assertions.Assertions.assertThat;

import java.io.IOException;
import java.util.List;

import org.junit.Test;

import nl.bro.cpt.gef.dto.GefCptFile;
import nl.bro.cpt.gef.dto.GefCptSurvey;
import nl.bro.cpt.gef.dto.GefFile;
import nl.bro.cpt.gef.logic.GefSurveySet;
import nl.bro.cpt.gef.logic.GefUnmarshaller;
import nl.bro.cpt.gef.logic.impl.GefParserImpl;
import nl.bro.dto.gef.testutil.TestUtil;
import nl.broservices.xsd.brocommon.v_3_0.IndicationYesNoUnknownEnumeration;
import nl.broservices.xsd.cptcommon.v_1_1.CPTMethodType;
import nl.broservices.xsd.cptcommon.v_1_1.CPTStandardType;
import nl.broservices.xsd.cptcommon.v_1_1.DeliveryContextType;
import nl.broservices.xsd.cptcommon.v_1_1.ProcessingType;
import nl.broservices.xsd.cptcommon.v_1_1.QualityClassType;
import nl.broservices.xsd.cptcommon.v_1_1.SurveyPurposeType;
import nl.broservices.xsd.iscpt.v_1_1.GeotechnicalCPTSurveyType;

public class GefToBroMapperImbroTest {

    @Test
    public void testBroYNUnknownMapping() {

        IndicationYesNoUnknownEnumeration value = GefToBroMapperImbro.getInstance().getYNValueWithUnknown( "Nee" );
        assertThat( value ).isEqualTo( IndicationYesNoUnknownEnumeration.NEE );

        value = GefToBroMapperImbro.getInstance().getYNValueWithUnknown( "NEE" );
        assertThat( value ).isEqualTo( IndicationYesNoUnknownEnumeration.NEE );

        value = GefToBroMapperImbro.getInstance().getYNValueWithUnknown( "nee" );
        assertThat( value ).isEqualTo( IndicationYesNoUnknownEnumeration.NEE );

        value = GefToBroMapperImbro.getInstance().getYNValueWithUnknown( "ja" );
        assertThat( value ).isEqualTo( IndicationYesNoUnknownEnumeration.JA );

        value = GefToBroMapperImbro.getInstance().getYNValueWithUnknown( "Ja" );
        assertThat( value ).isEqualTo( IndicationYesNoUnknownEnumeration.JA );

        value = GefToBroMapperImbro.getInstance().getYNValueWithUnknown( "JA" );
        assertThat( value ).isEqualTo( IndicationYesNoUnknownEnumeration.JA );

        value = GefToBroMapperImbro.getInstance().getYNValueWithUnknown( "onbekend" );
        assertThat( value ).isNull();

        value = GefToBroMapperImbro.getInstance().getYNValueWithUnknown( "Raxacoricofallapatorius" );
        assertThat( value ).isNull();
    }

    // artf-51903/51904
    @Test
    public void testProcessingMapping() {

        GefCptSurvey survey = new GefCptSurvey();
        GefCptFile cptFile = new GefCptFile();
        survey.setGefCptFile( cptFile );

        // expert correction
        cptFile.setMeasurementText110( "ja" );
        // signal processing
        cptFile.setMeasurementText20( "ja" );
        // process interuptions
        cptFile.setMeasurementText21( "ja" );

        ProcessingType processing = GefToBroMapperImbro.getInstance().gefToProcessing( cptFile );

        assertThat( processing ).isNotNull();
        assertThat( processing.getExpertCorrectionPerformed() ).isEqualTo( IndicationYesNoUnknownEnumeration.JA );
        assertThat( processing.getInterruptionProcessingPerformed() ).isEqualTo( IndicationYesNoUnknownEnumeration.JA );
        assertThat( processing.getSignalProcessingPerformed() ).isEqualTo( IndicationYesNoUnknownEnumeration.JA );

        processing = null;

        // expert correction
        cptFile.setMeasurementText110( "nee" );
        // signal processing
        cptFile.setMeasurementText20( "nee" );
        // process interuptions
        cptFile.setMeasurementText21( "Nee" );

        processing = GefToBroMapperImbro.getInstance().gefToProcessing( cptFile );

        assertThat( processing ).isNotNull();
        assertThat( processing.getExpertCorrectionPerformed() ).isEqualTo( IndicationYesNoUnknownEnumeration.NEE );
        assertThat( processing.getInterruptionProcessingPerformed() ).isEqualTo( IndicationYesNoUnknownEnumeration.NEE );
        assertThat( processing.getSignalProcessingPerformed() ).isEqualTo( IndicationYesNoUnknownEnumeration.NEE );
    }

    @Test
    public void testMeasurementText6kwaliteitsklasseMapper() {
        GefCptSurvey survey = new GefCptSurvey();
        GefCptFile cptFile = new GefCptFile();
        survey.setGefCptFile( cptFile );

        cptFile.setMeasurementText6kwaliteitsklasse( "1" );
        QualityClassType value = GefToBroMapperImbro.getInstance().getQualityClass( survey );
        assertThat( value.getValue() ).isEqualTo( "klasse1" );

        cptFile.setMeasurementText6kwaliteitsklasse( "2" );
        value = GefToBroMapperImbro.getInstance().getQualityClass( survey );
        assertThat( value.getValue() ).isEqualTo( "klasse2" );

        cptFile.setMeasurementText6kwaliteitsklasse( "3" );
        value = GefToBroMapperImbro.getInstance().getQualityClass( survey );
        assertThat( value.getValue() ).isEqualTo( "klasse3" );

        cptFile.setMeasurementText6kwaliteitsklasse( "4" );
        value = GefToBroMapperImbro.getInstance().getQualityClass( survey );
        assertThat( value.getValue() ).isEqualTo( "klasse4" );

        cptFile.setMeasurementText6kwaliteitsklasse( "5" );
        value = GefToBroMapperImbro.getInstance().getQualityClass( survey );
        assertThat( value.getValue() ).isEqualTo( "klasse5" );

        cptFile.setMeasurementText6kwaliteitsklasse( "6" );
        value = GefToBroMapperImbro.getInstance().getQualityClass( survey );
        assertThat( value.getValue() ).isEqualTo( "klasse6" );

        cptFile.setMeasurementText6kwaliteitsklasse( "7" );
        value = GefToBroMapperImbro.getInstance().getQualityClass( survey );
        assertThat( value.getValue() ).isEqualTo( "klasse7" );

        cptFile.setMeasurementText6kwaliteitsklasse( "" );
        value = GefToBroMapperImbro.getInstance().getQualityClass( survey );
        assertThat( value ).isNull();

        cptFile.setMeasurementText6kwaliteitsklasse( "Raxacoricofallipatorius" );
        value = GefToBroMapperImbro.getInstance().getQualityClass( survey );
        assertThat( value ).isNull();
    }

    @Test
    public void testMeasurementText6SondeernormMapper() {

        CPTStandardType standard = GefToBroMapperImbro.getInstance().getStandard( "3680" );
        assertThat( standard ).isNull();

        standard = GefToBroMapperImbro.getInstance().getStandard( "5140" );
        assertThat( standard.getValue() ).isEqualTo( "NEN5140" );

        standard = GefToBroMapperImbro.getInstance().getStandard( "22476-1" );
        assertThat( standard.getValue() ).isEqualTo( "ISO22476D1" );

        standard = GefToBroMapperImbro.getInstance().getStandard( "22476-12" );
        assertThat( standard.getValue() ).isEqualTo( "ISO22476D12" );

        standard = GefToBroMapperImbro.getInstance().getStandard( "Raxacoricofallipatorius" );
        assertThat( standard ).isNull();

        standard = GefToBroMapperImbro.getInstance().getStandard( "onbekend" );
        assertThat( standard ).isNull();

        standard = GefToBroMapperImbro.getInstance().getStandard( "" );
        assertThat( standard ).isNull();

        standard = GefToBroMapperImbro.getInstance().getStandard( null );
        assertThat( standard ).isNull();
    }

    @Test
    public void testGetMethodImbro() {

        CPTMethodType method = GefToBroMapperImbro.getInstance().getMethod( "0" );
        assertThat( method ).isNull();

        method = GefToBroMapperImbro.getInstance().getMethod( "1" );
        assertThat( method.getValue() ).isEqualTo( "mechanischDiscontinu" );

        method = GefToBroMapperImbro.getInstance().getMethod( "2" );
        assertThat( method.getValue() ).isEqualTo( "mechanischContinu" );

        method = GefToBroMapperImbro.getInstance().getMethod( "3" );
        assertThat( method.getValue() ).isEqualTo( "elektrischDiscontinu" );

        method = GefToBroMapperImbro.getInstance().getMethod( "4" );
        assertThat( method.getValue() ).isEqualTo( "elektrischContinu" );

        method = GefToBroMapperImbro.getInstance().getMethod( "5" );
        assertThat( method ).isNull();

        method = GefToBroMapperImbro.getInstance().getMethod( "" );
        assertThat( method ).isNull();

        method = GefToBroMapperImbro.getInstance().getMethod( null );
        assertThat( method ).isNull();
    }

    @Test
    public void testGetSurveyPurpose() {

        SurveyPurposeType surveyPurpose = GefToBroMapperImbro.getInstance().getSurveyPurpose( "waterkering" );
        assertThat( surveyPurpose.getValue() ).isEqualTo( "waterkering" );

        surveyPurpose = GefToBroMapperImbro.getInstance().getSurveyPurpose( "bouwwerk en constructie" );
        assertThat( surveyPurpose.getValue() ).isEqualTo( "bouwwerkConstructie" );

        surveyPurpose = GefToBroMapperImbro.getInstance().getSurveyPurpose( "infrastructuur land" );
        assertThat( surveyPurpose.getValue() ).isEqualTo( "infrastructuurLand" );

        surveyPurpose = GefToBroMapperImbro.getInstance().getSurveyPurpose( "infrastructuur water" );
        assertThat( surveyPurpose.getValue() ).isEqualTo( "infrastructuurWater" );

        surveyPurpose = GefToBroMapperImbro.getInstance().getSurveyPurpose( "milieuonderzoek" );
        assertThat( surveyPurpose.getValue() ).isEqualTo( "milieuonderzoek" );

        surveyPurpose = GefToBroMapperImbro.getInstance().getSurveyPurpose( "controle onderzoek" );
        assertThat( surveyPurpose.getValue() ).isEqualTo( "controleOnderzoek" );

        surveyPurpose = GefToBroMapperImbro.getInstance().getSurveyPurpose( "vergunning" );
        assertThat( surveyPurpose.getValue() ).isEqualTo( "vergunning" );

        surveyPurpose = GefToBroMapperImbro.getInstance().getSurveyPurpose( "overig onderzoek" );
        assertThat( surveyPurpose.getValue() ).isEqualTo( "overigOnderzoek" );

    }

    @Test
    public void testGetDeliveryContext() {

        DeliveryContextType deliveryContext = GefToBroMapperImbro.getInstance().getDeliveryContext( "rechtsgrond mijnbouwwet" );
        assertThat( deliveryContext.getValue() ).isEqualTo( "MBW" );

        deliveryContext = GefToBroMapperImbro.getInstance().getDeliveryContext( "rechtsgrond waterwet" );
        assertThat( deliveryContext.getValue() ).isEqualTo( "WW" );

        deliveryContext = GefToBroMapperImbro.getInstance().getDeliveryContext( "opdracht publieke taakuitvoering" );
        assertThat( deliveryContext.getValue() ).isEqualTo( "publiekeTaak" );

    }

    @Test
    public void test1() throws IOException {

        // - prepare
        String cptFileName = "GEF_JW_IMBRO.GEF";
        byte[] cptFileContent = TestUtil.getFileBytes( "/" + cptFileName );

        GefUnmarshaller unmarshaller = new GefParserImpl();
        GefCptFile cptFile = (GefCptFile) unmarshaller.unmarshall( cptFileName, cptFileContent, TRANSACTION_TYPE_REGISTRATION );

        GefCptSurvey survey = new GefCptSurvey();
        survey.setGefCptFile( cptFile );

        // - action
        GeotechnicalCPTSurveyType imbroSurvey = GefToBroMapperImbro.getInstance().createSurvey( survey );

        // -- verify entire survey
        GeotechnicalCPTSurveyType controlSurvey = TestUtil.fetchXml( "/GEF_JW_IMBRO.xml", GeotechnicalCPTSurveyType.class );
        controlSurvey.getConePenetrometerSurvey().getDissipationTest(); // init dissipationtest
        assertReflectionEquals( controlSurvey, imbroSurvey );

    }

    @Test
    public void test2() throws IOException {

        // - prepare
        String cptFileName = "12_registration-IMBRO-completion-CPT.GEF";
        byte[] cptFileContent = TestUtil.getFileBytes( "/" + cptFileName );

        GefUnmarshaller unmarshaller = new GefParserImpl();
        GefCptFile cptFile = (GefCptFile) unmarshaller.unmarshall( cptFileName, cptFileContent, TRANSACTION_TYPE_REGISTRATION );

        GefCptSurvey survey = new GefCptSurvey();
        survey.setGefCptFile( cptFile );

        // - action
        GeotechnicalCPTSurveyType imbroSurvey = GefToBroMapperImbro.getInstance().createSurvey( survey );

        // -- verify entire survey
        GeotechnicalCPTSurveyType controlSurvey = TestUtil.fetchXml( "/12_registration-IMBRO-completion-CPT.xml", GeotechnicalCPTSurveyType.class );
        controlSurvey.getConePenetrometerSurvey().getDissipationTest(); // init dissipationtest
        assertReflectionEquals( controlSurvey, imbroSurvey );
    }

    @Test
    public void test3() throws IOException {

        // - prepare
        String cptFileName = "39_correction-IMBRO-completion-CPT.GEF";
        byte[] cptFileContent = TestUtil.getFileBytes( "/" + cptFileName );

        GefUnmarshaller unmarshaller = new GefParserImpl();
        GefCptFile cptFile = (GefCptFile) unmarshaller.unmarshall( cptFileName, cptFileContent, TRANSACTION_TYPE_CORRECTION );

        GefCptSurvey survey = new GefCptSurvey();
        survey.setGefCptFile( cptFile );

        // - action
        GeotechnicalCPTSurveyType imbroSurvey = GefToBroMapperImbro.getInstance().createSurvey( survey );

        // -- verify entire survey
        GeotechnicalCPTSurveyType controlSurvey = TestUtil.fetchXml( "/39_correction-IMBRO-completion-CPT.xml", GeotechnicalCPTSurveyType.class );
        controlSurvey.getConePenetrometerSurvey().getDissipationTest(); // init dissipationtest
        assertReflectionEquals( controlSurvey, imbroSurvey );
    }

    @Test
    public void test4() throws IOException {

        // - prepare
        String cptFileName = "CPT-F3b-i3-23913-completion-CPT.GEF";
        String disFileName = "CPT-F3b-i3-23913-completion-DISS.GEF";
        byte[] cptFileContent = TestUtil.getFileBytes( "/" + cptFileName );
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

        List<GefCptSurvey> surveys = surveySet.getGefSurveyList( IMBRO, TRANSACTION_TYPE_REGISTRATION );
        GefSurveySet.validateConsistency( surveys );
        GefSurveySet.validateContent( surveys, TRANSACTION_TYPE_REGISTRATION );

        // - action
        GeotechnicalCPTSurveyType imbroSurvey = GefToBroMapperImbro.getInstance().createSurvey( surveys.get( 0 ) );

        // - verify
        assertThat( imbroSurvey.getConePenetrometerSurvey().getDissipationTest() ).hasSize( 1 );
        assertThat( surveys.get( 0 ).getGefCptFile().getValidationErrors() ).isEmpty();
        assertThat( surveys.get( 0 ).getGefDisFiles().get( 0 ).getValidationErrors() ).isEmpty();

        // -- verify entire survey
        GeotechnicalCPTSurveyType controlSurvey = TestUtil.fetchXml( "/CPT_F3b-i3-23913-completion-CPT.xml", GeotechnicalCPTSurveyType.class );
        assertReflectionEquals( controlSurvey, imbroSurvey );

    }
}
