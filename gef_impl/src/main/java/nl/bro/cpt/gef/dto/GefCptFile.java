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
package nl.bro.cpt.gef.dto;

import static nl.bro.cpt.gef.transform.MappingConstants.GEF_IND_NO;
import static nl.bro.cpt.gef.transform.MappingConstants.GEF_IND_YES;
import static nl.bro.cpt.gef.transform.MappingConstants.NOT_NULL;
import static nl.bro.cpt.gef.transform.MappingConstants.NULL;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import javax.validation.Valid;
import javax.validation.constraints.DecimalMax;
import javax.validation.constraints.DecimalMin;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

import org.hibernate.validator.constraints.NotEmpty;

import nl.bro.dto.gef.groups.Correction;
import nl.bro.dto.gef.groups.Imbro;
import nl.bro.dto.gef.groups.ImbroA;
import nl.bro.dto.gef.validation.AdditionalInvestigationValid;
import nl.bro.dto.gef.validation.AssertAbsence;
import nl.bro.dto.gef.validation.AssertFilled;
import nl.bro.dto.gef.validation.AssertPresence;
import nl.bro.dto.gef.validation.AtLeastOneMeasurementPresent;
import nl.bro.dto.gef.validation.DVPMethodGeenWhenOffsetUnknown;
import nl.bro.dto.gef.validation.DateInPast;
import nl.bro.dto.gef.validation.DateValid;
import nl.bro.dto.gef.validation.GefLocationValid;
import nl.bro.dto.gef.validation.IncompleteDateValid;
import nl.bro.dto.gef.validation.IsBefore;
import nl.bro.dto.gef.validation.IsNotBefore01011930;
import nl.bro.dto.gef.validation.SpecimenVarsEmptyWhenNotPredrilled;
import nl.bro.dto.gef.validation.SpecimenVarsValid;
import nl.bro.dto.gef.validation.StandardAccordingQualityClass;
import nl.bro.dto.gef.validation.TimeValid;
import nl.bro.dto.gef.validation.ValidCode;
import nl.bro.dto.gef.validation.ValidMes1;
import nl.bro.dto.gef.validation.WaterDepthNotNullWhenLVRPWaterbodem;

@AdditionalInvestigationValid( groups = Imbro.class )
@StandardAccordingQualityClass

// : de volgende controle kan misschien problemen opleveren bij partieel leveren van dissipatie testen.
@AssertFilled( whenRef = "${measurementText109}", thenThisCollectionMustBeFilled = "${expectedChildFileNameList}", hasRefValue = GEF_IND_YES )

// controles op basis van aanwezigheid aanvullend onderzoek
@AssertPresence(  whenRef = "${measurementText111}", hasRefValue = GEF_IND_YES, thenThisFieldMustBePresent = "${measurementText114}", groups = Imbro.class )
// volgende controles op basis van aanwezigheid plaatselijke wrijving.
@AssertPresence( whenRef = "${columnInfo3}", hasRefValue = GEF_IND_YES,  thenThisFieldMustBePresent = "${measurementVar2}", groups = Imbro.class )
@AssertPresence( whenRef = "${columnInfo3}", hasRefValue = GEF_IND_YES, thenThisFieldMustBePresent = "${measurementVar4}", groups = Imbro.class )
@AssertPresence( whenRef = "${columnInfo3}", hasRefValue = GEF_IND_YES, thenThisFieldMustBePresent = "${measurementVar5}", groups = Imbro.class )
@AssertPresence( whenRef = "${measurementVarPresent42}", hasRefValue = GEF_IND_YES, thenThisFieldMustBePresent = "${measurementVar42}", groups = Imbro.class )

// aanwezigheid kolommen
@AtLeastOneMeasurementPresent( column = "2", mandatory = true )
@AtLeastOneMeasurementPresent( column = "3" )
@AtLeastOneMeasurementPresent( column = "4" )
@AtLeastOneMeasurementPresent( column = "5" )
@AtLeastOneMeasurementPresent( column = "6" )
@AtLeastOneMeasurementPresent( column = "7" )
@AtLeastOneMeasurementPresent( column = "8" )
@AtLeastOneMeasurementPresent( column = "9" )
@AtLeastOneMeasurementPresent( column = "10" )
@AtLeastOneMeasurementPresent( column = "11" )
@AtLeastOneMeasurementPresent( column = "12" )
@AtLeastOneMeasurementPresent( column = "13" )
@AtLeastOneMeasurementPresent( column = "14" )
@AtLeastOneMeasurementPresent( column = "15" )
@AtLeastOneMeasurementPresent( column = "21" )
@AtLeastOneMeasurementPresent( column = "22" )
@AtLeastOneMeasurementPresent( column = "23" )
@AtLeastOneMeasurementPresent( column = "31" )
@AtLeastOneMeasurementPresent( column = "32" )
@AtLeastOneMeasurementPresent( column = "33" )
@AtLeastOneMeasurementPresent( column = "34" )
@AtLeastOneMeasurementPresent( column = "35" )
@AtLeastOneMeasurementPresent( column = "36" )
@AtLeastOneMeasurementPresent( column = "39" )

// zero load measurements
@AssertPresence( whenRef = "${measurementVar20}",  hasRefValue = NOT_NULL, thenThisFieldMustBePresent = "${measurementVar21}" )
@AssertAbsence( whenRef = "${measurementVar20}",  hasRefValue = NULL, thenThisFieldMustBeAbsent = "${measurementVar21}" )
@AssertPresence( whenRef = "${measurementVar22}",  hasRefValue = NOT_NULL, thenThisFieldMustBePresent = "${measurementVar23}" )
@AssertAbsence( whenRef = "${measurementVar22}",  hasRefValue = NULL, thenThisFieldMustBeAbsent = "${measurementVar23}" )
@AssertPresence( whenRef = "${measurementVar24}",  hasRefValue = NOT_NULL, thenThisFieldMustBePresent = "${measurementVar25}" )
@AssertAbsence( whenRef = "${measurementVar24}",  hasRefValue = NULL, thenThisFieldMustBeAbsent = "${measurementVar25}" )
@AssertPresence( whenRef = "${measurementVar26}",  hasRefValue = NOT_NULL, thenThisFieldMustBePresent = "${measurementVar27}" )
@AssertAbsence( whenRef = "${measurementVar26}",  hasRefValue = NULL, thenThisFieldMustBeAbsent = "${measurementVar27}" )
@AssertPresence( whenRef = "${measurementVar28}",  hasRefValue = NOT_NULL, thenThisFieldMustBePresent = "${measurementVar29}" )
@AssertAbsence( whenRef = "${measurementVar28}",  hasRefValue = NULL, thenThisFieldMustBeAbsent = "${measurementVar29}" )
@AssertPresence( whenRef = "${measurementVar30}",  hasRefValue = NOT_NULL, thenThisFieldMustBePresent = "${measurementVar31}" )
@AssertAbsence( whenRef = "${measurementVar30}",  hasRefValue = NULL, thenThisFieldMustBeAbsent = "${measurementVar31}" )
@AssertPresence( whenRef = "${measurementVar32}",  hasRefValue = NOT_NULL, thenThisFieldMustBePresent = "${measurementVar33}" )
@AssertAbsence( whenRef = "${measurementVar32}",  hasRefValue = NULL, thenThisFieldMustBeAbsent = "${measurementVar33}" )
@AssertPresence( whenRef = "${measurementVar34}",  hasRefValue = NOT_NULL, thenThisFieldMustBePresent = "${measurementVar35}" )
@AssertAbsence( whenRef = "${measurementVar34}",  hasRefValue = NULL, thenThisFieldMustBeAbsent = "${measurementVar35}" )
@AssertPresence( whenRef = "${measurementVar36}",  hasRefValue = NOT_NULL, thenThisFieldMustBePresent = "${measurementVar37}" )
@AssertAbsence( whenRef = "${measurementVar36}",  hasRefValue = NULL, thenThisFieldMustBeAbsent = "${measurementVar37}" )


