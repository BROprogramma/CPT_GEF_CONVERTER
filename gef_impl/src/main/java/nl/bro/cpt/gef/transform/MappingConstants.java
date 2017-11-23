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

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import com.google.common.collect.ImmutableMap;
import com.google.common.collect.ImmutableSet;

import nl.broservices.xsd.brocommon.v_3_0.IndicationYesNoEnumeration;
import nl.broservices.xsd.brocommon.v_3_0.IndicationYesNoUnknownEnumeration;
import nl.broservices.xsd.brocommon.v_3_0.PartialDateType;
import nl.broservices.xsd.brocommon.v_3_0.VoidReasonEnumeration;

/**
 *
 * @author derksenjpam,  J.M. van Ommeren
 */
public class MappingConstants {

    private static final nl.broservices.xsd.brocommon.v_3_0.ObjectFactory BRO_OF = new nl.broservices.xsd.brocommon.v_3_0.ObjectFactory();


    // constants

    public static final String NULL = "<NULL>";
    public static final String NOT_NULL = "<NOT_NULL>";
    public static final String TRANSACTION_TYPE_REGISTRATION = "R";
    public static final String TRANSACTION_TYPE_CORRECTION = "C";
    public static final String IMBRO = "IMBRO";
    public static final String IMBROA = "IMBRO/A";
    public static final String REGISTRATION_STATUS_REGISTRATION = "geregistreerd";

    public static final String BRO_IND_YES = "ja";
    public static final String BRO_IND_NO = "nee";
    public static final String BRO_IND_UNKNOWN = "onbekend";
    public static final String BRO_IND_NOT_APPLICABLE = "nvt";
    public static final String GEF_IND_YES = "ja";
    public static final String GEF_IND_NO = "nee";
    public static final String GEF_IND_UNKNOWN = "onbekend";
    public static final String NEGEREN = "negeren";
    public static final String MAAIVELD = "maaiveld";
    public static final String WATERBODEM = "waterbodem";
    public static final String FLOW_BED = "flow bed";
    public static final String WB = "wb";
    public static final String KAART_GROOTSCHALIG = "kaartGrootschalig";
    public static final String KAART_KLEINSCHALIG = "kaartKleinschalig";
    public static final String LANDMETING_ONBEKEND = "landmetingOnbekend";
    public static final String KAART_ONBEKEND = "kaartOnbekend";
    public static final String CPT_STANDARD_3680 = "3680";

    public static final String CS_CORRECTION_REASON = "urn:bro:cpt:CorrectionReason";
    public static final String CS_METHOD = "urn:bro:cpt:CPTMethod";
    public static final String CS_QUALITY_CLASS = "urn:bro:cpt:QualityClass";
    public static final String CS_STANDARD = "urn:bro:cpt:CPTStandard";
    public static final String CS_SURVEY_PURPOSE = "urn:bro:cpt:SurveyPurpose";
    public static final String CS_LOCAL_VERTICAL_REFERENCE_POINT = "urn:bro:cpt:LocalVerticalReferencePoint";
    public static final String CS_HORIZONTAL_POSITIONING_METHOD = "urn:bro:cpt:HorizontalPositioningMethod";
    public static final String CS_VERTICAL_POSITIONING_METHOD = "urn:bro:cpt:VerticalPositioningMethod";
    public static final String CS_STOP_CRITERION = "urn:bro:cpt:StopCriterion";
    public static final String CS_VERTICAL_DATUM = "urn:bro:cpt:VerticalDatum";
    public static final String CS_DELIVERY_CONTEXT = "urn:bro:cpt:DeliveryContext";

    public static final String UOM_M = "m";
    public static final String UOM_MM = "mm";
    public static final String UOM_DEG = "deg";
    public static final String UOM_1 = "1";
    public static final String UOM_MPA = "MPa";
    public static final String UOM_S_M = "S/m";
    public static final String UOM_MM2 = "mm2";

    public static final String UNKNOWN_DATE_EXPRESSION = "java( MappingConstants.unknownDate() )";

    public static final Set<String> WATERBODEM_VALUES = ImmutableSet.<String> builder()
                                                                    .add( WATERBODEM ) //
                                                                    .add( WB ) //
                                                                    .add( FLOW_BED ) //
                                                                    .build(); //

    public static final Map<String, String> EMPTY_MAP = ImmutableMap.<String, String> builder().build();

