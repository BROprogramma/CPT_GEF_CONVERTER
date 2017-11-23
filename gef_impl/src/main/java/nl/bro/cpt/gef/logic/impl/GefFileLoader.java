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

import static nl.bro.cpt.gef.dto.GefFile.Types.CPT;
import static nl.bro.cpt.gef.dto.GefFile.Types.DISS;

import java.io.Serializable;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.log4j.Logger;

import com.google.common.collect.ImmutableSet;

import nl.bro.cpt.gef.dto.DataRow;
import nl.bro.cpt.gef.dto.DataRowCpt;
import nl.bro.cpt.gef.dto.DataRowDiss;
import nl.bro.cpt.gef.dto.GefCptFile;
import nl.bro.cpt.gef.dto.GefDisFile;
import nl.bro.cpt.gef.dto.GefFile;
import nl.bro.cpt.gef.dto.GefFileFacade;
import nl.bro.cpt.gef.dto.SpecimenVar;
import nl.bro.cpt.gef.transform.MappingConstants;
import nl.bro.gef.antlr.GefParser.DatavaluesContext;
import nl.bro.gef.antlr.GefParser.FileContext;
import nl.bro.gef.antlr.GefParser.HeaderrowContext;
import nl.bro.gef.antlr.GefParser.VariabeleContext;
import nl.bro.gef.antlr.GefParser.WaardeContext;
import nl.bro.gef.antlr.GefParserBaseListener;

public class GefFileLoader extends GefParserBaseListener {

    private static final String QC_NOT_APPLICABLE = "nvt";
    private static final Logger LOG = Logger.getLogger( GefFileLoader.class );
    private static final String PARENT = "#PARENT";

    private enum HeaderVarEnum {

        GEFID,
        PROCEDURECODE,
        PROJECTNAME,
        FILEOWNER,
        FILEDATE,
        PROJECTID,
        TESTID,
        OBJECTID,
        CHILD,
        PARENT,
        COLUMN,
        COLUMNINFO,
        COLUMNMINMAX,
        COLUMNSEPARATOR,
        COLUMNTEXT,
        COLUMNVOID,
        COMPANYID,
        DATAFORMAT,
        FIRSTSCAN,
        LASTSCAN,
        MEASUREMENTTEXT,
        MEASUREMENTVAR,
        MEASUREMENTCODE,
        REPORTCODE,
        REPORTVAR,
        RECORDSEPARATOR,
        REPORTDATAFORMAT,
        SPECIMENVAR,
        STARTDATE,
        STARTTIME,
        STRUCTURETEXT,
        STRUCTURETYPE,
        XYID,
        ZID,
        OS,
        DATATYPE,
        ANALYSISTEXT,
        EQUIPMENT,
        LANGUAGE,
        COMMENT;

        static HeaderVarEnum valueOfStr(String value) {
            if ( value == null ) {
                return null;
            }
            String upperVal = value.toUpperCase();
            for ( HeaderVarEnum curr : values() ) {
                if ( curr.name().equals( upperVal ) ) {
                    return curr;
                }
            }
            return null;
        }
    }

    private static class SpecimenVarComparator implements Comparator<SpecimenVar>, Serializable {

        private static final long serialVersionUID = 3005502104906857791L;

        @Override
        public int compare(SpecimenVar lhs, SpecimenVar rhs) {
            if ( lhs == null || rhs == null ) {
                return -1;
            }
            return lhs.getBovendiepte().compareTo( rhs.getBovendiepte() );
        }

    }

    private static class MinMaxPair {

        private BigDecimal min;
        private BigDecimal max;

        MinMaxPair(BigDecimal min, BigDecimal max) {
            this.min = min;
            this.max = max;
        }

        @SuppressWarnings( "unused" )
        BigDecimal getMin() {
            return min;
        }

        @SuppressWarnings( "unused" )
        BigDecimal getMax() {
            return max;
        }
    }

    private interface HeaderRowChainOfResponsibility {

        /**
         * Chain of responsibility handle methode.
         *
         * @param headerVarType
         * @param splittedValue
         */
        void handle(HeaderVarEnum headerVarType, String[] splittedValue);

        HeaderRowChainOfResponsibility getNext();

        HeaderRowChainOfResponsibility setNext(HeaderRowChainOfResponsibility next);
    }

    private abstract class BaseHRChainOfResponsibility implements HeaderRowChainOfResponsibility {

        private HeaderRowChainOfResponsibility next;

        /**
         * Handle de waarde en return true of return false.
         *
         * @param headerVarType
         * @param splittedValue
         * @return afgehandeld (true) of niet (false).
         */
        protected abstract boolean handleInternal(HeaderVarEnum headerVarType, String[] splittedValue);

        @Override
        public void handle(HeaderVarEnum headerVarType, String[] splittedValue) {

            if ( !handleInternal( headerVarType, splittedValue ) ) {
                getNext().handle( headerVarType, splittedValue );
            }
        }

        /**
         * Haal de volgende chain of responsibility entry op.
         */
        @Override
        public HeaderRowChainOfResponsibility getNext() {
            return next;
        }

        /**
         * zet de volgende waarde in de chain en retourneert de gezette waarde.
         *
         * @param next
         * @return de waarde die is gezet
         */
        @Override
        public HeaderRowChainOfResponsibility setNext(HeaderRowChainOfResponsibility next) {
            this.next = next;
            return this.next;
        }
    }

    private static final String CRLF = String.format( "%n" );

