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

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import javax.validation.ConstraintViolation;
import javax.validation.Validator;
import javax.validation.groups.Default;

import org.junit.Test;

import nl.bro.cpt.gef.dto.SpecimenVar;
import nl.bro.dto.gef.validation.SpecimenVarsValid;
import nl.bro.dto.gef.validation.support.GefValidation;

public class TestSpecimenVarsValidator {

    static class TestBean {

        @SpecimenVarsValid
        private List<SpecimenVar> specimenVars;

        public List<SpecimenVar> getSpecimenVars() {
            return specimenVars;
        }

        public void setSpecimenVars(List<SpecimenVar> specimenVars) {
            this.specimenVars = specimenVars;
        }
    }

    @Test
    public void testValidatorWhenIsNull() {

        TestBean bean = new TestBean();

        Validator validator = GefValidation.buildFactory().getValidator();
        Set<ConstraintViolation<TestBean>> cvs = validator.validate( bean, Default.class );

        assertThat( cvs ).isEmpty();
    }

    @Test
    public void testValidatorWhenIsEmpty() {

        TestBean bean = new TestBean();
        bean.setSpecimenVars( new ArrayList<SpecimenVar>() );

        Validator validator = GefValidation.buildFactory().getValidator();
        Set<ConstraintViolation<TestBean>> cvs = validator.validate( bean, Default.class );

        assertThat( cvs ).isEmpty();
    }

    @Test
    public void testValidatorWhenValid() {

        TestBean bean = new TestBean();
        List<SpecimenVar> list = new ArrayList<SpecimenVar>();
        SpecimenVar var = new SpecimenVar();
        var.setBovendiepte( BigDecimal.ONE );
        var.setOnderdiepte( BigDecimal.TEN );
        list.add( var );
        bean.setSpecimenVars( list );

        Validator validator = GefValidation.buildFactory().getValidator();
        Set<ConstraintViolation<TestBean>> cvs = validator.validate( bean, Default.class );

        assertThat( cvs ).isEmpty();
    }

    @Test
    public void testValidatorWhenInvalid() {

        TestBean bean = new TestBean();
        List<SpecimenVar> list = new ArrayList<SpecimenVar>();
        SpecimenVar var = new SpecimenVar();
        var.setBovendiepte( BigDecimal.TEN );
        var.setOnderdiepte( BigDecimal.ONE );
        list.add( var );
        bean.setSpecimenVars( list );

        Validator validator = GefValidation.buildFactory( ).getValidator();
        Set<ConstraintViolation<TestBean>> cvs = validator.validate( bean, Default.class );

        assertThat( cvs.size() ).isEqualTo( 1 );
    }

}
