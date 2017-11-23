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

import static nl.bro.cpt.gef.transform.MappingConstants.IMBROA;
import static nl.bro.cpt.gef.transform.MappingConstants.TRANSACTION_TYPE_REGISTRATION;
import static org.fest.assertions.Assertions.assertThat;

import java.io.IOException;
import java.util.List;

import javax.xml.bind.JAXBException;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.runners.MockitoJUnitRunner;

import nl.bro.cpt.gef.dto.GefCptSurvey;
import nl.bro.cpt.gef.dto.GefFile;
import nl.bro.cpt.gef.dto.IfcCptFile;
import nl.bro.cpt.gef.logic.GefSurveySet;
import nl.bro.cpt.gef.logic.GefUnmarshaller;
import nl.bro.cpt.gef.transform.GefToBroMapperImbroA;
import nl.bro.dto.gef.testutil.TestUtil;
import nl.broservices.xsd.iscpt.v_1_1.RegistrationRequestType;

/**
 * Test class gebaseerd op de minimale GEF zoals aangeleverd door Ruud.
 *
 * @author J.M. van Ommeren
 */
@RunWith( MockitoJUnitRunner.class )
public class MinimalGefTester {

    @Test
    public void testMinimalGefValidatity() throws IOException {

        String filename = "minimal.gef";
        byte[] fileContent = TestUtil.getFileBytes( "/" + filename );

        GefUnmarshaller unmarshaller = new GefParserImpl();
        GefFile gf = unmarshaller.unmarshall( filename, fileContent, TRANSACTION_TYPE_REGISTRATION );

        IfcCptFile cptFile = (IfcCptFile) gf;
        assertThat( cptFile.getParseErrors().size() ).isEqualTo( 0 );

        GefSurveySet surveySet = new GefSurveySet();
        surveySet.addFile( gf );

        List<GefCptSurvey> surveys = surveySet.getGefSurveyList( IMBROA, TRANSACTION_TYPE_REGISTRATION );
        GefSurveySet.validateConsistency( surveys );
        GefSurveySet.validateContent( surveys, TRANSACTION_TYPE_REGISTRATION );

        assertThat( surveys.size() ).isEqualTo( 1 );
        assertThat( surveys.get( 0 ) ).isNotNull();
        assertThat( surveys.get( 0 ).getGefCptFile() ).isNotNull();
        assertThat( surveys.get( 0 ).getGefCptFile().getValidationErrors() ).isEmpty();
    }

    @Test
    public void testMinimalGefBroValidaty() throws IOException, JAXBException {

        String filename = "minimal.gef";
        byte[] fileContent = TestUtil.getFileBytes( "/" + filename );

        GefUnmarshaller unmarshaller = new GefParserImpl();
        GefFile gf = unmarshaller.unmarshall( filename, fileContent, TRANSACTION_TYPE_REGISTRATION );

        GefSurveySet surveySet = new GefSurveySet();
        surveySet.addFile( gf );

        List<GefCptSurvey> surveys = surveySet.getGefSurveyList( IMBROA, TRANSACTION_TYPE_REGISTRATION );

        assertThat( surveys.size() ).isEqualTo( 1 );
        GefCptSurvey survey = surveys.get( 0 );
        assertThat( survey ).isNotNull();

        RegistrationRequestType geoSurvey = GefToBroMapperImbroA.getInstance().gefToRegistrationRequest( survey, "blah" );
        assertThat( geoSurvey ).isNotNull();
    }
}