    // Sondeer normen:
    private static final String NEN_3680 = "3680";
    private static final String NEN_5140 = "5140";
    private static final String NEN_EN_ISO_22476_1 = "22476-1";
    private static final String NEN_EN_ISO_22476_12 = "22476-12";

    private static final String SONDEERNORM_POSTFIX = "sondeernorm";
    private static final String QUALITYCLASS_POSTFIX = "kwaliteitsklasse";

    private static final String LOWER_DEPTH_MEA_KEY = "13";

    private static final String BASE_SET_MEASUREMENTTEXT = "setMeasurementText";
    private static final String BASE_SET_MEASUREMENTVAR = "setMeasurementVar";
    private static final String BASE_SET_DATABLOCKCI = "setDatablockci";

    private GefFile.Types fileType = GefFile.Types.UNKNOWN;
    private String fileDate;

    @SuppressWarnings( "rawtypes" )
    private GefFileFacade gefFile;

    private String columnSeparator = " ";
    private String recordSeparator = CRLF;

    private int nrColumns;

    private List<String> childFiles;
    private String parentFile;
    private BigDecimal parentDepth;

    private String gefId;
    private String reportCode;

    private String companyId;

    private String projectId;
    private String testId;

    private String[] xyid;
    private String[] zid;

    private String startDate;
    private String startTime;

    private Set<String> measuredVars = null;
    private Set<String> measurementVarsSet = null;
    private Map<String, BigDecimal> bigDecimalMeasurementVars = null;
    private Map<String, String> stringMeasurementVars = null;
    private Map<String, String> measurementTexts = null;
    private Map<String, String> columnInfos = null;
    private Map<String, BigDecimal> columnVoidValues = null;
    private Map<String, MinMaxPair> columnMinMaxValues = null;
    private List<SpecimenVar> specimenVars = null;
    private StringBuilder dataRows = null;

    private List<String> parseErrors;

    private String headerVar;
    private String headerValue;

    private HeaderRowChainOfResponsibility headerRowChainOfResponsibility;

    public GefFileLoader() {

        initialize();
    }

    public void initialize() {

        measuredVars = new HashSet<>();
        measurementVarsSet = new HashSet<>();
        bigDecimalMeasurementVars = new HashMap<>();
        stringMeasurementVars = new HashMap<>();
        measurementTexts = new HashMap<>();
        columnInfos = new HashMap<>();
        columnVoidValues = new HashMap<>();
        columnMinMaxValues = new HashMap<>();
        specimenVars = new ArrayList<>();
        dataRows = new StringBuilder();
        parseErrors = new ArrayList<>();

        initialiseHeaderRowChainOfResponsibility();
    }

    private void initialiseHeaderRowChainOfResponsibility() {

        headerRowChainOfResponsibility = createMeasurementVarChainEntry();
        // zorg ervoor dat we de eerste chain-entry niet kwijtraken!
        headerRowChainOfResponsibility.setNext( createColumnInfoChainEntry() )
                                      .setNext( createColumnVoidChainEntry() )
                                      .setNext( createColumnMinMaxChainEntry() )
                                      .setNext( createMeasurementTextChainEntry() )
                                      .setNext( createSpecimenVarChainEntry() )
                                      .setNext( createChildChainEntry() )
                                      .setNext( createColumnSeperatorChainEntry() )
                                      .setNext( createTestIdChainEntry() )
                                      .setNext( createCompanyIdChainEntry() )
                                      .setNext( createParentChainEntry() )
                                      .setNext( createGefIdChainEntry() )
                                      .setNext( createRecordSeperatorChainEntry() )
                                      .setNext( createProjectIdChainEntry() )
                                      .setNext( createStartDateChainEntry() )
                                      .setNext( createStartTimeChainEntry() )
                                      .setNext( createXYIDChainEntry() )
                                      .setNext( createZIDChainEntry() )
                                      .setNext( createFileDateChainEntry() )
                                      .setNext( createNrColumnChainEntry() )
                                      .setNext( createReportCodeChainEntry() )
                                      .setNext( createTerminalChainEntry() );
    }

    private HeaderRowChainOfResponsibility createReportCodeChainEntry() {
        return new BaseHRChainOfResponsibility() {
            @Override
            protected boolean handleInternal(HeaderVarEnum headerVarType, String[] splittedValue) {
                if ( headerVarType == HeaderVarEnum.REPORTCODE ) {
                    setReportCode( splittedValue );
                    return true;
                }
                return false;
            }
        };
    }

    private HeaderRowChainOfResponsibility createNrColumnChainEntry() {
        return new BaseHRChainOfResponsibility() {
            @Override
            protected boolean handleInternal(HeaderVarEnum headerVarType, String[] splittedValue) {
                if ( headerVarType == HeaderVarEnum.COLUMN ) {
                    setNrColumn( headerValue );
                    return true;
                }
                return false;
            }
        };
    }

    private BaseHRChainOfResponsibility createTerminalChainEntry() {
        return new BaseHRChainOfResponsibility() {
            @Override
            protected boolean handleInternal(HeaderVarEnum headerVarType, String[] splittedValue) {
                // Terminerende node voor de chain - wanneer niet afgehandeld dan wel detecteren in LOG, maar niet
                // falen!
                LOG.info( String.format( "Found unmapped header variable: [%s] value [%s]", headerVar, headerValue ) );
                return true;
            }
        };
    }

