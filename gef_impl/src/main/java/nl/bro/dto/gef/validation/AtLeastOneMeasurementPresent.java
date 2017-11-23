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
package nl.bro.dto.gef.validation;

import static java.lang.annotation.RetentionPolicy.RUNTIME;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Repeatable;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import javax.validation.Constraint;
import javax.validation.Payload;

import nl.bro.dto.gef.validation.impl.AtLeastOneMeasurementPresentValidator;

/**
 * Controlleerd ieder DataRow#DatablockciX over alle rijen. Indien gesteld is dat deze aanwezig moet zijn in de gemeten
 * parameters dan moet er tenminste 1 waarde aanwezig zijn. Indien de waarde verplicht is (mandatory) dan moet er
 * sowieso een waarde aanwezig zijn.
 *
 * @author derksenjpam
 */
@Target( { ElementType.TYPE } )
@Retention( RetentionPolicy.RUNTIME )
@Documented
@Repeatable(AtLeastOneMeasurementPresent.List.class)
@Constraint( validatedBy = AtLeastOneMeasurementPresentValidator.class )
public @interface AtLeastOneMeasurementPresent {

    /**
     * The measured var to which this applies.
     *
     * @return
     */
    String column() default "";

    boolean mandatory() default false;

    String message() default "{nl.bro.dto.gef.validation.AtLeastOneMeasurementPresent.message}";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};

    @Target( { ElementType.TYPE, ElementType.ANNOTATION_TYPE } )
    @Retention( RUNTIME )
    @Documented
    @interface List {

        AtLeastOneMeasurementPresent[] value();
    }
}
