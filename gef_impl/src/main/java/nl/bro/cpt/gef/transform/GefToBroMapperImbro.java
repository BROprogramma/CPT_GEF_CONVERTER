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

import static nl.bro.cpt.gef.transform.GefToBroMapperCommon.toLowerCase;
import static nl.bro.cpt.gef.transform.MappingConstants.BRO_IND_NOT_APPLICABLE;
import static nl.bro.cpt.gef.transform.MappingConstants.CON_METHOD_CONV_IMBRO;
import static nl.bro.cpt.gef.transform.MappingConstants.CPT_STANDARD_3680;
import static nl.bro.cpt.gef.transform.MappingConstants.CPT_STANDARD_CONV_IMBRO;
import static nl.bro.cpt.gef.transform.MappingConstants.CPT_VERT_DAT_CONV_IMBRO;
import static nl.bro.cpt.gef.transform.MappingConstants.DEL_CON_CONV_IMBRO;
import static nl.bro.cpt.gef.transform.MappingConstants.LOC_VERT_REF_POINT_CONV_IMBRO;
import static nl.bro.cpt.gef.transform.MappingConstants.METHOD_HOR_POS_CONV_IMBRO;
import static nl.bro.cpt.gef.transform.MappingConstants.QUALITY_CLASS_CONV_IMBRO;
import static nl.bro.cpt.gef.transform.MappingConstants.STOP_CRITERIUM_CONV_IMBRO;
import static nl.bro.cpt.gef.transform.MappingConstants.SURVEY_PURPOSE_CONV_IMBRO;
import static nl.bro.cpt.gef.transform.MappingConstants.VERT_METHOD_CONV_IMBRO;
import static nl.bro.cpt.gef.transform.MappingConstants.YES_NO_CONV_YES_NO;
import static nl.bro.cpt.gef.transform.MappingConstants.YES_NO_CONV_YES_NO_UNKNOWN_IMBRO;

import java.util.List;

import javax.xml.bind.JAXBElement;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;
import org.mapstruct.factory.Mappers;

import nl.bro.cpt.gef.dto.GefCptSurvey;
import nl.bro.cpt.gef.dto.IfcCptFile;
import nl.bro.cpt.gef.dto.IfcDisFile;
import nl.bro.cpt.gef.transform.GefToBroMapperCommon.DoNotSelectMethod;
import nl.broservices.xsd.brocommon.v_3_0.IndicationYesNoEnumeration;
import nl.broservices.xsd.brocommon.v_3_0.IndicationYesNoUnknownEnumeration;
import nl.broservices.xsd.brocommon.v_3_0.PartialDateType;
import nl.broservices.xsd.cptcommon.v_1_1.AdditionalInvestigationType;
import nl.broservices.xsd.cptcommon.v_1_1.CPTMethodType;
import nl.broservices.xsd.cptcommon.v_1_1.CPTStandardType;
import nl.broservices.xsd.cptcommon.v_1_1.ConePenetrationTestType;
import nl.broservices.xsd.cptcommon.v_1_1.ConePenetrometerSurveyType;
import nl.broservices.xsd.cptcommon.v_1_1.ConePenetrometerType;
import nl.broservices.xsd.cptcommon.v_1_1.DeliveredLocationType;
import nl.broservices.xsd.cptcommon.v_1_1.DeliveredVerticalPositionType;
import nl.broservices.xsd.cptcommon.v_1_1.DeliveryContextType;
import nl.broservices.xsd.cptcommon.v_1_1.DissipationTestType;
import nl.broservices.xsd.cptcommon.v_1_1.HorizontalPositioningMethodType;
import nl.broservices.xsd.cptcommon.v_1_1.LocalVerticalReferencePointType;
import nl.broservices.xsd.cptcommon.v_1_1.ObjectFactory;
import nl.broservices.xsd.cptcommon.v_1_1.ProcessingType;
import nl.broservices.xsd.cptcommon.v_1_1.QualityClassType;
import nl.broservices.xsd.cptcommon.v_1_1.StopCriterionType;
import nl.broservices.xsd.cptcommon.v_1_1.SurveyPurposeType;
import nl.broservices.xsd.cptcommon.v_1_1.TrajectoryType;
import nl.broservices.xsd.cptcommon.v_1_1.VerticalDatumType;
import nl.broservices.xsd.cptcommon.v_1_1.VerticalPositioningMethodType;
import nl.broservices.xsd.iscpt.v_1_1.CorrectionRequestType;
import nl.broservices.xsd.iscpt.v_1_1.GeotechnicalCPTSurveyType;
import nl.broservices.xsd.iscpt.v_1_1.RegistrationRequestType;

/**
 * @author derksenjpam, J.M. van Ommeren
 */