    private BaseHRChainOfResponsibility createFileDateChainEntry() {
        return new BaseHRChainOfResponsibility() {
            @Override
            protected boolean handleInternal(HeaderVarEnum headerVarType, String[] splittedValue) {
                if ( headerVarType == HeaderVarEnum.FILEDATE ) {
                    setFileDate( headerValue );
                    return true;
                }
                return false;
            }
        };
    }

    private BaseHRChainOfResponsibility createZIDChainEntry() {
        return new BaseHRChainOfResponsibility() {
            @Override
            protected boolean handleInternal(HeaderVarEnum headerVarType, String[] splittedValue) {
                if ( headerVarType == HeaderVarEnum.ZID ) {
                    handleVerticalDeliveredLocation( splittedValue );
                    return true;
                }
                return false;
            }
        };
    }

    private BaseHRChainOfResponsibility createXYIDChainEntry() {
        return new BaseHRChainOfResponsibility() {
            @Override
            protected boolean handleInternal(HeaderVarEnum headerVarType, String[] splittedValue) {
                if ( headerVarType == HeaderVarEnum.XYID ) {
                    handleDeliveredLocation( splittedValue );
                    return true;
                }
                return false;
            }
        };
    }

    private BaseHRChainOfResponsibility createStartTimeChainEntry() {
        return new BaseHRChainOfResponsibility() {
            @Override
            protected boolean handleInternal(HeaderVarEnum headerVarType, String[] splittedValue) {
                if ( headerVarType == HeaderVarEnum.STARTTIME ) {
                    setStartTime( headerValue.trim() );
                    return true;
                }
                return false;
            }
        };
    }

    private BaseHRChainOfResponsibility createStartDateChainEntry() {
        return new BaseHRChainOfResponsibility() {
            @Override
            protected boolean handleInternal(HeaderVarEnum headerVarType, String[] splittedValue) {
                if ( headerVarType == HeaderVarEnum.STARTDATE ) {
                    setStartDate( headerValue.trim() );
                    return true;
                }
                return false;
            }
        };
    }

    private BaseHRChainOfResponsibility createSpecimenVarChainEntry() {
        return new BaseHRChainOfResponsibility() {
            @Override
            protected boolean handleInternal(HeaderVarEnum headerVarType, String[] splittedValue) {
                if ( headerVarType == HeaderVarEnum.SPECIMENVAR ) {
                    handleSpecimenVar( splittedValue );
                    return true;
                }
                return false;
            }
        };
    }

    private BaseHRChainOfResponsibility createRecordSeperatorChainEntry() {
        return new BaseHRChainOfResponsibility() {
            @Override
            protected boolean handleInternal(HeaderVarEnum headerVarType, String[] splittedValue) {
                if ( headerVarType == HeaderVarEnum.RECORDSEPARATOR && headerValue != null && !headerValue.isEmpty() ) {
                    recordSeparator = headerValue;
                    return true;
                }
                return false;
            }
        };
    }

    private BaseHRChainOfResponsibility createMeasurementVarChainEntry() {
        return new BaseHRChainOfResponsibility() {
            @Override
            protected boolean handleInternal(HeaderVarEnum headerVarType, String[] splittedValue) {
                if ( headerVarType == HeaderVarEnum.MEASUREMENTVAR ) {
                    handleMeasurementVar( splittedValue );
                    return true;
                }
                return false;
            }
        };
    }

    private BaseHRChainOfResponsibility createMeasurementTextChainEntry() {
        return new BaseHRChainOfResponsibility() {
            @Override
            protected boolean handleInternal(HeaderVarEnum headerVarType, String[] splittedValue) {
                if ( headerVarType == HeaderVarEnum.MEASUREMENTTEXT ) {
                    handleMeasurementText( splittedValue );
                    return true;
                }
                return false;
            }
        };
    }

    private BaseHRChainOfResponsibility createCompanyIdChainEntry() {
        return new BaseHRChainOfResponsibility() {
            @Override
            protected boolean handleInternal(HeaderVarEnum headerVarType, String[] splittedValue) {
                if ( headerVarType == HeaderVarEnum.COMPANYID ) {
                    String responsibleParty = splittedValue != null && splittedValue.length > 1 ? splittedValue[1] : "";
                    setResponsibleParty( nullIfEmpty( responsibleParty ) );
                    return true;
                }
                return false;
            }
        };
    }

    private BaseHRChainOfResponsibility createColumnVoidChainEntry() {
        return new BaseHRChainOfResponsibility() {
            @Override
            protected boolean handleInternal(HeaderVarEnum headerVarType, String[] splittedValue) {
                if ( headerVarType == HeaderVarEnum.COLUMNVOID ) {
                    handleColumnVoid( splittedValue );
                    return true;
                }
                return false;
            }
        };
    }

    private BaseHRChainOfResponsibility createColumnSeperatorChainEntry() {
        return new BaseHRChainOfResponsibility() {
            @Override
            protected boolean handleInternal(HeaderVarEnum headerVarType, String[] splittedValue) {
                if ( headerVarType == HeaderVarEnum.COLUMNSEPARATOR && headerValue != null && !headerValue.trim().isEmpty() ) {
                    columnSeparator = headerValue;
                    return true;
                }
                return false;
            }
        };
    }

    private BaseHRChainOfResponsibility createColumnMinMaxChainEntry() {
        return new BaseHRChainOfResponsibility() {
            @Override
            protected boolean handleInternal(HeaderVarEnum headerVarType, String[] splittedValue) {
                if ( headerVarType == HeaderVarEnum.COLUMNMINMAX ) {
                    handleColumnMinMax( splittedValue );
                    return true;
                }
                return false;
            }
        };
    }