@IsBefore( future = "${measurementText112}", past = "${measurementText105}" )
@IsBefore( future = "${measurementText112}", past = "${measurementText107}" )
@IsBefore( future = "${measurementText112}", past = "${measurementText113}" )
@IsBefore( future = "${measurementText112}", past = "${measurementText114}" )
@IsBefore( future = "${measurementText112}", past = "${startDate}" )

// measurementText42 == geen alleen wanneer verschuiving onbekend is (null)
@DVPMethodGeenWhenOffsetUnknown( groups = ImbroA.class )

// location based mustBeCheckedOn
@GefLocationValid
@WaterDepthNotNullWhenLVRPWaterbodem( groups = Imbro.class )
@SpecimenVarsEmptyWhenNotPredrilled
public class GefCptFile extends GefFileFacade<DataRowCpt> implements IfcCptFile {

    private static final long serialVersionUID = -5909071347828866632L;

    // hulpvelden voor PARENT en CHILD bestandsnamen
    private String parentFileName;
    private List<String> expectedChildFileNameList;

    @NotNull
    private String gefId;

    @NotNull
    @ValidCode( allowedCodes = { "GEF-CPT-Report.1.1.0", "GEF-CPT-Report.1.1.1", "GEF-CPT-Report.1.1.2" }, isCaseSensitive = false )
    private String reportCode;

    /*
     * COMPANYID [Text], [uitvoerder (text)], [integer]: = uitvoerder (GeotechnicalCPTSurvey). Condities (IMBRO,
     * IMBRO/A): verplicht (pas op) moet voldoen aan NNNNNNNN
     */
    private String companyId;

    /*
     * measurementtext4 [conusttype (text)], [text] Conustype (conepenetormeter)
     */
    @NotNull( groups = Imbro.class )
    @Size( min = 1, max = 200 )
    @ValidMes1
    private String measurementText4;

    /*
     * MEASUREMENTTEXT5 [omschrijving (text)], [text] description (conepenetrometer)
     */
    @NotNull( groups = Imbro.class )
    @Size( min = 1, max = 200 )
    @ValidMes1
    private String measurementText5;

    /*
     * MEASUREMENTTEXT6 [sondeernorm (text)], [text] SONDEERNORM Bepalen voordat de kwaliteitsklasse bepaald wordt!
     */
    @NotEmpty( groups = Imbro.class )
    @ValidCode( allowedCodes = { "5140", "22476-1", "22476-12" }, groups = Imbro.class )
    @ValidCode( allowedCodes = { "onbekend", "3680", "5140", "22476-1", "22476-12" }, isCaseSensitive = false, groups = ImbroA.class )
    private String measurementText6sondeernorm;

    /*
     * MEASUREMENTTEXT6 [kwaliteitsklasse (text)], [text] KWALITEITSKLASSE Bepalen nadat de sondeernorm is bepaald.
     */
    @NotEmpty( groups = Imbro.class )
    @ValidCode( allowedCodes = { "1", "2", "3", "4", "5", "6", "7" }, groups = Imbro.class )
    @ValidCode( allowedCodes = { "1", "2", "3", "4", "5", "6", "7", "onbekend", "nvt" }, isCaseSensitive = false, groups = ImbroA.class )
    private String measurementText6kwaliteitsklasse;

    /*
     * MEASUREMENTTEXT9 [lokaal verticaal referentiepunt (text)], [text] Lokaal verticaal referentiepunt (aangeleverde
     * locatie)
     */
    @NotNull
    @ValidCode( allowedCodes = { "maaiveld", "mv", "ground level", "groundlevel", "waterbodem", "wb", "flow bed" }, isCaseSensitive = false, groups = Imbro.class )
    @ValidCode( allowedCodes = { "maaiveld", "mv", "ground level", "groundlevel", "waterbodem", "wb", "flow bed", "nap", "msl" }, isCaseSensitive = false, groups = ImbroA.class )
    private String measurementText9;

    /*
     * MEASUREMENTTEXT11 [omstandigheden (text)], [text] IMBRO: Verplicht indien aanvullend onderzoek is uitgevoerd en
     * de attributen hoedanigheid en grondwaterstand geen waarde hebben en er geen verwijderede lagen zijn beschreven.
     * omstandigheden (aanvullend onderzoek)
     */
    @Size( min = 1, max = 200 )
    @ValidMes1
    private String measurementText11;

    /*
     * MEASUREMENTTEXT20 [signaalbewerking uitgevoerd (text)], [text] IMBRO: ja/nee -> signaalbewerking uitgevoerd
     * (bewerking) IMBROA/A: er is geen codelijst vastgesteld, (Tekst anders dan "nee"of "onbekend")
     */
    @NotNull( groups = Imbro.class )
    @ValidCode( allowedCodes = { "ja", "nee" }, isCaseSensitive = false, groups = Imbro.class )
    private String measurementText20;

    /*
     * MEASUREMENTTEXT21 [bewerking onderbrekingen uitgevoerd (text)], [text] IMBRO: ja/nee -> bewerking onderbrekingen
     * uitgevoerd (bewerking) IMBROA/A: er is geen codelijst vastgesteld, (Tekst anders dan "nee"of "onbekend")
     */
    @NotNull( groups = Imbro.class )
    @ValidCode( allowedCodes = { "ja", "nee" }, isCaseSensitive = false, groups = Imbro.class )
    private String measurementText21;

    /*
     * MEASUREMENTTEXT42 [methode verticale positiebepaling (text)], [text] methode verticale positiebepaling
     * (aangeleverde verticale positie)
     */
    @NotNull( groups = Imbro.class )
    @ValidCode( allowedCodes = { "MAH2", "MAH3", "MRG1", "MRG2", "MDGN", "MDGM", "METZ", "MTA2", "MWP1", "MWP2", "MWP3" }, isCaseSensitive = false, groups = Imbro.class )
    @ValidCode(
        allowedCodes = { "MAH2", "MAH3", "MRG1", "MRG2", "MDGN", "MDGM", "METZ", "MTA2", "MWP1", "MWP2", "MWP3", "MAH1", "MAHN", "MDGZ", "GEEN", "MDGP", "MTGR", "MH10", "MTKL", "MT25", "MT50", "MONB",
            "MMET", "METN", "MGOV" },
        isCaseSensitive = false,
        groups = ImbroA.class )
    private String measurementText42;

