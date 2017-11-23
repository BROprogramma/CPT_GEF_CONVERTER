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
package nl.bro.dto.gef.validation.impl;

import nl.bro.dto.gef.validation.support.ValidationUtil;

import static org.fest.assertions.Assertions.assertThat;

import java.util.Date;

import org.junit.Test;

public class ValidationUtilTest {

    @Test
    public void testValidDateInNull() {

        Date result = ValidationUtil.parseDate( null );
        assertThat( result ).isNull();
    }

    @Test
    public void testValidDateInIncomplete() {

        Date result = ValidationUtil.parseDate( "2000, 12" );
        assertThat( result ).isNull();
    }

    @Test
    public void testValidIncompleteDateInNull() {

        Date result = ValidationUtil.parseIncompleteDate( null );
        assertThat( result ).isNull();
    }

    @Test
    public void testValidIncompleteDateOvercomplete() {

        Date result = ValidationUtil.parseIncompleteDate( "2000, 12, 1, 12" );
        assertThat( result ).isNull();
    }

    @Test
    public void testValidTimeInNull() {

        Date result = ValidationUtil.parseTime( null );
        assertThat( result ).isNull();
    }

    @Test
    public void testValidTimeIncomplete() {

        Date result = ValidationUtil.parseTime( "12, 0" );
        assertThat( result ).isNull();
    }

    @Test
    public void testValidTimeParseError() {

        Date result = ValidationUtil.parseTime( "100, 100, 100" );
        assertThat( result ).isNull();
    }

    @Test
    public void testValidDateParseError() {

        Date result = ValidationUtil.parseDate( "123456, 123456, 123456" );
        assertThat( result ).isNull();
    }

    @Test
    public void testValidDateParseError2() {

        Date result = ValidationUtil.parseDate( "2015, 12, 2" );
        assertThat( result ).isNotNull();
    }

}
