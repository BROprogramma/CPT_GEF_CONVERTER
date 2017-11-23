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
import nl.bro.dto.gef.validation.IncompleteDateValid;

public class TestIncompleteDateValidator {

    private static class IncompleteDateBean {

        @IncompleteDateValid
        private String incompleteDate;

        public void setIncompleteDate(String incompleteDate) {
            this.incompleteDate = incompleteDate;
        }

    }

    @Test
    public void testValidIncompleteDate1() {

        IncompleteDateBean bean = new IncompleteDateBean();
        bean.setIncompleteDate( "2015, 01, 01" );

        Validator validator = GefValidation.buildFactory().getValidator();
        Set<ConstraintViolation<IncompleteDateBean>> cvs = validator.validate( bean, Default.class );

        assertThat( cvs ).isEmpty();
    }

    @Test
    public void testInvalidIncompleteDate6() {

        IncompleteDateBean bean = new IncompleteDateBean();
        bean.setIncompleteDate( "2015, 01" );

        Validator validator = GefValidation.buildFactory().getValidator();
        Set<ConstraintViolation<IncompleteDateBean>> cvs = validator.validate( bean, Default.class );

        assertThat( cvs.size() ).isEqualTo( 1 );
    }

    @Test
    public void testValidIncompleteDate2() {

        IncompleteDateBean bean = new IncompleteDateBean();
        bean.setIncompleteDate( "2015, 01, -" );

        Validator validator = GefValidation.buildFactory().getValidator();
        Set<ConstraintViolation<IncompleteDateBean>> cvs = validator.validate( bean, Default.class );

        assertThat( cvs ).isEmpty();
    }

    @Test
    public void testValidIncompleteDate3() {

        IncompleteDateBean bean = new IncompleteDateBean();
        bean.setIncompleteDate( "2015,01," );

        Validator validator = GefValidation.buildFactory().getValidator();
        Set<ConstraintViolation<IncompleteDateBean>> cvs = validator.validate( bean, Default.class );

        assertThat( cvs ).isEmpty();
    }

    @Test
    public void testValidIncompleteDate4() {

        IncompleteDateBean bean = new IncompleteDateBean();
        bean.setIncompleteDate( "2015,-," );

        Validator validator = GefValidation.buildFactory().getValidator();
        Set<ConstraintViolation<IncompleteDateBean>> cvs = validator.validate( bean, Default.class );

        assertThat( cvs ).isEmpty();
    }

    @Test
    public void testValidIncompleteDate5() {

        IncompleteDateBean bean = new IncompleteDateBean();
        bean.setIncompleteDate( "2015,01, " );

        Validator validator = GefValidation.buildFactory().getValidator();
        Set<ConstraintViolation<IncompleteDateBean>> cvs = validator.validate( bean, Default.class );

        assertThat( cvs ).isEmpty();
    }

    @Test
    public void testInvalidIncompleteDate7() {

        IncompleteDateBean bean = new IncompleteDateBean();
        bean.setIncompleteDate( "2015,-,01" );

        Validator validator = GefValidation.buildFactory().getValidator();
        Set<ConstraintViolation<IncompleteDateBean>> cvs = validator.validate( bean, Default.class );

        assertThat( cvs.size() ).isEqualTo( 1 );
    }

    @Test
    public void testInvalidIncompleteDate5() {

        IncompleteDateBean bean = new IncompleteDateBean();
        bean.setIncompleteDate( "2015" );

        Validator validator = GefValidation.buildFactory().getValidator();
        Set<ConstraintViolation<IncompleteDateBean>> cvs = validator.validate( bean, Default.class );

        assertThat( cvs.size() ).isEqualTo( 1 );
    }

    @Test
    public void testInvalidIncompleteDate1() {

        IncompleteDateBean bean = new IncompleteDateBean();
        bean.setIncompleteDate( "" );

        Validator validator = GefValidation.buildFactory().getValidator();
        Set<ConstraintViolation<IncompleteDateBean>> cvs = validator.validate( bean, Default.class );

        assertThat( cvs.size() ).isEqualTo( 1 );
    }

    @Test
    public void testInvalidIncompleteDate2() {

        IncompleteDateBean bean = new IncompleteDateBean();
        bean.setIncompleteDate( "zomaar een tekstje" );

        Validator validator = GefValidation.buildFactory().getValidator();
        Set<ConstraintViolation<IncompleteDateBean>> cvs = validator.validate( bean, Default.class );

        assertThat( cvs.size() ).isEqualTo( 1 );
    }

    @Test
    public void testNotSoInvalidIncompleteDate3() {

        IncompleteDateBean bean = new IncompleteDateBean();
        bean.setIncompleteDate( "2015, 01, 1" );

        Validator validator = GefValidation.buildFactory().getValidator();
        Set<ConstraintViolation<IncompleteDateBean>> cvs = validator.validate( bean, Default.class );

        assertThat( cvs ).isEmpty();
    }

    @Test
    public void testNotSoInvalidIncompleteDate4() {

        IncompleteDateBean bean = new IncompleteDateBean();
        bean.setIncompleteDate( "2015, 1, 01" );

        Validator validator = GefValidation.buildFactory().getValidator();
        Set<ConstraintViolation<IncompleteDateBean>> cvs = validator.validate( bean, Default.class );

        assertThat( cvs ).isEmpty();
    }

}
