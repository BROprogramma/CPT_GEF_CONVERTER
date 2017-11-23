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
package nl.bro.dto.gef.validation.support;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * @author derksenjpam
 */
public class ValidationUtil {

    private ValidationUtil() {
    }

    public static Date parseDate(String in) {
        if ( in != null ) {
            String[] elements = in.split( "," );

            if ( elements.length == 3 ) {

                StringBuilder format = new StringBuilder();
                StringBuilder value = new StringBuilder();
                try {
                    addElementToDate( format, value, elements, 0, "yyyy" );
                    addElementToDate( format, value, elements, 1, "MM" );
                    addElementToDate( format, value, elements, 2, "dd" );
                    SimpleDateFormat dateFormat = new SimpleDateFormat( "yyyyMMdd" );

                    dateFormat.setLenient( false );
                    return dateFormat.parse( value.toString() );
                }
                catch ( ParseException ex ) {
                    return null;
                }
            }
        }
        return null;
    }

    public static Date parseIncompleteDate(String in) {
        if ( in != null ) {
            StringBuilder format = new StringBuilder();
            StringBuilder value = new StringBuilder();
            String[] elements = in.split( "," );

            try {
                addElementToDate( format, value, elements, 0, "yyyy" );
                addElementToDate( format, value, elements, 1, "MM" );
                addElementToDate( format, value, elements, 2, "dd" );
                validateFormatString( format );

                if ( elements.length == 3 || ( elements.length == 2 && in.endsWith( "," ) ) ) {
                    SimpleDateFormat dateFormat = new SimpleDateFormat( format.toString() );
                    dateFormat.setLenient( false );
                    return dateFormat.parse( value.toString() );
                }
            }
            catch ( ParseException ex ) {
                return null;
            }
        }
        return null;
    }

    private static void validateFormatString(StringBuilder format) throws ParseException {

        String formatStr = format.toString();
        String formatToCompare = "";
        if ( formatStr.length() >= 4 ) {
            formatToCompare += "yyyy";
        }
        if ( formatStr.length() >= 6 ) {
            formatToCompare += "MM";
        }
        if ( formatStr.length() == 8 ) {
            formatToCompare += "dd";
        }

        if ( !formatToCompare.equals( formatStr ) ) {
            throw new ParseException( "invalid format string", 0 );
        }
    }

    private static void addElementToDate(StringBuilder format, StringBuilder value, String[] elements, int i, String formatElement) throws ParseException {

        if ( elements == null || i >= elements.length || elements[i].trim().isEmpty() || "-".equals( elements[i].trim() ) ) {
            return;
        }

        String element = elements[i].trim();
        if ( element.length() > formatElement.length() ) {
            throw new ParseException( "invalid element length", i );
        }

        while ( element.length() < formatElement.length() ) {
            element = "0" + element;
        }
        value.append( element );
        format.append( formatElement );
    }

    public static Date parseTime(String in) {
        if ( in != null ) {
            String[] elements = in.split( "," );

            if ( elements.length == 3 ) {
                StringBuilder value = new StringBuilder();
                if ( elements[0].trim().length() != 2 || elements[1].trim().length() != 2 || elements[2].trim().length() != 2 ) {
                    return null;
                }
                value.append( elements[0].trim() );
                value.append( elements[1].trim() );
                value.append( elements[2].trim() );
                SimpleDateFormat dateFormat = new SimpleDateFormat( "HHmmss" );
                dateFormat.setLenient( false );
                try {
                    return dateFormat.parse( value.toString() );
                }
                catch ( ParseException ex ) {
                    return null;
                }
            }
        }
        return null;
    }

}