    public static final Map<String, IndicationYesNoEnumeration> YES_NO_CONV_YES_NO = ImmutableMap.<String, IndicationYesNoEnumeration> builder()
                                                                                                 .put( GEF_IND_YES, IndicationYesNoEnumeration.JA ) //
                                                                                                 .put( GEF_IND_NO, IndicationYesNoEnumeration.NEE ) //
                                                                                                 .build(); //

    /**
     * YES_NO_CONV_YES_NO_UNKNOWN_IMBRO: Voor IMBRO bestaat er geen onbekend.
     */
    public static final Map<String, IndicationYesNoUnknownEnumeration> YES_NO_CONV_YES_NO_UNKNOWN_IMBRO =
        ImmutableMap.<String, IndicationYesNoUnknownEnumeration> builder()
                    .put( GEF_IND_YES, IndicationYesNoUnknownEnumeration.JA ) //
                    .put( GEF_IND_NO, IndicationYesNoUnknownEnumeration.NEE ) //
                    .build(); //

    public static final Map<String, IndicationYesNoUnknownEnumeration> YES_NO_CONV_YES_NO_UNKNOWN_IMBROA;
    static {
        YES_NO_CONV_YES_NO_UNKNOWN_IMBROA = new HashMap<>();
        YES_NO_CONV_YES_NO_UNKNOWN_IMBROA.put( GEF_IND_YES, IndicationYesNoUnknownEnumeration.JA );
        YES_NO_CONV_YES_NO_UNKNOWN_IMBROA.put( GEF_IND_NO, IndicationYesNoUnknownEnumeration.NEE );
        YES_NO_CONV_YES_NO_UNKNOWN_IMBROA.put( "", IndicationYesNoUnknownEnumeration.ONBEKEND );
        YES_NO_CONV_YES_NO_UNKNOWN_IMBROA.put( null, IndicationYesNoUnknownEnumeration.ONBEKEND );
        YES_NO_CONV_YES_NO_UNKNOWN_IMBROA.put( GEF_IND_UNKNOWN, IndicationYesNoUnknownEnumeration.ONBEKEND );
    }

    public static final Map<String, String> CPT_STANDARD_CONV_IMBRO = ImmutableMap.<String, String> builder()
                                                                                  .put( "5140", "NEN5140" ) //
                                                                                  .put( "22476-1", "ISO22476D1" ) //
                                                                                  .put( "22476-12", "ISO22476D12" ) //
                                                                                  .build(); //

    public static final Map<String, String> CPT_STANDARD_CONV_IMBROA;

    static {
        CPT_STANDARD_CONV_IMBROA = new HashMap<>();
        CPT_STANDARD_CONV_IMBROA.put( CPT_STANDARD_3680, "NEN3680" );
        CPT_STANDARD_CONV_IMBROA.put( "5140", "NEN5140" );
        CPT_STANDARD_CONV_IMBROA.put( "22476-1", "ISO22476D1" );
        CPT_STANDARD_CONV_IMBROA.put( "22476-12", "ISO22476D12" );
        CPT_STANDARD_CONV_IMBROA.put( null, BRO_IND_UNKNOWN );
        CPT_STANDARD_CONV_IMBROA.put( "", BRO_IND_UNKNOWN );
    }

    public static final Map<String, String> CRS_CONV = ImmutableMap.<String, String> builder()
                                                                   .put( "31000", "28992" ) // RD
                                                                   .put( "28992", "28992" ) // RD
                                                                   .put( "4258", "4258" ) // ETRS
                                                                   .put( "4326", "4326" ) // WGS
                                                                   .build(); //

    public static final Map<String, Integer> CRS_SCALE_CONV = ImmutableMap.<String, Integer> builder()
                                                                          .put( "31000", 3 ) // RD
                                                                          .put( "28992", 3 ) // RD
                                                                          .put( "4258", 9 ) // ETRS
                                                                          .put( "4326", 9 ) // WGS
                                                                          .build(); //

    public static final Map<String, Boolean> CRS_GEOGRAPHIC_CONV = ImmutableMap.<String, Boolean> builder()
                                                                               .put( "31000", true ) // RD
                                                                               .put( "28992", true ) // RD
                                                                               .put( "4258", false ) // ETRS
                                                                               .put( "4326", false ) // WGS
                                                                               .build(); //