@Mapper( unmappedTargetPolicy = ReportingPolicy.ERROR, uses = { GefToBroMapperCommon.class, ObjectFactory.class, nl.broservices.xsd.iscpt.v_1_1.ObjectFactory.class } )
public abstract class GefToBroMapperImbro {

    private static final GefToBroMapperImbro INSTANCE = Mappers.getMapper( GefToBroMapperImbro.class );
    private static final nl.broservices.xsd.cptcommon.v_1_1.ObjectFactory CPT_OF = new nl.broservices.xsd.cptcommon.v_1_1.ObjectFactory();
    private static final TestResultMapper RESULT_MAPPER = new TestResultMapper();
    private static final GefToBroMapperCommon COMMON_MAPPER = GefToBroMapperCommon.getInstance();

    @Mapping( target = "requestReference", source = "requestReference" )
    @Mapping( target = "deliveryAccountableParty", source = "survey.gefCptFile.measurementText101" )
    @Mapping( target = "qualityRegime", constant = "IMBRO" )
    @Mapping( target = "sourceDocument.CPT", source = "survey" )
    @Mapping( target = "broId", ignore = true )
    @Mapping( target = "underPrivilege", ignore = true )
    public abstract RegistrationRequestType gefToRegistrationRequest(GefCptSurvey survey, String requestReference);

    @Mapping( target = "requestReference", source = "requestReference" )
    @Mapping( target = "deliveryAccountableParty", source = "survey.gefCptFile.measurementText101" )
    @Mapping( target = "qualityRegime", constant = "IMBRO" )
    @Mapping( target = "sourceDocument.CPT", source = "survey" )
    @Mapping( target = "broId", source = "survey.gefCptFile.testId" )
    @Mapping( target = "correctionReason", source = "correctionReason" )
    @Mapping( target = "underPrivilege", ignore = true )
    public abstract CorrectionRequestType gefToCorrectionRequest(GefCptSurvey survey, String requestReference, String correctionReason);

    @Mapping( target = "additionalInvestigation", source = "survey" )
    @Mapping( target = "additionalInvestigationPerformed", source = "survey.gefCptFile.measurementText111" )
    @Mapping( target = "conePenetrometerSurvey", source = "survey" )
    @Mapping( target = "deliveredLocation", source = "survey" )
    @Mapping( target = "deliveredVerticalPosition", source = "survey" )
    @Mapping( target = "deliveryContext", source = "survey.gefCptFile.measurementText102" )
    @Mapping( target = "objectIdAccountableParty", source = "survey.gefCptFile" )
    @Mapping( target = "researchOperator", source = "survey.gefCptFile.companyId" )
    @Mapping( target = "researchReportDate", source = "survey.gefCptFile.measurementText112" )
    @Mapping( target = "cptStandard", source = "survey.gefCptFile.measurementText6sondeernorm" )
    @Mapping( target = "surveyPurpose", source = "survey.gefCptFile.measurementText103" )
    protected abstract GeotechnicalCPTSurveyType createSurvey(GefCptSurvey survey);

    @Mapping( target = "horizontalPositioningMethod", source = "survey.gefCptFile.measurementText43" )
    @Mapping( target = "horizontalPositioningDate", source = "survey.gefCptFile.measurementText105" )
    @Mapping( target = "horizontalPositioningOperator", source = "survey.gefCptFile.measurementText104" )
    @Mapping( target = "location", source = "survey.gefCptFile" )
    protected abstract DeliveredLocationType gefToDeliveredLocation(GefCptSurvey survey);

    @Mapping( target = "verticalDatum", source = "survey.gefCptFile.zidVerticalDatum" )
    @Mapping( target = "localVerticalReferencePoint", source = "survey.gefCptFile.measurementText9" )
    @Mapping( target = "verticalPositioningMethod", source = "survey.gefCptFile.measurementText42" )
    @Mapping( target = "verticalPositioningOperator", source = "survey.gefCptFile.measurementText106" )
    @Mapping( target = "offset", source = "survey.gefCptFile.zidOffset" )
    @Mapping( target = "verticalPositioningDate", source = "survey.gefCptFile.measurementText107" )
    @Mapping( target = "waterDepth", source = "survey" )
    protected abstract DeliveredVerticalPositionType gefToDeliveredVerticalPosition(GefCptSurvey survey);

    protected AdditionalInvestigationType additionalInvestigationMapper(GefCptSurvey survey) {
        IndicationYesNoUnknownEnumeration performed = getYNValueWithUnknown( survey.getGefCptFile().getMeasurementText111() );
        if ( IndicationYesNoUnknownEnumeration.JA.equals( performed ) ) {
            return gefToAdditionalInvestigation( survey );
        }
        return null;
    }

