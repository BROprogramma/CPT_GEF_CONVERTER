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
import java.util.Set;

import javax.validation.ConstraintViolation;
import javax.validation.Validator;
import javax.validation.groups.Default;

import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.runners.MockitoJUnitRunner;

import nl.bro.cpt.gef.dto.GefCptFile;
import nl.bro.cpt.gef.dto.GefFile;
import nl.bro.cpt.gef.logic.GefUnmarshaller;
import nl.bro.cpt.gef.logic.impl.GefParserImpl;
import nl.bro.dto.gef.groups.Imbro;
import nl.bro.dto.gef.groups.ImbroA;
import nl.bro.dto.gef.testutil.TestUtil;
import nl.bro.dto.gef.validation.support.GefValidation;

/**
 * Test de verschillende combo's van sondeernorm en kwaliteits klasse.
 * <p>
 * De volgende combinaties zijn toegstaan: <br/>
 * <b>IMBRO</b>
 * <ul>
 * <li>norm 5140 - klasse 1, 2, 3, 4</li>
 * <li>norm 22476-1 - klasse 1, 2, 3, 4</li>
 * <li>norm 22476-12 - klasse 5, 6, 7</li>
 * </ul>
 * <br/>
 * <b>IMBRO/A</b>
 * <ul>
 * <li>norm 5140 - klasse 1, 2, 3, 4, onbekend</li>
 * <li>norm 22476-1 - 1, 2, 3, 4, onbekend</li>
 * <li>norm 22476-12 - klasse 5, 6, 7, onbekend</li>
 * <li>norm 3680 - klasse nvt</li>
 * <li>norm onbekend - klasse onbekend</li>
 * </ul>
 * </p>
 *
 * @author ommerenjmv
 */
@RunWith( MockitoJUnitRunner.class )
public class TestStandardAccordingQualityClassValidator {

    private static GefCptFile cpt;

    @BeforeClass
    public static void setup() throws IOException {

        // lees een geldig GEF-CPT bestand in:
        String filename = "CPT-F3b-i3-28753-CPT.gef";
        byte[] fileContent = TestUtil.getFileBytes( "/" + filename );

        GefUnmarshaller unmarshaller = new GefParserImpl();
        GefFile gf = unmarshaller.unmarshall( filename, fileContent, TRANSACTION_TYPE_REGISTRATION );

        cpt = (GefCptFile) gf;

    }

    @Test
    public void testImbroNorm3680Permutations() {

        Validator validator = GefValidation.buildFactory().getValidator();

        cpt.setMeasurementText6sondeernorm( "3680" );

        cpt.setMeasurementText6kwaliteitsklasse( "nvt" );
        Set<ConstraintViolation<GefCptFile>> cvs = validator.validate( cpt, Imbro.class, Default.class );
        assertThat( cvs.size() ).isEqualTo( 2 );

        cpt.setMeasurementText6kwaliteitsklasse( "onbekend" );
        cvs = validator.validate( cpt, Imbro.class, Default.class );
        assertThat( cvs.size() ).isEqualTo( 3 );

        cpt.setMeasurementText6kwaliteitsklasse( "1" );
        cvs = validator.validate( cpt, Imbro.class, Default.class );
        assertThat( cvs.size() ).isEqualTo( 2 );

        cpt.setMeasurementText6kwaliteitsklasse( "2" );
        cvs = validator.validate( cpt, Imbro.class, Default.class );
        assertThat( cvs.size() ).isEqualTo( 2 );

        cpt.setMeasurementText6kwaliteitsklasse( "3" );
        cvs = validator.validate( cpt, Imbro.class, Default.class );
        assertThat( cvs.size() ).isEqualTo( 2 );

        cpt.setMeasurementText6kwaliteitsklasse( "4" );
        cvs = validator.validate( cpt, Imbro.class, Default.class );
        assertThat( cvs.size() ).isEqualTo( 2 );

        cpt.setMeasurementText6kwaliteitsklasse( "5" );
        cvs = validator.validate( cpt, Imbro.class, Default.class );
        assertThat( cvs.size() ).isEqualTo( 2 );

        cpt.setMeasurementText6kwaliteitsklasse( "6" );
        cvs = validator.validate( cpt, Imbro.class, Default.class );
        assertThat( cvs.size() ).isEqualTo( 2 );

        cpt.setMeasurementText6kwaliteitsklasse( "7" );
        cvs = validator.validate( cpt, Imbro.class, Default.class );
        assertThat( cvs.size() ).isEqualTo( 2 );

    }

