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

import static javax.xml.datatype.DatatypeConstants.FIELD_UNDEFINED;
import static nl.bro.cpt.gef.transform.MappingConstants.CS_CORRECTION_REASON;
import static nl.bro.cpt.gef.transform.MappingConstants.CS_DELIVERY_CONTEXT;
import static nl.bro.cpt.gef.transform.MappingConstants.CS_HORIZONTAL_POSITIONING_METHOD;
import static nl.bro.cpt.gef.transform.MappingConstants.CS_LOCAL_VERTICAL_REFERENCE_POINT;
import static nl.bro.cpt.gef.transform.MappingConstants.CS_METHOD;
import static nl.bro.cpt.gef.transform.MappingConstants.CS_QUALITY_CLASS;
import static nl.bro.cpt.gef.transform.MappingConstants.CS_STANDARD;
import static nl.bro.cpt.gef.transform.MappingConstants.CS_STOP_CRITERION;
import static nl.bro.cpt.gef.transform.MappingConstants.CS_SURVEY_PURPOSE;
import static nl.bro.cpt.gef.transform.MappingConstants.CS_VERTICAL_DATUM;
import static nl.bro.cpt.gef.transform.MappingConstants.CS_VERTICAL_POSITIONING_METHOD;
import static nl.bro.cpt.gef.transform.MappingConstants.UOM_1;
import static nl.bro.cpt.gef.transform.MappingConstants.UOM_DEG;
import static nl.bro.cpt.gef.transform.MappingConstants.UOM_M;
import static nl.bro.cpt.gef.transform.MappingConstants.UOM_MM;
import static nl.bro.cpt.gef.transform.MappingConstants.UOM_MM2;
import static nl.bro.cpt.gef.transform.MappingConstants.UOM_MPA;
import static nl.bro.cpt.gef.transform.MappingConstants.UOM_S_M;
import static nl.bro.cpt.gef.transform.TruncationMapper.truncate;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.math.BigDecimal;
import java.time.ZoneId;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.TimeZone;

import javax.xml.bind.JAXBElement;
import javax.xml.datatype.DatatypeConfigurationException;
import javax.xml.datatype.DatatypeConstants;
import javax.xml.datatype.DatatypeFactory;
import javax.xml.datatype.XMLGregorianCalendar;

import org.apache.log4j.Logger;
import org.mapstruct.AfterMapping;
import org.mapstruct.BeforeMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.Qualifier;
import org.mapstruct.ReportingPolicy;
import org.mapstruct.factory.Mappers;

import com.google.common.base.Splitter;

import nl.bro.cpt.gef.dto.GefCptSurvey;
import nl.bro.cpt.gef.dto.IfcCptFile;
import nl.bro.cpt.gef.dto.SpecimenVar;
import nl.bro.cpt.gef.transform.TruncationMapper.Fraction0;
import nl.bro.cpt.gef.transform.TruncationMapper.Fraction1;
import nl.bro.cpt.gef.transform.TruncationMapper.Fraction2;
import nl.bro.cpt.gef.transform.TruncationMapper.Fraction3;
import nl.broservices.xsd.brocommon.v_3_0.IndicationYesNoEnumeration;
import nl.broservices.xsd.brocommon.v_3_0.PartialDateType;
import nl.broservices.xsd.brocommon.v_3_0.VoidReasonEnumeration;
import nl.broservices.xsd.cptcommon.v_1_1.AzimuthType;
import nl.broservices.xsd.cptcommon.v_1_1.CPTMethodType;
import nl.broservices.xsd.cptcommon.v_1_1.CPTStandardType;
import nl.broservices.xsd.cptcommon.v_1_1.ConeDiameterType;
import nl.broservices.xsd.cptcommon.v_1_1.ConePenetrationDepthType;
import nl.broservices.xsd.cptcommon.v_1_1.ConePenetrometerSurveyType;
import nl.broservices.xsd.cptcommon.v_1_1.ConeResistanceType;
import nl.broservices.xsd.cptcommon.v_1_1.ConeSurfaceAreaType;
import nl.broservices.xsd.cptcommon.v_1_1.ConeSurfaceQuotientType;
import nl.broservices.xsd.cptcommon.v_1_1.ConeToFrictionSleeveDistanceType;
import nl.broservices.xsd.cptcommon.v_1_1.DeliveryContextType;
import nl.broservices.xsd.cptcommon.v_1_1.ElectricalConductivityType;
import nl.broservices.xsd.cptcommon.v_1_1.FrictionSleeveSurfaceAreaType;
import nl.broservices.xsd.cptcommon.v_1_1.FrictionSleeveSurfaceQuotientType;
import nl.broservices.xsd.cptcommon.v_1_1.HorizontalPositioningMethodType;
import nl.broservices.xsd.cptcommon.v_1_1.InclinationType;
import nl.broservices.xsd.cptcommon.v_1_1.LayerDepthType;
import nl.broservices.xsd.cptcommon.v_1_1.LocalFrictionType;
import nl.broservices.xsd.cptcommon.v_1_1.LocalVerticalReferencePointType;
import nl.broservices.xsd.cptcommon.v_1_1.ObjectFactory;
import nl.broservices.xsd.cptcommon.v_1_1.OffsetType;
import nl.broservices.xsd.cptcommon.v_1_1.ParametersType;
import nl.broservices.xsd.cptcommon.v_1_1.PorePressureType;
import nl.broservices.xsd.cptcommon.v_1_1.QualityClassType;
import nl.broservices.xsd.cptcommon.v_1_1.RemovedLayerType;
import nl.broservices.xsd.cptcommon.v_1_1.StopCriterionType;
import nl.broservices.xsd.cptcommon.v_1_1.SurveyPurposeType;
import nl.broservices.xsd.cptcommon.v_1_1.VerticalDatumType;
import nl.broservices.xsd.cptcommon.v_1_1.VerticalPositioningMethodType;
import nl.broservices.xsd.cptcommon.v_1_1.ZeroLoadMeasurementType;
import nl.broservices.xsd.iscpt.v_1_1.CorrectionReasonType;
import nl.broservices.xsd.iscpt.v_1_1.GeotechnicalCPTSurveyType;