    /*
     * MEASUREMENTTEXT43 [methode locatiebepaling (text)], [text] methode locatiebepaling (aangeleverde locatie)
     */
    @NotNull( groups = Imbro.class )
    @ValidCode( allowedCodes = { "LDGP", "LDGM", "LRG1", "LRG2", "LRG3", "LRG4", "LMETZ", "LTA2" }, isCaseSensitive = false, groups = Imbro.class )
    @ValidCode(
        allowedCodes = { "LDGP", "LDGM", "LRG1", "LRG2", "LRG2", "LRG3", "LRG4", "LMETZ", "LTA2", "LDGZ", "LDGN", "LGBKN", "LGPS", "LTGR", "LD01", "LD02", "LD05", "LD10", "LD25", "LT10", "LTKL",
            "LT25", "LT50", "LMET", "LMETN", "LGOV", "LSOV", "LONB" },
        isCaseSensitive = false,
        groups = ImbroA.class )
    private String measurementText43;

    /*
     * MEASUREMENTTEXT101 [text], [bronhouder (text)] bronhouder (GeotechnicalCPTSurvey)
     */
    @NotNull
    private String measurementText101;

    /*
     * MEASUREMENTTEXT102 [kader aanlevering (text)], [text] kader aanlevering (GeotechnicalCPTSurvey)
     */
    @NotNull( groups = Imbro.class )
    @ValidCode( allowedCodes = { "rechtsgrond mijnbouwwet", "rechtsgrond waterwet", "opdracht publieke taakuitvoering" }, isCaseSensitive = false, groups = Imbro.class )
    private String measurementText102;

    /*
     * MEASUREMENTTEXT103 [kader inwinning (text)], [text] kader inwinning (GeotechnicalCPTSurvey)
     */
    @NotNull( groups = Imbro.class )
    @ValidCode(
        allowedCodes = { "waterkering", "bouwwerk en constructie", "infrastructuur land", "infrastructuur water", "milieuonderzoek", "controle onderzoek", "vergunning", "overig onderzoek" },
        isCaseSensitive = false,
        groups = Imbro.class )
    private String measurementText103;

    /*
     * MEASUREMENTTEXT104 [text], [uitvoerder locatiebepaling (text)] uitvoerder locatiebepaling (aangeleverde locatie)
     */
    @NotNull( groups = Imbro.class )
    private String measurementText104;

    /*
     * MEASUREMENTTEXT105 [JJJJ (number)], [MM (number)], [DD (number)] datum locatiebepaling (Aangeleverde locatie)
     */
    @NotNull( groups = Imbro.class )
    @DateValid( groups = Imbro.class )
    @IsNotBefore01011930
    private String measurementText105;

    /*
     * MEASUREMENTTEXT106 [text], [uitvoerder verticale positiebepaling (aangeleverde verticale positie)] uitvoerder
     * verticale positiebepaling (Aangeleverde verticale positie)
     */
    @NotNull( groups = Imbro.class )
    private String measurementText106;

    /*
     * MEASUREMENTTEXT107 [JJJJ (number)], [MM (number)], [DD (number)] datum verticale positiebepaling (Aangeleverde
     */
    @NotNull( groups = Imbro.class )
    @DateValid( groups = Imbro.class )
    @IsNotBefore01011930
    private String measurementText107;

    /*
     * MEASUREMENTTEXT108 [hoedanigheid oppervlakte (text)], [text] hoedanigheid oppervlakte (Aanvullend onderzoek)
     */
    @Size( min = 1, max = 200 )
    @ValidMes1
    private String measurementText108;

    /*
     * MEASUREMENTTEXT109 [dissipatie test uitgevoerd (text)], [text] dissipatietest uitgevoerd (sondeeronderzoek)
     */
    @NotNull( groups = Imbro.class )
    @ValidCode( allowedCodes = { "ja", "nee" }, isCaseSensitive = false, groups = Imbro.class )
    private String measurementText109;

    /*
     * MEASUREMENTTEXT110 [expertcorrectie uitgevoerd (text)], [text] expertcorrectie uitgevoerd (bewerking)
     */
    @NotNull( groups = Imbro.class )
    @ValidCode( allowedCodes = { "ja", "nee" }, isCaseSensitive = false, groups = Imbro.class )
    private String measurementText110;

    /*
     * MEASUREMENTTEXT111 [aanvullend onderzoek uitgevoerd (text)], [text] aanvullend onderzoek uitgevoerd
     * (GeotechnicalCPTSurvey)
     */
    @NotNull( groups = Imbro.class )
    @ValidCode( allowedCodes = { "ja", "nee" }, isCaseSensitive = false, groups = Imbro.class )
    private String measurementText111;

    /*
     * MEASUREMENTTEXT112 [JJJJ (number)], [MM (number)], [DD (number)] rapportagedatum (GeotechnicalCPTSurvey)
     * Conditie: datum niet in de toekomst!
     */
    @NotNull( groups = Imbro.class )
    @DateValid( groups = Imbro.class )
    @DateInPast( groups = Imbro.class )
    @IsNotBefore01011930
    private String measurementText112;

    /*
     * MEASUREMENTTEXT113 [JJJJ (number)], [MM (number)], [DD (number)] datum laatste bewerking (Sondeeronderzoek) Datum
     * niet na rapportagedatum (measurementText112)
     */
    @NotNull( groups = Imbro.class )
    @DateValid( groups = Imbro.class )
    @IsNotBefore01011930
    private String measurementText113;

    /*
     * MEASUREMENTTEXT114 [JJJJ (number)], [MM (number)], [DD (number)] datum onderzoek (aanvullend onderzoek) Datum
     * niet na rapportagedatum (measurementText112)
     */
    @DateValid( groups = Imbro.class )
    private String measurementText114;

    /* FILEDATE */
    @NotNull( groups = ImbroA.class )
    @IncompleteDateValid( groups = ImbroA.class )
    private String fileDate;

    /*
     * PROJECTID (object-ID bronhouder 1e gedeelte)
     */
    @NotNull
    private String projectId;

    /*
     * TESTID (object-Id bronhouder 2e gedeelte)
     */
    @NotNull
    @Pattern( regexp = "CPT\\d{12}", message = "{nl.bro.dto.gef.validation.BroIdPattern.message}", groups = { Correction.class } )
    private String testId;

    /* MEASUREMENTVAR1 (oppervlakte conuspunt) */
    @NotNull( groups = Imbro.class )
    @DecimalMin( value = "25" )
    @DecimalMax( value = "2000" )
    private BigDecimal measurementVar1;

    /*
     * MEASUREMENTVAR2 (oppervlakte kleefmantel) Conditioneel null -> verplicht als plaatselijke wrijving bepaald is.
     */
    @DecimalMin( value = "230" )
    @DecimalMax( value = "25000" )
    private BigDecimal measurementVar2;

    /* MEASUREMENTVAR3 (oppervlaktequotient conuspunt) */
    @DecimalMin( value = "0.05" )
    @DecimalMax( value = "1.00" )
    private BigDecimal measurementVar3;

    /*
     * MEASUREMENTVAR4 (oppervlaktequotient kleefmantel) Conditioneel null -> verplicht als plaatselijke wrijving
     * bepaald is.
     */
    @DecimalMin( value = "0.2" )
    @DecimalMax( value = "4.0" )
    private BigDecimal measurementVar4;

    /*
     * MEASUREMENTVAR5 (afstand conus tot midden kleefmantel) Conditioneel null -> verplicht als plaatselijke wrijving
     * bepaald is.
     */
    @DecimalMin( value = "1" )
    @DecimalMax( value = "1000" )
    private BigDecimal measurementVar5;

