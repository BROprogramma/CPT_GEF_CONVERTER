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

import nl.bro.cpt.gef.dto.DataRowDiss;
import nl.bro.dto.gef.validation.support.GefValidation;
import nl.bro.dto.gef.validation.AtLeastOnePorePressureInDTResults;

public class TestAtLeastOnePorePressureInDTResultsValidator {

    private static class TestBean {

        @AtLeastOnePorePressureInDTResults
        private List<DataRowDiss> datarows;

        public void setDatarows(List<DataRowDiss> datarows) {
            this.datarows = datarows;
        }
    }

    @Test
    public void testEmptyList() {

        TestBean bean = new TestBean();
        bean.setDatarows( new ArrayList<DataRowDiss>() );

        Validator validator = GefValidation.buildFactory().getValidator();
        Set<ConstraintViolation<TestBean>> cvs = validator.validate( bean, Default.class );

        assertThat( cvs.size() ).isEqualTo( 1 );
    }

    @Test
    public void testNullList() {

        TestBean bean = new TestBean();

        Validator validator = GefValidation.buildFactory().getValidator();
        Set<ConstraintViolation<TestBean>> cvs = validator.validate( bean, Default.class );

        assertThat( cvs.size() ).isEqualTo( 1 );
    }

    @Test
    public void testEmptyDissipationRow() {

        TestBean bean = new TestBean();
        DataRowDiss drDiss = new DataRowDiss();
        List<DataRowDiss> list = new ArrayList<DataRowDiss>();
        list.add( drDiss );
        bean.setDatarows( list );

        Validator validator = GefValidation.buildFactory().getValidator();
        Set<ConstraintViolation<TestBean>> cvs = validator.validate( bean, Default.class );

        assertThat( cvs.size() ).isEqualTo( 1 );
    }

    @Test
    public void testDissipationRowFilled1() {
        TestBean bean = new TestBean();
        DataRowDiss drDiss = new DataRowDiss();
        drDiss.setDatablockci5( BigDecimal.ONE );
        drDiss.setDatablockci6( BigDecimal.ONE );
        drDiss.setDatablockci7( BigDecimal.ONE );
        List<DataRowDiss> list = new ArrayList<DataRowDiss>();
        list.add( drDiss );
        bean.setDatarows( list );

        Validator validator = GefValidation.buildFactory().getValidator();
        Set<ConstraintViolation<TestBean>> cvs = validator.validate( bean, Default.class );

        assertThat( cvs ).isEmpty();
    }

    @Test
    public void testDissipationRowFilled2() {
        TestBean bean = new TestBean();
        DataRowDiss drDiss = new DataRowDiss();
        drDiss.setDatablockci5( BigDecimal.ONE );
        drDiss.setDatablockci6( BigDecimal.ONE );
        List<DataRowDiss> list = new ArrayList<DataRowDiss>();
        list.add( drDiss );
        bean.setDatarows( list );

        Validator validator = GefValidation.buildFactory().getValidator();
        Set<ConstraintViolation<TestBean>> cvs = validator.validate( bean, Default.class );

        assertThat( cvs ).isEmpty();
    }

    @Test
    public void testDissipationRowFilled3() {
        TestBean bean = new TestBean();
        DataRowDiss drDiss = new DataRowDiss();
        drDiss.setDatablockci5( BigDecimal.ONE );
        List<DataRowDiss> list = new ArrayList<DataRowDiss>();
        list.add( drDiss );
        bean.setDatarows( list );

        Validator validator = GefValidation.buildFactory().getValidator();
        Set<ConstraintViolation<TestBean>> cvs = validator.validate( bean, Default.class );

        assertThat( cvs ).isEmpty();
    }

    @Test
    public void testDissipationRowFilled4() {
        TestBean bean = new TestBean();
        DataRowDiss drDiss = new DataRowDiss();
        drDiss.setDatablockci6( BigDecimal.ONE );
        List<DataRowDiss> list = new ArrayList<DataRowDiss>();
        list.add( drDiss );
        bean.setDatarows( list );

        Validator validator = GefValidation.buildFactory().getValidator();
        Set<ConstraintViolation<TestBean>> cvs = validator.validate( bean, Default.class );

        assertThat( cvs ).isEmpty();
    }

    @Test
    public void testDissipationRowFilled5() {
        TestBean bean = new TestBean();
        DataRowDiss drDiss = new DataRowDiss();
        drDiss.setDatablockci7( BigDecimal.ONE );
        List<DataRowDiss> list = new ArrayList<DataRowDiss>();
        list.add( drDiss );
        bean.setDatarows( list );

        Validator validator = GefValidation.buildFactory().getValidator();
        Set<ConstraintViolation<TestBean>> cvs = validator.validate( bean, Default.class );

        assertThat( cvs ).isEmpty();
    }

    @Test
    public void testDissipationRowFilled6() {
        TestBean bean = new TestBean();
        DataRowDiss drDiss = new DataRowDiss();
        drDiss.setDatablockci6( BigDecimal.ONE );
        drDiss.setDatablockci7( BigDecimal.ONE );
        List<DataRowDiss> list = new ArrayList<DataRowDiss>();
        list.add( drDiss );
        bean.setDatarows( list );

        Validator validator = GefValidation.buildFactory().getValidator();
        Set<ConstraintViolation<TestBean>> cvs = validator.validate( bean, Default.class );

        assertThat( cvs ).isEmpty();
    }

    @Test
    public void testDissipationRowFilled7() {
        TestBean bean = new TestBean();
        DataRowDiss drDiss = new DataRowDiss();
        drDiss.setDatablockci5( BigDecimal.ONE );
        drDiss.setDatablockci7( BigDecimal.ONE );
        List<DataRowDiss> list = new ArrayList<DataRowDiss>();
        list.add( drDiss );
        bean.setDatarows( list );

        Validator validator = GefValidation.buildFactory().getValidator();
        Set<ConstraintViolation<TestBean>> cvs = validator.validate( bean, Default.class );

        assertThat( cvs ).isEmpty();
    }
}