import net.opengis.gml.v_3_2_1.DirectPositionType;
import net.opengis.gml.v_3_2_1.FeaturePropertyType;
import net.opengis.gml.v_3_2_1.PointType;
import net.opengis.gml.v_3_2_1.TimeIndeterminateValueType;
import net.opengis.gml.v_3_2_1.TimeInstantPropertyType;
import net.opengis.gml.v_3_2_1.TimeInstantType;
import net.opengis.gml.v_3_2_1.TimePositionType;

/**
 * @author derksenjpam, J.M. van Ommeren
 */
@Mapper( unmappedTargetPolicy = ReportingPolicy.ERROR, uses = { TruncationMapper.class, ObjectFactory.class } )
public abstract class GefToBroMapperCommon {

    private static final Logger LOG = Logger.getLogger( GefToBroMapperCommon.class );
    private static final String TZ_AMSTERDAM = "Europe/Amsterdam";
    private static final String FMT_TO_SRS_NAME = "urn:ogc:def:crs:EPSG::%s";
    private static final GefToBroMapperCommon INSTANCE = Mappers.getMapper( GefToBroMapperCommon.class );
    private static final net.opengis.gml.v_3_2_1.ObjectFactory GML_OF = new net.opengis.gml.v_3_2_1.ObjectFactory();
    private static final nl.broservices.xsd.brocommon.v_3_0.ObjectFactory BRO_OF = new nl.broservices.xsd.brocommon.v_3_0.ObjectFactory();
    private static final nl.broservices.xsd.cptcommon.v_1_1.ObjectFactory CPT_OF = new nl.broservices.xsd.cptcommon.v_1_1.ObjectFactory();
    protected static final net.opengis.sa.v_2_0.ObjectFactory SAM_OF = new net.opengis.sa.v_2_0.ObjectFactory();

    @BeforeMapping
    public void resetGmlId(GefCptSurvey survey, @MappingTarget GeotechnicalCPTSurveyType result) {
        GmlIdGenerator.reset();
    }

    @Mapping( target = "sequenceNumber", source = "volgnummer" )
    @Mapping( target = "upperBoundary", source = "bovendiepte" )
    @Mapping( target = "lowerBoundary", source = "onderdiepte" )
    @Mapping( target = "description", source = "beschrijving" )
    protected abstract RemovedLayerType gefSpecimenVarToBro(SpecimenVar v);

