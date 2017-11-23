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
package nl.bro.dto.gef;

import static nl.bro.cpt.gef.transform.MappingConstants.IMBROA;
import static nl.bro.cpt.gef.transform.MappingConstants.TRANSACTION_TYPE_REGISTRATION;
import static org.fest.assertions.Assertions.assertThat;
import static org.junit.Assert.fail;

import java.io.IOException;
import java.math.BigDecimal;
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
import nl.bro.cpt.gef.logic.impl.GefParserImpl;
import nl.bro.dto.gef.testutil.TestUtil;

@RunWith( MockitoJUnitRunner.class )
public class GefReaderTest {

    @Test
    public void testGefReader() {

        try {
            List<byte[]> fileDataList = getInputFiles1();
            List<String> fileNameList = getInputFiles1Names();
            GefSurveySet surveySet = new GefSurveySet();

            for ( int i = 0; i < fileDataList.size(); i++ ) {
                GefUnmarshaller unmarshaller = new GefParserImpl();
                GefFile gf = unmarshaller.unmarshall( fileNameList.get( i ), fileDataList.get( i ), "registratie" );
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

            // check some values, not all - as that would be superfluous
            assertThat( surveys.size() ).isEqualTo( 1 );
            GefCptSurvey survey = surveys.get( 0 );
            assertThat( survey ).isNotNull();
            assertThat( survey.getGefCptFile() ).isNotNull();
            IfcCptFile cptFile = survey.getGefCptFile();
            assertThat( cptFile.getCompanyId() ).isEqualTo( "56214098" );
            assertThat( cptFile.getFileDate() ).isEqualTo( "2015, 09, 02" );
            assertThat( cptFile.getMeasurementText11() ).isEqualTo( "Stortregen" );
            assertThat( cptFile.getMeasurementVar13() ).isEqualTo( new BigDecimal( "0.30" ) );
            assertThat( survey.getGefDisFiles().size() ).isEqualTo( 1 );
            IfcDisFile disFile = survey.getGefDisFiles().get( 0 );
            assertThat( disFile ).isNotNull();
            assertThat( disFile.getGefId() ).isEqualTo( "1.1.0" );
            assertThat( disFile.getParentDepth() ).isEqualTo( new BigDecimal( "5.990" ) );
        }
        catch ( IOException io ) {
            Assert.fail( "Test configaratie incorrect: " + io.getMessage() );
        }
        catch ( Exception e ) {
            Assert.fail( e.getMessage() );
        }
    }

    private static List<byte[]> getInputFiles1() throws IOException {

        List<byte[]> list = new ArrayList<byte[]>();
        list.add( TestUtil.getFileBytes( "/20151020vbCPT1.GEF" ) );
        list.add( TestUtil.getFileBytes( "/20151020vbDIS1.GEF" ) );

        return list;
    }

    private static List<String> getInputFiles1Names() {
        List<String> list = new ArrayList<String>();

        list.add( "20151020vbCPT1.GEF" );
        list.add( "20151020vbDIS1.GEF" );

        return list;
    }
}
