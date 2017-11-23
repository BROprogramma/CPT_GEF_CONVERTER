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

import java.io.IOException;
import java.io.InputStream;
import java.io.StringWriter;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBElement;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;

import org.apache.log4j.Logger;

import com.google.common.collect.ImmutableMap;

import nl.bro.cpt.gef.dto.DataRow;
import nl.bro.cpt.gef.dto.DataRowCpt;
import nl.bro.cpt.gef.dto.DataRowDiss;
import nl.broservices.xsd.cptcommon.v_1_1.ConePenetrationTestResultType;
import nl.broservices.xsd.cptcommon.v_1_1.DissipationTestResultType;
import nl.broservices.xsd.cptcommon.v_1_1.ObjectFactory;

import au.com.bytecode.opencsv.CSVWriter;
import net.opengis.swe.v_2_0.DataArrayType;
import net.opengis.swe.v_2_0.DataArrayType.Encoding;
import net.opengis.swe.v_2_0.DataRecordType;
import net.opengis.swe.v_2_0.NilValue;
import net.opengis.swe.v_2_0.NilValuesPropertyType;
import net.opengis.swe.v_2_0.QuantityType;
import net.opengis.swe.v_2_0.TextEncodingType;

/**
 * @author derksenjpam
 */
public class TestResultMapper {

    // inner classes

    private static class DataRecordDefinition {

        private static final int HASHCODE = 5;
        private final String recordName;
        private final String recordNilValue;

        DataRecordDefinition(String recordName, String recordNilValue) {
            this.recordName = recordName;
            this.recordNilValue = recordNilValue;
        }

        String getRecordName() {
            return recordName;
        }

        String getRecordNilValue() {
            return recordNilValue;
        }

        @Override
        public int hashCode() {
            return HASHCODE;
        }

        @Override
        public boolean equals(Object obj) {
            if ( obj == null ) {
                return false;
            }
            if ( getClass() != obj.getClass() ) {
                return false;
            }
            final DataRecordDefinition other = (DataRecordDefinition) obj;
            return recordNameEqual( other ) && nilValuesEqual( other );
        }

        private boolean nilValuesEqual(final DataRecordDefinition other) {
            return this.getRecordNilValue() == null ? other.getRecordNilValue() == null : this.getRecordNilValue().equals( other.getRecordNilValue() );
        }

        private boolean recordNameEqual(final DataRecordDefinition other) {
            return this.getRecordName() == null ? other.getRecordName() == null : this.getRecordName().equals( other.getRecordName() );
        }

    }

    private static class Result {

        private final TextEncodingType textEncoding;
        private final List<Object> valuesList;

        Result(TextEncodingType textEncoding, List<Object> valuesList) {
            this.textEncoding = textEncoding;
            this.valuesList = valuesList;
        }

        TextEncodingType getTextEncoding() {
            return textEncoding;
        }

        List<Object> getValuesList() {
            return valuesList;
        }

    }

    private abstract static class DataRowMapper {

        private String nillValue;

        protected String toString(BigDecimal value, int fraction) {
            String result = nillValue;
            if ( value != null ) {
                result = TruncationMapper.truncate( value, fraction ).toString().trim();
            }
            return result;
        }

        public String getNillValue() {
            if ( nillValue != null ) {
                return nillValue;
            }
            else {
                return "";
            }
        }

        public void setNillValue(String nillValue) {
            this.nillValue = nillValue;
        }

        abstract String map(DataRow dtr);
    }

    // file names and urls
    private static final String CPT_RECORD_FILE_NAME = "schema/cpttestresult_record.xml";
    private static final String CPT_RECORD_NAME = "ConePenetrationTestResultRecord";
    private static final String DT_RECORD_FILE_NAME = "schema/dissipationtestresult_record.xml";
    private static final String DT_RECORD_NAME = "DissipationTestResultRecord";
    private static final String CPT_HREF_RECORD = "http://www.broservices.nl/xsd/cptcommon/1.0/cpttestresult_record.xml";
    private static final String DT_HREF_RECORD = "http://www.broservices.nl/xsd/cptcommon/1.0/dissipationtestresult_record.xml";