    @Mapping( target = "coneResistance", expression = "java( measuredParamToBro( survey, 2 ) )" )
    @Mapping( target = "depth", expression = "java( measuredParamToBro( survey, 11 ) )" )
    @Mapping( target = "electricalConductivity", expression = "java( measuredParamToBro( survey, 23 ) )" )
    @Mapping( target = "correctedConeResistance", expression = "java( measuredParamToBro( survey, 13 ) )" )
    @Mapping( target = "inclinationNS", expression = "java( measuredParamToBro( survey, 9 ) )" )
    @Mapping( target = "inclinationEW", expression = "java( measuredParamToBro( survey, 10 ) )" )
    @Mapping( target = "inclinationX", expression = "java( measuredParamToBro( survey, 21 ) )" )
    @Mapping( target = "inclinationY", expression = "java( measuredParamToBro( survey, 22 ) )" )
    @Mapping( target = "inclinationResultant", expression = "java( measuredParamToBro( survey, 8 ) )" )
    @Mapping( target = "magneticDeclination", expression = "java( measuredParamToBro( survey, 36 ) )" )
    @Mapping( target = "magneticInclination", expression = "java( measuredParamToBro( survey, 35 ) )" )
    @Mapping( target = "magneticFieldStrengthX", expression = "java( measuredParamToBro( survey, 31 ) )" )
    @Mapping( target = "magneticFieldStrengthY", expression = "java( measuredParamToBro( survey, 32 ) )" )
    @Mapping( target = "magneticFieldStrengthZ", expression = "java( measuredParamToBro( survey, 33 ) )" )
    @Mapping( target = "netConeResistance", expression = "java( measuredParamToBro( survey, 14 ) )" )
    @Mapping( target = "localFriction", expression = "java( measuredParamToBro( survey, 3 ) )" )
    @Mapping( target = "poreRatio", expression = "java( measuredParamToBro( survey, 15 ) )" )
    @Mapping( target = "penetrationLength", expression = "java( measuredParamToBro( survey, 1 ) )" )
    @Mapping( target = "temperature", expression = "java( measuredParamToBro( survey, 39 ) )" )
    @Mapping( target = "magneticFieldStrengthTotal", expression = "java( measuredParamToBro( survey, 34 ) )" )
    @Mapping( target = "elapsedTime", expression = "java( measuredParamToBro( survey, 12 ) )" )
    @Mapping( target = "porePressureU1", expression = "java( measuredParamToBro( survey, 5 ) )" )
    @Mapping( target = "porePressureU2", expression = "java( measuredParamToBro( survey, 6 ) )" )
    @Mapping( target = "porePressureU3", expression = "java( measuredParamToBro( survey, 7 ) )" )
    @Mapping( target = "frictionRatio", expression = "java( measuredParamToBro( survey, 4 ) )" )
    protected abstract ParametersType gefToParameters(GefCptSurvey survey);

    protected JAXBElement<ZeroLoadMeasurementType> zeroloadMapper(IfcCptFile in) {
        JAXBElement<ZeroLoadMeasurementType> zeroLoadMeasurementElem;
        ZeroLoadMeasurementType zlm = gefToZeroLoadMeasurement( in );
        if ( checkIfFilled( zlm ) ) {
            zeroLoadMeasurementElem = CPT_OF.createConePenetrometerTypeZeroLoadMeasurement( gefToZeroLoadMeasurement( in ) );
            zeroLoadMeasurementElem.setNil( false );
        }
        else {
            zeroLoadMeasurementElem = CPT_OF.createConePenetrometerTypeZeroLoadMeasurement( null );
            zeroLoadMeasurementElem.setNil( true );
        }
        return zeroLoadMeasurementElem;
    }

    @DoNotSelectMethod
    @Mapping( target = "coneResistanceAfter", source = "measurementVar21" )
    @Mapping( target = "coneResistanceBefore", source = "measurementVar20" )
    @Mapping( target = "electricalConductivityAfter", source = "measurementVar37" )
    @Mapping( target = "electricalConductivityBefore", source = "measurementVar36" )
    @Mapping( target = "inclinationNSAfter", source = "measurementVar33" )
    @Mapping( target = "inclinationNSBefore", source = "measurementVar32" )
    @Mapping( target = "inclinationEWAfter", source = "measurementVar35" )
    @Mapping( target = "inclinationEWBefore", source = "measurementVar34" )
    @Mapping( target = "inclinationResultantAfter", source = "measurementVar31" )
    @Mapping( target = "inclinationResultantBefore", source = "measurementVar30" )
    @Mapping( target = "localFrictionAfter", source = "measurementVar23" )
    @Mapping( target = "localFrictionBefore", source = "measurementVar22" )
    @Mapping( target = "porePressureU1After", source = "measurementVar25" )
    @Mapping( target = "porePressureU1Before", source = "measurementVar24" )
    @Mapping( target = "porePressureU2After", source = "measurementVar27" )
    @Mapping( target = "porePressureU2Before", source = "measurementVar26" )
    @Mapping( target = "porePressureU3After", source = "measurementVar29" )
    @Mapping( target = "porePressureU3Before", source = "measurementVar28" )
    protected abstract ZeroLoadMeasurementType gefToZeroLoadMeasurement(IfcCptFile cptFile);