    @DoNotSelectMethod
    @Mapping( target = "investigationDate", source = "survey.gefCptFile.measurementText114" )
    @Mapping( target = "groundwaterLevel", source = "survey.gefCptFile.measurementVar14" )
    @Mapping( target = "surfaceDescription", source = "survey.gefCptFile.measurementText108" )
    @Mapping( target = "conditions", source = "survey.gefCptFile.measurementText11" )
    @Mapping( target = "removedLayer", source = "survey.gefCptFile.specimenVar" )
    protected abstract AdditionalInvestigationType gefToAdditionalInvestigation(GefCptSurvey survey);

    @Mapping( target = "finalProcessingDate", source = "survey.gefCptFile.measurementText113" )
    @Mapping( target = "qualityClass", source = "survey" )
    @Mapping( target = "sensorAzimuth", source = "survey.gefCptFile.measurementVar42" )
    @Mapping( target = "cptMethod", source = "survey.gefCptFile.measurementVar12" )
    @Mapping( target = "stopCriterion", source = "survey.gefCptFile.measurementVar17" )
    @Mapping( target = "conePenetrationTest", source = "survey.gefCptFile" )
    @Mapping( target = "conePenetrometer", source = "survey.gefCptFile" )
    @Mapping( target = "dissipationTest", source = "survey.gefDisFiles" )
    @Mapping( target = "dissipationTestPerformed", source = "survey.gefCptFile.measurementText109" )
    @Mapping( target = "trajectory", source = "survey" )
    @Mapping( target = "parameters", source = "survey" )
    @Mapping( target = "procedure", source = "survey.gefCptFile" )
    @Mapping( target = "id", expression = GmlIdGenerator.EXPRESSION )
    @Mapping( target = "type", ignore = true )
    @Mapping( target = "relatedObservation", ignore = true )
    @Mapping( target = "sampledFeature", ignore = true )
    @Mapping( target = "relatedSamplingFeature", ignore = true )
    @Mapping( target = "parameter", ignore = true )
    protected abstract ConePenetrometerSurveyType gefToConePenetrometerSurvey(GefCptSurvey survey);

    @Mapping( target = "expertCorrectionPerformed", source = "cptFile.measurementText110" )
    @Mapping( target = "interruptionProcessingPerformed", source = "cptFile.measurementText21" )
    @Mapping( target = "signalProcessingPerformed", source = "cptFile.measurementText20" )
    protected abstract ProcessingType gefToProcessing(IfcCptFile cptFile);

    protected ConePenetrationTestType gefToConepenetrationTest(IfcCptFile cptFile) {

        ConePenetrationTestType test = CPT_OF.createConePenetrationTestType();
        test.setResultTime( COMMON_MAPPER.gefToTimeInstantProperty( cptFile.getStartDate(), cptFile.getStartTime() ) );
        test.setPhenomenonTime( COMMON_MAPPER.gefToTimeInstantProperty( cptFile.getStartDate(), cptFile.getStartTime() ) );
        test.setCptResult( RESULT_MAPPER.toCPTResultsJaxb( cptFile.getDataBlock() ) );
        test.setId( GmlIdGenerator.getNewId() );

        return test;
    }

    @Mapping( target = "finalDepth", source = "gefCptFile.measurementVar16" )
    @Mapping( target = "predrilledDepth", source = "gefCptFile.measurementVar13" )
    protected abstract TrajectoryType gefToTrajectory(GefCptSurvey survey);

    @Mapping( target = "coneToFrictionSleeveDistance", source = "measurementVar5" )
    @Mapping( target = "coneDiameter", source = "measurementVar130" )
    @Mapping( target = "conePenetrometerType", source = "measurementText4" )
    @Mapping( target = "description", source = "measurementText5" )
    @Mapping( target = "coneSurfaceArea", source = "measurementVar1" )
    @Mapping( target = "frictionSleeveSurfaceArea", source = "measurementVar2" )
    @Mapping( target = "coneSurfaceQuotient", source = "measurementVar3" )
    @Mapping( target = "frictionSleeveSurfaceQuotient", source = "measurementVar4" )
    @Mapping( target = "zeroLoadMeasurement", source = "cptFile" )
    protected abstract ConePenetrometerType gefToConepenetroMeter(IfcCptFile cptFile);

    protected DissipationTestType gefToDissipationTest(IfcDisFile disFile) {

        DissipationTestType test = CPT_OF.createDissipationTestType();
        test.setPhenomenonTime( COMMON_MAPPER.gefToTimeInstantProperty( disFile.getStartDate(), disFile.getStartTime() ) );
        test.setResultTime( COMMON_MAPPER.gefToTimeInstantProperty( disFile.getStartDate(), disFile.getStartTime() ) );
        test.setPenetrationLength( COMMON_MAPPER.convertToConePenetrationDepth( disFile.getParentDepth() ) );
        test.setDisResult( RESULT_MAPPER.toDTResultsJaxb( disFile.getDataBlock() ) );
        test.setId( GmlIdGenerator.getNewId() );

        return test;
    }

