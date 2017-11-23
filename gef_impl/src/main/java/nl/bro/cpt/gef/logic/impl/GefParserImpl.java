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

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import org.antlr.v4.runtime.ANTLRInputStream;
import org.antlr.v4.runtime.CommonTokenStream;
import org.antlr.v4.runtime.ConsoleErrorListener;
import org.antlr.v4.runtime.ParserRuleContext;
import org.antlr.v4.runtime.tree.ParseTreeWalker;
import org.apache.log4j.Logger;

import nl.bro.cpt.gef.dto.GefFile;
import nl.bro.cpt.gef.logic.GefUnmarshaller;
import nl.bro.gef.antlr.GefLexer;
import nl.bro.gef.antlr.GefParser;

public class GefParserImpl implements GefUnmarshaller {

    private static final long serialVersionUID = 393759468690621416L;
    private static final Logger LOG = Logger.getLogger( GefParserImpl.class );


    @Override
    public GefFile unmarshall( String fileName, byte[] fileData, String transactionType ) {

        LOG.debug( "unmarshall: " + fileName );

        try {
            InputStream source = new ByteArrayInputStream( fileData );
            InputStreamReader isr = new InputStreamReader( source, "cp1252" );
            ANTLRInputStream is = new ANTLRInputStream( isr );

            GefLexer lexer = new GefLexer( is );
            lexer.removeErrorListener( ConsoleErrorListener.INSTANCE );

            CommonTokenStream tokens = new CommonTokenStream( lexer );
            GefParser parser = new GefParser( tokens );
            GefParserErrorListener errorListener = new GefParserErrorListener();
            parser.addErrorListener( errorListener );

            // parse
            ParserRuleContext tree = parser.file();

            // create standard walker
            ParseTreeWalker walker = new ParseTreeWalker();

            GefFileLoader listener = new GefFileLoader();
            walker.walk( listener, tree );

            listener.getGefFile().getParseErrors().addAll( errorListener.getErrorMessages() );

            GefFile gefFile = listener.getGefFile();
            gefFile.setFileName( fileName );
            gefFile.setTransactionType( transactionType );
            return gefFile;
        }
        catch ( IOException io ) {
            throw new IllegalStateException( io );
        }
    }
}
