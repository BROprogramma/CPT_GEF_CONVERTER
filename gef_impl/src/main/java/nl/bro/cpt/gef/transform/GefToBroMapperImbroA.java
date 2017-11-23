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
import static nl.bro.cpt.gef.transform.MappingConstants.BRO_IND_UNKNOWN;
import static nl.bro.cpt.gef.transform.MappingConstants.CON_METHOD_CONV_IMBROA;
import static nl.bro.cpt.gef.transform.MappingConstants.CPT_STANDARD_3680;
import static nl.bro.cpt.gef.transform.MappingConstants.CPT_STANDARD_CONV_IMBROA;
import static nl.bro.cpt.gef.transform.MappingConstants.CPT_VERT_DAT_CONV_IMBROA;
import static nl.bro.cpt.gef.transform.MappingConstants.DELIVERY_CONTEXT_MISC;
import static nl.bro.cpt.gef.transform.MappingConstants.LOC_VERT_REF_POINT_CONV_IMBROA;
import static nl.bro.cpt.gef.transform.MappingConstants.METHOD_HOR_POS_CONV_IMBROA;
import static nl.bro.cpt.gef.transform.MappingConstants.QUALITY_CLASS_CONV_IMBROA;
import static nl.bro.cpt.gef.transform.MappingConstants.STOP_CRITERIUM_CONV_IMBROA;
import static nl.bro.cpt.gef.transform.MappingConstants.SURVEY_PURPOSE_CONV_IMBROA_UNKNOWN;
import static nl.bro.cpt.gef.transform.MappingConstants.UNKNOWN_DATE_EXPRESSION;
import static nl.bro.cpt.gef.transform.MappingConstants.UOM_1;
import static nl.bro.cpt.gef.transform.MappingConstants.UOM_MM;
import static nl.bro.cpt.gef.transform.MappingConstants.UOM_MM2;
import static nl.bro.cpt.gef.transform.MappingConstants.VERT_METHOD_CONV_IMBROA;
import static nl.bro.cpt.gef.transform.MappingConstants.YES_NO_CONV_YES_NO_UNKNOWN_IMBROA;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.math.BigDecimal;
import java.util.List;

import javax.xml.bind.JAXBElement;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.NullValueCheckStrategy;
import org.mapstruct.Qualifier;
import org.mapstruct.ReportingPolicy;
import org.mapstruct.factory.Mappers;

import nl.bro.cpt.gef.dto.GefCptSurvey;
import nl.bro.cpt.gef.dto.IfcCptFile;
import nl.bro.cpt.gef.dto.IfcDisFile;
import nl.bro.cpt.gef.transform.GefToBroMapperCommon.DoNotSelectMethod;
import nl.broservices.xsd.brocommon.v_3_0.IndicationYesNoEnumeration;
import nl.broservices.xsd.brocommon.v_3_0.IndicationYesNoUnknownEnumeration;
import nl.broservices.xsd.brocommon.v_3_0.PartialDateType;
import nl.broservices.xsd.brocommon.v_3_0.VoidReasonEnumeration;
import nl.broservices.xsd.cptcommon.v_1_1.AdditionalInvestigationType;
import nl.broservices.xsd.cptcommon.v_1_1.CPTMethodType;
import nl.broservices.xsd.cptcommon.v_1_1.CPTStandardType;
import nl.broservices.xsd.cptcommon.v_1_1.ConePenetrationTestType;
import nl.broservices.xsd.cptcommon.v_1_1.ConePenetrometerSurveyType;
import nl.broservices.xsd.cptcommon.v_1_1.ConePenetrometerType;
import nl.broservices.xsd.cptcommon.v_1_1.ConeSurfaceAreaType;
import nl.broservices.xsd.cptcommon.v_1_1.ConeToFrictionSleeveDistanceType;
import nl.broservices.xsd.cptcommon.v_1_1.DeliveredLocationType;
import nl.broservices.xsd.cptcommon.v_1_1.DeliveredVerticalPositionType;
import nl.broservices.xsd.cptcommon.v_1_1.DeliveryContextType;
import nl.broservices.xsd.cptcommon.v_1_1.DissipationTestType;
import nl.broservices.xsd.cptcommon.v_1_1.FrictionSleeveSurfaceAreaType;
import nl.broservices.xsd.cptcommon.v_1_1.FrictionSleeveSurfaceQuotientType;
import nl.broservices.xsd.cptcommon.v_1_1.HorizontalPositioningMethodType;
import nl.broservices.xsd.cptcommon.v_1_1.LayerDepthType;
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
 *
 * @author derksenjpam, J.M. van Ommeren
 */