    private BaseHRChainOfResponsibility createColumnInfoChainEntry() {
        return new BaseHRChainOfResponsibility() {
            @Override
            protected boolean handleInternal(HeaderVarEnum headerVarType, String[] splittedValue) {
                if ( headerVarType == HeaderVarEnum.COLUMNINFO ) {
                    handleColumnInfo( splittedValue );
                    return true;
                }
                return false;
            }
        };
    }

    private BaseHRChainOfResponsibility createGefIdChainEntry() {
        return new BaseHRChainOfResponsibility() {
            @Override
            protected boolean handleInternal(HeaderVarEnum headerVarType, String[] splittedValue) {
                if ( headerVarType == HeaderVarEnum.GEFID ) {
                    setGefId( splittedValue );
                    return true;
                }
                return false;
            }
        };
    }

    private BaseHRChainOfResponsibility createParentChainEntry() {
        return new BaseHRChainOfResponsibility() {
            @Override
            protected boolean handleInternal(HeaderVarEnum headerVarType, String[] splittedValue) {
                if ( headerVarType == HeaderVarEnum.PARENT ) {
                    setParentFile( splittedValue != null && splittedValue.length > 0 ? splittedValue[0].trim() : null );
                    setParentDepth( splittedValue != null && splittedValue.length > 1 ? splittedValue[1].trim() : null );
                    return true;
                }
                return false;
            }
        };
    }

    private BaseHRChainOfResponsibility createChildChainEntry() {
        return new BaseHRChainOfResponsibility() {
            @Override
            protected boolean handleInternal(HeaderVarEnum headerVarType, String[] splittedValue) {
                if ( headerVarType == HeaderVarEnum.CHILD ) {
                    String value = splittedValue != null ? splittedValue[1].trim() : null;
                    if ( value != null ) {
                        addChildFile( value );
                    }
                    return true;
                }
                return false;
            }
        };
    }

    private BaseHRChainOfResponsibility createTestIdChainEntry() {
        return new BaseHRChainOfResponsibility() {
            @Override
            protected boolean handleInternal(HeaderVarEnum headerVarType, String[] splittedValue) {
                if ( headerVarType == HeaderVarEnum.TESTID ) {
                    setTestId( headerValue );
                    return true;
                }
                return false;
            }
        };
    }

    private BaseHRChainOfResponsibility createProjectIdChainEntry() {
        return new BaseHRChainOfResponsibility() {
            @Override
            protected boolean handleInternal(HeaderVarEnum headerVarType, String[] splittedValue) {
                if ( headerVarType == HeaderVarEnum.PROJECTID ) {
                    setProjectId( headerValue );
                    return true;
                }
                return false;
            }
        };
    }

    public GefFile getGefFile() {
        return gefFile;
    }

    @Override
    public void enterFile(FileContext ctx) {

        int length = ctx.getText().length() > 255 ? 255 : ctx.getText().length();
        LOG.debug( String.format( "Going to parse GEF file: Content =  %s...", ctx.getText().substring( 0, length ) ) );

        if ( ctx.getText().contains( PARENT ) ) {
            // er is een parent gedefinieerd voor dit bestand - de aanname is dat dit 'dus' een dissipatie bestand is...
            fileType = GefFile.Types.DISS;
            gefFile = new GefDisFile();
        }
        else {
            fileType = GefFile.Types.CPT;
            gefFile = new GefCptFile();
        }
    }

    @Override
    public void exitFile(FileContext ctx) {

        // Als specimenvar ingevuld - zet de onderdiepte van de laatste laag naar de waarde van MeasurementVar nr.13
        if ( !specimenVars.isEmpty() ) {
            BigDecimal lowerDepth = bigDecimalMeasurementVars.get( LOWER_DEPTH_MEA_KEY );
            specimenVars.get( specimenVars.size() - 1 ).setOnderdiepte( lowerDepth );
        }

        processMeasurementTexts();
        processMeasurementVars();
        processSpecimenVars();
        processMiscellaneousData();
        processDatablock();

        gefFile.getParseErrors().addAll( parseErrors );
    }

    @SuppressWarnings( "unchecked" )
    private void processMeasurementVars() {

        if ( fileType == GefFile.Types.DISS ) {
            // measurement vars worden alleen gebruikt bij GEF-CPT
            return;
        }

        for ( Map.Entry<String, BigDecimal> entry : bigDecimalMeasurementVars.entrySet() ) {
            String methodName = BASE_SET_MEASUREMENTVAR + entry.getKey();
            boolean success = invokeMethod( gefFile, methodName, entry.getValue(), BigDecimal.class );
            if ( success ) {
                measurementVarsSet.add( entry.getKey() );
            }
        }

        for ( Map.Entry<String, String> entry : stringMeasurementVars.entrySet() ) {
            String methodName = BASE_SET_MEASUREMENTVAR + entry.getKey();
            boolean success = invokeMethod( gefFile, methodName, entry.getValue(), String.class );
            if ( success ) {
                measurementVarsSet.add( entry.getKey() );
            }
        }
        gefFile.setMeasurementVarsSet( measurementVarsSet );
    }

