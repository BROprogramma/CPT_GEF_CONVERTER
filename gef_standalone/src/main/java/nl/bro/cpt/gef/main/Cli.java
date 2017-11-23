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

import java.io.File;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.DefaultParser;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;

import nl.bro.cpt.gef.transform.MappingConstants;

public class Cli {

    private static final Logger LOG = Logger.getLogger( ConvertGef.class.getName() );

    private static final String QR_IMBRO = "IMBRO";
    private static final String QR_IMBROA = "IMBROA";
    private static final String TT_REGISTRATION = "R";
    private static final String TT_CORRECTION = "C";

    private final String[] args;
    private final Options options = new Options();

    private String qualityRegime = null;
    private String transactionType = null;
    private String requestReference = null;
    private String correctionReason = null;
    private String objectIdAccountableParty = null;
    private String outputDir = null;
    private boolean shouldExit = false;
    private String[] arguments = null;

    public Cli(String[] args) {
        this.args = args;
        options.addOption( "h", "help", false, "shows this help." );
        options.addOption( "q", "qualityRegime", true, String.format( "%s or %s", QR_IMBRO, QR_IMBROA ) );
        options.addOption( "r", "requestReference", true, "maximum 200 characters" );
        options.addOption( "t", "transactionType", true, String.format( "%s or %s", TT_REGISTRATION, TT_CORRECTION ) );
        options.addOption( "c", "correctionReason", true, "should comply to a valid correction reason" );
        options.addOption( "o", "objectIdAccountableParty", true, "in case of corrections, the objectIdAccountableParty must be specified" );
        options.addOption( "d", "outputDir", true, "directory the xml to" );
    }

    public void parse() {
        CommandLineParser parser = new DefaultParser();
        CommandLine cmd = null;
        try {
            cmd = parser.parse( options, args );

            if ( cmd.hasOption( "h" ) ) {
                help();
            }

            handleQualityRegime( cmd );
            handleRequestReference( cmd );
            handleTransactionType( cmd );
            handleOutputDir( cmd );
            arguments = cmd.getArgs();
        }
        catch ( ParseException e ) {
            LOG.log( Level.SEVERE, "Failed to parse comand line properties", e );
            help();
        }
    }

    public String getTransactionType() {
        return transactionType;
    }

    public String getQualityRegime() {
        return qualityRegime;
    }

    public String getRequestReference() {
        return requestReference;
    }

    public String getCorrectionReason() {
        return correctionReason;
    }

    public String getOutputDir() {
        return outputDir;
    }

    public String getObjectIdAccountableParty() {
        return objectIdAccountableParty;
    }

    public String[] getArguments() {
        return arguments;
    }

    public boolean isShouldExit() {
        return shouldExit;
    }

    private void help() {

        if ( !shouldExit ) {
            // This prints out some help
            HelpFormatter formatter = new HelpFormatter();
            formatter.setWidth( 100 );
            String cmdLineSyntax =  ConvertGef.class.getSimpleName() + "  -r <RR> -q <QR> [-t R | -t C -c <CR> -o <ID>] [-d <DIR>] <GEF_FILES>";
            formatter.printHelp( cmdLineSyntax, options );
            shouldExit = true;
        }
    }

    private void handleRequestReference(CommandLine cmd) {
        if ( cmd.hasOption( "r" ) ) {
            LOG.log( Level.INFO, "requestReference={0}", cmd.getOptionValue( "r" ) );
            requestReference = cmd.getOptionValue( "r" );
        }
        else {
            LOG.log( Level.SEVERE, "Missing r option" );
            help();
        }
    }

    private void handleOutputDir(CommandLine cmd) {
        if ( cmd.hasOption( "d" ) ) {
            LOG.log( Level.INFO, "outputDirectory={0}", cmd.getOptionValue( "d" ) );
            outputDir = cmd.getOptionValue( "d" );
            File test = new File( outputDir );
            if ( !test.isDirectory() ) {
                LOG.log( Level.SEVERE, "outputDirectory={0} does not exist!",outputDir  );
            }
        }
        else {
            outputDir = ".";
            LOG.log( Level.INFO, "Missing d option, using current directory as output." );
        }
    }

    private void handleObjectIdAccountableParty(CommandLine cmd) {
        if ( cmd.hasOption( "r" ) ) {
            LOG.log( Level.INFO, "objectIdAccountableParty={0}", cmd.getOptionValue( "o" ) );
            objectIdAccountableParty = cmd.getOptionValue( "o" );
        }
        else {
            LOG.log( Level.SEVERE, "Missing o option" );
            help();
        }
    }

    private void handleCorrectionReason(CommandLine cmd) {
        if ( cmd.hasOption( "c" ) ) {
            LOG.log( Level.INFO, "correctionReason={0}", cmd.getOptionValue( "c" ) );
            correctionReason = cmd.getOptionValue( "c" );
        }
        else {
            LOG.log( Level.SEVERE, "Missing c option" );
            help();
        }
    }

    private void ignoreObjectIdAccountableParty(CommandLine cmd) {
        if ( cmd.hasOption( "o" ) ) {
            LOG.log( Level.INFO, "Ignoring o option" );
        }
    }

    private void ignoreCorrectionReason(CommandLine cmd) {
        if ( cmd.hasOption( "c" ) ) {
            LOG.log( Level.INFO, "Ignoring c option" );
        }
    }

    private void handleQualityRegime(CommandLine cmd) {
        if ( cmd.hasOption( "q" ) ) {
            LOG.log( Level.INFO, "qualityRegime={0}", cmd.getOptionValue( "q" ) );
            if ( QR_IMBRO.equals( cmd.getOptionValue( "q" ) ) ) {
                qualityRegime = MappingConstants.IMBRO;
            }
            else if ( QR_IMBROA.equals( cmd.getOptionValue( "q" ) ) ) {
                qualityRegime = MappingConstants.IMBROA;
            }
            else {
                LOG.log( Level.SEVERE, "option q should be one {0} or {1}", new String[]{ QR_IMBRO, QR_IMBROA } );
                help();
            }
        }
        else {
            LOG.log( Level.SEVERE, "Missing q option" );
            help();
        }
    }

    private void handleTransactionType(CommandLine cmd) {
        if ( cmd.hasOption( "t" ) ) {
            LOG.log( Level.INFO, "transactionType={0}", cmd.getOptionValue( "t" ) );
            if ( TT_CORRECTION.equals( cmd.getOptionValue( "t" ) ) ) {
                transactionType = MappingConstants.TRANSACTION_TYPE_CORRECTION;
                handleCorrectionReason( cmd );
                handleObjectIdAccountableParty( cmd );
            }
            else if ( TT_REGISTRATION.equals( cmd.getOptionValue( "t" ) ) ) {
                transactionType = MappingConstants.TRANSACTION_TYPE_REGISTRATION;
                ignoreCorrectionReason( cmd );
                ignoreObjectIdAccountableParty( cmd );
            }
            else {
                LOG.log( Level.SEVERE, "option t should be one {0} (register) or {1} (correct)", new String[]{ TT_REGISTRATION, TT_CORRECTION } );
                help();
            }
        }
        else {
            LOG.log( Level.SEVERE, "Missing t option" );
            help();
        }
    }

}
