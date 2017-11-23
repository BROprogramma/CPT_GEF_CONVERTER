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

import org.junit.Test;

import nl.bro.cpt.gef.dto.GefCptFile;

public class ConeResistanceZeroLoadMeasurementsValidatorTest {

    @Test
    public void testValidatorCptNull() {

        ConeResistanceZeroLoadMeasurementsValidator validator = new ConeResistanceZeroLoadMeasurementsValidator();

        boolean result = validator.isValid( null, null );

        assertThat( result ).isEqualTo( true );
    }

    @Test
    public void testValidatorAllMeaNull() {

        ConeResistanceZeroLoadMeasurementsValidator validator = new ConeResistanceZeroLoadMeasurementsValidator();
        GefCptFile cpt = new GefCptFile();

        boolean result = validator.isValid( cpt, null );

        assertThat( result ).isEqualTo( true );
    }

    @Test
    public void testValidatorMea20NotNull() {

        ConeResistanceZeroLoadMeasurementsValidator validator = new ConeResistanceZeroLoadMeasurementsValidator();
        GefCptFile cpt = new GefCptFile();

        cpt.setMeasurementVar20( BigDecimal.ZERO );

        boolean result = validator.isValid( cpt, null );

        assertThat( result ).isEqualTo( false );
    }

    @Test
    public void testValidatorMea21NotNull() {

        ConeResistanceZeroLoadMeasurementsValidator validator = new ConeResistanceZeroLoadMeasurementsValidator();
        GefCptFile cpt = new GefCptFile();

        cpt.setMeasurementVar21( BigDecimal.ZERO );

        boolean result = validator.isValid( cpt, null );

        assertThat( result ).isEqualTo( false );
    }
}