    // measure types
    @Mapping( target = "value", source = "value", qualifiedBy = Fraction3.class )
    @Mapping( target = "uom", constant = UOM_M )
    protected abstract OffsetType convertToOffset(BigDecimal value);

    @Mapping( target = "value", source = "value", qualifiedBy = Fraction0.class )
    @Mapping( target = "uom", constant = UOM_MM )
    abstract ConeDiameterType convertToDiameter(BigDecimal value);

    @Mapping( target = "value", source = "value", qualifiedBy = Fraction2.class )
    @Mapping( target = "uom", constant = UOM_1 )
    abstract ConeSurfaceQuotientType convertToConeSurfaceQuotient(BigDecimal value);

    @Mapping( target = "value", source = "value", qualifiedBy = Fraction0.class )
    @Mapping( target = "uom", constant = UOM_DEG )
    abstract AzimuthType convertToAzimuth(BigDecimal value);

    @Mapping( target = "value", source = "value", qualifiedBy = Fraction3.class )
    @Mapping( target = "uom", constant = UOM_MPA )
    abstract LocalFrictionType convertToLocalFriction(BigDecimal value);

    @Mapping( target = "value", source = "value", qualifiedBy = Fraction0.class )
    @Mapping( target = "uom", constant = UOM_DEG )
    abstract InclinationType convertToInclination(BigDecimal value);

    @Mapping( target = "value", source = "value", qualifiedBy = Fraction3.class )
    @Mapping( target = "uom", constant = UOM_MPA )
    abstract PorePressureType convertToPorePressure(BigDecimal value);

    @Mapping( target = "value", source = "value", qualifiedBy = Fraction2.class )
    @Mapping( target = "uom", constant = UOM_M )
    abstract LayerDepthType convertToLayerDepth(BigDecimal value);

    @Mapping( target = "value", source = "value", qualifiedBy = Fraction3.class )
    @Mapping( target = "uom", constant = UOM_MPA )
    abstract ConeResistanceType convertToConeResistance(BigDecimal value);

    @Mapping( target = "value", source = "value", qualifiedBy = Fraction3.class )
    @Mapping( target = "uom", constant = UOM_S_M )
    abstract ElectricalConductivityType convertToElectricalConductivity(BigDecimal value);

    @Mapping( target = "value", source = "value", qualifiedBy = Fraction3.class )
    @Mapping( target = "uom", constant = UOM_M )
    abstract ConePenetrationDepthType convertToConePenetrationDepth(BigDecimal value);

    @Mapping( target = "value", source = "value", qualifiedBy = Fraction0.class )
    @Mapping( target = "uom", constant = UOM_MM )
    abstract ConeToFrictionSleeveDistanceType convertToConeToFrictionSleeveDistance(BigDecimal value);

    @Mapping( target = "value", source = "value", qualifiedBy = Fraction0.class )
    @Mapping( target = "uom", constant = UOM_MM2 )
    abstract FrictionSleeveSurfaceAreaType convertToFrictionSleeveSurfaceArea(BigDecimal value);

    @Mapping( target = "value", source = "value", qualifiedBy = Fraction1.class )
    @Mapping( target = "uom", constant = UOM_1 )
    abstract FrictionSleeveSurfaceQuotientType convertToFrictionSleeveSurfaceQuotient(BigDecimal value);

    @Mapping( target = "value", source = "value", qualifiedBy = Fraction0.class )
    @Mapping( target = "uom", constant = UOM_MM2 )
    abstract ConeSurfaceAreaType convertToConeSurfaceArea(BigDecimal value);

    // Code Spaces
    @Mapping( target = "codeSpace", constant = CS_CORRECTION_REASON )
    abstract CorrectionReasonType toCorrectionReasonType(String value);

