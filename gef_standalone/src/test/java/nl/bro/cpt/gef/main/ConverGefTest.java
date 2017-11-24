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
package nl.bro.cpt.gef.main;

import static org.fest.assertions.Assertions.assertThat;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.LogRecord;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import nl.bro.cpt.gef.main.util.TestHandler;

public class ConverGefTest {

    @Before
    public void init() {
        TestHandler.LOG_RECORDS.clear();
    }

    @After
    public void cleanup() {
        TestHandler.LOG_RECORDS.clear();
    }

    private List<String> getMessages() {
        List<String> messages = new ArrayList<>();
        for ( LogRecord record : TestHandler.LOG_RECORDS ) {
            messages.add( record.getMessage() );
        }
        return messages;
    }

    @Test
    public void testNoArguments() {

        // -- action
        ConvertGef.main( new String[0] );

        // -- verify
        List<String> messages = getMessages();
        assertThat( messages ).contains( "Missing t option", "Missing r option", "Missing q option" );
    }

    @Test
    public void testNonExistingFile() {

        // -- action
        ConvertGef.main( new String[] { "-r", "test", "-t", "R", "-q", "IMBRO" } );
        List<String> messages = getMessages();

        // -- verify
        assertThat( messages.toString() ).contains( "Er kunnen geen GEF files gevonden worden in deze directory." );

    }

    @Test
    public void testNormal() {

        // -- action
        ConvertGef.main(  new String[] { "-r", "test", "-t", "R", "-q", "IMBRO", "-d", "target", "src/test/resources" } );

        // -- verify
        File file = new File( "target/CPT-F3b-i3-23913-completion-CPT.xml");
        assertThat( file.exists() );

        // -- cleanup
        file.delete();
    }
}
