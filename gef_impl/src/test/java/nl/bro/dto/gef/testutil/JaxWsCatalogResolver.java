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

import java.io.IOException;
import java.io.InputStream;
import java.io.Reader;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLStreamHandler;
import javax.xml.parsers.SAXParserFactory;
import org.apache.log4j.Logger;
import org.apache.xml.resolver.Catalog;
import org.apache.xml.resolver.CatalogManager;
import org.apache.xml.resolver.readers.OASISXMLCatalogReader;
import org.apache.xml.resolver.readers.SAXCatalogReader;
import org.w3c.dom.ls.LSInput;
import org.w3c.dom.ls.LSResourceResolver;

public class JaxWsCatalogResolver implements LSResourceResolver {

    private static final Logger LOG = Logger.getLogger( JaxWsCatalogResolver.class.getName() );

    private Catalog catalog = null;

    public JaxWsCatalogResolver() throws IOException {
        catalog = new Catalog( CatalogManager.getStaticManager() );
        attachReaderToCatalog( catalog );
        ClassLoader loader = Thread.currentThread().getContextClassLoader();
        URL catalogUrl = loader.getResource( "META-INF/jax-ws-catalog.xml" );
        catalog.parseCatalog( catalogUrl );
    }

    @Override
    public LSInput resolveResource( String type, String namespaceURI, String publicId, String systemId,
            String baseURI ) {

        LOG.debug( String.format( "resolving, type: %s, namespaceURI:%s, publicId: %s, systemId: %s, baseURI: %s",
                type, namespaceURI, publicId, systemId, baseURI ) );
        String resolvedId = null;
        try {
            if ( systemId != null && !systemId.contains( "/" ) ) {
                // this is most likely only a file, must be an import
                if ( baseURI != null ) {
                    int lastIndexOfSlash = baseURI.lastIndexOf( "/" );
                    resolvedId = baseURI.substring( 0, lastIndexOfSlash + 1 ) + systemId;
                }
            }
            else {
                resolvedId = catalog.resolveSystem( systemId );
            }
        }
        catch ( IOException ex ) {
            LOG.trace( ex );
        }

        if ( resolvedId != null ) {
            return new LSInputImpl( publicId, resolvedId, baseURI );
        }
        return null;
    }

    private void attachReaderToCatalog( Catalog catalog ) {
        SAXParserFactory spf = SAXParserFactory.newInstance();
        spf.setNamespaceAware( true );
        spf.setValidating( false );

        SAXCatalogReader saxReader = new SAXCatalogReader( spf );
        saxReader.setCatalogParser( OASISXMLCatalogReader.namespaceName, "catalog",
                "org.apache.xml.resolver.readers.OASISXMLCatalogReader" );
        catalog.addReader( "application/xml", saxReader );
    }

    public static class LSInputImpl implements LSInput {

        private String publicId;
        private String systemId;
        private String baseSystemId;

        private InputStream byteStream = null;
        private Reader charStream = null;
        private String data = null;

        private String encoding = null;

        private boolean certifiedText = false;

        public LSInputImpl( String publicId, String systemId, String baseSystemId ) {
            try {
                this.publicId = publicId;
                this.systemId = systemId;
                this.baseSystemId = baseSystemId;
                ClassLoader loader = Thread.currentThread().getContextClassLoader();
                URL url = new URL( null, systemId, new ClassPathURLHandler( loader ) );
                this.byteStream = url.openStream();
            }
            catch ( MalformedURLException ex ) {
                LOG.trace( ex );

            }
            catch ( IOException ex ) {
                LOG.trace( ex );
            }
        }

        @Override
        public InputStream getByteStream() {
            return byteStream;
        }

        @Override
        public void setByteStream( InputStream byteStream ) {
            this.byteStream = byteStream;
        }

        @Override
        public Reader getCharacterStream() {
            return charStream;
        }

        @Override
        public void setCharacterStream( Reader characterStream ) {
            this.charStream = characterStream;
        }

        @Override
        public String getStringData() {
            return data;
        }

        @Override
        public void setStringData( String stringData ) {
            this.data = stringData;
        }

        @Override
        public String getEncoding() {
            return encoding;
        }

        @Override
        public void setEncoding( String encoding ) {
            this.encoding = encoding;
        }

        @Override
        public String getPublicId() {
            return publicId;
        }

        @Override
        public void setPublicId( String publicId ) {
            this.publicId = publicId;
        }

        @Override
        public String getSystemId() {
            return systemId;
        }

        @Override
        public void setSystemId( String systemId ) {
            this.systemId = systemId;
        }

        @Override
        public String getBaseURI() {
            return baseSystemId;
        }

        @Override
        public void setBaseURI( String baseURI ) {
            baseSystemId = baseURI;
        }

        @Override
        public boolean getCertifiedText() {
            return certifiedText;
        }

        @Override
        public void setCertifiedText( boolean certifiedText ) {
            this.certifiedText = certifiedText;
        }

    }

    public static class ClassPathURLHandler extends URLStreamHandler {

        private final ClassLoader classLoader;

        public ClassPathURLHandler() {
            this.classLoader = getClass().getClassLoader();
        }

        public ClassPathURLHandler( ClassLoader classLoader ) {
            this.classLoader = classLoader;
        }

        @Override
        protected URLConnection openConnection( URL u ) throws IOException {
            String resource = u.getPath();
            resource = !resource.startsWith( "/" ) ? resource : resource.substring( 1, resource.length() );
            final URL resourceUrl = classLoader.getResource( resource );
            LOG.debug( "Trying to Open resource: " + resource );
            return resourceUrl.openConnection();
        }
    }

}