    @DoNotSelectMethod
    @Mapping( target = "codeSpace", constant = CS_METHOD )
    abstract CPTMethodType toMethodType(String value);

    @DoNotSelectMethod
    @Mapping( target = "codeSpace", constant = CS_QUALITY_CLASS )
    abstract QualityClassType toQualityClassType(String value);

    @DoNotSelectMethod
    @Mapping( target = "codeSpace", constant = CS_STANDARD )
    abstract CPTStandardType toStandardType(String value);

    @DoNotSelectMethod
    @Mapping( target = "codeSpace", constant = CS_SURVEY_PURPOSE )
    abstract SurveyPurposeType toSurveyPurposeType(String value);

    @DoNotSelectMethod
    @Mapping( target = "codeSpace", constant = CS_LOCAL_VERTICAL_REFERENCE_POINT )
    abstract LocalVerticalReferencePointType toLocalVerticalReferencePointType(String value);

    @DoNotSelectMethod
    @Mapping( target = "codeSpace", constant = CS_HORIZONTAL_POSITIONING_METHOD )
    abstract HorizontalPositioningMethodType toHorizontalPositioningMethodType(String value);

    @DoNotSelectMethod
    @Mapping( target = "codeSpace", constant = CS_VERTICAL_POSITIONING_METHOD )
    abstract VerticalPositioningMethodType toVerticalPositioningMethodType(String value);

    @DoNotSelectMethod
    @Mapping( target = "codeSpace", constant = CS_STOP_CRITERION )
    abstract StopCriterionType toStopCriterionType(String value);

    @DoNotSelectMethod
    @Mapping( target = "codeSpace", constant = CS_VERTICAL_DATUM )
    abstract VerticalDatumType toVerticalDatumType(String value);

    @DoNotSelectMethod
    @Mapping( target = "codeSpace", constant = CS_DELIVERY_CONTEXT )
    abstract DeliveryContextType toDeliveryContextType(String value);

    // corrections after mapping
    @AfterMapping
    protected void toJaxbAfter(@MappingTarget ConePenetrometerSurveyType jaxb) {
        JAXBElement<FeaturePropertyType> sampledFeature = SAM_OF.createSFSamplingFeatureTypeSampledFeature( null );
        sampledFeature.setNil( true );
        jaxb.getSampledFeature().add( sampledFeature );
    }

    // common simple types
    protected JAXBElement<OffsetType> convertToOffsetElement(BigDecimal value) {
        OffsetType offsetType = null;
        if ( value != null ) {
            offsetType = convertToOffset( value );
        }
        JAXBElement<OffsetType> offsetTypeElem = CPT_OF.createDeliveredVerticalPositionTypeOffset( offsetType );
        offsetTypeElem.setNil( value == null );
        return offsetTypeElem;
    }

    protected JAXBElement<OffsetType> getWaterDepth(GefCptSurvey survey) {
        BigDecimal verticalPos = survey.getGefCptFile().getMeasurementVar15();
        JAXBElement<OffsetType> waterDepth;
        if ( verticalPos == null && survey.getGefCptFile().getMeasurementText9() != null ) {
            String lvpr = survey.getGefCptFile().getMeasurementText9().toLowerCase();
            if ( MappingConstants.WATERBODEM_VALUES.contains( lvpr ) ) {
                waterDepth = CPT_OF.createDeliveredVerticalPositionTypeWaterDepth( null );
                waterDepth.setNil( true );
            }
            else {
                // maaiveld, er zijn slechts 2 alternatieven in BRO (maaiveld, waterbodem).
                waterDepth = null;
            }
        }
        else {
            OffsetType offSetType = GefToBroMapperCommon.getInstance().convertToOffset( truncate( verticalPos, 3 ) );
            waterDepth = CPT_OF.createDeliveredVerticalPositionTypeWaterDepth( offSetType );
            waterDepth.setNil( false );
        }

        return waterDepth;
    }

    public String getObjectIdAccountableParty(IfcCptFile cptFile) {
        if ( MappingConstants.NEGEREN.equals( cptFile.getProjectId() ) ) {
            return cptFile.getTestId();
        }
        else {
            return cptFile.getProjectId() + "/" + cptFile.getTestId();
        }
    }