    @SuppressWarnings( "unchecked" )
    private void processMiscellaneousData() {

        // Catch all methode voor alle entiteiten die niet in bulk verwerkt worden:
        gefFile.setProjectId( projectId );
        gefFile.setTestId( testId );
        gefFile.setParentFileName( parentFile );
        gefFile.setParentDepth( parentDepth );
        gefFile.setExpectedChildFileNameList( getChildFiles() );
        gefFile.setCompanyId( companyId );
        gefFile.setGefId( gefId );
        gefFile.setReportCode( reportCode );

        gefFile.setFileType( fileType );
        gefFile.setFileDate( fileDate );

        gefFile.setStartDate( startDate );
        gefFile.setStartTime( startTime );

        if ( xyid != null && xyid.length >= 3 ) {
            gefFile.setXyidCrs( xyid[0].trim() );
            gefFile.setXyidX( new BigDecimal( xyid[1].trim() ) );
            gefFile.setXyidY( new BigDecimal( xyid[2].trim() ) );
        }

        if ( zid != null && zid.length >= 2 ) {
            gefFile.setZidOffset( new BigDecimal( zid[1].trim() ) );
            if ( zid[0] == null || zid[0].isEmpty() ) {
                // om te voorkomen dat dit bij gef-validatie een foutmelding geeft - hier onbekend invoegen.
                zid[0] = MappingConstants.GEF_IND_UNKNOWN;
            }
            gefFile.setZidVerticalDatum( zid[0].trim() );
        }
    }

    @SuppressWarnings( "unchecked" )
    private void processDatablock() {

        if ( nrColumns <= 0 ) {
            addParseError( "#COLUMN not set or 0" );
            return;
        }

        Set<String> brokenIndices = new HashSet<>();
        List<String> dataRecords = splitRecords( dataRows.toString().trim() );
        int recordIndex = 0;
        for ( String record : dataRecords ) {
            recordIndex++;
            DataRow targetObj = createDataRowInstance();
            if ( recordIndex == dataRecords.size() && !record.endsWith( recordSeparator ) ) {
                // add a record separator to the last if record when not there
                record = record.concat( recordSeparator );
            }
            List<BigDecimal> dataItems = splitItems( record, nrColumns );

            if ( dataItems.isEmpty() ) {
                addParseError( String.format( "No data columns found in row# %s", recordIndex ) );
            }
            for ( int i = 0; i < dataItems.size() && i < nrColumns; ++i ) {
                BigDecimal value = dataItems.get( i );
                // corrigeer voor de off-by-one factor in het GEF file zelf!
                String index = Integer.toString( i + 1 );

                if ( !brokenIndices.contains( index ) ) {
                    boolean stop = !assignValueToDataRow( targetObj, value, index, Integer.toString( recordIndex ) );
                    if ( stop ) {
                        brokenIndices.add( index );
                    }
                }
            }

            if ( CPT.equals( fileType ) || DISS.equals( fileType ) ) {
                gefFile.getDataBlock().add( targetObj );
            }
            else {
                addParseError( String.format( "Invalid format for file: [%s] - file cannot be identified as GEF-CPT or GEF-DISS file", gefFile.getFileName() ) );
                return;
            }

        }

        if ( CPT.equals( fileType ) ) {
            // sorteer het datablock op length (veplichte parameter).
            gefFile.getDataBlock().sort( new DataRow.CompareOnLenght() );
        }

        gefFile.setMeasuredParameters( measuredVars );
    }

    /**
     * @param targetObj
     * @param value
     * @param index
     * @param recordIndex
     * @return false when (non-fatal) error, stop looping
     */
    private boolean assignValueToDataRow(DataRow targetObj, BigDecimal value, String index, String recordIndex) {
        BigDecimal actual = null;
        BigDecimal voidValue = columnVoidValues.get( index );

        if ( value != null ) {
            if ( voidValue != null && value.compareTo( voidValue ) == 0 ) {
                actual = null;
            }
            else {
                actual = value;
            }
        }
        else {
            // NULL waarde betekent in dit geval dat de waarde niet naar BigDecimal geconverteerd kon worden.
            addParseError( String.format( "value in (row=%s, column=%s) is not in a valid number format", recordIndex, index ) );
        }

        // Zet de juiste datablock entry in het targetObj.
        String actualIndex = columnInfos.get( index );

        if ( actualIndex != null ) {
            if ( actual != null ) {
                // er is een waarde om te zetten!
                String methodName = BASE_SET_DATABLOCKCI + actualIndex;
                boolean success = invokeMethod( targetObj, methodName, actual, BigDecimal.class );
                if ( success ) {
                    measuredVars.add( actualIndex );
                }
            }
            // merk op: wanneer de waarde NULL is dan was deze gelijk aan de columnvoid waarde uit het GEF
            // bestand zelf.
            // Dit betekent hier dat de waarde niet gezet hoeft te worden maar wel aangemerkt moet worden als
            // gezet - anders
            // falen de validaties.
            targetObj.setFilledEntry( actualIndex, true );
        }
        else {
            LOG.debug( String.format( "No column info found for index: %s", index ) );
            addParseError( String.format( "No #COLUMNINFO for index %s", index ) );
            return false;
        }
        return true;
    }

    private DataRow createDataRowInstance() {
        DataRow targetObj = null;
        switch ( fileType ) {
            case DISS:
                targetObj = new DataRowDiss();
                break;
            case CPT:
                /* fallthrough */
            case UNKNOWN:
                /* fallthrough */
            default:
                /* Neem aan dat het om een CPT bestand gaat... */
                targetObj = new DataRowCpt();
                break;
        }
        return targetObj;
    }