    /* MEASUREMENTVAR12 (sondeermethode) */
    @NotNull( groups = Imbro.class )
    @ValidCode( allowedCodes = { "1", "2", "3", "4" }, isCaseSensitive = false, groups = Imbro.class )
    @ValidCode( allowedCodes = { "0", "1", "2", "3", "4", "5" }, isCaseSensitive = false, groups = ImbroA.class )
    private String measurementVar12;

    /* MEASUREMENTVAR13 (voorgeboord tot) */
    @NotNull( groups = Imbro.class )
    @DecimalMin( value = "0.00" )
    @DecimalMax( value = "99.99" )
    private BigDecimal measurementVar13;

    /*
     * MEASUREMENTVAR14 (grondwaterstand) conditioneel als aanvullend onderzoek uitgevoerd en attributen hoedanigheid
     * oppervlakte en omstandigheden hebben geen waarde en er zijn geen verwijderde lagen opgegeven.
     */
    @DecimalMin( value = "-99.99" )
    @DecimalMax( value = "99.99" )
    private BigDecimal measurementVar14;

    /* MEASUREMENTVAR15 (waterdiepte) */
    @DecimalMin( value = "0.000" )
    @DecimalMax( value = "100.000" )
    private BigDecimal measurementVar15;

    /* MEASUREMENTVAR16 (einddiepte) */
    @NotNull( groups = Imbro.class )
    @DecimalMin( value = "0.000" )
    @DecimalMax( value = "200.000" )
    private BigDecimal measurementVar16;

    /* MEASUREMENTVAR17 (stopcriterium) */
    @NotNull( groups = Imbro.class )
    @ValidCode( allowedCodes = { "0", "1", "2", "3", "4", "5", "6", "7", "8" }, isCaseSensitive = false )
    private String measurementVar17;

    /*
     * MEASUREMENTVAR20 (conusweerstand vooraf) Conditioneel: mag ontbreken als er geen nulmetingen gedaan zijn!
     */
    @DecimalMin( value = "-999.999" )
    @DecimalMax( value = "999.999" )
    private BigDecimal measurementVar20;

    /*
     * MEASUREMENTVAR21 (conusweerstand achteraf) Conditioneel: mag ontbreken als er geen nulmetingen gedaan zijn!
     */
    @DecimalMin( value = "-999.999" )
    @DecimalMax( value = "999.999" )
    private BigDecimal measurementVar21;

    /*
     * MEASUREMENTVAR22 (plaatselijke wrijving vooraf) conditie: ingevuld als measurementvar23 is ingevuld
     */
    @DecimalMin( value = "-9.999" )
    @DecimalMax( value = "9.999" )
    private BigDecimal measurementVar22;

    /*
     * MEASUREMENTVAR23 (plaatselijke wrijving achteraf) conditie: ingevuld als measurementvar22 is ingevuld
     */
    @DecimalMin( value = "-9.999" )
    @DecimalMax( value = "9.999" )
    private BigDecimal measurementVar23;

    /*
     * MEASUREMENTVAR24 (waterspanning u1 vooraf) conditie: ingevuld als measurementvar25 is ingevuld
     */
    @DecimalMin( value = "-99.999" )
    @DecimalMax( value = "99.999" )
    private BigDecimal measurementVar24;

    /*
     * MEASUREMENTVAR25 (waterspanning u1 achteraf) conditie: ingevuld als measurementvar24 is ingevuld
     */
    @DecimalMin( value = "-99.999" )
    @DecimalMax( value = "99.999" )
    private BigDecimal measurementVar25;

    /*
     * MEASUREMENTVAR26 (waterspanning u2 vooraf) conditie: ingevuld als measurementvar27 is ingevuld
     */
    @DecimalMin( value = "-99.999" )
    @DecimalMax( value = "99.999" )
    private BigDecimal measurementVar26;

    /*
     * MEASUREMENTVAR27 (waterspanning u2 achteraf) conditie: ingevuld als measurementvar26 is ingevuld
     */
    @DecimalMin( value = "-99.999" )
    @DecimalMax( value = "99.999" )
    private BigDecimal measurementVar27;

    /*
     * MEASUREMENTVAR28 (waterspanning u3 vooraf) conditie: ingevuld als measurementvar29 is ingevuld
     */
    @DecimalMin( value = "-99.999" )
    @DecimalMax( value = "99.999" )
    private BigDecimal measurementVar28;

    /*
     * MEASUREMENTVAR29 (waterspanning u3 achteraf) conditie: ingevuld als measurementvar28 is ingevuld
     */
    @DecimalMin( value = "-99.999" )
    @DecimalMax( value = "99.999" )
    private BigDecimal measurementVar29;

    /*
     * MEASUREMENTVAR30 (hellingresultante vooraf) conditie: ingevuld als measurementvar31 is ingevuld
     */
    @DecimalMin( value = "-99" )
    @DecimalMax( value = "99" )
    private BigDecimal measurementVar30;

    /*
     * MEASUREMENTVAR31 (hellingresultante achteraf) conditie: ingevuld als measurementvar30 is ingevuld
     */
    @DecimalMin( value = "-99" )
    @DecimalMax( value = "99" )
    private BigDecimal measurementVar31;

    /*
     * MEASUREMENTVAR32 (helling noord-zuid vooraf) conditie: ingevuld als measurementvar33 is ingevuld
     */
    @DecimalMin( value = "-99" )
    @DecimalMax( value = "99" )
    private BigDecimal measurementVar32;

    /*
     * MEASUREMENTVA33 (helling noord-zuid achteraf) conditie: ingevuld als measurementvar32 is ingevuld
     */
    @DecimalMin( value = "-99" )
    @DecimalMax( value = "99" )
    private BigDecimal measurementVar33;

    /*
     * MEASUREMENTVAR34 (helling oost-west vooraf) conditie: ingevuld als measurementvar35 is ingevuld
     */
    @DecimalMin( value = "-99" )
    @DecimalMax( value = "99" )
    private BigDecimal measurementVar34;

    /*
     * MEASUREMENTVAR35 (helling oost-west achteraf) conditie: ingevuld als measurementvar34 is ingevuld
     */
    @DecimalMin( value = "-99" )
    @DecimalMax( value = "99" )
    private BigDecimal measurementVar35;

    /*
     * MEASUREMENTVAR36 (elektrische geleidbaarheid vooraf) conditie: ingevuld als measurementvar37 is ingevuld
     */
    @DecimalMin( value = "-99.999" )
    @DecimalMax( value = "99.999" )
    private BigDecimal measurementVar36;

    /*
     * MEASUREMENTVAR37 (elektrische geleidbaarheid achteraf) onditie: ingevuld als measurementvar36 is ingevuld
     */
    @DecimalMin( value = "-99.999" )
    @DecimalMax( value = "99.999" )
    private BigDecimal measurementVar37;

    /*
     * MEASUREMENTVAR42 (sensorazimuth)
     */
    @DecimalMin( value = "0" )
    @DecimalMax( value = "360" )
    private BigDecimal measurementVar42;

    /* MEASUREMENTVAR130 (conusdiameter) */
    @DecimalMin( value = "8" )
    @DecimalMax( value = "51" )
    private BigDecimal measurementVar130;

    /*
     * SPECIMENVAR (verwijderde lagen) Conditie: tenminste 1 verwijderde laag beschrijven als aanvullend onderzoek is
     * uitgevoerd en de attributen omstandigheden, hoedanigheid oppervlakte en grondwaterstand geen waarde hebben.
     */
    @Valid
    @SpecimenVarsValid
    private List<SpecimenVar> specimenVar;

