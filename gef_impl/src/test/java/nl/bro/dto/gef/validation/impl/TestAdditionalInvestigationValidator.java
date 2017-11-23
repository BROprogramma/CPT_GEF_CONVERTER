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
import java.util.ArrayList;
import java.util.Set;

import javax.validation.ConstraintViolation;
import javax.validation.Validator;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.runners.MockitoJUnitRunner;

import nl.bro.cpt.gef.dto.GefCptFile;
import nl.bro.cpt.gef.dto.GefFile;
import nl.bro.cpt.gef.dto.SpecimenVar;
import nl.bro.cpt.gef.logic.GefUnmarshaller;
import nl.bro.cpt.gef.logic.impl.GefParserImpl;
import nl.bro.cpt.gef.transform.MappingConstants;
import nl.bro.dto.gef.groups.Imbro;
import nl.bro.dto.gef.testutil.TestUtil;
import nl.bro.dto.gef.validation.support.GefValidation;

@RunWith( MockitoJUnitRunner.class )
public class TestAdditionalInvestigationValidator {

    @Test
    public void testAllFilled() {

        GefCptFile file = getGefFile();

        Validator validator = GefValidation.buildFactory().getValidator();
        Set<ConstraintViolation<GefCptFile>> cvs = validator.validate( file, Imbro.class );

        assertThat( cvs ).isEmpty();
    }

    @Test
    public void testMeasurementText111No() {

        GefCptFile file = getGefFile();

        file.setMeasurementText111( MappingConstants.GEF_IND_NO );

        Validator validator = GefValidation.buildFactory().getValidator();
        Set<ConstraintViolation<GefCptFile>> cvs = validator.validate( file, Imbro.class );

        assertThat( cvs ).isEmpty();
    }

    @Test
    public void testMeasurementText108Null() {

        GefCptFile file = getGefFile();

        file.setMeasurementText108( null );

        Validator validator = GefValidation.buildFactory().getValidator();
        Set<ConstraintViolation<GefCptFile>> cvs = validator.validate( file, Imbro.class );

        assertThat( cvs ).isEmpty();
    }

    @Test
    public void testMeasurementText11Null() {

        GefCptFile file = getGefFile();

        file.setMeasurementText11( null );

        Validator validator = GefValidation.buildFactory().getValidator();
        Set<ConstraintViolation<GefCptFile>> cvs = validator.validate( file, Imbro.class );

        assertThat( cvs ).isEmpty();
    }

    @Test
    public void testMeasurementVar4Null() {

        GefCptFile file = getGefFile();

        file.setMeasurementVar14( null );

        Validator validator = GefValidation.buildFactory().getValidator();
        Set<ConstraintViolation<GefCptFile>> cvs = validator.validate( file, Imbro.class );

        assertThat( cvs ).isEmpty();
    }

    @Test
    public void testSpecimenVarNull() {

        GefCptFile file = getGefFile();

        file.setSpecimenVar( null );

        Validator validator = GefValidation.buildFactory().getValidator();
        Set<ConstraintViolation<GefCptFile>> cvs = validator.validate( file, Imbro.class );

        assertThat( cvs ).isEmpty();
    }

    @Test
    public void testOnlySpecimenVarNotNull() {

        GefCptFile file = getGefFile();

        file.setMeasurementText108( null );
        file.setMeasurementText11( null );
        file.setMeasurementVar14( null );

        Validator validator = GefValidation.buildFactory().getValidator();
        Set<ConstraintViolation<GefCptFile>> cvs = validator.validate( file, Imbro.class );

        assertThat( cvs ).isEmpty();
    }

    @SuppressWarnings( "unchecked" )
    @Test
    public void testAllNullSpecimenVarEmpty() {

        GefCptFile file = getGefFile();

        file.setMeasurementText108( null );
        file.setMeasurementText11( null );
        file.setMeasurementVar14( null );
        file.setSpecimenVar( new ArrayList<SpecimenVar>() );

        Validator validator = GefValidation.buildFactory().getValidator();
        Set<ConstraintViolation<GefCptFile>> cvs = validator.validate( file, Imbro.class );

        assertThat( file.getMeasurementText111() ).isEqualTo( MappingConstants.GEF_IND_YES );
        assertThat( cvs.size() ).isEqualTo( 1 );
        ConstraintViolation<GefCptFile> violation = (ConstraintViolation<GefCptFile>) cvs.toArray()[0];
        assertThat( violation.getMessage() ).startsWith( "Het is verplicht ten minste 1 verwijderde laag te beschrijven" );
    }

    @SuppressWarnings( "unchecked" )
    @Test
    public void testAllNull() {

        GefCptFile file = getGefFile();

        file.setMeasurementText108( null );
        file.setMeasurementText11( null );
        file.setMeasurementVar14( null );
        file.setSpecimenVar( null );

        Validator validator = GefValidation.buildFactory().getValidator();
        Set<ConstraintViolation<GefCptFile>> cvs = validator.validate( file, Imbro.class );

        assertThat( file.getMeasurementText111() ).isEqualTo( MappingConstants.GEF_IND_YES );
        assertThat( cvs.size() ).isEqualTo( 1 );
        ConstraintViolation<GefCptFile> violation = (ConstraintViolation<GefCptFile>) cvs.toArray()[0];
        assertThat( violation.getMessage() ).startsWith( "Het is verplicht ten minste 1 verwijderde laag te beschrijven" );
    }

    @Test
    public void testSpecimenVarAndMeasurementVarNotNull() {

        GefCptFile file = getGefFile();

        file.setMeasurementText108( null );
        file.setMeasurementText11( null );

        Validator validator = GefValidation.buildFactory().getValidator();
        Set<ConstraintViolation<GefCptFile>> cvs = validator.validate( file, Imbro.class );

        assertThat( cvs ).isEmpty();
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

}