    @Test
    public void testImbroNormOnbekendPermutations() {

        Validator validator = GefValidation.buildFactory().getValidator();

        cpt.setMeasurementText6sondeernorm( "onbekend" );

        cpt.setMeasurementText6kwaliteitsklasse( "nvt" );
        Set<ConstraintViolation<GefCptFile>> cvs = validator.validate( cpt, Imbro.class, Default.class );
        assertThat( cvs.size() ).isEqualTo( 3 );

        cpt.setMeasurementText6kwaliteitsklasse( "onbekend" );
        cvs = validator.validate( cpt, Imbro.class, Default.class );
        assertThat( cvs.size() ).isEqualTo( 2 );

        cpt.setMeasurementText6kwaliteitsklasse( "1" );
        cvs = validator.validate( cpt, Imbro.class, Default.class );
        assertThat( cvs.size() ).isEqualTo( 2 );

        cpt.setMeasurementText6kwaliteitsklasse( "2" );
        cvs = validator.validate( cpt, Imbro.class, Default.class );
        assertThat( cvs.size() ).isEqualTo( 2 );

        cpt.setMeasurementText6kwaliteitsklasse( "3" );
        cvs = validator.validate( cpt, Imbro.class, Default.class );
        assertThat( cvs.size() ).isEqualTo( 2 );

        cpt.setMeasurementText6kwaliteitsklasse( "4" );
        cvs = validator.validate( cpt, Imbro.class, Default.class );
        assertThat( cvs.size() ).isEqualTo( 2 );

        cpt.setMeasurementText6kwaliteitsklasse( "5" );
        cvs = validator.validate( cpt, Imbro.class, Default.class );
        assertThat( cvs.size() ).isEqualTo( 2 );

        cpt.setMeasurementText6kwaliteitsklasse( "6" );
        cvs = validator.validate( cpt, Imbro.class, Default.class );
        assertThat( cvs.size() ).isEqualTo( 2 );

        cpt.setMeasurementText6kwaliteitsklasse( "7" );
        cvs = validator.validate( cpt, Imbro.class, Default.class );
        assertThat( cvs.size() ).isEqualTo( 2 );
    }

    @Test
    public void testImbroNorm5140Permutations() {

        Validator validator = GefValidation.buildFactory().getValidator();

        cpt.setMeasurementText6sondeernorm( "5140" );

        cpt.setMeasurementText6kwaliteitsklasse( "nvt" );
        Set<ConstraintViolation<GefCptFile>> cvs = validator.validate( cpt, Imbro.class, Default.class );
        assertThat( cvs.size() ).isEqualTo( 2 );

        cpt.setMeasurementText6kwaliteitsklasse( "onbekend" );
        cvs = validator.validate( cpt, Imbro.class, Default.class );
        assertThat( cvs.size() ).isEqualTo( 1 );

        cpt.setMeasurementText6kwaliteitsklasse( "1" );
        cvs = validator.validate( cpt, Imbro.class, Default.class );
        assertThat( cvs ).isEmpty();

        cpt.setMeasurementText6kwaliteitsklasse( "2" );
        cvs = validator.validate( cpt, Imbro.class, Default.class );
        assertThat( cvs ).isEmpty();

        cpt.setMeasurementText6kwaliteitsklasse( "3" );
        cvs = validator.validate( cpt, Imbro.class, Default.class );
        assertThat( cvs ).isEmpty();

        cpt.setMeasurementText6kwaliteitsklasse( "4" );
        cvs = validator.validate( cpt, Imbro.class, Default.class );
        assertThat( cvs ).isEmpty();

        cpt.setMeasurementText6kwaliteitsklasse( "5" );
        cvs = validator.validate( cpt, Imbro.class, Default.class );
        assertThat( cvs.size() ).isEqualTo( 1 );

        cpt.setMeasurementText6kwaliteitsklasse( "6" );
        cvs = validator.validate( cpt, Imbro.class, Default.class );
        assertThat( cvs.size() ).isEqualTo( 1 );

        cpt.setMeasurementText6kwaliteitsklasse( "7" );
        cvs = validator.validate( cpt, Imbro.class, Default.class );
        assertThat( cvs.size() ).isEqualTo( 1 );
    }

