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

import java.util.HashSet;
import java.util.Set;

/**
 * This class verifies whether a character complies to a certain character set. It also takes into consideration UTF-8
 * characters more than 2 octets long. See also {@link http://docs.oracle.com/javase/tutorial/i18n/text/unicode.html}
 * and {@link https://gist.github.com/schierlm/aa37036335528b9b12bb}
 */
public class CharSetCheck {

    private static class CharSet {

        private Set<Character> characters = new HashSet<>();

        /**
         * Adds one character to the set.
         *
         * @param ch the character to add.
         */
        public void add(Character ch) {
            characters.add( ch );
        }

        /**
         * Adds all characters between start/stop to the set including the start and stop character.
         *
         * @param start the start of the character sequence
         * @param stop the stop of the character sequence
         */
        public void addAll(Character start, Character stop) {
            for ( char ch = start.charValue(); ch <= stop.charValue(); ++ch ) {
                characters.add( ch );
            }
        }

        public boolean contains(char ch) {
            return characters.contains( ch );
        }

    }

    private static CharSet mes1CharSet = new CharSet();
    static {
        mes1CharSet.addAll( (char) 0x20, (char) 0x7e );
        mes1CharSet.addAll( (char) 0xa0, (char) 0x113 );
        mes1CharSet.addAll( (char) 0x116, (char) 0x12b );
        mes1CharSet.addAll( (char) 0x12e, (char) 0x14d );
        mes1CharSet.addAll( (char) 0x150, (char) 0x17e );
        mes1CharSet.add( (char) 0x2c7 );
        mes1CharSet.addAll( (char) 0x2d8, (char) 0x2db );
        mes1CharSet.add( (char) 0x2dd );
        mes1CharSet.add( (char) 0x2015 );
        mes1CharSet.addAll( (char) 0x2018, (char) 0x2019 );
        mes1CharSet.addAll( (char) 0x201c, (char) 0x201d );
        mes1CharSet.add( (char) 0x20ac );
        mes1CharSet.add( (char) 0x2122 );
        mes1CharSet.add( (char) 0x2126 );
        mes1CharSet.addAll( (char) 0x215b, (char) 0x215e );
        mes1CharSet.addAll( (char) 0x2190, (char) 0x2193 );
        mes1CharSet.add( (char) 0x266a );
    }

    private static CharSet c0CharSet = new CharSet();
    static {
        c0CharSet.addAll( (char) 0x00, (char) 0x20 );
    }

    private static CharSet asciiCharSet = new CharSet();
    static {
        asciiCharSet.addAll( (char) 0x20, (char) 0x7e );
    }

    private CharSetCheck() {
    }

    public static boolean isValidAscii(CharSequence sequence) {
        for ( int i = sequence.length(); --i >= 0; ) {
            if ( !isValidAscii( sequence.charAt( i ) ) ) {
                return false;
            }
        }
        return true;
    }

    public static boolean isValidMes1(CharSequence sequence) {
        for ( int i = sequence.length(); --i >= 0; ) {
            if ( !isValidMes1( sequence.charAt( i ) ) ) {
                return false;
            }
        }
        return true;
    }

    public static boolean isValidAscii(char ch) {
        return asciiCharSet.contains( ch );
    }

    /**
     * Valid character from control 0 set.
     *
     * @param ch
     * @return true if valid c0 character
     */
    public static boolean isValidC0(char ch) {
        return c0CharSet.contains( ch );
    }

    public static boolean isValidMes1(char ch) {
        return mes1CharSet.contains( ch );
    }
}