    protected abstract List<DissipationTestType> gefToDissipationTestList(List<IfcDisFile> disFiles);

    // survey.getGefCptFile().getMeasurementText6sondeernorm()
    protected CPTStandardType getStandard(String value) {
        return COMMON_MAPPER.toStandardType( CPT_STANDARD_CONV_IMBRO.getOrDefault( toLowerCase( value ), null ) );
    }

    // survey.getGefCptFile().getMeasurementText103();
    protected SurveyPurposeType getSurveyPurpose(String value) {
        return COMMON_MAPPER.toSurveyPurposeType( SURVEY_PURPOSE_CONV_IMBRO.getOrDefault( toLowerCase( value ), null ) );
    }

    // survey.getGefCptFile().getMeasurementText102();
    protected DeliveryContextType getDeliveryContext(String value) {
        return COMMON_MAPPER.toDeliveryContextType( DEL_CON_CONV_IMBRO.getOrDefault( toLowerCase( value ), null ) );
    }

    // survey.getGefCptFile().getMeasurementText43();
    protected HorizontalPositioningMethodType getHorizontalPositioningMethod(String value) {
        return COMMON_MAPPER.toHorizontalPositioningMethodType( METHOD_HOR_POS_CONV_IMBRO.getOrDefault( toLowerCase( value ), null ) );
    }

    // survey.getGefCptFile().getMeasurementText9();
    protected LocalVerticalReferencePointType getLocalVerticalReferencePoint(String value) {
        return COMMON_MAPPER.toLocalVerticalReferencePointType( LOC_VERT_REF_POINT_CONV_IMBRO.getOrDefault( toLowerCase( value ), null ) );
    }

    // survey.getGefCptFile().getMeasurementText42();
    protected VerticalPositioningMethodType getVerticalPositioningMethod(String value) {
        return COMMON_MAPPER.toVerticalPositioningMethodType( VERT_METHOD_CONV_IMBRO.getOrDefault( toLowerCase( value ), null ) );
    }

    protected QualityClassType getQualityClass(GefCptSurvey survey) {
        if ( CPT_STANDARD_3680.equals( survey.getGefCptFile().getMeasurementText6sondeernorm() ) ) {
            return COMMON_MAPPER.toQualityClassType( BRO_IND_NOT_APPLICABLE );
        }
        return COMMON_MAPPER.toQualityClassType( QUALITY_CLASS_CONV_IMBRO.getOrDefault( toLowerCase( survey.getGefCptFile().getMeasurementText6kwaliteitsklasse() ), null ) );
    }

    // survey.getGefCptFile().getMeasurementVar12();
    protected CPTMethodType getMethod(String value) {
        return COMMON_MAPPER.toMethodType( CON_METHOD_CONV_IMBRO.getOrDefault( toLowerCase( value ), null ) );
    }

    // survey.getGefCptFile().getMeasurementVar17();
    protected StopCriterionType getStopCriterion(String value) {
        return COMMON_MAPPER.toStopCriterionType( STOP_CRITERIUM_CONV_IMBRO.getOrDefault( toLowerCase( value ), null ) );
    }

    // survey.getGefCptFile().getZidVerticalDatum()
    protected VerticalDatumType getZidVerticalDatum(String value) {
        return COMMON_MAPPER.toVerticalDatumType( CPT_VERT_DAT_CONV_IMBRO.getOrDefault( toLowerCase( value ), null ) );
    }

    // cpt.getGefCptFile().getMeasurementText107()
    protected JAXBElement<PartialDateType> getVerticalPositioningDate(String value) {
        JAXBElement<PartialDateType> verticalPositionDate = CPT_OF.createDeliveredVerticalPositionTypeVerticalPositioningDate( COMMON_MAPPER.gefToPartialDate( value ) );
        verticalPositionDate.setNil( false );
        return verticalPositionDate;
    }

    /**
     * @param value
     * @return JA (ja, JA) , NEE (nee, NEE), null (alle andere opties)
     */
    protected IndicationYesNoUnknownEnumeration getYNValueWithUnknown(String value) {
        return YES_NO_CONV_YES_NO_UNKNOWN_IMBRO.getOrDefault( toLowerCase( value ), null );
    }

    protected IndicationYesNoEnumeration getDissipationTestPerformed(String value) {
        return YES_NO_CONV_YES_NO.getOrDefault( toLowerCase( value ), null );
    }

    /**
     * @return the instance
     */
    public static GefToBroMapperImbro getInstance() {
        return INSTANCE;
    }

}