    @Test
    public void testImbroNorm224761Permutations() {

        Validator validator = GefValidation.buildFactory().getValidator();

        cpt.setMeasurementText6sondeernorm( "22476-1" );

        cpt.setMeasurementText6kwaliteitsklasse( "nvt" );
        Set<ConstraintViolation<GefCptFile>> cvs = validator.validate( cpt, Imbro.class, Default.class );
        assertThat( cvs.size() ).isEqualTo( 2 );

        cpt.setMeasurementText6kwaliteitsklasse( "onbekend" );
        cvs = validator.validate( cpt, Imbro.class, Default.class );
        assertThat( cvs.size() ).isEqualTo( 1 );

        cpt.setMeasurementText6kwaliteitsklasse( "1" );
        cvs = validator.validate( cpt, Imbro.class, Default.class );
        assertThat( cvs ).isEmpty();

        cpt.setMeasurementText6kwaliteitsklasse( "2" );
        cvs = validator.validate( cpt, Imbro.class, Default.class );
        assertThat( cvs ).isEmpty();

        cpt.setMeasurementText6kwaliteitsklasse( "3" );
        cvs = validator.validate( cpt, Imbro.class, Default.class );
        assertThat( cvs ).isEmpty();

        cpt.setMeasurementText6kwaliteitsklasse( "4" );
        cvs = validator.validate( cpt, Imbro.class, Default.class );
        assertThat( cvs ).isEmpty();

        cpt.setMeasurementText6kwaliteitsklasse( "5" );
        cvs = validator.validate( cpt, Imbro.class, Default.class );
        assertThat( cvs.size() ).isEqualTo( 1 );

        cpt.setMeasurementText6kwaliteitsklasse( "6" );
        cvs = validator.validate( cpt, Imbro.class, Default.class );
        assertThat( cvs.size() ).isEqualTo( 1 );

        cpt.setMeasurementText6kwaliteitsklasse( "7" );
        cvs = validator.validate( cpt, Imbro.class, Default.class );
        assertThat( cvs.size() ).isEqualTo( 1 );
    }

    @Test
    public void testImbroNorm2247612Permutations() {

        Validator validator = GefValidation.buildFactory().getValidator();

        cpt.setMeasurementText6sondeernorm( "22476-12" );

        cpt.setMeasurementText6kwaliteitsklasse( "nvt" );
        Set<ConstraintViolation<GefCptFile>> cvs = validator.validate( cpt, Imbro.class, Default.class );
        assertThat( cvs.size() ).isEqualTo( 2 );

        cpt.setMeasurementText6kwaliteitsklasse( "onbekend" );
        cvs = validator.validate( cpt, Imbro.class, Default.class );
        assertThat( cvs.size() ).isEqualTo( 1 );

        cpt.setMeasurementText6kwaliteitsklasse( "1" );
        cvs = validator.validate( cpt, Imbro.class, Default.class );
        assertThat( cvs.size() ).isEqualTo( 1 );

        cpt.setMeasurementText6kwaliteitsklasse( "2" );
        cvs = validator.validate( cpt, Imbro.class, Default.class );
        assertThat( cvs.size() ).isEqualTo( 1 );

        cpt.setMeasurementText6kwaliteitsklasse( "3" );
        cvs = validator.validate( cpt, Imbro.class, Default.class );
        assertThat( cvs.size() ).isEqualTo( 1 );

        cpt.setMeasurementText6kwaliteitsklasse( "4" );
        cvs = validator.validate( cpt, Imbro.class, Default.class );
        assertThat( cvs.size() ).isEqualTo( 1 );

        cpt.setMeasurementText6kwaliteitsklasse( "5" );
        cvs = validator.validate( cpt, Imbro.class, Default.class );
        assertThat( cvs ).isEmpty();

        cpt.setMeasurementText6kwaliteitsklasse( "6" );
        cvs = validator.validate( cpt, Imbro.class, Default.class );
        assertThat( cvs ).isEmpty();

        cpt.setMeasurementText6kwaliteitsklasse( "7" );
        cvs = validator.validate( cpt, Imbro.class, Default.class );
        assertThat( cvs ).isEmpty();
    }

