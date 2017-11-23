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
package nl.bro.dto.gef.validation.impl;

import static nl.bro.cpt.gef.transform.MappingConstants.TRANSACTION_TYPE_REGISTRATION;
import static org.fest.assertions.Assertions.assertThat;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import javax.validation.ConstraintViolation;
import javax.validation.Validator;
import javax.validation.groups.Default;

import org.junit.Test;

import nl.bro.cpt.gef.dto.GefDisFile;
import nl.bro.cpt.gef.logic.GefUnmarshaller;
import nl.bro.cpt.gef.logic.impl.GefParserImpl;
import nl.bro.dto.gef.groups.Imbro;
import nl.bro.dto.gef.groups.ImbroA;
import nl.bro.dto.gef.testutil.TestUtil;
import nl.bro.dto.gef.validation.support.GefValidation;

public class TestIsCptStartBeforeDisValidator {

    @Test
    public void testCptDateInPastImbroA() throws IOException {

        // -- prepare
        String disFileName = "20151020vbDIS1.GEF";
        byte[] disFileContent = TestUtil.getFileBytes( "/" + disFileName );
        GefUnmarshaller unmarshaller = new GefParserImpl();
        GefDisFile disFile = (GefDisFile) unmarshaller.unmarshall( disFileName, disFileContent, TRANSACTION_TYPE_REGISTRATION );
        disFile.setStartTime( null );
        disFile.setCptStartTime( null );
        disFile.setStartDate( "2015,08,23" );
        disFile.setCptStartDate( "2015,08,22" );

        // -- action
        Validator validator = GefValidation.buildFactory().getValidator();
        Set<ConstraintViolation<GefDisFile>> cvs = validator.validate( disFile, ImbroA.class, Default.class );

        // -- verify
        List<String> messages = getValidationMessages( cvs );
        assertThat( messages ).isEmpty();

    }

    @Test
    public void testCptTimeSameTimeSameImbro() throws IOException {

        // -- prepare
        String disFileName = "20151020vbDIS1.GEF";
        byte[] disFileContent = TestUtil.getFileBytes( "/" + disFileName );
        GefUnmarshaller unmarshaller = new GefParserImpl();
        GefDisFile disFile = (GefDisFile) unmarshaller.unmarshall( disFileName, disFileContent, TRANSACTION_TYPE_REGISTRATION );
        disFile.setStartTime( "12,00,00" );
        disFile.setCptStartTime( "12,00,00" );
        disFile.setStartDate( "2015,08,01" );
        disFile.setCptStartDate( "2015,08,01" );

        // -- action
        Validator validator = GefValidation.buildFactory().getValidator();
        Set<ConstraintViolation<GefDisFile>> cvs = validator.validate( disFile, Imbro.class, Default.class );

        // -- verify
        List<String> messages = getValidationMessages( cvs );
        assertThat( messages ).isEmpty();

    }

    @Test
    public void testCptTimeSameTimeInPastImbro() throws IOException {

        // -- prepare
        String disFileName = "20151020vbDIS1.GEF";
        byte[] disFileContent = TestUtil.getFileBytes( "/" + disFileName );
        GefUnmarshaller unmarshaller = new GefParserImpl();
        GefDisFile disFile = (GefDisFile) unmarshaller.unmarshall( disFileName, disFileContent, TRANSACTION_TYPE_REGISTRATION );
        disFile.setStartTime( "12,00,01" );
        disFile.setCptStartTime( "12,00,00" );
        disFile.setStartDate( "2015,08,01" );
        disFile.setCptStartDate( "2015,08,01" );

        // -- action
        Validator validator = GefValidation.buildFactory().getValidator();
        Set<ConstraintViolation<GefDisFile>> cvs = validator.validate( disFile, Imbro.class, Default.class );

        // -- verify
        List<String> messages = getValidationMessages( cvs );
        assertThat( messages ).isEmpty();

    }

    @Test
    public void testInvalidCptDateInFutureImbroA() throws IOException {

        // -- prepare
        String disFileName = "20151020vbDIS1.GEF";
        byte[] disFileContent = TestUtil.getFileBytes( "/" + disFileName );
        GefUnmarshaller unmarshaller = new GefParserImpl();
        GefDisFile disFile = (GefDisFile) unmarshaller.unmarshall( disFileName, disFileContent, TRANSACTION_TYPE_REGISTRATION );
        disFile.setStartTime( null );
        disFile.setCptStartTime( null );
        disFile.setStartDate( "2015,08,01" );
        disFile.setCptStartDate( "2015,08,02" );

        // -- action
        Validator validator = GefValidation.buildFactory().getValidator();
        Set<ConstraintViolation<GefDisFile>> cvs = validator.validate( disFile, ImbroA.class, Default.class );

        // -- verify
        List<String> messages = getValidationMessages( cvs );
        assertThat( messages ).containsExactly( "GEF#STARTDATE = 2015,08,02 waarde is niet correct: mag niet na DIS#STARTDATE = 2015,08,01 liggen." );

    }

    @Test
    public void testInvalidCptTimeInFutureImbro() throws IOException {

        // -- prepare
        String disFileName = "20151020vbDIS1.GEF";
        byte[] disFileContent = TestUtil.getFileBytes( "/" + disFileName );
        GefUnmarshaller unmarshaller = new GefParserImpl();
        GefDisFile disFile = (GefDisFile) unmarshaller.unmarshall( disFileName, disFileContent, TRANSACTION_TYPE_REGISTRATION );
        disFile.setStartTime( "12,00,00" );
        disFile.setCptStartTime( "12,00,01" );
        disFile.setStartDate( "2015,08,01" );
        disFile.setCptStartDate( "2015,08,01" );

        // -- action
        Validator validator = GefValidation.buildFactory().getValidator();
        Set<ConstraintViolation<GefDisFile>> cvs = validator.validate( disFile, Imbro.class, Default.class );

        // -- verify
        List<String> messages = getValidationMessages( cvs );
        assertThat( messages ).containsExactly( "GEF#STARTTIME = 12,00,01 waarde is niet correct: mag niet na DIS#STARTTIME = 12,00,00 liggen." );

    }

    private List<String> getValidationMessages(Set<ConstraintViolation<GefDisFile>> cvs) {
        Iterator<ConstraintViolation<GefDisFile>> it = cvs.iterator();
        List<String> messages = new ArrayList<>();
        while ( it.hasNext() ) {
            ConstraintViolation<GefDisFile> violation = it.next();
            messages.add( violation.getMessage() );
        }
        return messages;
    }
}
