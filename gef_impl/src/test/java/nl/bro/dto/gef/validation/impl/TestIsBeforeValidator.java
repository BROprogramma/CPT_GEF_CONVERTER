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
import nl.bro.dto.gef.validation.IsBefore;

public class TestIsBeforeValidator {

    @IsBefore( future = "${future}", past = "${past}" )
    public class IsBeforeBean {

        private String future;
        private String past;

        public String getFuture() {
            return future;
        }

        public void setFuture(String future) {
            this.future = future;
        }

        public String getPast() {
            return past;
        }

        public void setPast(String past) {
            this.past = past;
        }

    }

    @Test
    public void testIsBeforeValid1() {
        IsBeforeBean bean = new IsBeforeBean();

        bean.setFuture( "2100, 01, 01" );
        bean.setPast( "2000, 01, 01" );

        Validator validator = GefValidation.buildFactory().getValidator();
        Set<ConstraintViolation<IsBeforeBean>> cvs = validator.validate( bean, Default.class );

        assertThat( cvs ).isEmpty();
    }

    @Test
    public void testIsBeforeValid2() {
        IsBeforeBean bean = new IsBeforeBean();

        bean.setFuture( "2100, 1, 1" );
        bean.setPast( "2000, 01, 01" );

        Validator validator = GefValidation.buildFactory().getValidator();
        Set<ConstraintViolation<IsBeforeBean>> cvs = validator.validate( bean, Default.class );

        assertThat( cvs ).isEmpty();
    }

    @Test
    public void testIsBeforeValid3() {
        IsBeforeBean bean = new IsBeforeBean();

        bean.setFuture( "2100, 01, 01" );
        bean.setPast( "2000, 1, 1" );

        Validator validator = GefValidation.buildFactory().getValidator();
        Set<ConstraintViolation<IsBeforeBean>> cvs = validator.validate( bean, Default.class );

        assertThat( cvs ).isEmpty();
    }

    @Test
    public void testIsBeforeValid4() {
        IsBeforeBean bean = new IsBeforeBean();

        bean.setFuture( "2100, 1, 1" );
        bean.setPast( "2000, 1, 1" );

        Validator validator = GefValidation.buildFactory().getValidator();
        Set<ConstraintViolation<IsBeforeBean>> cvs = validator.validate( bean, Default.class );

        assertThat( cvs ).isEmpty();
    }

    @Test
    public void testIsBeforeValid5() {
        IsBeforeBean bean = new IsBeforeBean();

        bean.setFuture( "2000, 01, 01" );
        bean.setPast( "2000, 01, 01" );

        Validator validator = GefValidation.buildFactory().getValidator();
        Set<ConstraintViolation<IsBeforeBean>> cvs = validator.validate( bean, Default.class );

        assertThat( cvs ).isEmpty();
    }

    @Test
    public void testIsBeforeInValid1() {
        IsBeforeBean bean = new IsBeforeBean();

        bean.setFuture( "2000, 01, 01" );
        bean.setPast( "2100, 01, 01" );

        Validator validator = GefValidation.buildFactory().getValidator();
        Set<ConstraintViolation<IsBeforeBean>> cvs = validator.validate( bean, Default.class );

        assertThat( cvs.size() ).isEqualTo( 1 );
    }
}