    private List<BigDecimal> splitItems(String record, int nrColumns) {
        return GefUtility.splitDataItems( record, columnSeparator, recordSeparator, nrColumns );
    }

    private List<String> splitRecords(String records) {
        return GefUtility.splitDataRows( records, recordSeparator );
    }

    @SuppressWarnings( "unchecked" )
    private void processSpecimenVars() {

        specimenVars.sort( new SpecimenVarComparator() );

        // Zet de volgnummers en onderdiepten.
        for ( int i = 0; i < specimenVars.size(); ++i ) {
            specimenVars.get( i ).setVolgnummer( i + 1 );
            if ( i > 0 ) {
                specimenVars.get( i - 1 ).setOnderdiepte( specimenVars.get( i ).getBovendiepte() );
            }
        }

        if ( !specimenVars.isEmpty() ) {
            specimenVars.get( specimenVars.size() - 1 ).setOnderdiepte( gefFile.getMeasurementVar13() );
            gefFile.setSpecimenVar( specimenVars );
        }
    }

    private void processMeasurementTexts() {

        if ( fileType == GefFile.Types.DISS ) {
            // measurement texts worden alleen gebruikt bij GEF-CPT
            return;
        }

        for ( Map.Entry<String, String> entry : measurementTexts.entrySet() ) {
            String methodName = BASE_SET_MEASUREMENTTEXT + entry.getKey();
            invokeMethod( gefFile, methodName, entry.getValue(), String.class );
        }

    }

    @SuppressWarnings( "rawtypes" )
    private boolean invokeMethod(Object object, String methodName, Object value, Class argClazz) {

        try {
            Method method = object.getClass().getMethod( methodName, argClazz );
            method.invoke( object, value );
        }
        catch ( NoSuchMethodException | IllegalArgumentException | InvocationTargetException e ) {
            LOG.debug( e.getMessage(), e );
            return false;
        }
        catch ( SecurityException | IllegalAccessException e ) {
            LOG.error( e.getMessage(), e );
            throw new IllegalArgumentException( String.format( "Failed to invoke method [%s] with value [%s] on %s instance", methodName, value.toString(), object.getClass().getName() ) );
        }
        return true;
    }

    @Override
    public void enterHeaderrow(HeaderrowContext ctx) {
        headerVar = null;
        headerValue = null;
    }

    @Override
    public void exitHeaderrow(HeaderrowContext ctx) {

        if ( headerVar == null && headerValue == null ) {
            // kennelijk was dit een lege regel - niks te doen.
            return;
        }

        LOG.debug( String.format( "Processing header row[ %s ] - value = %s", headerVar, headerValue ) );
        // Sla de header waarde op:
        HeaderVarEnum hve = HeaderVarEnum.valueOfStr( headerVar );
        if ( hve == null ) {
            // niet herkende waarde in GEF bestand - vooralsnog betekent dit dat we deze moeten negeren.
            // Log iig een bericht!
            LOG.info( String.format( "Unrecognized header variable: [%s] value [%s]", headerVar, headerValue ) );
            return;
        }

        if ( headerValue == null ) {
            // niet goed geformatteerd - lege string array aanmaken
            LOG.info( String.format( "Header variable: %s does not have a header value", headerVar ) );
            headerValue = "";
        }
        String[] splittedValue = GefUtility.commaDelimitedListToStringArray( headerValue );
        headerRowChainOfResponsibility.handle( hve, splittedValue );

        headerVar = null;
        headerValue = null;
    }

    private void setParentDepth(String valueAsString) {

        try {
            if ( valueAsString != null ) {
                parentDepth = new BigDecimal( valueAsString.trim() );
            }
        }
        catch ( NumberFormatException e ) {
            addParseError( String.format( "#PARENT invalid value for second parameter: '%s' - invalid double value", valueAsString ) );
        }
    }

    private void addParseError(String error) {

        parseErrors.add( error );
    }

    private void setNrColumn(String headerValue) {
        if ( headerValue == null || headerValue.trim().isEmpty() ) {
            addParseError( "#COLUMN has no value" );
            nrColumns = 0;
        }

        try {
            nrColumns = Integer.parseInt( headerValue.trim() );
        }
        catch ( NumberFormatException ex ) {

            addParseError( String.format( "#COLUMN has invalid integer value: %s", headerValue ) );
            nrColumns = 0;
        }
    }

    private void setFileDate(String value) {
        this.fileDate = value;
    }

    private void addChildFile(String value) {
        getChildFiles().add( value );
    }

    private void setParentFile(String value) {
        this.parentFile = value;
    }

    private void setResponsibleParty(String value) {
        this.companyId = value;
    }

    private void setProjectId(String value) {
        this.projectId = value;
    }

    private void setTestId(String value) {
        this.testId = value == null ? null : value.trim();
    }

    protected void setReportCode(String[] splittedHeaderVals) {

        StringBuilder reportCodeBuilder = new StringBuilder();
        String seperator = "";
        for ( int i = 0; i < splittedHeaderVals.length && i < 4; ++i ) {
            reportCodeBuilder.append( seperator ).append( splittedHeaderVals[i].trim() );
            seperator = ".";
        }

        this.reportCode = reportCodeBuilder.toString();
    }

    private void setGefId(String[] splittedHeaderVals) {

        StringBuilder gefIdBuilder = new StringBuilder();
        String seperator = "";
        for ( String component : splittedHeaderVals ) {
            gefIdBuilder.append( seperator ).append( component.trim() );
            seperator = ".";
        }

        this.gefId = gefIdBuilder.toString();
    }