    public static final Map<String, String> CPT_VERT_DAT_CONV_IMBRO = ImmutableMap.<String, String> builder()
                                                                                  .put( "31000", "NAP" ) //
                                                                                  .put( "5709", "NAP" ) //
                                                                                  .put( "5861", "LAT" ) //
                                                                                  .put( "5714", "MSL" ) //
                                                                                  .build();

    public static final Map<String, String> CPT_VERT_DAT_CONV_IMBROA;

    static {
        CPT_VERT_DAT_CONV_IMBROA = new HashMap<>();
        CPT_VERT_DAT_CONV_IMBROA.put( "31000", "NAP" );
        CPT_VERT_DAT_CONV_IMBROA.put( "5709", "NAP" );
        CPT_VERT_DAT_CONV_IMBROA.put( "5861", "LAT" );
        CPT_VERT_DAT_CONV_IMBROA.put( "5714", "MSL" );
        CPT_VERT_DAT_CONV_IMBROA.put( "", BRO_IND_UNKNOWN );
        CPT_VERT_DAT_CONV_IMBROA.put( null, BRO_IND_UNKNOWN );
    }

    public static final Map<String, String> METHOD_HOR_POS_CONV_IMBRO = ImmutableMap.<String, String> builder()
                                                                                    .put( "ldgp", "DGPS50tot200cm" )
                                                                                    .put( "ldgm", "GPS200tot1000cm" )
                                                                                    .put( "lrg1", "RTKGPS0tot2cm" )
                                                                                    .put( "lrg2", "RTKGPS2tot5cm" )
                                                                                    .put( "lrg3", "RTKGPS5tot10cm" )
                                                                                    .put( "lrg4", "RTKGPS10tot50cm" )
                                                                                    .put( "lmetz", "tachymetrie0tot10cm" )
                                                                                    .put( "lta2", "tachymetrie10tot50cm" )
                                                                                    .build();

    public static final Map<String, String> METHOD_HOR_POS_CONV_IMBROA;
    static {
        METHOD_HOR_POS_CONV_IMBROA = new HashMap<>();
        METHOD_HOR_POS_CONV_IMBROA.put( "ldgp", "DGPS50tot200cm" );
        METHOD_HOR_POS_CONV_IMBROA.put( "ldgm", "GPS200tot1000cm" );
        METHOD_HOR_POS_CONV_IMBROA.put( "lrg1", "RTKGPS0tot2cm" );
        METHOD_HOR_POS_CONV_IMBROA.put( "lrg2", "RTKGPS2tot5cm" );
        METHOD_HOR_POS_CONV_IMBROA.put( "lrg3", "RTKGPS5tot10cm" );
        METHOD_HOR_POS_CONV_IMBROA.put( "lrg4", "RTKGPS10tot50cm" );
        METHOD_HOR_POS_CONV_IMBROA.put( "lmetz", "tachymetrie0tot10cm" );
        METHOD_HOR_POS_CONV_IMBROA.put( "lta2", "tachymetrie10tot50cm" );
        METHOD_HOR_POS_CONV_IMBROA.put( "ldgz", "DGPS0tot100cm" );
        METHOD_HOR_POS_CONV_IMBROA.put( "ldgn", "DGPS100tot500cm" );
        METHOD_HOR_POS_CONV_IMBROA.put( "lgbkn", "GBKNOnbekend" );
        METHOD_HOR_POS_CONV_IMBROA.put( "lgps", "GPSOnbekend" );
        METHOD_HOR_POS_CONV_IMBROA.put( "ltgr", KAART_GROOTSCHALIG );
        METHOD_HOR_POS_CONV_IMBROA.put( "ld01", KAART_GROOTSCHALIG );
        METHOD_HOR_POS_CONV_IMBROA.put( "ld02", KAART_GROOTSCHALIG );
        METHOD_HOR_POS_CONV_IMBROA.put( "ld05", KAART_GROOTSCHALIG );
        METHOD_HOR_POS_CONV_IMBROA.put( "ld10", KAART_GROOTSCHALIG );
        METHOD_HOR_POS_CONV_IMBROA.put( "ld25", KAART_GROOTSCHALIG );
        METHOD_HOR_POS_CONV_IMBROA.put( "lt10", KAART_GROOTSCHALIG );
        METHOD_HOR_POS_CONV_IMBROA.put( "ltkl", KAART_KLEINSCHALIG );
        METHOD_HOR_POS_CONV_IMBROA.put( "lt25", KAART_KLEINSCHALIG );
        METHOD_HOR_POS_CONV_IMBROA.put( "lt50", KAART_KLEINSCHALIG );
        METHOD_HOR_POS_CONV_IMBROA.put( "lmet", LANDMETING_ONBEKEND );
        METHOD_HOR_POS_CONV_IMBROA.put( "lmetn", LANDMETING_ONBEKEND );
        METHOD_HOR_POS_CONV_IMBROA.put( "lgov", BRO_IND_UNKNOWN );
        METHOD_HOR_POS_CONV_IMBROA.put( "lsov", BRO_IND_UNKNOWN );
        METHOD_HOR_POS_CONV_IMBROA.put( "lonb", BRO_IND_UNKNOWN );
        METHOD_HOR_POS_CONV_IMBROA.put( "", BRO_IND_UNKNOWN );

    }

