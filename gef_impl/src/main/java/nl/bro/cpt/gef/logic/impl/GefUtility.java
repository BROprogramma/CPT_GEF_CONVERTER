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

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

import javax.xml.datatype.DatatypeConstants;

import org.apache.log4j.Logger;

public class GefUtility {

    private static final Logger LOG = Logger.getLogger( GefUtility.class );

    private static final Pattern LTRIM = Pattern.compile( "^\\s+" );

    private GefUtility() {
    }

    /**
     * This routine splits the data rows. However, it maintains the record separator attached to each row. Leading
     * characters are trimmed in order to be lenient when the user uses a whitespace character (enters, spaces, tabs)
     * after the record separator.
     *
     * @param rows
     * @param recordSeparator record
     * @return list of rows.
     */
    public static List<String> splitDataRows(String rows, String recordSeparator) {

        int start = 0;
        int stop = rows.indexOf( recordSeparator, start );

        List<String> found = new ArrayList<>();
        while ( stop > -1 ) {
            String sub = rows.substring( start, stop + recordSeparator.length() );
            if ( sub.length() > 0 ) {
                found.add( ltrim( sub ) );
            }
            start = stop + recordSeparator.length();
            stop = rows.indexOf( recordSeparator, start );
        }
        // add the remainder.
        String sub = rows.substring( start );
        if ( sub.length() > 0 ) {
            found.add( ltrim( sub ) );
        }
        return found;
    }

    private static BigDecimal convertValue(String value) {

        if ( value.trim().length() == 0 || "-".equals( value.trim() ) ) {
            return null;
        }

        try {
            return new BigDecimal( value.trim() );
        }
        catch ( NumberFormatException e ) {
            LOG.error( String.format( "Failed to convert value[%s] to BigDecimal - adding NULL value to list", value ) );
        }
        return null;
    }

    public static List<BigDecimal> splitDataItems(String record, String columnSeparator, String recordSeparator, int nrColumns) {

        int start = 0;
        int stop = record.indexOf( columnSeparator, start );

        List<BigDecimal> found = new ArrayList<>();
        while ( stop > -1 ) {
            String sub = record.substring( start, stop );
            if ( sub != null && !sub.endsWith( recordSeparator ) ) {
                found.add( convertValue( sub ) );
            }
            start = stop + columnSeparator.length();
            stop = record.indexOf( columnSeparator, start );
        }

        String sub = record.substring( start );
        if ( sub != null && sub.endsWith( recordSeparator ) && found.size() < nrColumns ) {
            found.add( convertValue( sub.substring( 0, sub.length() - 1 ) ) );
        }

        return found;
    }

    public static int getDateIntValue(String[] values, int index) {

        String value = "";
        if ( values != null && index < values.length ) {
            value = values[index];
            if ( value != null ) {
                value = value.trim();
            }
            else {
                value = "";
            }
        }

        if ( !value.isEmpty() && !"-".equals( value.trim() ) ) {

            try {
                return Integer.valueOf( value.trim() );
            }
            catch ( NumberFormatException e ) {
                LOG.warn( String.format( "Cannot convert String[%s] to integer format", value.trim() ) );
            }
        }

        return DatatypeConstants.FIELD_UNDEFINED;
    }

    /**
     * n GEF the hash (#), the equals sign (=) and the comma (,) have a special meaning. # signals the start of a new
     * code word, = signals the end of a code word while the comma separates two fields of information. Therefore it is
     * not possible to use these signs as part of a field without special precautions. In order to use these characters,
     * they have to be taken literal. The same method as in the Unix operating system is applied: escaping special
     * characters with a backslash. This makes the backslash a special character as well. see artf52528
     *
     * @param str
     * @return
     */
    public static String[] commaDelimitedListToStringArray(String str) {

        // see
        // http://stackoverflow.com/questions/820172/how-to-split-a-comma-separated-string-while-ignoring-escaped-commas
        if ( !str.contains( "\\" ) ) {
            return str.split( "," );
        }

        String[] temp = str.split( "(?<!\\\\),", -1 );

        // remove the escapeChar for the end result
        String[] result = new String[temp.length];
        for ( int i = 0; i < temp.length; i++ ) {
            result[i] = temp[i].replaceAll( "\\\\,", "," ).replaceAll( "\\\\\\#", "#" ).replaceAll( "\\\\=", "=" );
        }

        return result;
    }

    /**
     * Left trim method
     *
     * @param s in
     * @return left-trimmed string (whitespace characters such as tabs, crlf, spaces and the likes are removed from the
     *         beginning of the string.
     */
    private static String ltrim(String s) {
        return LTRIM.matcher( s ).replaceAll( "" );
    }

}