    @Test
    public void testImbroANorm3680Permutations() {

        Validator validator = GefValidation.buildFactory().getValidator();

        cpt.setMeasurementText6sondeernorm( "3680" );

        cpt.setMeasurementText6kwaliteitsklasse( "nvt" );
        Set<ConstraintViolation<GefCptFile>> cvs = validator.validate( cpt, ImbroA.class, Default.class );
        assertThat( cvs ).isEmpty();

        cpt.setMeasurementText6kwaliteitsklasse( "onbekend" );
        cvs = validator.validate( cpt, ImbroA.class, Default.class );
        assertThat( cvs.size() ).isEqualTo( 1 );

        cpt.setMeasurementText6kwaliteitsklasse( "1" );
        cvs = validator.validate( cpt, ImbroA.class, Default.class );
        assertThat( cvs.size() ).isEqualTo( 1 );

        cpt.setMeasurementText6kwaliteitsklasse( "2" );
        cvs = validator.validate( cpt, ImbroA.class, Default.class );
        assertThat( cvs.size() ).isEqualTo( 1 );

        cpt.setMeasurementText6kwaliteitsklasse( "3" );
        cvs = validator.validate( cpt, ImbroA.class, Default.class );
        assertThat( cvs.size() ).isEqualTo( 1 );

        cpt.setMeasurementText6kwaliteitsklasse( "4" );
        cvs = validator.validate( cpt, ImbroA.class, Default.class );
        assertThat( cvs.size() ).isEqualTo( 1 );

        cpt.setMeasurementText6kwaliteitsklasse( "5" );
        cvs = validator.validate( cpt, ImbroA.class, Default.class );
        assertThat( cvs.size() ).isEqualTo( 1 );

        cpt.setMeasurementText6kwaliteitsklasse( "6" );
        cvs = validator.validate( cpt, ImbroA.class, Default.class );
        assertThat( cvs.size() ).isEqualTo( 1 );

        cpt.setMeasurementText6kwaliteitsklasse( "7" );
        cvs = validator.validate( cpt, ImbroA.class, Default.class );
        assertThat( cvs.size() ).isEqualTo( 1 );

    }

    @Test
    public void testImbroANormOnbekendPermutations() {

        Validator validator = GefValidation.buildFactory().getValidator();

        cpt.setMeasurementText6sondeernorm( "onbekend" );

        cpt.setMeasurementText6kwaliteitsklasse( "nvt" );
        Set<ConstraintViolation<GefCptFile>> cvs = validator.validate( cpt, ImbroA.class, Default.class );
        assertThat( cvs.size() ).isEqualTo( 1 );

        cpt.setMeasurementText6kwaliteitsklasse( "onbekend" );
        cvs = validator.validate( cpt, ImbroA.class, Default.class );
        assertThat( cvs ).isEmpty();

        cpt.setMeasurementText6kwaliteitsklasse( "1" );
        cvs = validator.validate( cpt, ImbroA.class, Default.class );
        assertThat( cvs.size() ).isEqualTo( 1 );

        cpt.setMeasurementText6kwaliteitsklasse( "2" );
        cvs = validator.validate( cpt, ImbroA.class, Default.class );
        assertThat( cvs.size() ).isEqualTo( 1 );

        cpt.setMeasurementText6kwaliteitsklasse( "3" );
        cvs = validator.validate( cpt, ImbroA.class, Default.class );
        assertThat( cvs.size() ).isEqualTo( 1 );

        cpt.setMeasurementText6kwaliteitsklasse( "4" );
        cvs = validator.validate( cpt, ImbroA.class, Default.class );
        assertThat( cvs.size() ).isEqualTo( 1 );

        cpt.setMeasurementText6kwaliteitsklasse( "5" );
        cvs = validator.validate( cpt, ImbroA.class, Default.class );
        assertThat( cvs.size() ).isEqualTo( 1 );

        cpt.setMeasurementText6kwaliteitsklasse( "6" );
        cvs = validator.validate( cpt, ImbroA.class, Default.class );
        assertThat( cvs.size() ).isEqualTo( 1 );

        cpt.setMeasurementText6kwaliteitsklasse( "7" );
        cvs = validator.validate( cpt, ImbroA.class, Default.class );
        assertThat( cvs.size() ).isEqualTo( 1 );
    }