    /* STARTDATE (start-datumtijd - datum component) */
    @NotNull( groups = Imbro.class )
    @DateValid( groups = Imbro.class )
    @IncompleteDateValid( groups = ImbroA.class )
    @IsNotBefore01011930
    @DateInPast( groups = ImbroA.class )
    private String startDate;

    /* STARTTIME (start datumtijd - tijd component) */
    @NotNull( groups = Imbro.class )
    @TimeValid( groups = Imbro.class )
    private String startTime;

    /* XYIDCRS (aangeleverde locatie - coordinaten stelsel) */
    @NotNull
    @ValidCode( allowedCodes = { "31000", "28992", "4258", "4326" }, isCaseSensitive = false, groups = Imbro.class )
    @ValidCode( allowedCodes = { "31000", "28992", "4258", "4326" }, isCaseSensitive = false, groups = ImbroA.class )
    private String xyidCrs;

    /*
     * XYIDX (aangeleverde locatie - x)
     */
    @NotNull
    @DecimalMin( value = "0" )
    private BigDecimal xyidX;

    /*
     * XYIDY (aangeleverde locatie - y)
     */
    @NotNull
    @DecimalMin( value = "0" )
    private BigDecimal xyidY;

    /* ZIDVerticalDatum (verticaal referentie vlak) */
    @NotNull( groups = Imbro.class )
    @ValidCode( allowedCodes = { "31000", "5709", "5861", "5714", "onbekend" }, isCaseSensitive = false )
    private String zidVerticalDatum;

    /* ZIDOffset (verschuiving) */
    @NotNull( groups = Imbro.class )
    @DecimalMin( value = "-999.999" )
    @DecimalMax( value = "999.999" )
    private BigDecimal zidOffset;

    /* DATABLOCK = conuspenetratietestresultaten */
    @Valid
    private List<DataRowCpt> dataBlock;

    /*
     * helper to thest the combination of PROJECTID/TESTID
     */
    @Size( min = 1, max = 200, message = "{nl.bro.dto.gef.validation.ProjectTestId.size.message}" )
    @ValidMes1( message = "{nl.bro.dto.gef.validation.ProjectTestId.mes1.message}" )
    String getValidProjectTestId() {
        return ( projectId == null ? "" : projectId ) + "/" + ( testId == null ? "" : testId );
    }

    @Override
    public BigDecimal getMeasurementVar1() {
        return measurementVar1;
    }

    /*
     * (non-Javadoc)
     * @see nl.bro.cpt.gef.dto.IfcCptFile#setMeasurementVar1(java.math.BigDecimal)
     */
    @Override
    public void setMeasurementVar1(BigDecimal measurementVar1) {
        this.measurementVar1 = measurementVar1;
    }

    @Override
    public BigDecimal getMeasurementVar2() {
        return measurementVar2;
    }

    /*
     * (non-Javadoc)
     * @see nl.bro.cpt.gef.dto.IfcCptFile#setMeasurementVar2(java.math.BigDecimal)
     */
    @Override
    public void setMeasurementVar2(BigDecimal measurementVar2) {
        this.measurementVar2 = measurementVar2;
    }

    @Override
    public BigDecimal getMeasurementVar3() {
        return measurementVar3;
    }

    /*
     * (non-Javadoc)
     * @see nl.bro.cpt.gef.dto.IfcCptFile#setMeasurementVar3(java.math.BigDecimal)
     */
    @Override
    public void setMeasurementVar3(BigDecimal measurementVar3) {
        this.measurementVar3 = measurementVar3;
    }

    @Override
    public BigDecimal getMeasurementVar4() {
        return measurementVar4;
    }

    /*
     * (non-Javadoc)
     * @see nl.bro.cpt.gef.dto.IfcCptFile#setMeasurementVar4(java.math.BigDecimal)
     */
    @Override
    public void setMeasurementVar4(BigDecimal measurementVar4) {
        this.measurementVar4 = measurementVar4;
    }

    @Override
    public BigDecimal getMeasurementVar5() {
        return measurementVar5;
    }

    /*
     * (non-Javadoc)
     * @see nl.bro.cpt.gef.dto.IfcCptFile#setMeasurementVar5(java.math.BigDecimal)
     */
    @Override
    public void setMeasurementVar5(BigDecimal measurementVar5) {
        this.measurementVar5 = measurementVar5;
    }

    @Override
    public String getMeasurementVar12() {
        return measurementVar12;
    }

    /*
     * (non-Javadoc)
     * @see nl.bro.cpt.gef.dto.IfcCptFile#setMeasurementVar12(java.lang.String)
     */
    @Override
    public void setMeasurementVar12(String measurementVar12) {
        this.measurementVar12 = measurementVar12;
    }

    @Override
    public BigDecimal getMeasurementVar13() {
        return measurementVar13;
    }

    /*
     * (non-Javadoc)
     * @see nl.bro.cpt.gef.dto.IfcCptFile#setMeasurementVar13(java.math.BigDecimal)
     */
    @Override
    public void setMeasurementVar13(BigDecimal measurementVar13) {
        this.measurementVar13 = measurementVar13;
    }

    @Override
    public BigDecimal getMeasurementVar14() {
        return measurementVar14;
    }

    /*
     * (non-Javadoc)
     * @see nl.bro.cpt.gef.dto.IfcCptFile#setMeasurementVar14(java.math.BigDecimal)
     */
    @Override
    public void setMeasurementVar14(BigDecimal measurementVar14) {
        this.measurementVar14 = measurementVar14;
    }

    public BigDecimal getMeasurementVar15() {
        return measurementVar15;
    }

    public void setMeasurementVar15( BigDecimal measurementVar15 ) {
        this.measurementVar15 = measurementVar15;
    }

    @Override
    public BigDecimal getMeasurementVar16() {
        return measurementVar16;
    }

    /*
     * (non-Javadoc)
     * @see nl.bro.cpt.gef.dto.IfcCptFile#setMeasurementVar16(java.math.BigDecimal)
     */
    @Override
    public void setMeasurementVar16(BigDecimal measurementVar16) {
        this.measurementVar16 = measurementVar16;
    }

    @Override
    public String getMeasurementVar17() {
        return measurementVar17;
    }

    /*
     * (non-Javadoc)
     * @see nl.bro.cpt.gef.dto.IfcCptFile#setMeasurementVar17(java.lang.String)
     */
    @Override
    public void setMeasurementVar17(String measurementVar17) {
        this.measurementVar17 = measurementVar17;
    }

    @Override
    public BigDecimal getMeasurementVar20() {
        return measurementVar20;
    }

    /*
     * (non-Javadoc)
     * @see nl.bro.cpt.gef.dto.IfcCptFile#setMeasurementVar20(java.math.BigDecimal)
     */
    @Override
    public void setMeasurementVar20(BigDecimal measurementVar20) {
        this.measurementVar20 = measurementVar20;
    }

    @Override
    public BigDecimal getMeasurementVar21() {
        return measurementVar21;
    }

    /*
     * (non-Javadoc)
     * @see nl.bro.cpt.gef.dto.IfcCptFile#setMeasurementVar21(java.math.BigDecimal)
     */
    @Override
    public void setMeasurementVar21(BigDecimal measurementVar21) {
        this.measurementVar21 = measurementVar21;
    }

    @Override
    public BigDecimal getMeasurementVar22() {
        return measurementVar22;
    }