    private static boolean checkIfFilled(ZeroLoadMeasurementType zlm) {

        return zlm.isSetConeResistanceAfter() || zlm.isSetConeResistanceBefore() //
            || zlm.isSetElectricalConductivityAfter() || zlm.isSetElectricalConductivityBefore()//
            || zlm.isSetInclinationEWAfter() || zlm.isSetInclinationEWBefore() //
            || zlm.isSetInclinationNSAfter() || zlm.isSetInclinationNSBefore()//
            || zlm.isSetInclinationResultantAfter() || zlm.isSetInclinationResultantBefore() //
            || zlm.isSetLocalFrictionAfter() || zlm.isSetLocalFrictionBefore()//
            || zlm.isSetPorePressureU1After() || zlm.isSetPorePressureU1Before() //
            || zlm.isSetPorePressureU2After() || zlm.isSetPorePressureU2Before()//
            || zlm.isSetPorePressureU3After() || zlm.isSetPorePressureU3Before(); //
    }

    protected IndicationYesNoEnumeration measuredParamToBro(GefCptSurvey src, int paramIndex) {
        if ( src.getGefCptFile().getMeasuredParameters().contains( Integer.toString( paramIndex ) ) ) {
            return IndicationYesNoEnumeration.JA;
        }
        return IndicationYesNoEnumeration.NEE;
    }

    protected PointType gefLocactionToBroLocation(IfcCptFile cptFile) {

        PointType pointType = GML_OF.createPointType();
        String crs = MappingConstants.CRS_CONV.get( cptFile.getXyidCrs() );
        if ( crs == null ) {
            return null;
        }
        pointType.setId( GmlIdGenerator.getNewId() );
        pointType.setSrsName( String.format( FMT_TO_SRS_NAME, crs ) );

        DirectPositionType positionType = GML_OF.createDirectPositionType();
        int scale = MappingConstants.CRS_SCALE_CONV.get( cptFile.getXyidCrs() );
        boolean isGeographic = MappingConstants.CRS_GEOGRAPHIC_CONV.get( cptFile.getXyidCrs() );
        if ( isGeographic ) {
            positionType.getValue().add( TruncationMapper.truncate( cptFile.getXyidX(), scale ) );
            positionType.getValue().add( TruncationMapper.truncate( cptFile.getXyidY(), scale ) );
        }
        else {
            positionType.getValue().add( TruncationMapper.truncate( cptFile.getXyidY(), scale ) );
            positionType.getValue().add( TruncationMapper.truncate( cptFile.getXyidX(), scale ) );
        }
        pointType.setPos( positionType );

        return pointType;
    }

    protected PartialDateType gefToPartialDate(String strDate) {

        if ( strDate == null ) {
            return null;
        }

        PartialDateType partialDate = BRO_OF.createPartialDateType();
        XMLGregorianCalendar xcal = gefToXMLGregorianCalendar( strDate );
        if ( !xcal.isValid() ) {
            partialDate.setVoidReason( VoidReasonEnumeration.ONBEKEND );
        }
        else if ( DatatypeConstants.DATE.equals( xcal.getXMLSchemaType() ) ) {
            partialDate.setDate( xcal );
        }
        else if ( DatatypeConstants.GYEARMONTH.equals( xcal.getXMLSchemaType() ) ) {
            partialDate.setYearMonth( xcal );
        }
        else if ( DatatypeConstants.GYEAR.equals( xcal.getXMLSchemaType() ) ) {
            partialDate.setYear( xcal );
        }
        else {
            partialDate.setVoidReason( VoidReasonEnumeration.ONBEKEND );
        }
        return partialDate;
    }

    protected TimeInstantPropertyType gefToTimeInstantProperty(String strDate) {
        if ( strDate == null ) {
            return null;
        }
        return gefToTimeInstantProperty( gefToXMLGregorianCalendar( strDate ) );
    }

    protected TimeInstantPropertyType gefToTimeInstantProperty(String strDate, String strTime) {
        if ( strDate == null || strTime == null ) {
            return null;
        }
        return gefToTimeInstantProperty( gefToXMLGregorianCalendar( strDate, strTime ) );
    }