    @Test
    public void testImbroANorm5140Permutations() {

        Validator validator = GefValidation.buildFactory().getValidator();

        cpt.setMeasurementText6sondeernorm( "5140" );

        cpt.setMeasurementText6kwaliteitsklasse( "nvt" );
        Set<ConstraintViolation<GefCptFile>> cvs = validator.validate( cpt, ImbroA.class, Default.class );
        assertThat( cvs.size() ).isEqualTo( 1 );

        cpt.setMeasurementText6kwaliteitsklasse( "onbekend" );
        cvs = validator.validate( cpt, ImbroA.class, Default.class );
        assertThat( cvs ).isEmpty();

        cpt.setMeasurementText6kwaliteitsklasse( "1" );
        cvs = validator.validate( cpt, ImbroA.class, Default.class );
        assertThat( cvs ).isEmpty();

        cpt.setMeasurementText6kwaliteitsklasse( "2" );
        cvs = validator.validate( cpt, ImbroA.class, Default.class );
        assertThat( cvs ).isEmpty();

        cpt.setMeasurementText6kwaliteitsklasse( "3" );
        cvs = validator.validate( cpt, ImbroA.class, Default.class );
        assertThat( cvs ).isEmpty();

        cpt.setMeasurementText6kwaliteitsklasse( "4" );
        cvs = validator.validate( cpt, ImbroA.class, Default.class );
        assertThat( cvs ).isEmpty();

        cpt.setMeasurementText6kwaliteitsklasse( "5" );
        cvs = validator.validate( cpt, ImbroA.class, Default.class );
        assertThat( cvs.size() ).isEqualTo( 1 );

        cpt.setMeasurementText6kwaliteitsklasse( "6" );
        cvs = validator.validate( cpt, ImbroA.class, Default.class );
        assertThat( cvs.size() ).isEqualTo( 1 );

        cpt.setMeasurementText6kwaliteitsklasse( "7" );
        cvs = validator.validate( cpt, ImbroA.class, Default.class );
        assertThat( cvs.size() ).isEqualTo( 1 );
    }

