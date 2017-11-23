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
package nl.bro.cpt.gef.logic.impl;

import static org.fest.assertions.Assertions.assertThat;

import java.math.BigDecimal;
import java.util.List;

import javax.xml.datatype.DatatypeConstants;

import org.junit.Test;

public class TestGefUtility {

    private static final String HAYSTACK1 = "1:2:3!1.1:2.1:2.3:!";
    private static final String HAYSTACK2 = "1:1.1:-: :abc.def:abcd!";

    @Test
    public void testDataRowSplitter() {

        List<String> splitted = GefUtility.splitDataRows( HAYSTACK1, "!" );

        assertThat( splitted ).isNotNull();
        assertThat( splitted.size() ).isEqualTo( 2 );
        assertThat( splitted.get( 0 ) ).isEqualTo( "1:2:3!" );
        assertThat( splitted.get( 1 ) ).isEqualTo( "1.1:2.1:2.3:!" );

    }

    @Test
    public void testDataItemSplitter() {

        List<BigDecimal> values = GefUtility.splitDataItems( HAYSTACK2, ":", "!", 5 );

        assertThat( values ).isNotNull();
        assertThat( values.size() ).isEqualTo( 5 );
        assertThat( values.get( 0 ) ).isEqualTo( new BigDecimal( "1" ) );
        assertThat( values.get( 1 ) ).isEqualTo( new BigDecimal( "1.1" ) );
        assertThat( values.get( 2 ) ).isNull();
        assertThat( values.get( 3 ) ).isNull();
        assertThat( values.get( 4 ) ).isNull();
    }

    @Test
    public void testGetDateIntValue() {

        String[] values = new String[4];
        values[0] = "1";
        values[1] = null;
        values[2] = "";
        values[3] = "-";

        int value = GefUtility.getDateIntValue( values, 0 );
        assertThat( value ).isEqualTo( 1 );
        value = GefUtility.getDateIntValue( values, 1 );
        assertThat( value ).isEqualTo( DatatypeConstants.FIELD_UNDEFINED );
        value = GefUtility.getDateIntValue( values, 2 );
        assertThat( value ).isEqualTo( DatatypeConstants.FIELD_UNDEFINED );
        value = GefUtility.getDateIntValue( values, 3 );
        assertThat( value ).isEqualTo( DatatypeConstants.FIELD_UNDEFINED );
    }

}