    private void handleVerticalDeliveredLocation(String[] splittedHeaderVals) {
        zid = splittedHeaderVals;
    }

    private void handleDeliveredLocation(String[] splittedHeaderVals) {
        xyid = splittedHeaderVals;
    }

    private void setStartDate(String value) {

        startDate = value;
    }

    private void setStartTime(String value) {

        startTime = value;
    }

    private List<String> getChildFiles() {
        if ( childFiles == null ) {
            childFiles = new ArrayList<String>();
        }
        return childFiles;
    }

    private void handleSpecimenVar(String[] splittedHeaderVals) {

        if ( splittedHeaderVals.length != 4 ) {
            addParseError( "Invalid number of datafields for #SPECIMENVAR " + ( splittedHeaderVals.length == 0 ? "" : splittedHeaderVals[0].trim() ) );
            return;
        }

        SpecimenVar var = new SpecimenVar();

        BigDecimal upperDepth;
        try {
            upperDepth = new BigDecimal( splittedHeaderVals[1].trim() );
        }
        catch ( NumberFormatException e ) {
            String error = String.format( "Invalid number format for #SPECIMENVAR %s -> '%s'", splittedHeaderVals[0], splittedHeaderVals[1] );
            LOG.info( error );
            addParseError( error );
            return;
        }

        var.setOnderdiepte( null );
        var.setBovendiepte( upperDepth );
        var.setBeschrijving( splittedHeaderVals[3].trim() );

        specimenVars.add( var );
    }

    private void handleMeasurementVar(String[] splittedHeaderVals) {

        if ( splittedHeaderVals.length != 4 ) {
            addParseError( "Invalid number of datafields for #MEASUREMENTVAR " + ( splittedHeaderVals.length == 0 ? "" : splittedHeaderVals[0].trim() ) );
            return;
        }

        String key = splittedHeaderVals[0].trim();
        String value = splittedHeaderVals[1].trim();

        if ( value == null || value.isEmpty() || "-".equals( value ) ) {
            value = null;
        }

        // if measurementvar een string variabele.
        if ( "17".equals( key ) || "12".equals( key ) || "9".equals( key ) ) {
            stringMeasurementVars.put( key, value );
        }
        else {
            // anders afhandelen als big-decimal.
            bigDecimalMeasurementVars.put( key, measurementVarToBigDecimal( key, value ) );
        }
    }

    private BigDecimal measurementVarToBigDecimal(String key, String value) {
        BigDecimal dblValue = null;
        if ( value != null && !value.trim().isEmpty() ) {
            try {
                dblValue = new BigDecimal( value.trim() );
            }
            catch ( NumberFormatException ex ) {
                addParseError( String.format( "#MEASUREMENTVAR%s value[%s] cannot be converted to number format", key, value ) );
            }
        }
        return dblValue;
    }

    private void handleMeasurementText(String[] splittedHeaderVals) {

        // Merk op: afhankelijk van het eerste veld in de measurement text (de index of type nummer) is er
        // alternatieve afhandeling nodig. Mogelijkheden zijn:
        // 1 - de data zit in element 2 van splittedHeaderVals
        // 2 - de data zit in element 3 van splittedHeaderVals
        // 3 - de data is een datum waarde (elementen 2, 3 en 4 waarvan een of meer optioneel)
        // 4 - measurement text 6 -> sondeernorm en kwaliteitsklasse geabstraheerd uit element 2 van splittedHeaderVals.
        final Set<Integer> secondElementKeys = ImmutableSet.of( 4, 5, 9, 11, 20, 21, 42, 43, 102, 103, 108, 109, 110, 111 );
        final Set<Integer> thirdElementKeys = ImmutableSet.of( 101, 104, 106 );
        final Set<Integer> dateKeys = ImmutableSet.of( 105, 107, 112, 113, 114 );

        Integer key = Integer.valueOf( splittedHeaderVals[0].trim() );

        if ( secondElementKeys.contains( key ) ) {
            if ( splittedHeaderVals.length >= 2 ) {
                measurementTexts.put( key.toString(), nullIfEmpty( splittedHeaderVals[1] ) );
            }
            else {
                addParseError( String.format( "#MEASUREMENTTEXT[%s] cannot be parsed - should have at least 2 elements", key ) );
            }
        }
        else if ( thirdElementKeys.contains( key ) ) {
            if ( splittedHeaderVals.length >= 3 ) {
                measurementTexts.put( key.toString(), nullIfEmpty( splittedHeaderVals[2] ) );
            }
            else {
                addParseError( String.format( "#MEASUREMENTTEXT[%s] cannot be parsed - should have at least 3 elements", key ) );
            }
        }
        else if ( dateKeys.contains( key ) ) {
            String date = reconstituteDateValue( splittedHeaderVals );
            measurementTexts.put( key.toString(), date );
        }
        else if ( key.equals( 6 ) ) {
            handleAsSondeernormAndQualityClass( splittedHeaderVals, key.toString() );
        }
        else {
            LOG.info( String.format( "MEASUREMENTTEXT with value [%s] cannot be parsed - ignoring this value", headerValue ) );
        }
    }

    private static String nullIfEmpty(String value) {

        String returnValue = value.trim();
        if ( returnValue.isEmpty() || "-".equals( returnValue ) ) {
            returnValue = null;
        }
        return returnValue;
    }