    /*
     * (non-Javadoc)
     * @see nl.bro.cpt.gef.dto.IfcCptFile#setMeasurementVar22(java.math.BigDecimal)
     */
    @Override
    public void setMeasurementVar22(BigDecimal measurementVar22) {
        this.measurementVar22 = measurementVar22;
    }

    @Override
    public BigDecimal getMeasurementVar23() {
        return measurementVar23;
    }

    /*
     * (non-Javadoc)
     * @see nl.bro.cpt.gef.dto.IfcCptFile#setMeasurementVar23(java.math.BigDecimal)
     */
    @Override
    public void setMeasurementVar23(BigDecimal measurementVar23) {
        this.measurementVar23 = measurementVar23;
    }

    @Override
    public BigDecimal getMeasurementVar24() {
        return measurementVar24;
    }

    /*
     * (non-Javadoc)
     * @see nl.bro.cpt.gef.dto.IfcCptFile#setMeasurementVar24(java.math.BigDecimal)
     */
    @Override
    public void setMeasurementVar24(BigDecimal measurementVar24) {
        this.measurementVar24 = measurementVar24;
    }

    @Override
    public BigDecimal getMeasurementVar25() {
        return measurementVar25;
    }

    /*
     * (non-Javadoc)
     * @see nl.bro.cpt.gef.dto.IfcCptFile#setMeasurementVar25(java.math.BigDecimal)
     */
    @Override
    public void setMeasurementVar25(BigDecimal measurementVar25) {
        this.measurementVar25 = measurementVar25;
    }

    @Override
    public BigDecimal getMeasurementVar26() {
        return measurementVar26;
    }

    /*
     * (non-Javadoc)
     * @see nl.bro.cpt.gef.dto.IfcCptFile#setMeasurementVar26(java.math.BigDecimal)
     */
    @Override
    public void setMeasurementVar26(BigDecimal measurementVar26) {
        this.measurementVar26 = measurementVar26;
    }

    @Override
    public BigDecimal getMeasurementVar27() {
        return measurementVar27;
    }

    /*
     * (non-Javadoc)
     * @see nl.bro.cpt.gef.dto.IfcCptFile#setMeasurementVar27(java.math.BigDecimal)
     */
    @Override
    public void setMeasurementVar27(BigDecimal measurementVar27) {
        this.measurementVar27 = measurementVar27;
    }

    @Override
    public BigDecimal getMeasurementVar28() {
        return measurementVar28;
    }

    /*
     * (non-Javadoc)
     * @see nl.bro.cpt.gef.dto.IfcCptFile#setMeasurementVar28(java.math.BigDecimal)
     */
    @Override
    public void setMeasurementVar28(BigDecimal measurementVar28) {
        this.measurementVar28 = measurementVar28;
    }

    @Override
    public BigDecimal getMeasurementVar29() {
        return measurementVar29;
    }

    /*
     * (non-Javadoc)
     * @see nl.bro.cpt.gef.dto.IfcCptFile#setMeasurementVar29(java.math.BigDecimal)
     */
    @Override
    public void setMeasurementVar29(BigDecimal measurementVar29) {
        this.measurementVar29 = measurementVar29;
    }

    @Override
    public BigDecimal getMeasurementVar30() {
        return measurementVar30;
    }

    /*
     * (non-Javadoc)
     * @see nl.bro.cpt.gef.dto.IfcCptFile#setMeasurementVar30(java.math.BigDecimal)
     */
    @Override
    public void setMeasurementVar30(BigDecimal measurementVar30) {
        this.measurementVar30 = measurementVar30;
    }

    @Override
    public BigDecimal getMeasurementVar31() {
        return measurementVar31;
    }

    /*
     * (non-Javadoc)
     * @see nl.bro.cpt.gef.dto.IfcCptFile#setMeasurementVar31(java.math.BigDecimal)
     */
    @Override
    public void setMeasurementVar31(BigDecimal measurementVar31) {
        this.measurementVar31 = measurementVar31;
    }

    @Override
    public BigDecimal getMeasurementVar32() {
        return measurementVar32;
    }

    /*
     * (non-Javadoc)
     * @see nl.bro.cpt.gef.dto.IfcCptFile#setMeasurementVar32(java.math.BigDecimal)
     */
    @Override
    public void setMeasurementVar32(BigDecimal measurementVar32) {
        this.measurementVar32 = measurementVar32;
    }

    @Override
    public BigDecimal getMeasurementVar33() {
        return measurementVar33;
    }

    /*
     * (non-Javadoc)
     * @see nl.bro.cpt.gef.dto.IfcCptFile#setMeasurementVar33(java.math.BigDecimal)
     */
    @Override
    public void setMeasurementVar33(BigDecimal measurementVar33) {
        this.measurementVar33 = measurementVar33;
    }

    @Override
    public BigDecimal getMeasurementVar34() {
        return measurementVar34;
    }

    /*
     * (non-Javadoc)
     * @see nl.bro.cpt.gef.dto.IfcCptFile#setMeasurementVar34(java.math.BigDecimal)
     */
    @Override
    public void setMeasurementVar34(BigDecimal measurementVar34) {
        this.measurementVar34 = measurementVar34;
    }

    @Override
    public BigDecimal getMeasurementVar35() {
        return measurementVar35;
    }

    /*
     * (non-Javadoc)
     * @see nl.bro.cpt.gef.dto.IfcCptFile#setMeasurementVar35(java.math.BigDecimal)
     */
    @Override
    public void setMeasurementVar35(BigDecimal measurementVar35) {
        this.measurementVar35 = measurementVar35;
    }

    @Override
    public BigDecimal getMeasurementVar36() {
        return measurementVar36;
    }

    /*
     * (non-Javadoc)
     * @see nl.bro.cpt.gef.dto.IfcCptFile#setMeasurementVar36(java.math.BigDecimal)
     */
    @Override
    public void setMeasurementVar36(BigDecimal measurementVar36) {
        this.measurementVar36 = measurementVar36;
    }

    @Override
    public BigDecimal getMeasurementVar37() {
        return measurementVar37;
    }

    /*
     * (non-Javadoc)
     * @see nl.bro.cpt.gef.dto.IfcCptFile#setMeasurementVar37(java.math.BigDecimal)
     */
    @Override
    public void setMeasurementVar37(BigDecimal measurementVar37) {
        this.measurementVar37 = measurementVar37;
    }

    @Override
    public BigDecimal getMeasurementVar42() {
        return measurementVar42;
    }

    /*
     * (non-Javadoc)
     * @see nl.bro.cpt.gef.dto.IfcCptFile#setMeasurementVar42(java.math.BigDecimal)
     */
    @Override
    public void setMeasurementVar42(BigDecimal measurementVar42) {
        this.measurementVar42 = measurementVar42;
    }

    @Override
    public String getMeasurementText4() {
        return measurementText4;
    }

    /*
     * (non-Javadoc)
     * @see nl.bro.cpt.gef.dto.IfcCptFile#setMeasurementText4(java.lang.String)
     */
    @Override
    public void setMeasurementText4(String measurementText4) {
        this.measurementText4 = measurementText4;
    }

    @Override
    public String getMeasurementText5() {
        return measurementText5;
    }

    /*
     * (non-Javadoc)
     * @see nl.bro.cpt.gef.dto.IfcCptFile#setMeasurementText5(java.lang.String)
     */
    @Override
    public void setMeasurementText5(String measurementText5) {
        this.measurementText5 = measurementText5;
    }