@Mapper( unmappedTargetPolicy = ReportingPolicy.ERROR, //
        uses = { GefToBroMapperCommon.class, ObjectFactory.class, TestResultMapper.class }, //
        nullValueCheckStrategy = NullValueCheckStrategy.ON_IMPLICIT_CONVERSION,
        imports = IndicationYesNoUnknownEnumeration.class //
) //
public abstract class GefToBroMapperImbroA {

    private static final GefToBroMapperImbroA INSTANCE = Mappers.getMapper( GefToBroMapperImbroA.class );
    private static final nl.broservices.xsd.brocommon.v_3_0.ObjectFactory BRO_OF = new nl.broservices.xsd.brocommon.v_3_0.ObjectFactory();
    private static final nl.broservices.xsd.cptcommon.v_1_1.ObjectFactory CPT_OF = new nl.broservices.xsd.cptcommon.v_1_1.ObjectFactory();
    private static final GefToBroMapperCommon COMMON_MAPPER = GefToBroMapperCommon.getInstance();

    @Mapping( target = "requestReference", source = "requestReference" )
    @Mapping( target = "deliveryAccountableParty", source = "survey.gefCptFile.measurementText101" )
    @Mapping( target = "qualityRegime", constant = "IMBRO_A" )
    @Mapping( target = "sourceDocument.CPT", source = "survey" )
    @Mapping( target = "broId", ignore = true )
    @Mapping( target = "underPrivilege", ignore = true )
    public abstract RegistrationRequestType gefToRegistrationRequest(GefCptSurvey survey, String requestReference);

    @Mapping( target = "requestReference", source = "requestReference" )
    @Mapping( target = "deliveryAccountableParty", source = "survey.gefCptFile.measurementText101" )
    @Mapping( target = "qualityRegime", constant = "IMBRO_A" )
    @Mapping( target = "sourceDocument.CPT", source = "survey" )
    @Mapping( target = "broId", source = "survey.gefCptFile.testId" )
    @Mapping( target = "correctionReason", source = "correctionReason" )
    @Mapping( target = "underPrivilege", ignore = true )
    public abstract CorrectionRequestType gefToCorrectionRequest(GefCptSurvey survey, String requestReference, String correctionReason);

    @Mapping( target = "additionalInvestigation", source = "survey" )
    @Mapping( target = "additionalInvestigationPerformed", source = "survey" )
    @Mapping( target = "conePenetrometerSurvey", source = "survey" )
    @Mapping( target = "deliveredLocation", source = "survey" )
    @Mapping( target = "deliveredVerticalPosition", source = "survey" )
    @Mapping( target = "deliveryContext", constant = DELIVERY_CONTEXT_MISC )
    @Mapping( target = "objectIdAccountableParty", source = "survey.gefCptFile" )
    @Mapping( target = "researchOperator", source = "survey.gefCptFile.companyId" )
    @Mapping( target = "researchReportDate", source = "survey.gefCptFile.fileDate" )
    @Mapping( target = "cptStandard", source = "survey.gefCptFile.measurementText6sondeernorm", defaultValue = BRO_IND_UNKNOWN )
    @Mapping( target = "surveyPurpose", constant = SURVEY_PURPOSE_CONV_IMBROA_UNKNOWN )
    protected abstract GeotechnicalCPTSurveyType createSurvey(GefCptSurvey survey);

    @Mapping( target = "horizontalPositioningMethod", source = "survey.gefCptFile.measurementText43", defaultValue = BRO_IND_UNKNOWN )
    @Mapping( target = "location", source = "survey.gefCptFile" )
    @Mapping( target = "horizontalPositioningDate", expression = UNKNOWN_DATE_EXPRESSION )
    @Mapping( target = "horizontalPositioningOperator", ignore = true )
    protected abstract DeliveredLocationType gefToDeliveredLocation(GefCptSurvey survey);

