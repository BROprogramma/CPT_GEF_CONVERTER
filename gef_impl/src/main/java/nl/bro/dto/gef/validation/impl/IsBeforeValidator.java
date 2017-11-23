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

import nl.bro.dto.gef.validation.support.ValidationUtil;

import static nl.bro.dto.gef.validation.support.ELResolver.resolve;
import static nl.bro.dto.gef.validation.support.GefMessageInterpolator.createSubstitutes;

import java.util.Date;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

import nl.bro.dto.gef.validation.support.GefMessageInterpolator.Substitutes;
import nl.bro.dto.gef.validation.IsBefore;

/**
 * For more information on this solution, see {@link http
 * ://illegalargumentexception.blogspot.nl/2008/04/java-using-el-outside-j2ee.html}
 */
public class IsBeforeValidator implements ConstraintValidator<IsBefore, Object> {

    private IsBefore annotation;

    @Override
    public void initialize(IsBefore annotation) {
        this.annotation = annotation;
    }

    @Override
    public boolean isValid(Object validatedObject, ConstraintValidatorContext context) {

        // init
        boolean valid = true;

        if ( validatedObject != null ) {

            // obtain past date
            Date past = null;
            if ( annotation.past().startsWith( "$" ) ) {
                past = ValidationUtil.parseIncompleteDate( (String) resolve( annotation.past(), validatedObject ) );
            }

            // obtain future date
            Date future = null;
            if ( annotation.future().startsWith( "$" ) ) {
                future = ValidationUtil.parseIncompleteDate( (String) resolve( annotation.future(), validatedObject ) );
            }

            // do actual check
            if ( future != null && past != null ) {
                valid = past.equals( future ) || past.before( future );
            }

            if ( !valid ) {
                // allways add the values, they will always be printed.
                Substitutes substitutes = createSubstitutes( annotation, validatedObject );
                substitutes.addValue( "${futureValue}", future );
                substitutes.addValue( "${pastValue}", past );
                substitutes.addAttribute( "${futureAttribute}", annotation.future() );
                substitutes.addAttribute( "${pastAttribute}", annotation.past() );
            }
        }
        return valid;
    }

}