    // separators
    private static final String BLOCK_SEPARATOR = ";";
    private static final char TOKEN_SEPARATOR_CHAR = ',';
    private static final String TOKEN_SEPARATOR = new String( new char[] { TOKEN_SEPARATOR_CHAR } );

    private static final String DECIMAL_SEPARATOR = ".";

    // object factory
    private static final ObjectFactory CPT_OF = new ObjectFactory();
    private static final net.opengis.swe.v_2_0.ObjectFactory SWE_OF = new net.opengis.swe.v_2_0.ObjectFactory();

    // logger
    private static final Logger LOG = Logger.getLogger( TestResultMapper.class );

    private static final Map<String, DataRowMapper> FROM_CPT_MAP = ImmutableMap.<String, DataRowMapper> builder().put( "penetrationLength", new DataRowMapper() {
        @Override
        String map(DataRow cpt) {
            return toString( cpt.getDatablockci1(), 3 );
        }
    } ).put( "depth", new DataRowMapper() {
        @Override
        String map(DataRow cpt) {
            return toString( cpt.getDatablockci11(), 3 );
        }
    } ).put( "elapsedTime", new DataRowMapper() {
        @Override
        String map(DataRow cpt) {
            return toString( cpt.getDatablockci12(), 1 );
        }
    } ).put( "coneResistance", new DataRowMapper() {
        @Override
        String map(DataRow cpt) {
            return toString( cpt.getDatablockci2(), 3 );
        }
    } ).put( "correctedConeResistance", new DataRowMapper() {
        @Override
        String map(DataRow cpt) {
            return toString( cpt.getDatablockci13(), 3 );
        }
    } ).put( "netConeResistance", new DataRowMapper() {
        @Override
        String map(DataRow cpt) {
            return toString( cpt.getDatablockci14(), 3 );
        }
    } ).put( "magneticFieldStrengthX", new DataRowMapper() {
        @Override
        String map(DataRow cpt) {
            return toString( cpt.getDatablockci31(), 0 );
        }
    } ).put( "magneticFieldStrengthY", new DataRowMapper() {
        @Override
        String map(DataRow cpt) {
            return toString( cpt.getDatablockci32(), 0 );
        }
    } ).put( "magneticFieldStrengthZ", new DataRowMapper() {
        @Override
        String map(DataRow cpt) {
            return toString( cpt.getDatablockci33(), 0 );
        }
    } ).put( "magneticFieldStrengthTotal", new DataRowMapper() {
        @Override
        String map(DataRow cpt) {
            return toString( cpt.getDatablockci34(), 0 );
        }
    } ).put( "electricalConductivity", new DataRowMapper() {
        @Override
        String map(DataRow cpt) {
            return toString( cpt.getDatablockci23(), 3 );
        }
    } ).put( "inclinationEW", new DataRowMapper() {
        @Override
        String map(DataRow cpt) {
            return toString( cpt.getDatablockci10(), 0 );
        }
    } ).put( "inclinationNS", new DataRowMapper() {
        @Override
        String map(DataRow cpt) {
            return toString( cpt.getDatablockci9(), 0 );
        }
    } ).put( "inclinationX", new DataRowMapper() {
        @Override
        String map(DataRow cpt) {
            return toString( cpt.getDatablockci21(), 0 );
        }
    } ).put( "inclinationY", new DataRowMapper() {
        @Override
        String map(DataRow cpt) {
            return toString( cpt.getDatablockci22(), 0 );
        }
    } ).put( "inclinationResultant", new DataRowMapper() {
        @Override
        String map(DataRow cpt) {
            return toString( cpt.getDatablockci8(), 0 );
        }
    } ).put( "magneticInclination", new DataRowMapper() {
        @Override
        String map(DataRow cpt) {
            return toString( cpt.getDatablockci35(), 0 );
        }
    } ).put( "magneticDeclination", new DataRowMapper() {
        @Override
        String map(DataRow cpt) {
            return toString( cpt.getDatablockci36(), 0 );
        }
    } ).put( "localFriction", new DataRowMapper() {
        @Override
        String map(DataRow cpt) {
            return toString( cpt.getDatablockci3(), 3 );
        }
    } ).put( "poreRatio", new DataRowMapper() {
        @Override
        String map(DataRow cpt) {
            return toString( cpt.getDatablockci15(), 3 );
        }
    } ).put( "temperature", new DataRowMapper() {
        @Override
        String map(DataRow cpt) {
            return toString( cpt.getDatablockci39(), 1 );
        }
    } ).put( "porePressureU1", new DataRowMapper() {
        @Override
        String map(DataRow cpt) {
            return toString( cpt.getDatablockci5(), 3 );
        }
    } ).put( "porePressureU2", new DataRowMapper() {
        @Override
        String map(DataRow cpt) {
            return toString( cpt.getDatablockci6(), 3 );
        }
    } ).put( "porePressureU3", new DataRowMapper() {
        @Override
        String map(DataRow cpt) {
            return toString( cpt.getDatablockci7(), 3 );
        }
    } ).put( "frictionRatio", new DataRowMapper() {
        @Override
        String map(DataRow cpt) {
            return toString( cpt.getDatablockci4(), 1 );
        }
    } ).build();

