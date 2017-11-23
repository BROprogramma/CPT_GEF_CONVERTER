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

import java.io.InputStream;

import org.antlr.v4.runtime.ANTLRInputStream;
import org.antlr.v4.runtime.CommonTokenStream;
import org.antlr.v4.runtime.ParserRuleContext;
import org.antlr.v4.runtime.tree.ParseTreeWalker;

import nl.bro.cpt.gef.logic.impl.GefParserErrorListener;
import nl.bro.gef.antlr.GefLexer;
import nl.bro.gef.antlr.GefParser;

public class ParseAndDumpGefFile {

    private ParseAndDumpGefFile() {

    }

    public static void main(String[] args) throws Exception {

        InputStream source = Object.class.getResourceAsStream( "/20150925vbCPT1.GEF" );
        ANTLRInputStream is = new ANTLRInputStream( source );

        GefLexer lexer = new GefLexer( is );
        CommonTokenStream tokens = new CommonTokenStream( lexer );
        GefParser parser = new GefParser( tokens );

        GefParserErrorListener errorListener = new GefParserErrorListener();
        parser.addErrorListener( errorListener );

        ParserRuleContext tree = parser.file(); // parse

        ParseTreeWalker walker = new ParseTreeWalker(); // create standard walker

        walker.walk( new GefFileDumper(), tree );
    }
}