    @Mapping( target = "verticalDatum", source = "survey.gefCptFile.zidVerticalDatum", defaultValue = BRO_IND_UNKNOWN )
    @Mapping( target = "localVerticalReferencePoint", source = "survey.gefCptFile.measurementText9" )
    @Mapping( target = "verticalPositioningMethod", source = "survey.gefCptFile.measurementText42", defaultValue = BRO_IND_UNKNOWN )
    @Mapping( target = "offset", source = "survey.gefCptFile.zidOffset" )
    @Mapping( target = "verticalPositioningDate", source = "survey" )
    @Mapping( target = "waterDepth", source = "survey" )
    @Mapping( target = "verticalPositioningOperator", ignore = true )
    protected abstract DeliveredVerticalPositionType gefToDeliveredVerticalPosition(GefCptSurvey survey);

    protected AdditionalInvestigationType additionalInvestigationMapper(GefCptSurvey survey) {
        IndicationYesNoUnknownEnumeration performed = getAdditionalInvestigationPerformed( survey );
        if ( IndicationYesNoUnknownEnumeration.JA.equals( performed ) ) {
            return gefToAdditionalInvestigation( survey );
        }
        return null;
    }

    @DoNotSelectMethod
    @Mapping( target = "investigationDate", source = "survey.gefCptFile.startDate" )
    @Mapping( target = "groundwaterLevel", source = "survey.gefCptFile.measurementVar14" )
    @Mapping( target = "surfaceDescription", ignore = true )
    @Mapping( target = "conditions", source = "survey.gefCptFile.measurementText11" )
    @Mapping( target = "removedLayer", source = "survey.gefCptFile.specimenVar" )
    protected abstract AdditionalInvestigationType gefToAdditionalInvestigation(GefCptSurvey survey);

    @Mapping( target = "qualityClass", source = "survey" )
    @Mapping( target = "sensorAzimuth", source = "survey.gefCptFile.measurementVar42" )
    @Mapping( target = "cptMethod", source = "survey.gefCptFile.measurementVar12", defaultValue = BRO_IND_UNKNOWN )
    @Mapping( target = "stopCriterion", source = "survey.gefCptFile.measurementVar17", defaultValue = BRO_IND_UNKNOWN )
    @Mapping( target = "conePenetrationTest", source = "survey.gefCptFile" )
    @Mapping( target = "conePenetrometer", source = "survey.gefCptFile" )
    @Mapping( target = "dissipationTest", source = "survey.gefDisFiles" )
    @Mapping( target = "dissipationTestPerformed", source = "survey.gefDisFiles" )
    @Mapping( target = "trajectory", source = "survey.gefCptFile" )
    @Mapping( target = "parameters", source = "survey" )
    @Mapping( target = "procedure", source = "survey.gefCptFile" )
    @Mapping( target = "finalProcessingDate", expression = UNKNOWN_DATE_EXPRESSION )
    @Mapping( target = "id", expression = GmlIdGenerator.EXPRESSION )
    @Mapping( target = "type", ignore = true )
    @Mapping( target = "sampledFeature", ignore = true )
    @Mapping( target = "relatedObservation", ignore = true )
    @Mapping( target = "relatedSamplingFeature", ignore = true )
    @Mapping( target = "parameter", ignore = true )
    protected abstract ConePenetrometerSurveyType gefToConePenetrometerSurvey(GefCptSurvey survey);

    @Mapping( target = "phenomenonTime", source = "cptFile.startDate" )
    @Mapping( target = "resultTime", source = "cptFile.startDate" )
    @Mapping( target = "cptResult", source = "cptFile.dataBlock" )
    @Mapping( target = "id", expression = GmlIdGenerator.EXPRESSION )
    @Mapping( target = "type", ignore = true )
    @Mapping( target = "validTime", ignore = true )
    @Mapping( target = "procedure", ignore = true )
    @Mapping( target = "observedProperty", ignore = true )
    @Mapping( target = "featureOfInterest", ignore = true )
    @Mapping( target = "relatedObservation", ignore = true )
    @Mapping( target = "parameter", ignore = true )
    protected abstract ConePenetrationTestType gefToConepenetrationTest(IfcCptFile cptFile);