    private void handleAsSondeernormAndQualityClass(String[] splittedHeaderVals, String key) {
        String source = splittedHeaderVals[1].trim();
        String sondeernorm = findSondeerNorm( source );
        String qualityClass = findQualityClass( source.replace( sondeernorm, "" ), sondeernorm );

        measurementTexts.put( key + SONDEERNORM_POSTFIX, sondeernorm );
        measurementTexts.put( key + QUALITYCLASS_POSTFIX, qualityClass );
    }

    private String reconstituteDateValue(String[] splittedHeaderVals) {
        StringBuilder dateBuilder = new StringBuilder();
        String seperator = "";
        int index = 1;
        while ( index < splittedHeaderVals.length ) {
            String part = splittedHeaderVals[index].trim();
            if ( !part.isEmpty() && !"-".equals( part ) ) {
                dateBuilder.append( seperator ).append( part );
                seperator = ", ";
            }
            ++index;
        }
        return dateBuilder.toString();
    }

    private String findQualityClass(String value, String sondeerNorm) {

        int[] foundElements = { value.indexOf( "1" ), value.indexOf( "2" ), value.indexOf( "3" ), value.indexOf( "4" ), value.indexOf( "5" ), value.indexOf( "6" ), value.indexOf( "7" ) };

        for ( int i = 0; i < foundElements.length; ++i ) {
            if ( foundElements[i] > 0 ) {
                return Integer.toString( i + 1 );
            }
        }

        // Let op: het kan zijn dat de tekst 'nvt' gebruikt wordt (in combo met NEN3680)
        if ( value.indexOf( QC_NOT_APPLICABLE ) > 0 ) {
            return QC_NOT_APPLICABLE;
        }

        LOG.warn( String.format( "Cannot determine quality class from resultant value [%s]", value ) );
        return NEN_3680.equals( sondeerNorm ) ? QC_NOT_APPLICABLE : MappingConstants.GEF_IND_UNKNOWN;
    }

    private String findSondeerNorm(String value) {

        int index = value.indexOf( NEN_EN_ISO_22476_12 );

        String retVal = "";
        if ( index >= 0 ) {
            retVal = NEN_EN_ISO_22476_12;
        }

        index = value.indexOf( NEN_EN_ISO_22476_1 );
        if ( retVal.isEmpty() && index >= 0 ) {
            retVal = NEN_EN_ISO_22476_1;
        }

        index = value.indexOf( NEN_5140 );
        if ( retVal.isEmpty() && index >= 0 ) {
            retVal = NEN_5140;
        }

        index = value.indexOf( NEN_3680 );
        if ( retVal.isEmpty() && index >= 0 ) {
            retVal = NEN_3680;
        }

        if ( retVal.isEmpty() ) {
            LOG.warn( String.format( "Cannot determine sondeernorm from value [%s]", value ) );
            retVal = MappingConstants.GEF_IND_UNKNOWN;
        }

        return retVal;
    }

    private void handleColumnVoid(String[] splittedHeaderVals) {

        String key = splittedHeaderVals[0].trim();
        String value = splittedHeaderVals[1].trim();

        BigDecimal dblValue = null;
        try {
            dblValue = new BigDecimal( value.trim() );
        }
        catch ( NumberFormatException e ) {
            addParseError( String.format( "#COLUMNVOID key[%s]: value[%s] is in an invalid floating point format", key, value ) );
        }

        columnVoidValues.put( key, dblValue );
    }

    private void handleColumnMinMax(String[] splittedHeaderVals) {

        String key = splittedHeaderVals[0].trim();
        String valueMin = splittedHeaderVals[1].trim();
        String valueMax = splittedHeaderVals[2].trim();

        BigDecimal min = null;
        try {
            min = new BigDecimal( valueMin.trim() );
        }
        catch ( NumberFormatException e ) {
            addParseError( String.format( "#COLUMNMINMAX key[%s]: minimum value[%s] is in an invalid floating point format", key, valueMin ) );
        }

        BigDecimal max = null;
        try {
            max = new BigDecimal( valueMax.trim() );
        }
        catch ( NumberFormatException e ) {
            addParseError( String.format( "#COLUMNMINMAX key[%s]: maximum value[%s] is in an invalid floating point format", key, valueMax ) );
        }

        columnMinMaxValues.put( key, new MinMaxPair( min, max ) );
    }

    private void handleColumnInfo(String[] splittedHeaderVals) {

        if ( splittedHeaderVals.length != 4 ) {
            addParseError( "Invalid number of datafields for #COLUMNINFO " + ( splittedHeaderVals.length == 0 ? "" : splittedHeaderVals[0].trim() ) );
            return;

        }
        // kolom index in datablock
        String key = splittedHeaderVals[0].trim();
        // kolom index van de weergegeven entiteit (b.v. sondeer trajectlengte)
        String value = splittedHeaderVals[3].trim();

        columnInfos.put( key, value );
    }

    @Override
    public void enterDatavalues(DatavaluesContext ctx) {
        dataRows.append( ctx.getText().trim() );
        // voeg een carriage CR/LF toe - deze zou anders verdwijnen (artf-51339)
        dataRows.append( " " );
        dataRows.append( CRLF );
    }

    @Override
    public void enterWaarde(WaardeContext ctx) {

        headerValue = ctx.getText().trim();
    }

    @Override
    public void enterVariabele(VariabeleContext ctx) {

        headerVar = ctx.getText().trim();
    }

    public GefFile.Types getFileType() {
        return fileType;
    }

}
