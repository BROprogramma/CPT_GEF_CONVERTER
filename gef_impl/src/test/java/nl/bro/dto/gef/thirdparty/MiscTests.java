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
package nl.bro.dto.gef.thirdparty;

import static org.fest.assertions.Assertions.assertThat;

import org.junit.Test;

/**
 * Class is primair bedoeld voor functionaliteits testjes op bestaande 3rd party componenten.
 *
 * @author ommerenjmv
 */
public class MiscTests {

    @Test
    public void testStringSplitter() {

        String[] splittedValue = ( new String( ", tekst,nog een tekst" ) ).split( "," );

        assertThat( splittedValue ).isNotNull();
        assertThat( splittedValue.length ).isEqualTo( 3 );
        assertThat( splittedValue[0] ).isEqualTo( "" );
        assertThat( splittedValue[1] ).isEqualTo( " tekst" );
        assertThat( splittedValue[2] ).isEqualTo( "nog een tekst" );

    }
}