    @Mapping( target = "frictionSleeveSurfaceArea", source = "cptFile" )
    @Mapping( target = "coneDiameter", source = "measurementVar130" )
    @Mapping( target = "conePenetrometerType", source = "measurementText4", defaultValue = BRO_IND_UNKNOWN, qualifiedBy = TextToUnknownWhenNullOrEmpty.class )
    @Mapping( target = "description", source = "measurementText5", defaultValue = BRO_IND_UNKNOWN, qualifiedBy = TextToUnknownWhenNullOrEmpty.class )
    @Mapping( target = "coneSurfaceArea", source = "cptFile" )
    @Mapping( target = "coneSurfaceQuotient", source = "measurementVar3" )
    @Mapping( target = "frictionSleeveSurfaceQuotient", source = "cptFile.measurementVar4" )
    @Mapping( target = "coneToFrictionSleeveDistance", source = "cptFile" )
    @Mapping( target = "zeroLoadMeasurement", source = "cptFile" )
    protected abstract ConePenetrometerType gefToConepenetroMeter(IfcCptFile cptFile);

    @Mapping( target = "interruptionProcessingPerformed", source = "cptFile.measurementText21" )
    @Mapping( target = "signalProcessingPerformed", source = "cptFile.measurementText20" )
    @Mapping( target = "expertCorrectionPerformed", constant = "ONBEKEND" )
    protected abstract ProcessingType gefToProcessing(IfcCptFile cptFile);

    @Mapping( target = "phenomenonTime", source = "disFile.startDate" )
    @Mapping( target = "resultTime", source = "disFile.startDate" )
    @Mapping( target = "penetrationLength", source = "disFile.parentDepth" )
    @Mapping( target = "disResult", source = "disFile.dataBlock" )
    @Mapping( target = "id", expression = GmlIdGenerator.EXPRESSION )
    @Mapping( target = "type", ignore = true )
    @Mapping( target = "validTime", ignore = true )
    @Mapping( target = "procedure", ignore = true )
    @Mapping( target = "observedProperty", ignore = true )
    @Mapping( target = "featureOfInterest", ignore = true )
    @Mapping( target = "relatedObservation", ignore = true )
    @Mapping( target = "parameter", ignore = true )
    protected abstract DissipationTestType gefToDissipationTest(IfcDisFile disFile);

    protected abstract List<DissipationTestType> gefToDissipationTestList(List<IfcDisFile> disFiles);

    // survey.getGefCptFile().getMeasurementText6sondeernorm()
    protected CPTStandardType getStandard(String value) {
        return COMMON_MAPPER.toStandardType( CPT_STANDARD_CONV_IMBROA.getOrDefault( toLowerCase( value ), BRO_IND_UNKNOWN ) );
    }

    // constant: SURVEY_PURPOSE_CONV_IMBROA_UNKNOWN
    protected SurveyPurposeType getSurveyPurpose(String value) {
        return COMMON_MAPPER.toSurveyPurposeType( value );
    }

    protected IndicationYesNoUnknownEnumeration getAdditionalInvestigationPerformed(GefCptSurvey survey) {
        IndicationYesNoUnknownEnumeration mappedValue;
        if ( survey.getGefCptFile().getMeasurementText11() != null || survey.getGefCptFile().getMeasurementVar14() != null
                || ( survey.getGefCptFile().getSpecimenVar() != null && !survey.getGefCptFile().getSpecimenVar().isEmpty() ) ) {
            mappedValue = IndicationYesNoUnknownEnumeration.JA;
        }
        else {
            // quality regime == IMBRO/A - geen additional investigation performed
            mappedValue = IndicationYesNoUnknownEnumeration.ONBEKEND;
        }
        return mappedValue;
    }

    // constant: DELIVERY_CONTEXT_MISC
    protected DeliveryContextType getDeliveryContext(String value) {
        return COMMON_MAPPER.toDeliveryContextType( value );
    }

    // survey.getGefCptFile().getMeasurementText43()
    protected HorizontalPositioningMethodType getHorizontalPositioningMethod(String value) {
        return COMMON_MAPPER.toHorizontalPositioningMethodType( METHOD_HOR_POS_CONV_IMBROA.getOrDefault( toLowerCase( value ), BRO_IND_UNKNOWN ) );
    }