    public static final Map<String, String> LOC_VERT_REF_POINT_CONV_IMBRO = ImmutableMap.<String, String> builder()
                                                                                        .put( MAAIVELD, MAAIVELD )
                                                                                        .put( "mv", MAAIVELD )
                                                                                        .put( "ground level", MAAIVELD )
                                                                                        .put( "groundlevel", MAAIVELD )
                                                                                        .put( WATERBODEM, WATERBODEM )
                                                                                        .put( "wb", WATERBODEM )
                                                                                        .put( FLOW_BED, WATERBODEM )
                                                                                        .build();

    public static final Map<String, String> LOC_VERT_REF_POINT_CONV_IMBROA = ImmutableMap.<String, String> builder()
                                                                                         .put( MAAIVELD, MAAIVELD )
                                                                                         .put( "mv", MAAIVELD )
                                                                                         .put( "ground level", MAAIVELD )
                                                                                         .put( "groundlevel", MAAIVELD )
                                                                                         .put( WATERBODEM, WATERBODEM )
                                                                                         .put( "wb", WATERBODEM )
                                                                                         .put( FLOW_BED, WATERBODEM )
                                                                                         .put( "nap", "NAP" )
                                                                                         .put( "msl", "MSL" )
                                                                                         .build();

    public static final Map<String, String> VERT_METHOD_CONV_IMBRO = ImmutableMap.<String, String> builder()
                                                                                 .put( "mah2", "AHN2" )
                                                                                 .put( "mah3", "AHN3" )
                                                                                 .put( "mrg1", "RTKGPS0tot4cm" )
                                                                                 .put( "mrg2", "RTKGPS4tot10cm" )
                                                                                 .put( "mdgn", "RTKGPS10tot20cm" )
                                                                                 .put( "mdgm", "RTKGPS20tot100cm" )
                                                                                 .put( "metz", "tachymetrie0tot10cm" )
                                                                                 .put( "mta2", "tachymetrie10tot50cm" )
                                                                                 .put( "mwp1", "waterpassing0tot2cm" )
                                                                                 .put( "mwp2", "waterpassing2tot4cm" )
                                                                                 .put( "mwp3", "waterpassing4tot10cm" )
                                                                                 .build();