    @Override
    public String getMeasurementText9() {
        return measurementText9;
    }

    /*
     * (non-Javadoc)
     * @see nl.bro.cpt.gef.dto.IfcCptFile#setMeasurementText9(java.lang.String)
     */
    @Override
    public void setMeasurementText9(String measurementText9) {
        this.measurementText9 = measurementText9;
    }

    @Override
    public String getMeasurementText11() {
        return measurementText11;
    }

    /*
     * (non-Javadoc)
     * @see nl.bro.cpt.gef.dto.IfcCptFile#setMeasurementText11(java.lang.String)
     */
    @Override
    public void setMeasurementText11(String measurementText11) {
        this.measurementText11 = measurementText11;
    }

    @Override
    public String getMeasurementText20() {
        return measurementText20;
    }

    /*
     * (non-Javadoc)
     * @see nl.bro.cpt.gef.dto.IfcCptFile#setMeasurementText20(java.lang.String)
     */
    @Override
    public void setMeasurementText20(String measurementText20) {
        this.measurementText20 = measurementText20;
    }

    @Override
    public String getMeasurementText21() {
        return measurementText21;
    }

    /*
     * (non-Javadoc)
     * @see nl.bro.cpt.gef.dto.IfcCptFile#setMeasurementText21(java.lang.String)
     */
    @Override
    public void setMeasurementText21(String measurementText21) {
        this.measurementText21 = measurementText21;
    }

    @Override
    public String getMeasurementText42() {
        return measurementText42;
    }

    /*
     * (non-Javadoc)
     * @see nl.bro.cpt.gef.dto.IfcCptFile#setMeasurementText42(java.lang.String)
     */
    @Override
    public void setMeasurementText42(String measurementText42) {
        this.measurementText42 = measurementText42;
    }

    @Override
    public String getMeasurementText43() {
        return measurementText43;
    }

    /*
     * (non-Javadoc)
     * @see nl.bro.cpt.gef.dto.IfcCptFile#setMeasurementText43(java.lang.String)
     */
    @Override
    public void setMeasurementText43(String measurementText43) {
        this.measurementText43 = measurementText43;
    }

    @Override
    public String getMeasurementText101() {
        return measurementText101;
    }

    /*
     * (non-Javadoc)
     * @see nl.bro.cpt.gef.dto.IfcCptFile#setMeasurementText101(java.lang.String)
     */
    @Override
    public void setMeasurementText101(String measurementText101) {
        this.measurementText101 = measurementText101;
    }

    @Override
    public String getMeasurementText102() {
        return measurementText102;
    }

    /*
     * (non-Javadoc)
     * @see nl.bro.cpt.gef.dto.IfcCptFile#setMeasurementText102(java.lang.String)
     */
    @Override
    public void setMeasurementText102(String measurementText102) {
        this.measurementText102 = measurementText102;
    }

    @Override
    public String getMeasurementText103() {
        return measurementText103;
    }

    /*
     * (non-Javadoc)
     * @see nl.bro.cpt.gef.dto.IfcCptFile#setMeasurementText103(java.lang.String)
     */
    @Override
    public void setMeasurementText103(String measurementText103) {
        this.measurementText103 = measurementText103;
    }

    @Override
    public String getMeasurementText104() {
        return measurementText104;
    }

    /*
     * (non-Javadoc)
     * @see nl.bro.cpt.gef.dto.IfcCptFile#setMeasurementText104(java.lang.String)
     */
    @Override
    public void setMeasurementText104(String measurementText104) {
        this.measurementText104 = measurementText104;
    }

    @Override
    public String getMeasurementText105() {
        return measurementText105;
    }

    /*
     * (non-Javadoc)
     * @see nl.bro.cpt.gef.dto.IfcCptFile#setMeasurementText105(java.lang.String)
     */
    @Override
    public void setMeasurementText105(String measurementText105) {
        this.measurementText105 = measurementText105;
    }

    @Override
    public String getMeasurementText106() {
        return measurementText106;
    }

    /*
     * (non-Javadoc)
     * @see nl.bro.cpt.gef.dto.IfcCptFile#setMeasurementText106(java.lang.String)
     */
    @Override
    public void setMeasurementText106(String measurementText106) {
        this.measurementText106 = measurementText106;
    }

    @Override
    public String getMeasurementText107() {
        return measurementText107;
    }

    /*
     * (non-Javadoc)
     * @see nl.bro.cpt.gef.dto.IfcCptFile#setMeasurementText107(java.lang.String)
     */
    @Override
    public void setMeasurementText107(String measurementText107) {
        this.measurementText107 = measurementText107;
    }

    @Override
    public String getMeasurementText108() {
        return measurementText108;
    }

    /*
     * (non-Javadoc)
     * @see nl.bro.cpt.gef.dto.IfcCptFile#setMeasurementText108(java.lang.String)
     */
    @Override
    public void setMeasurementText108(String measurementText108) {
        this.measurementText108 = measurementText108;
    }

    @Override
    public String getMeasurementText109() {
        return measurementText109;
    }

    /*
     * (non-Javadoc)
     * @see nl.bro.cpt.gef.dto.IfcCptFile#setMeasurementText109(java.lang.String)
     */
    @Override
    public void setMeasurementText109(String measurementText109) {
        this.measurementText109 = measurementText109;
    }

    @Override
    public String getMeasurementText110() {
        return measurementText110;
    }

    /*
     * (non-Javadoc)
     * @see nl.bro.cpt.gef.dto.IfcCptFile#setMeasurementText110(java.lang.String)
     */
    @Override
    public void setMeasurementText110(String measurementText110) {
        this.measurementText110 = measurementText110;
    }

    @Override
    public String getMeasurementText111() {
        return measurementText111;
    }

    /*
     * (non-Javadoc)
     * @see nl.bro.cpt.gef.dto.IfcCptFile#setMeasurementText111(java.lang.String)
     */
    @Override
    public void setMeasurementText111(String measurementText111) {
        this.measurementText111 = measurementText111;
    }

    @Override
    public String getMeasurementText112() {
        return measurementText112;
    }

    /*
     * (non-Javadoc)
     * @see nl.bro.cpt.gef.dto.IfcCptFile#setMeasurementText112(java.lang.String)
     */
    @Override
    public void setMeasurementText112(String measurementText112) {
        this.measurementText112 = measurementText112;
    }

    @Override
    public String getMeasurementText113() {
        return measurementText113;
    }

    /*
     * (non-Javadoc)
     * @see nl.bro.cpt.gef.dto.IfcCptFile#setMeasurementText113(java.lang.String)
     */
    @Override
    public void setMeasurementText113(String measurementText113) {
        this.measurementText113 = measurementText113;
    }

    @Override
    public String getMeasurementText114() {
        return measurementText114;
    }

    /*
     * (non-Javadoc)
     * @see nl.bro.cpt.gef.dto.IfcCptFile#setMeasurementText114(java.lang.String)
     */
    @Override
    public void setMeasurementText114(String measurementText114) {
        this.measurementText114 = measurementText114;
    }

    @Override
    public BigDecimal getMeasurementVar130() {
        return measurementVar130;
    }

    /*
     * (non-Javadoc)
     * @see nl.bro.cpt.gef.dto.IfcCptFile#setMeasurementVar130(java.math.BigDecimal)
     */
    @Override
    public void setMeasurementVar130(BigDecimal measurementVar130) {
        this.measurementVar130 = measurementVar130;
    }

