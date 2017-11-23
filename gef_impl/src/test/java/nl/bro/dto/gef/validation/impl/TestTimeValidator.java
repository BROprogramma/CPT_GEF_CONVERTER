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

import static org.fest.assertions.Assertions.assertThat;

import java.util.Set;

import javax.validation.ConstraintViolation;
import javax.validation.Validator;
import javax.validation.groups.Default;

import org.junit.Test;

import nl.bro.dto.gef.validation.support.GefValidation;
import nl.bro.dto.gef.validation.TimeValid;

public class TestTimeValidator {

    private static class TimeBean {

        @TimeValid
        private String time;

        public void setTime(String time) {
            this.time = time;
        }
    }

    @Test
    public void testValidTime() {
        TimeBean bean = new TimeBean();
        bean.setTime( "12, 00, 00" );

        Validator validator = GefValidation.buildFactory().getValidator();
        Set<ConstraintViolation<TimeBean>> cvs = validator.validate( bean, Default.class );

        assertThat( cvs ).isEmpty();
    }

    @Test
    public void testInvalidTime1() {
        TimeBean bean = new TimeBean();
        bean.setTime( "1, 0, 0" );

        Validator validator = GefValidation.buildFactory().getValidator();
        Set<ConstraintViolation<TimeBean>> cvs = validator.validate( bean, Default.class );

        assertThat( cvs.size() ).isEqualTo( 1 );
    }

    @Test
    public void testInvalidTime2() {
        TimeBean bean = new TimeBean();
        bean.setTime( "12, 00" );

        Validator validator = GefValidation.buildFactory().getValidator();
        Set<ConstraintViolation<TimeBean>> cvs = validator.validate( bean, Default.class );

        assertThat( cvs.size() ).isEqualTo( 1 );
    }

    @Test
    public void testInvalidTime3() {
        TimeBean bean = new TimeBean();
        bean.setTime( "12" );

        Validator validator = GefValidation.buildFactory().getValidator();
        Set<ConstraintViolation<TimeBean>> cvs = validator.validate( bean, Default.class );

        assertThat( cvs.size() ).isEqualTo( 1 );
    }

    @Test
    public void testInvalidTime4() {
        TimeBean bean = new TimeBean();
        bean.setTime( "zomaar, een, testje" );

        Validator validator = GefValidation.buildFactory().getValidator();
        Set<ConstraintViolation<TimeBean>> cvs = validator.validate( bean, Default.class );

        assertThat( cvs.size() ).isEqualTo( 1 );
    }

    @Test
    public void testInvalidTime5() {
        TimeBean bean = new TimeBean();
        bean.setTime( "01, 00, 0" );

        Validator validator = GefValidation.buildFactory().getValidator();
        Set<ConstraintViolation<TimeBean>> cvs = validator.validate( bean, Default.class );

        assertThat( cvs.size() ).isEqualTo( 1 );
    }

    @Test
    public void testInvalidTime6() {
        TimeBean bean = new TimeBean();
        bean.setTime( "24, 00, 00" );

        Validator validator = GefValidation.buildFactory().getValidator();
        Set<ConstraintViolation<TimeBean>> cvs = validator.validate( bean, Default.class );

        assertThat( cvs.size() ).isEqualTo( 1 );
    }

    @Test
    public void testInvalidTime7() {
        TimeBean bean = new TimeBean();
        bean.setTime( "12, 61, 00" );

        Validator validator = GefValidation.buildFactory().getValidator();
        Set<ConstraintViolation<TimeBean>> cvs = validator.validate( bean, Default.class );

        assertThat( cvs.size() ).isEqualTo( 1 );
    }

    @Test
    public void testInvalidTime8() {
        TimeBean bean = new TimeBean();
        bean.setTime( "12, 00, 61" );

        Validator validator = GefValidation.buildFactory().getValidator();
        Set<ConstraintViolation<TimeBean>> cvs = validator.validate( bean, Default.class );

        assertThat( cvs.size() ).isEqualTo( 1 );
    }

}