    public static final Map<String, String> VERT_METHOD_CONV_IMBROA;
    static {
        VERT_METHOD_CONV_IMBROA = new HashMap<>();
        VERT_METHOD_CONV_IMBROA.put( "mah2", "AHN1" );
        VERT_METHOD_CONV_IMBROA.put( "mah3", "AHN3" );
        VERT_METHOD_CONV_IMBROA.put( "mrg1", "RTKGPS0tot4cm" );
        VERT_METHOD_CONV_IMBROA.put( "mrg2", "RTKGPS4tot10cm" );
        VERT_METHOD_CONV_IMBROA.put( "mdgn", "RTKGPS10tot20cm" );
        VERT_METHOD_CONV_IMBROA.put( "mdgm", "RTKGPS20tot100cm" );
        VERT_METHOD_CONV_IMBROA.put( "metz", "tachymetrie0tot10cm" );
        VERT_METHOD_CONV_IMBROA.put( "mta2", "tachymetrie10tot50cm" );
        VERT_METHOD_CONV_IMBROA.put( "mwp1", "waterpassing0tot2cm" );
        VERT_METHOD_CONV_IMBROA.put( "mwp2", "waterpassing2tot4cm" );
        VERT_METHOD_CONV_IMBROA.put( "mwp3", "waterpassing4tot10cm" );
        VERT_METHOD_CONV_IMBROA.put( "mah1", "AHN1" );
        VERT_METHOD_CONV_IMBROA.put( "mahn", "AHNOnbekend" );
        VERT_METHOD_CONV_IMBROA.put( "mdgz", "DGPS0tot10cm" );
        VERT_METHOD_CONV_IMBROA.put( "geen", "geen" );
        VERT_METHOD_CONV_IMBROA.put( "mdgp", "GPSOnbekend" );
        VERT_METHOD_CONV_IMBROA.put( "mh10", KAART_GROOTSCHALIG );
        VERT_METHOD_CONV_IMBROA.put( "mtgr", KAART_GROOTSCHALIG );
        VERT_METHOD_CONV_IMBROA.put( "mt25", KAART_KLEINSCHALIG );
        VERT_METHOD_CONV_IMBROA.put( "mtkl", KAART_KLEINSCHALIG );
        VERT_METHOD_CONV_IMBROA.put( "mt50", KAART_KLEINSCHALIG );
        VERT_METHOD_CONV_IMBROA.put( "monb", KAART_ONBEKEND );
        VERT_METHOD_CONV_IMBROA.put( "mnet", LANDMETING_ONBEKEND );
        VERT_METHOD_CONV_IMBROA.put( "metn", LANDMETING_ONBEKEND );
        VERT_METHOD_CONV_IMBROA.put( "mgov", BRO_IND_UNKNOWN );
        VERT_METHOD_CONV_IMBROA.put( null, BRO_IND_UNKNOWN );
    }

    public static final Map<String, String> SURVEY_PURPOSE_CONV_IMBRO = ImmutableMap.<String, String> builder()
                                                                                    .put( "waterkering", "waterkering" )
                                                                                    .put( "bouwwerk en constructie", "bouwwerkConstructie" )
                                                                                    .put( "infrastructuur land", "infrastructuurLand" )
                                                                                    .put( "infrastructuur water", "infrastructuurWater" )
                                                                                    .put( "milieuonderzoek", "milieuonderzoek" )
                                                                                    .put( "controle onderzoek", "controleOnderzoek" )
                                                                                    .put( "vergunning", "vergunning" )
                                                                                    .put( "overig onderzoek", "overigOnderzoek" )
                                                                                    .build();

    public static final String SURVEY_PURPOSE_CONV_IMBROA_UNKNOWN = "onbekend";

    public static final Map<String, String> DEL_CON_CONV_IMBRO =
        ImmutableMap.<String, String> builder().put( "rechtsgrond mijnbouwwet", "MBW" ).put( "rechtsgrond waterwet", "WW" ).put( "opdracht publieke taakuitvoering", "publiekeTaak" ).build();

    public static final String DELIVERY_CONTEXT_MISC = "archiefoverdracht";

    public static final Map<String, String> CON_METHOD_CONV_IMBRO = ImmutableMap.<String, String> builder()
                                                                                .put( "1", "mechanischDiscontinu" ) //
                                                                                .put( "2", "mechanischContinu" ) //
                                                                                .put( "3", "elektrischDiscontinu" ) //
                                                                                .put( "4", "elektrischContinu" ) //
                                                                                .build(); //

    public static final Map<String, String> CON_METHOD_CONV_IMBROA;

    static {
        CON_METHOD_CONV_IMBROA = new HashMap<>();
        CON_METHOD_CONV_IMBROA.put( "0", "elektrisch" );
        CON_METHOD_CONV_IMBROA.put( "1", "mechanischDiscontinu" );
        CON_METHOD_CONV_IMBROA.put( "2", "mechanischContinu" );
        CON_METHOD_CONV_IMBROA.put( "3", "elektrischDiscontinu" );
        CON_METHOD_CONV_IMBROA.put( "4", "elektrischContinu" );
        CON_METHOD_CONV_IMBROA.put( "5", "mechanisch" );
        CON_METHOD_CONV_IMBROA.put( null, BRO_IND_UNKNOWN );
        CON_METHOD_CONV_IMBROA.put( "", BRO_IND_UNKNOWN );
    }

