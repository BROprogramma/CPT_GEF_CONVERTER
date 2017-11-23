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
package nl.bro.cpt.gef.transform;

import static org.fest.assertions.Assertions.assertThat;

import java.math.BigDecimal;

import org.junit.Test;

public class TruncationMapperTest {


    @Test
    public void testTruncTo0Decimals() {

        BigDecimal seed = null;
        BigDecimal plant = TruncationMapper.toBigDecimal0( seed );
        assertThat( plant ).isNull();

        seed = BigDecimal.ONE;
        plant = TruncationMapper.toBigDecimal0( seed );
        assertThat( plant ).isEqualTo( BigDecimal.ONE );

        seed = new BigDecimal( "1.000" );
        plant = TruncationMapper.toBigDecimal0( seed );
        assertThat( plant ).isEqualTo( BigDecimal.ONE );
    }

    @Test
    public void testTruncTo1Decimal() {

        BigDecimal seed = null;
        BigDecimal plant = TruncationMapper.toBigDecimal1( seed );
        assertThat( plant ).isNull();

        seed = BigDecimal.ONE;
        plant = TruncationMapper.toBigDecimal1( seed );
        assertThat( plant.scale() ).isEqualTo( 1 );
        assertThat( plant.intValue() ).isEqualTo( 1 );

        seed = new BigDecimal( "1.000" );
        plant = TruncationMapper.toBigDecimal1( seed );
        assertThat( plant.scale() ).isEqualTo( 1 );
        assertThat( plant.intValue() ).isEqualTo( 1 );
    }

    @Test
    public void testTruncTo2Decimals() {

        BigDecimal seed = null;
        BigDecimal plant = TruncationMapper.toBigDecimal2( seed );
        assertThat( plant ).isNull();

        seed = BigDecimal.ONE;
        plant = TruncationMapper.toBigDecimal2( seed );
        assertThat( plant.scale() ).isEqualTo( 2 );
        assertThat( plant.intValue() ).isEqualTo( 1 );

        seed = new BigDecimal( "1.000" );
        plant = TruncationMapper.toBigDecimal2( seed );
        assertThat( plant.scale() ).isEqualTo( 2 );
        assertThat( plant.intValue() ).isEqualTo( 1 );
    }

    @Test
    public void testTruncTo3Decimals() {

        BigDecimal seed = null;
        BigDecimal plant = TruncationMapper.toBigDecimal3( seed );
        assertThat( plant ).isNull();

        seed = BigDecimal.ONE;
        plant = TruncationMapper.toBigDecimal3( seed );
        assertThat( plant.scale() ).isEqualTo( 3 );
        assertThat( plant.intValue() ).isEqualTo( 1 );

        seed = new BigDecimal( "1.00000" );
        plant = TruncationMapper.toBigDecimal3( seed );
        assertThat( plant.scale() ).isEqualTo( 3 );
        assertThat( plant.intValue() ).isEqualTo( 1 );
    }
}