    @Override
    public String getCompanyId() {
        return companyId;
    }

    /*
     * (non-Javadoc)
     * @see nl.bro.cpt.gef.dto.IfcCptFile#setCompanyId(java.lang.String)
     */
    @Override
    public void setCompanyId(String companyId) {
        this.companyId = companyId;
    }

    @Override
    public List<SpecimenVar> getSpecimenVar() {
        return specimenVar;
    }

    /*
     * (non-Javadoc)
     * @see nl.bro.cpt.gef.dto.IfcCptFile#setSpecimenVar(java.util.List)
     */
    @Override
    public void setSpecimenVar(List<SpecimenVar> specimenVar) {
        this.specimenVar = specimenVar;
    }

    @Override
    public String getStartDate() {
        return startDate;
    }

    /*
     * (non-Javadoc)
     * @see nl.bro.cpt.gef.dto.IfcCptFile#setStartDate(java.lang.String)
     */
    @Override
    public void setStartDate(String startDate) {
        this.startDate = startDate;
    }

    @Override
    public String getStartTime() {
        return startTime;
    }

    /*
     * (non-Javadoc)
     * @see nl.bro.cpt.gef.dto.IfcCptFile#setStartTime(java.lang.String)
     */
    @Override
    public void setStartTime(String startTime) {
        this.startTime = startTime;
    }

    @Override
    public String getProjectId() {
        return projectId;
    }

    /*
     * (non-Javadoc)
     * @see nl.bro.cpt.gef.dto.IfcCptFile#setProjectId(java.lang.String)
     */
    @Override
    public void setProjectId(String projectId) {
        this.projectId = projectId;
    }

    @Override
    public List<DataRowCpt> getDataBlock() {
        if ( dataBlock == null ) {
            dataBlock = new ArrayList<DataRowCpt>();
        }
        return dataBlock;
    }

    /*
     * (non-Javadoc)
     * @see nl.bro.cpt.gef.dto.IfcCptFile#setDataBlock(java.util.List)
     */
    @Override
    public void setDataBlock(List<DataRowCpt> dataBlock) {
        this.dataBlock = (List<DataRowCpt>) dataBlock;
    }

    @Override
    public String getTestId() {
        return testId;
    }

    /*
     * (non-Javadoc)
     * @see nl.bro.cpt.gef.dto.IfcCptFile#setTestId(java.lang.String)
     */
    @Override
    public void setTestId(String testId) {
        this.testId = testId;
    }

    @Override
    public String getMeasurementText6sondeernorm() {
        return measurementText6sondeernorm;
    }

    /*
     * (non-Javadoc)
     * @see nl.bro.cpt.gef.dto.IfcCptFile#setMeasurementText6sondeernorm(java.lang.String)
     */
    @Override
    public void setMeasurementText6sondeernorm(String measurementText6sondeernorm) {
        this.measurementText6sondeernorm = measurementText6sondeernorm;
    }

    @Override
    public String getMeasurementText6kwaliteitsklasse() {
        return measurementText6kwaliteitsklasse;
    }

    /*
     * (non-Javadoc)
     * @see nl.bro.cpt.gef.dto.IfcCptFile#setMeasurementText6kwaliteitsklasse(java.lang.String)
     */
    @Override
    public void setMeasurementText6kwaliteitsklasse(String measurementText6kwaliteitsklasse) {
        this.measurementText6kwaliteitsklasse = measurementText6kwaliteitsklasse;
    }

    @Override
    public String getXyidCrs() {
        return xyidCrs;
    }

    /*
     * (non-Javadoc)
     * @see nl.bro.cpt.gef.dto.IfcCptFile#setXyidCrs(java.lang.String)
     */
    @Override
    public void setXyidCrs(String xyidCrs) {
        this.xyidCrs = xyidCrs;
    }

    @Override
    public BigDecimal getXyidX() {
        return xyidX;
    }

    /*
     * (non-Javadoc)
     * @see nl.bro.cpt.gef.dto.IfcCptFile#setXyidX(java.math.BigDecimal)
     */
    @Override
    public void setXyidX(BigDecimal xyidX) {
        this.xyidX = xyidX;
    }

    @Override
    public BigDecimal getXyidY() {
        return xyidY;
    }

    /*
     * (non-Javadoc)
     * @see nl.bro.cpt.gef.dto.IfcCptFile#setXyidY(java.math.BigDecimal)
     */
    @Override
    public void setXyidY(BigDecimal xyidY) {
        this.xyidY = xyidY;
    }

    @Override
    public String getZidVerticalDatum() {
        return zidVerticalDatum;
    }

    /*
     * (non-Javadoc)
     * @see nl.bro.cpt.gef.dto.IfcCptFile#setZidVerticalDatum(java.lang.String)
     */
    @Override
    public void setZidVerticalDatum(String zidVerticalDatum) {
        this.zidVerticalDatum = zidVerticalDatum;
    }

    @Override
    public BigDecimal getZidOffset() {
        return zidOffset;
    }

    /*
     * (non-Javadoc)
     * @see nl.bro.cpt.gef.dto.IfcCptFile#setZidOffset(java.math.BigDecimal)
     */
    @Override
    public void setZidOffset(BigDecimal zidOffset) {
        this.zidOffset = zidOffset;
    }

    /**
     * @return the fileDate
     */
    @Override
    public String getFileDate() {
        return fileDate;
    }

    /*
     * (non-Javadoc)
     * @see nl.bro.cpt.gef.dto.IfcCptFile#setFileDate(java.lang.String)
     */
    @Override
    public void setFileDate(String fileDate) {
        this.fileDate = fileDate;
    }

    /**
     * @return the parentFileName
     */
    @Override
    public String getParentFileName() {
        return parentFileName;
    }

    /*
     * (non-Javadoc)
     * @see nl.bro.cpt.gef.dto.IfcCptFile#setParentFileName(java.lang.String)
     */
    @Override
    public void setParentFileName(String parentFileName) {
        this.parentFileName = parentFileName;
    }

    /**
     * @return the expectedChildFileNameList
     */
    @Override
    public List<String> getExpectedChildFileNameList() {
        if ( expectedChildFileNameList == null ) {
            expectedChildFileNameList = new ArrayList<String>();
        }
        return expectedChildFileNameList;
    }

    /*
     * (non-Javadoc)
     * @see nl.bro.cpt.gef.dto.IfcCptFile#setExpectedChildFileNameList(java.util.List)
     */
    @Override
    public void setExpectedChildFileNameList(List<String> expectedChildFileNameList) {
        this.expectedChildFileNameList = expectedChildFileNameList;
    }

    @Override
    public String getGefId() {
        return gefId;
    }

    @Override
    public void setGefId(String gefId) {
        this.gefId = gefId;
    }

    // convenience for validation
    public String getColumnInfo3() {
        return getMeasuredParameters().contains( "3" ) ? GEF_IND_YES : GEF_IND_NO;
    }

    // convenience for validation
    public String getMeasurementVarPresent42() {
        return getSetMeasurementVarsSet().contains( "42" ) ? GEF_IND_YES : GEF_IND_NO;
    }

    public String getReportCode() {
        return reportCode;
    }

    public void setReportCode(String reportCode) {
        this.reportCode = reportCode;
    }
}