    public static final Map<String, String> QUALITY_CLASS_CONV_IMBRO =
        ImmutableMap.<String, String> builder()
                    .put( "1", "klasse1" )
                    .put( "2", "klasse2" )
                    .put( "3", "klasse3" )
                    .put( "4", "klasse4" )
                    .put( "5", "klasse5" )
                    .put( "6", "klasse6" )
                    .put( "7", "klasse7" )
                    .build();

    public static final Map<String, String> STOP_CRITERIUM_CONV_IMBRO = ImmutableMap.<String, String> builder()
                                                                                    .put( "0", "einddiepte" )
                                                                                    .put( "1", "wegdrukkracht" )
                                                                                    .put( "2", "conusweerstand" )
                                                                                    .put( "3", "wrijvingsweerstand" )
                                                                                    .put( "4", "waterspanning" )
                                                                                    .put( "5", "hellingshoek" )
                                                                                    .put( "6", "obstakel" )
                                                                                    .put( "7", "bezwijkrisico" )
                                                                                    .put( "8", "storing" )
                                                                                    .build();

    public static final Map<String, String> QUALITY_CLASS_CONV_IMBROA;

    static {
        QUALITY_CLASS_CONV_IMBROA = new HashMap<>();
        QUALITY_CLASS_CONV_IMBROA.put( "1", "klasse1" );
        QUALITY_CLASS_CONV_IMBROA.put( "2", "klasse2" );
        QUALITY_CLASS_CONV_IMBROA.put( "3", "klasse3" );
        QUALITY_CLASS_CONV_IMBROA.put( "4", "klasse4" );
        QUALITY_CLASS_CONV_IMBROA.put( "5", "klasse5" );
        QUALITY_CLASS_CONV_IMBROA.put( "6", "klasse6" );
        QUALITY_CLASS_CONV_IMBROA.put( "7", "klasse7" );
        QUALITY_CLASS_CONV_IMBROA.put( "", BRO_IND_UNKNOWN );
        QUALITY_CLASS_CONV_IMBROA.put( null, BRO_IND_UNKNOWN );
        QUALITY_CLASS_CONV_IMBROA.put( "sondeernorm=3680", "nvt" );
    }

    public static final Map<String, String> STOP_CRITERIUM_CONV_IMBROA;

    static {
        STOP_CRITERIUM_CONV_IMBROA = new HashMap<>();
        STOP_CRITERIUM_CONV_IMBROA.put( "0", "einddiepte" );
        STOP_CRITERIUM_CONV_IMBROA.put( "1", "wegdrukkracht" );
        STOP_CRITERIUM_CONV_IMBROA.put( "2", "conusweerstand" );
        STOP_CRITERIUM_CONV_IMBROA.put( "3", "wrijvingsweerstand" );
        STOP_CRITERIUM_CONV_IMBROA.put( "4", "waterspanning" );
        STOP_CRITERIUM_CONV_IMBROA.put( "5", "hellingshoek" );
        STOP_CRITERIUM_CONV_IMBROA.put( "6", "obstakel" );
        STOP_CRITERIUM_CONV_IMBROA.put( "7", "bezwijkrisico" );
        STOP_CRITERIUM_CONV_IMBROA.put( "8", "storing" );
        STOP_CRITERIUM_CONV_IMBROA.put( null, BRO_IND_UNKNOWN );
        STOP_CRITERIUM_CONV_IMBROA.put( "", BRO_IND_UNKNOWN );
    }

    public static final Set<String> VALID_FILE_TYPE_CD_IMBRO = ImmutableSet.<String> builder()
                                                                           .add( "GEF" )
                                                                           .add( "GEF-CPT-Report,1,1,1" )
                                                                           .add( "GEF-CPT-Report,1,1,2" )
                                                                           .add( "GEF-CPT-Report,1,0,0" )
                                                                           .add( "GEF-CPT-Report,1,1,0" )
                                                                           .add( "LOGDIGI" )
                                                                           .add( "NENGEO" )
                                                                           .add( "NENPAAL" )
                                                                           .add( "NEURALOG" )
                                                                           .add( "UNIPLOT" )
                                                                           .build();

    public static PartialDateType unknownDate() {
        PartialDateType partialDate = BRO_OF.createPartialDateType();
        partialDate.setVoidReason( VoidReasonEnumeration.ONBEKEND );
        return partialDate;
    }

    private MappingConstants() {

    }

}