    // survey.getGefCptFile().getMeasurementText9();
    protected LocalVerticalReferencePointType getLocalVerticalReferencePoint(String value) {
        return COMMON_MAPPER.toLocalVerticalReferencePointType( LOC_VERT_REF_POINT_CONV_IMBROA.getOrDefault( toLowerCase( value ), null ) );
    }

    // survey.getGefCptFile().getMeasurementText42()
    protected VerticalPositioningMethodType getVerticalPositioningMethod(String value) {
        return COMMON_MAPPER.toVerticalPositioningMethodType( VERT_METHOD_CONV_IMBROA.getOrDefault( toLowerCase( value ), BRO_IND_UNKNOWN ) );
    }

    // cptFile.getMeasurementVar4()
    protected JAXBElement<FrictionSleeveSurfaceQuotientType> setFrictionSleeveSurfaceQuotient(BigDecimal value) {
        JAXBElement<FrictionSleeveSurfaceQuotientType> coneFrictionSleeveSurfaceQuotientElement;
        if ( value != null ) {
            FrictionSleeveSurfaceQuotientType coneFrictionSleeveSurfaceQuotient = COMMON_MAPPER.convertToFrictionSleeveSurfaceQuotient( value );
            coneFrictionSleeveSurfaceQuotientElement = CPT_OF.createConePenetrometerTypeFrictionSleeveSurfaceQuotient( coneFrictionSleeveSurfaceQuotient );
            coneFrictionSleeveSurfaceQuotientElement.setNil( false );
        }
        else {
            FrictionSleeveSurfaceQuotientType coneFrictionSleeveSurfaceQuotient = CPT_OF.createFrictionSleeveSurfaceQuotientType();
            coneFrictionSleeveSurfaceQuotient.setUom( UOM_1 );
            coneFrictionSleeveSurfaceQuotientElement = CPT_OF.createConePenetrometerTypeFrictionSleeveSurfaceQuotient( coneFrictionSleeveSurfaceQuotient );
            coneFrictionSleeveSurfaceQuotientElement.setNil( true );
        }
        return coneFrictionSleeveSurfaceQuotientElement;
    }

    protected JAXBElement<ConeToFrictionSleeveDistanceType> setConeToFrictionSleeveDistance(IfcCptFile cptFile) {
        JAXBElement<ConeToFrictionSleeveDistanceType> coneFrictionSleeveDistanceElement;
        BigDecimal value = cptFile.getMeasurementVar5();
        if ( value == null && cptFile.getMeasuredParameters().contains( "3" ) ) {
            ConeToFrictionSleeveDistanceType coneFrictionSleeveDistance = COMMON_MAPPER.convertToConeToFrictionSleeveDistance( BigDecimal.valueOf( 100L ) );
            coneFrictionSleeveDistanceElement = CPT_OF.createConePenetrometerTypeConeToFrictionSleeveDistance( coneFrictionSleeveDistance );
            coneFrictionSleeveDistanceElement.setNil( false );
        }
        if ( value == null ) {
            ConeToFrictionSleeveDistanceType coneFrictionSleeveDistance = CPT_OF.createConeToFrictionSleeveDistanceType();
            coneFrictionSleeveDistance.setUom( UOM_MM );
            coneFrictionSleeveDistanceElement = CPT_OF.createConePenetrometerTypeConeToFrictionSleeveDistance( coneFrictionSleeveDistance );
            coneFrictionSleeveDistanceElement.setNil( true );
        }
        else {
            ConeToFrictionSleeveDistanceType coneFrictionSleeveDistance = COMMON_MAPPER.convertToConeToFrictionSleeveDistance( value );
            coneFrictionSleeveDistanceElement = CPT_OF.createConePenetrometerTypeConeToFrictionSleeveDistance( coneFrictionSleeveDistance );
            coneFrictionSleeveDistanceElement.setNil( false );
        }
        return coneFrictionSleeveDistanceElement;
    }

    protected JAXBElement<ConeSurfaceAreaType> setConeSurfaceArea(IfcCptFile cptFile) {
        BigDecimal value = cptFile.getMeasurementVar1();
        if ( value == null ) {
            value = BigDecimal.valueOf( 1000L );
        }
        ConeSurfaceAreaType coneSurfaceArea = COMMON_MAPPER.convertToConeSurfaceArea( value );
        JAXBElement<ConeSurfaceAreaType> coneSurfaceAreaElement = CPT_OF.createConePenetrometerTypeConeSurfaceArea( coneSurfaceArea );
        coneSurfaceAreaElement.setNil( false );
        return coneSurfaceAreaElement;
    }