    private static final Map<String, DataRowMapper> FROM_DT_MAP = ImmutableMap.<String, DataRowMapper> builder().put( "elapsedTime", new DataRowMapper() {
        @Override
        String map(DataRow dt) {
            return toString( dt.getDatablockci12(), 1 );
        }
    } ).put( "coneResistance", new DataRowMapper() {
        @Override
        String map(DataRow dt) {
            return toString( dt.getDatablockci2(), 3 );
        }
    } ).put( "porePressureU1", new DataRowMapper() {
        @Override
        String map(DataRow dt) {
            return toString( dt.getDatablockci5(), 3 );
        }
    } ).put( "porePressureU2", new DataRowMapper() {
        @Override
        String map(DataRow dt) {
            return toString( dt.getDatablockci6(), 3 );
        }
    } ).put( "porePressureU3", new DataRowMapper() {
        @Override
        String map(DataRow dt) {
            return toString( dt.getDatablockci7(), 3 );
        }
    } ).build();

    private static final List<DataRowMapper> FROM_CPT = init( FROM_CPT_MAP, CPT_RECORD_FILE_NAME );
    private static final List<DataRowMapper> FROM_DT = init( FROM_DT_MAP, DT_RECORD_FILE_NAME );

    DissipationTestResultType toDTResultsJaxb(List<DataRowDiss> dptrs) {

        DissipationTestResultType dtrt = CPT_OF.createDissipationTestResultType();

        // init
        List<String[]> values = new ArrayList<>();
        for ( DataRow dptr : dptrs ) {
            String[] row = new String[FROM_DT.size()];
            for ( int i = 0; i < FROM_DT.size(); i++ ) {
                row[i] = FROM_DT.get( i ).map( dptr );
            }
            values.add( row );
        }
        Result result = maptTo( values );

        // set (empty) count
        dtrt.setElementCount( SWE_OF.createCountPropertyType() );

        // set encoding
        TextEncodingType textEncodingType = result.getTextEncoding();
        JAXBElement<TextEncodingType> jAXBTextEncodingType = SWE_OF.createTextEncoding( textEncodingType );
        Encoding encoding = SWE_OF.createDataArrayTypeEncoding();
        encoding.setAbstractEncoding( jAXBTextEncodingType );
        dtrt.setEncoding( encoding );

        // element type
        DataArrayType.ElementType datet = SWE_OF.createDataArrayTypeElementType();
        dtrt.setElementType( datet );
        datet.setName( DT_RECORD_NAME );
        datet.setHref( DT_HREF_RECORD );

        // values
        DissipationTestResultType.Values valuesType = CPT_OF.createDissipationTestResultTypeValues();
        valuesType.getContent().addAll( result.getValuesList() );
        dtrt.setValues( valuesType );
        return dtrt;
    }