    private TimeInstantPropertyType gefToTimeInstantProperty(XMLGregorianCalendar xcal) {

        TimeInstantPropertyType timeInstantProp = GML_OF.createTimeInstantPropertyType();

        TimePositionType timePosition = GML_OF.createTimePositionType();
        if ( xcal.isValid() ) {
            timePosition.getValue().add( xcal.toXMLFormat() );
        }
        else {
            timePosition.setIndeterminatePosition( TimeIndeterminateValueType.UNKNOWN );
        }

        TimeInstantType timeInstant = GML_OF.createTimeInstantType();
        timeInstant.setTimePosition( timePosition );
        timeInstant.setId( GmlIdGenerator.getNewId() );

        timeInstantProp.setTimeInstant( timeInstant );
        return timeInstantProp;
    }

    private XMLGregorianCalendar gefToXMLGregorianCalendar(String strDate) {

        XMLGregorianCalendar xcal;
        try {
            Splitter splitter = Splitter.on( ',' );
            List<String> dateParts = splitter.splitToList( strDate );

            // formaat: JJJ-MM-DD, JJJJ-MM, JJJJ
            xcal = DatatypeFactory.newInstance().newXMLGregorianCalendar(
                getXmlGcIntValue( dateParts, 0 ),
                getXmlGcIntValue( dateParts, 1 ),
                getXmlGcIntValue( dateParts, 2 ),
                FIELD_UNDEFINED,
                FIELD_UNDEFINED,
                FIELD_UNDEFINED,
                FIELD_UNDEFINED,
                FIELD_UNDEFINED );
        }
        catch ( DatatypeConfigurationException e ) {
            LOG.error( e.getMessage(), e );
            throw new IllegalStateException( e );
        }
        return xcal;
    }

    private int getXmlGcIntValue(List<String> splitted, int i) {

        if ( splitted == null || i < 0 || i >= splitted.size() ) {
            return DatatypeConstants.FIELD_UNDEFINED;
        }

        String strVal = splitted.get( i );
        if ( strVal == null || strVal.trim().length() == 0 || "-".equals( strVal.trim() ) ) {
            return DatatypeConstants.FIELD_UNDEFINED;
        }

        try {
            return Integer.valueOf( strVal.trim() );
        }
        catch ( NumberFormatException e ) {
            LOG.warn( String.format( "Cannot convert [%s] to integer", strVal.trim() ) );
            return DatatypeConstants.FIELD_UNDEFINED;
        }
    }

    private XMLGregorianCalendar gefToXMLGregorianCalendar(String strDate, String strTime) {

        XMLGregorianCalendar xcal;
        try {
            Splitter splitter = Splitter.on( ',' );
            List<String> dateParts = splitter.splitToList( strDate );
            List<String> timeParts = splitter.splitToList( strTime );

            // formaat: JJ-MM-DDTUU:MM:SS+UU:MM
            GregorianCalendar calendar = new GregorianCalendar(
                getGcIntValue( dateParts, 0 ), //
                getGcIntValue( dateParts, 1 ) - 1, // januari begint bij 0
                getGcIntValue( dateParts, 2 ), //
                getGcIntValue( timeParts, 0 ), //
                getGcIntValue( timeParts, 1 ), //
                getGcIntValue( timeParts, 2 ) //
            );
            calendar.setTimeZone( TimeZone.getTimeZone( ZoneId.of( TZ_AMSTERDAM ) ) );
            xcal = DatatypeFactory.newInstance().newXMLGregorianCalendar( calendar );
            xcal.setMillisecond( FIELD_UNDEFINED );
        }
        catch ( DatatypeConfigurationException e ) {
            LOG.error( e.getMessage(), e );
            throw new IllegalStateException( e );
        }
        return xcal;
    }

    private int getGcIntValue(List<String> splitted, int i) {

        if ( splitted == null || i < 0 || i >= splitted.size() ) {
            return 0;
        }

        String strVal = splitted.get( i );
        if ( strVal == null || strVal.trim().length() == 0 || "-".equals( strVal.trim() ) ) {
            return 0;
        }

        try {
            return Integer.valueOf( strVal.trim() );
        }
        catch ( NumberFormatException e ) {
            LOG.warn( String.format( "Cannot convert [%s] to integer", strVal.trim() ) );
            return 0;
        }
    }

    @DoNotSelectMethod
    protected static String toLowerCase(String in) {
        return in != null ? in.toLowerCase() : in;
    }

    @Qualifier
    @Target( ElementType.METHOD )
    @Retention( RetentionPolicy.CLASS )
    public static @interface DoNotSelectMethod {
    }

    public static GefToBroMapperCommon getInstance() {
        return INSTANCE;
    }

}