    @TextToUnknownWhenNullOrEmpty
    protected String setTextToUnknownWhenNullOrEmpty(String text) {
        if ( text == null || text.trim().length() == 0 ) {
            text = BRO_IND_UNKNOWN;
        }
        return text;
    }

    protected JAXBElement<FrictionSleeveSurfaceAreaType> setFrictionSleeveSurfaceArea(IfcCptFile cptFile) {
        JAXBElement<FrictionSleeveSurfaceAreaType> frictionSleeveSurfaceAreaElement;
        if ( cptFile.getMeasurementVar2() != null ) {
            FrictionSleeveSurfaceAreaType frictionSleeveSurfaceArea = COMMON_MAPPER.convertToFrictionSleeveSurfaceArea( cptFile.getMeasurementVar2() );
            frictionSleeveSurfaceAreaElement = CPT_OF.createConePenetrometerTypeFrictionSleeveSurfaceArea( frictionSleeveSurfaceArea );
            frictionSleeveSurfaceAreaElement.setNil( false );
        }
        else if ( cptFile.getMeasuredParameters().contains( "3" ) ) {
            FrictionSleeveSurfaceAreaType frictionSleeveSurfaceArea = COMMON_MAPPER.convertToFrictionSleeveSurfaceArea( BigDecimal.valueOf( 15000L ) );
            frictionSleeveSurfaceAreaElement = CPT_OF.createConePenetrometerTypeFrictionSleeveSurfaceArea( frictionSleeveSurfaceArea );
            frictionSleeveSurfaceAreaElement.setNil( false );
        }
        else {
            // moet handmatig, anders wordt het uom attribuut niet correct gezet.
            FrictionSleeveSurfaceAreaType frictionSleeveSurfaceArea = CPT_OF.createFrictionSleeveSurfaceAreaType();
            frictionSleeveSurfaceArea.setUom( UOM_MM2 );
            frictionSleeveSurfaceAreaElement = CPT_OF.createConePenetrometerTypeFrictionSleeveSurfaceArea( frictionSleeveSurfaceArea );
            frictionSleeveSurfaceAreaElement.setNil( true );
        }
        return frictionSleeveSurfaceAreaElement;
    }

    protected TrajectoryType gefToTrajectory(IfcCptFile cptFile) {

        if ( cptFile == null ) {
            return null;
        }

        TrajectoryType trajectory = CPT_OF.createTrajectoryType();
        setPredrilled( cptFile, trajectory );
        setFinalDepth( cptFile, trajectory );

        return trajectory;
    }

    private void setFinalDepth(IfcCptFile cptFile, TrajectoryType trajectory) {
        BigDecimal finalDepth = cptFile.getMeasurementVar16();
        if ( finalDepth == null ) {
            finalDepth = getFinalDepthFromDataBlock( cptFile );
        }
        if ( finalDepth != null ) {
            trajectory.setFinalDepth( COMMON_MAPPER.convertToConePenetrationDepth( finalDepth ) );
        }
    }

    private BigDecimal getFinalDepthFromDataBlock(IfcCptFile cptFile) {
        BigDecimal finalDepth = null;
        if ( cptFile.getDataBlock() != null && cptFile.getDataBlock().size() > 1 ) {
            finalDepth = cptFile.getDataBlock().get( cptFile.getDataBlock().size() - 1 ).getDatablockci11(); // diepte
            if ( finalDepth == null ) {
                finalDepth = cptFile.getDataBlock().get( cptFile.getDataBlock().size() - 1 ).getDatablockci1(); // sondeertrajectlengte
            }
        }
        return finalDepth;
    }