    ConePenetrationTestResultType toCPTResultsJaxb(List<DataRowCpt> cptrs) {

        ConePenetrationTestResultType cptrt = CPT_OF.createConePenetrationTestResultType();

        // init
        List<String[]> values = new ArrayList<>();
        for ( DataRow cptr : cptrs ) {
            String[] row = new String[FROM_CPT.size()];
            for ( int i = 0; i < FROM_CPT.size(); i++ ) {
                row[i] = FROM_CPT.get( i ).map( cptr );
            }
            values.add( row );
        }
        Result result = maptTo( values );

        // set (empty) count
        cptrt.setElementCount( SWE_OF.createCountPropertyType() );

        // set encoding
        TextEncodingType textEncodingType = result.getTextEncoding();
        JAXBElement<TextEncodingType> jAXBTextEncodingType = SWE_OF.createTextEncoding( textEncodingType );
        Encoding encoding = SWE_OF.createDataArrayTypeEncoding();
        encoding.setAbstractEncoding( jAXBTextEncodingType );
        cptrt.setEncoding( encoding );

        // element type
        DataArrayType.ElementType datet = SWE_OF.createDataArrayTypeElementType();
        cptrt.setElementType( datet );
        datet.setName( CPT_RECORD_NAME );
        datet.setHref( CPT_HREF_RECORD );

        // values
        ConePenetrationTestResultType.Values valuesType = CPT_OF.createConePenetrationTestResultTypeValues();
        valuesType.getContent().addAll( result.getValuesList() );
        cptrt.setValues( valuesType );
        return cptrt;
    }

    @SuppressWarnings( "unchecked" )
    private static List<DataRecordDefinition> readDataRecordDefinition(String resourceFileName) {
        List<DataRecordDefinition> result = new ArrayList<>();
        try {
            InputStream is = TestResultMapper.class.getClassLoader().getResourceAsStream( resourceFileName );
            JAXBContext jaxbContext = JAXBContext.newInstance( DataRecordType.class );
            Unmarshaller jaxbUnmarshaller = jaxbContext.createUnmarshaller();
            JAXBElement<DataRecordType> dataRecord = (JAXBElement<DataRecordType>) jaxbUnmarshaller.unmarshal( is );
            for ( DataRecordType.Field field : dataRecord.getValue().getField() ) {
                addFieldToResult( result, field );
            }
            is.close();
        }
        catch ( JAXBException | IOException ex ) {
            throw new IllegalStateException( ex );
        }
        return result;
    }

    private static void addFieldToResult(List<DataRecordDefinition> result, DataRecordType.Field field) {
        String nillValue = "";
        if ( field.getAbstractDataComponent().getDeclaredType().isAssignableFrom( QuantityType.class ) ) {
            QuantityType quantityType = (QuantityType) field.getAbstractDataComponent().getValue();
            NilValuesPropertyType nillValues = quantityType.getNilValues();
            Iterator<NilValue> iterator = nillValues.getNilValues().getNilValue().iterator();
            if ( iterator.hasNext() ) {
                nillValue = iterator.next().getValue();
            }
        }
        result.add( new DataRecordDefinition( field.getName(), nillValue ) );
    }

    private static Result maptTo(List<String[]> values) {
        Result result = null;
        StringWriter w = new StringWriter();
        CSVWriter csvWriter = new CSVWriter( w, TOKEN_SEPARATOR_CHAR, CSVWriter.NO_QUOTE_CHARACTER, BLOCK_SEPARATOR );
        try {
            csvWriter.writeAll( values );

            TextEncodingType te = SWE_OF.createTextEncodingType();
            te.setBlockSeparator( BLOCK_SEPARATOR );
            te.setDecimalSeparator( DECIMAL_SEPARATOR );
            te.setTokenSeparator( TOKEN_SEPARATOR );

            List<Object> blob = new ArrayList<>();
            blob.add( w.toString() );

            result = new Result( te, blob );
        }
        finally {
            try {
                csvWriter.close();
                w.close();
            }
            catch ( IOException e ) {
                throw new IllegalStateException( e );
            }
        }
        return result;
    }

    private static <T extends DataRowMapper> List<T> init(Map<String, T> map, String fileName) {
        List<DataRecordDefinition> entries = readDataRecordDefinition( fileName );
        List<T> result = new ArrayList<>();
        for ( DataRecordDefinition entry : entries ) {
            T mapping = map.get( entry.getRecordName() );
            if ( mapping != null ) {
                mapping.setNillValue( entry.getRecordNilValue() );
                result.add( mapping );
            }
            else {
                LOG.warn( "entry: " + entry.getRecordName() + "not present as defined by: " + fileName );
            }
        }
        return result;
    }

}
