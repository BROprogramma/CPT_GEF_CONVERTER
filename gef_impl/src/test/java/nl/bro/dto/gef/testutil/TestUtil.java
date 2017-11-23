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
package nl.bro.dto.gef.testutil;

import static org.junit.Assert.fail;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import javax.xml.XMLConstants;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBElement;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;
import javax.xml.bind.ValidationEvent;
import javax.xml.bind.ValidationEventHandler;
import javax.xml.transform.stream.StreamSource;
import javax.xml.validation.Schema;
import javax.xml.validation.SchemaFactory;

import org.junit.Assert;
import org.xml.sax.SAXException;

import nl.broservices.xsd.iscpt.v_1_1.ObjectFactory;

import junit.framework.AssertionFailedError;

public class TestUtil {

    private static final ObjectFactory CPT_IS_OF = new ObjectFactory();
    private static final net.opengis.sa.v_2_0.ObjectFactory SAM_OF = new net.opengis.sa.v_2_0.ObjectFactory();

    private static final JAXBContext CPT_JAXB_CTX = getJaxbContext();

    private static JAXBContext getJaxbContext() {
        try {
            return JAXBContext.newInstance( new Class[] { CPT_IS_OF.getClass(), SAM_OF.getClass() } );
        }
        catch ( JAXBException ex ) {
            throw new RuntimeException( ex );
        }
    }

    private TestUtil() {
        // Private constructor - deze class wordt nooit geinstantieerd!
    }

    public static byte[] getFileBytes(String filename) throws IOException {

        InputStream is = TestUtil.class.getResourceAsStream( filename );
        List<Byte> content = new ArrayList<>();

        if ( is == null ) {
            Assert.fail( "File: " + filename + " cannot be opened" );
        }

        int bt;
        while ( -1 != ( bt = is.read() ) ) {
            content.add( (byte) bt );
        }

        byte[] result = new byte[content.size()];
        for ( int i = 0; i < content.size(); ++i ) {
            result[i] = content.get( i );
        }

        return result;
    }

    public static Schema getSchema(String schemaName) {
        Schema schema = null;
        URL schemaUrl = TestUtil.class.getResource( schemaName );
        SchemaFactory factory = SchemaFactory.newInstance( XMLConstants.W3C_XML_SCHEMA_NS_URI );
        try {
            factory.setResourceResolver( new JaxWsCatalogResolver() );
            schema = factory.newSchema( schemaUrl );
        }
        catch ( SAXException | IOException | RuntimeException ex ) {
            fail();
        }
        return schema;
    }

    public static <T> T fetchXml(String file, Class<T> type) {

        try {
            Unmarshaller unmarshaller = CPT_JAXB_CTX.createUnmarshaller();
            unmarshaller.setEventHandler( new TestValidationEventHandler() );
            StreamSource xmlSource = new StreamSource( new InputStreamReader( TestUtil.class.getResourceAsStream( file ) ) );
            JAXBElement<T> element = unmarshaller.unmarshal( xmlSource, type );
            return element.getValue();
        }
        catch ( JAXBException ex ) {
           throw new AssertionFailedError();
        }
    }

    public static class TestValidationEventHandler implements ValidationEventHandler {

        private final List<ValidationEvent> events = new ArrayList<>();

        @Override
        public boolean handleEvent(ValidationEvent event) {
            events.add( event );
            return true;
        }

        public List<ValidationEvent> getEvents() {
            return events;
        }

    }
}