    private void setPredrilled(IfcCptFile cptFile, TrajectoryType trajectory) {
        BigDecimal value = cptFile.getMeasurementVar13();
        JAXBElement<LayerDepthType> predrilledDepthElement;
        if ( value != null ) {
            predrilledDepthElement = CPT_OF.createTrajectoryTypePredrilledDepth( COMMON_MAPPER.convertToLayerDepth( value ) );
            predrilledDepthElement.setNil( false );
        }
        else if ( cptFile.getDataBlock() != null && !cptFile.getDataBlock().isEmpty() && cptFile.getDataBlock().get( 0 ).getDatablockci1().compareTo( BigDecimal.valueOf( 0.02 ) ) == -1 ) {
            predrilledDepthElement = CPT_OF.createTrajectoryTypePredrilledDepth( COMMON_MAPPER.convertToLayerDepth( BigDecimal.ZERO ) );
            predrilledDepthElement.setNil( false );
        }
        else {
            predrilledDepthElement = CPT_OF.createTrajectoryTypePredrilledDepth( null );
            predrilledDepthElement.setNil( true );
        }
        trajectory.setPredrilledDepth( predrilledDepthElement );
    }

    protected IndicationYesNoEnumeration gefToDissipationTestPerformed(List<IfcDisFile> disFiles) {
        if ( disFiles.isEmpty() ) {
            return IndicationYesNoEnumeration.NEE;
        }
        else {
            return IndicationYesNoEnumeration.JA;
        }
    }

    protected QualityClassType getQualityClass(GefCptSurvey survey) {
        if ( CPT_STANDARD_3680.equals( survey.getGefCptFile().getMeasurementText6sondeernorm() ) ) {
            return COMMON_MAPPER.toQualityClassType( BRO_IND_NOT_APPLICABLE );
        }
        String value = survey.getGefCptFile().getMeasurementText6kwaliteitsklasse();
        return COMMON_MAPPER.toQualityClassType( QUALITY_CLASS_CONV_IMBROA.getOrDefault( toLowerCase( value ), BRO_IND_UNKNOWN ) );
    }

    // survey.getGefCptFile().getMeasurementVar12()
    protected CPTMethodType getMethod(String value) {
        return COMMON_MAPPER.toMethodType( CON_METHOD_CONV_IMBROA.getOrDefault( toLowerCase( value ), BRO_IND_UNKNOWN ) );
    }

    // survey.getGefCptFile().getMeasurementVar17()
    protected StopCriterionType getStopCriterion(String value) {
        return COMMON_MAPPER.toStopCriterionType( STOP_CRITERIUM_CONV_IMBROA.getOrDefault( toLowerCase( value ), BRO_IND_UNKNOWN ) );
    }

    // survey.getGefCptFile().getZidVerticalDatum()
    protected VerticalDatumType getZidVerticalDatum(String value) {
        return COMMON_MAPPER.toVerticalDatumType( CPT_VERT_DAT_CONV_IMBROA.getOrDefault( toLowerCase( value ), BRO_IND_UNKNOWN ) );
    }

    protected JAXBElement<PartialDateType> getVerticalPositioningDate(GefCptSurvey cpt) {

        JAXBElement<PartialDateType> verticalPositionDate;
        if ( cpt.getGefCptFile().getZidOffset() != null ) {
            // IMBRO/A waarde is onbekend wanneer er een offset is
            PartialDateType partialDateType = BRO_OF.createPartialDateType();
            partialDateType.setVoidReason( VoidReasonEnumeration.ONBEKEND );
            verticalPositionDate = CPT_OF.createDeliveredVerticalPositionTypeVerticalPositioningDate( partialDateType );
            verticalPositionDate.setNil( false );
        }
        else {
            // IMBRO/A waarde ontbreekt wanneer er geen offset is
            verticalPositionDate = CPT_OF.createDeliveredVerticalPositionTypeVerticalPositioningDate( null );
            verticalPositionDate.setNil( true );
        }
        return verticalPositionDate;
    }

    protected IndicationYesNoUnknownEnumeration getYNValueWithUnknown(String value) {
        return YES_NO_CONV_YES_NO_UNKNOWN_IMBROA.getOrDefault( toLowerCase( value ), IndicationYesNoUnknownEnumeration.JA );
    }

    /**
     * @return the instance
     */
    public static GefToBroMapperImbroA getInstance() {
        return INSTANCE;
    }

    @Qualifier
    @Target( ElementType.METHOD )
    @Retention( RetentionPolicy.CLASS )
    public static @interface TextToUnknownWhenNullOrEmpty {
    }

}