    @Test
    public void testImbroANorm224761Permutations() {

        Validator validator = GefValidation.buildFactory().getValidator();

        cpt.setMeasurementText6sondeernorm( "22476-1" );

        cpt.setMeasurementText6kwaliteitsklasse( "nvt" );
        Set<ConstraintViolation<GefCptFile>> cvs = validator.validate( cpt, ImbroA.class, Default.class );
        assertThat( cvs.size() ).isEqualTo( 1 );

        cpt.setMeasurementText6kwaliteitsklasse( "onbekend" );
        cvs = validator.validate( cpt, ImbroA.class, Default.class );
        assertThat( cvs ).isEmpty();

        cpt.setMeasurementText6kwaliteitsklasse( "1" );
        cvs = validator.validate( cpt, ImbroA.class, Default.class );
        assertThat( cvs ).isEmpty();

        cpt.setMeasurementText6kwaliteitsklasse( "2" );
        cvs = validator.validate( cpt, ImbroA.class, Default.class );
        assertThat( cvs ).isEmpty();

        cpt.setMeasurementText6kwaliteitsklasse( "3" );
        cvs = validator.validate( cpt, ImbroA.class, Default.class );
        assertThat( cvs ).isEmpty();

        cpt.setMeasurementText6kwaliteitsklasse( "4" );
        cvs = validator.validate( cpt, ImbroA.class, Default.class );
        assertThat( cvs ).isEmpty();

        cpt.setMeasurementText6kwaliteitsklasse( "5" );
        cvs = validator.validate( cpt, ImbroA.class, Default.class );
        assertThat( cvs.size() ).isEqualTo( 1 );

        cpt.setMeasurementText6kwaliteitsklasse( "6" );
        cvs = validator.validate( cpt, ImbroA.class, Default.class );
        assertThat( cvs.size() ).isEqualTo( 1 );

        cpt.setMeasurementText6kwaliteitsklasse( "7" );
        cvs = validator.validate( cpt, ImbroA.class, Default.class );
        assertThat( cvs.size() ).isEqualTo( 1 );
    }

    @Test
    public void testImbroANorm2247612Permutations() {

        Validator validator = GefValidation.buildFactory().getValidator();

        cpt.setMeasurementText6sondeernorm( "22476-12" );

        cpt.setMeasurementText6kwaliteitsklasse( "nvt" );
        Set<ConstraintViolation<GefCptFile>> cvs = validator.validate( cpt, ImbroA.class, Default.class );
        assertThat( cvs.size() ).isEqualTo( 1 );

        cpt.setMeasurementText6kwaliteitsklasse( "onbekend" );
        cvs = validator.validate( cpt, ImbroA.class, Default.class );
        assertThat( cvs ).isEmpty();

        cpt.setMeasurementText6kwaliteitsklasse( "1" );
        cvs = validator.validate( cpt, ImbroA.class, Default.class );
        assertThat( cvs.size() ).isEqualTo( 1 );

        cpt.setMeasurementText6kwaliteitsklasse( "2" );
        cvs = validator.validate( cpt, ImbroA.class, Default.class );
        assertThat( cvs.size() ).isEqualTo( 1 );

        cpt.setMeasurementText6kwaliteitsklasse( "3" );
        cvs = validator.validate( cpt, ImbroA.class, Default.class );
        assertThat( cvs.size() ).isEqualTo( 1 );

        cpt.setMeasurementText6kwaliteitsklasse( "4" );
        cvs = validator.validate( cpt, ImbroA.class, Default.class );
        assertThat( cvs.size() ).isEqualTo( 1 );

        cpt.setMeasurementText6kwaliteitsklasse( "5" );
        cvs = validator.validate( cpt, ImbroA.class, Default.class );
        assertThat( cvs ).isEmpty();

        cpt.setMeasurementText6kwaliteitsklasse( "6" );
        cvs = validator.validate( cpt, ImbroA.class, Default.class );
        assertThat( cvs ).isEmpty();

        cpt.setMeasurementText6kwaliteitsklasse( "7" );
        cvs = validator.validate( cpt, ImbroA.class, Default.class );
        assertThat( cvs ).isEmpty();
    }

    @Test
    public void testBoundaryConditions1() {

        Validator validator = GefValidation.buildFactory().getValidator();

        cpt.setMeasurementText6sondeernorm( null );

        cpt.setMeasurementText6kwaliteitsklasse( null );
        Set<ConstraintViolation<GefCptFile>> cvs = validator.validate( cpt, ImbroA.class, Default.class );
        assertThat( cvs ).isEmpty();
    }

    @Test
    public void testBoundaryConditions2() {

        Validator validator = GefValidation.buildFactory().getValidator();

        cpt.setMeasurementText6sondeernorm( null );

        cpt.setMeasurementText6kwaliteitsklasse( null );
        Set<ConstraintViolation<GefCptFile>> cvs = validator.validate( cpt, Imbro.class, Default.class );
        assertThat( cvs.size() ).isEqualTo( 2 );
    }
}
