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

import static nl.bro.cpt.gef.transform.MappingConstants.TRANSACTION_TYPE_CORRECTION;
import static org.fest.assertions.Assertions.assertThat;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import javax.validation.ConstraintViolation;
import javax.validation.Validator;
import javax.validation.groups.Default;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.runners.MockitoJUnitRunner;

import nl.bro.cpt.gef.dto.GefCptFile;
import nl.bro.cpt.gef.dto.GefFile;
import nl.bro.cpt.gef.logic.GefUnmarshaller;
import nl.bro.cpt.gef.logic.impl.GefParserImpl;
import nl.bro.dto.gef.groups.ImbroA;
import nl.bro.dto.gef.testutil.TestUtil;
import nl.bro.dto.gef.validation.support.GefValidation;

@RunWith( MockitoJUnitRunner.class )
public class TestSpecimenVarEmptyWhenNoPredrilledValidator {

    @Test
    public void testZidSetAndNotDvpMethodGeen() {

        GefCptFile file = getGefFile();
        file.setMeasurementVar13( BigDecimal.ZERO );

        Validator validator = GefValidation.buildFactory().getValidator();
        Set<ConstraintViolation<GefCptFile>> cvs = validator.validate( file, ImbroA.class, Default.class );

        assertThat( getMessages( cvs ) ).contains( "GEF#SPECIMENVAR mag niet aanwezig zijn: GEF#MEASUREMENTVAR13 = 0 bepaalt dat het moet ontbreken." );
    }

    private static GefCptFile getGefFile() {
        try {
            String filename = "CPT-F3b-i3-15249-completion-CPT.gef";
            byte[] fileContent = TestUtil.getFileBytes( "/" + filename );

            GefUnmarshaller unmarshaller = new GefParserImpl();
            GefFile gf = unmarshaller.unmarshall( filename, fileContent, TRANSACTION_TYPE_CORRECTION );

            return (GefCptFile) gf;
        }
        catch ( IOException e ) {
            Assert.fail( e.getMessage() );
        }
        // placate the compiler... this code is never reached.
        return null;
    }

    private List<String> getMessages(Set<ConstraintViolation<GefCptFile>> cvs) {
        List<String> result = new ArrayList<>();
        for ( ConstraintViolation<GefCptFile> cv : cvs ) {
            result.add( cv.getMessage() );
        }

        return result;
    }
}
