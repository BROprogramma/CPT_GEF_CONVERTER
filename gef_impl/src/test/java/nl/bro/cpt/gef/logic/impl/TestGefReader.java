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

import static nl.bro.cpt.gef.transform.MappingConstants.IMBRO;
import static nl.bro.cpt.gef.transform.MappingConstants.IMBROA;
import static nl.bro.cpt.gef.transform.MappingConstants.REGISTRATION_STATUS_REGISTRATION;
import static nl.bro.cpt.gef.transform.MappingConstants.TRANSACTION_TYPE_REGISTRATION;
import static org.fest.assertions.Assertions.assertThat;
import static org.junit.Assert.fail;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.runners.MockitoJUnitRunner;

import nl.bro.cpt.gef.dto.GefCptSurvey;
import nl.bro.cpt.gef.dto.GefFile;
import nl.bro.cpt.gef.dto.IfcCptFile;
import nl.bro.cpt.gef.dto.IfcDisFile;
import nl.bro.cpt.gef.logic.GefSurveySet;
import nl.bro.cpt.gef.logic.GefUnmarshaller;
import nl.bro.dto.gef.testutil.TestUtil;

@RunWith( MockitoJUnitRunner.class )
public class TestGefReader {

    @Test
    public void testGefCptAndDissFiles() {

        try {

            List<String> filenameList = new ArrayList<String>();
            filenameList.add( "20151020vbCPT1.GEF" );
            filenameList.add( "20151020vbDIS1.GEF" );

            List<byte[]> fileDataList = new ArrayList<byte[]>();
            fileDataList.add( TestUtil.getFileBytes( "/" + filenameList.get( 0 ) ) );
            fileDataList.add( TestUtil.getFileBytes( "/" + filenameList.get( 1 ) ) );

            GefSurveySet surveySet = new GefSurveySet();
            for ( int i = 0; i < fileDataList.size(); i++ ) {
                GefUnmarshaller unmarshaller = new GefParserImpl();
                GefFile gf = unmarshaller.unmarshall( filenameList.get( i ), fileDataList.get( i ), REGISTRATION_STATUS_REGISTRATION );
                if ( gf.getParseErrors().isEmpty() ) {
                    surveySet.addFile( gf );
                }
                else {
                    fail( "parse errors found: " + gf.getParseErrors() );
                }
            }

            List<GefCptSurvey> surveys = surveySet.getGefSurveyList( IMBRO, TRANSACTION_TYPE_REGISTRATION );
            GefSurveySet.validateConsistency( surveys );
            GefSurveySet.validateContent( surveys, TRANSACTION_TYPE_REGISTRATION );

            assertThat( surveys.size() ).isEqualTo( 1 );
            assertThat( surveys.get( 0 ).getGefCptFile() ).isNotNull();
            IfcCptFile cptFile = surveys.get( 0 ).getGefCptFile();
            assertThat( cptFile.getValidationErrors().size() ).isEqualTo( 0 );
            assertThat( cptFile.getParseErrors().size() ).isEqualTo( 0 );
            assertThat( surveys.get( 0 ).getGefDisFiles() ).isNotNull();
            assertThat( surveys.get( 0 ).getGefDisFiles().size() ).isEqualTo( 1 );
            IfcDisFile disFile = surveys.get( 0 ).getGefDisFiles().get( 0 );
            assertThat( disFile.getValidationErrors().size() ).isEqualTo( 0 );
            assertThat( disFile.getParseErrors().size() ).isEqualTo( 0 );

        }
        catch ( IOException e ) {
            Assert.fail( "Test configuration failure: " + e.getMessage() );
        }
        catch ( Exception e ) {
            Assert.fail( e.getMessage() );
        }
    }

    @Test
    public void testGefCptAndDissFiles2() {

        try {

            List<String> filenameList = new ArrayList<String>();
            filenameList.add( "CPT-F3b-i3-24693a-acceptance-CPT.gef" );
            filenameList.add( "CPT-F3b-i3-24693a-acceptance-DISS.gef" );

            List<byte[]> fileDataList = new ArrayList<byte[]>();
            fileDataList.add( TestUtil.getFileBytes( "/" + filenameList.get( 0 ) ) );
            fileDataList.add( TestUtil.getFileBytes( "/" + filenameList.get( 1 ) ) );

            GefSurveySet surveySet = new GefSurveySet();
            for ( int i = 0; i < fileDataList.size(); i++ ) {
                GefUnmarshaller unmarshaller = new GefParserImpl();
                GefFile gf = unmarshaller.unmarshall( filenameList.get( i ), fileDataList.get( i ), REGISTRATION_STATUS_REGISTRATION );
                if ( gf.getParseErrors().isEmpty() ) {
                    surveySet.addFile( gf );
                }
                else {
                    fail( "parse errors found: " + gf.getParseErrors() );
                }
            }

            List<GefCptSurvey> surveys = surveySet.getGefSurveyList( IMBROA, TRANSACTION_TYPE_REGISTRATION );
            GefSurveySet.validateConsistency( surveys );
            GefSurveySet.validateContent( surveys, TRANSACTION_TYPE_REGISTRATION );

            assertThat( surveys.size() ).isEqualTo( 1 );
            assertThat( surveys.get( 0 ).getGefCptFile() ).isNotNull();
            IfcCptFile cptFile = surveys.get( 0 ).getGefCptFile();
            assertThat( cptFile.getValidationErrors().size() ).isEqualTo( 0 );
            assertThat( cptFile.getParseErrors().size() ).isEqualTo( 0 );
            assertThat( surveys.get( 0 ).getGefDisFiles() ).isNotNull();
            assertThat( surveys.get( 0 ).getGefDisFiles().size() ).isEqualTo( 1 );
            IfcDisFile disFile = surveys.get( 0 ).getGefDisFiles().get( 0 );
            assertThat( disFile.getValidationErrors().size() ).isEqualTo( 0 );
            assertThat( disFile.getParseErrors().size() ).isEqualTo( 0 );

        }
        catch ( IOException e ) {
            Assert.fail( "Test configuration failure: " + e.getMessage() );
        }
        catch ( Exception e ) {
            Assert.fail( e.getMessage() );
        }
    }
}
