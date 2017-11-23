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

import org.antlr.v4.runtime.tree.ErrorNode;

import nl.bro.gef.antlr.GefParser.DatablockContext;
import nl.bro.gef.antlr.GefParser.DatablockrowContext;
import nl.bro.gef.antlr.GefParser.DatavaluesContext;
import nl.bro.gef.antlr.GefParser.FileContext;
import nl.bro.gef.antlr.GefParser.HeaderContext;
import nl.bro.gef.antlr.GefParser.HeaderrowContext;
import nl.bro.gef.antlr.GefParser.VariabeleContext;
import nl.bro.gef.antlr.GefParser.WaardeContext;
import nl.bro.gef.antlr.GefParserBaseListener;

public class GefFileDumper extends GefParserBaseListener {

    @Override
    public void enterFile(FileContext ctx) {
        System.out.println( "##enterFile## " );
    }

    @Override
    public void exitFile(FileContext ctx) {
        System.out.println( " ##exitFile##" );
    }

    @Override
    public void enterHeader(HeaderContext ctx) {
        System.out.println( "##enterHeader## " );
    }

    @Override
    public void exitHeader(HeaderContext ctx) {
        System.out.println( " ##exitHeader##" );
    }

    @Override
    public void enterDatablock(DatablockContext ctx) {
        System.out.println( "##enterDatablock## " );
    }

    @Override
    public void exitDatablock(DatablockContext ctx) {
        System.out.println( " ##exitDatablock##" );
    }

    @Override
    public void enterHeaderrow(HeaderrowContext ctx) {
        System.out.println( "##enterHeaderrow## " );
    }

    @Override
    public void exitHeaderrow(HeaderrowContext ctx) {
        System.out.println( " ##exitHeaderrow##" );
    }

    @Override
    public void enterDatablockrow(DatablockrowContext ctx) {
        System.out.print( "##enterDatablockrow## " );
    }

    @Override
    public void exitDatablockrow(DatablockrowContext ctx) {
        System.out.println( " ##exitDatablockrow##" );
    }

    @Override
    public void enterDatavalues(DatavaluesContext ctx) {
        System.out.print( "##enterDatavalues## " + ctx.getText() );
    }

    @Override
    public void exitDatavalues(DatavaluesContext ctx) {
        System.out.print( " ##exitDatavalues##" );
    }

    @Override
    public void enterWaarde(WaardeContext ctx) {
        System.out.print( "##enterWaarde## " + ctx.getText() );
    }

    @Override
    public void exitWaarde(WaardeContext ctx) {
        System.out.println( " ##exitWaarde##" );
    }

    @Override
    public void enterVariabele(VariabeleContext ctx) {
        System.out.print( "##enterVariabele## " + ctx.getText() );
    }

    @Override
    public void exitVariabele(VariabeleContext ctx) {
        System.out.println( " ##exitVariabele##" );
    }

    @Override
    public void visitErrorNode(ErrorNode node) {
        System.err.println();
        System.err.println( "visitErrorNode: " + node.getText() );
    }

}
