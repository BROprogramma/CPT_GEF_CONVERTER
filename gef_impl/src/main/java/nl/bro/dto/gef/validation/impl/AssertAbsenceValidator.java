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

import static nl.bro.cpt.gef.transform.MappingConstants.NOT_NULL;
import static nl.bro.cpt.gef.transform.MappingConstants.NULL;
import static nl.bro.dto.gef.validation.support.ELResolver.resolve;
import static nl.bro.dto.gef.validation.support.GefMessageInterpolator.createSubstitutes;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

import nl.bro.dto.gef.validation.AssertAbsence;

public class AssertAbsenceValidator implements ConstraintValidator<AssertAbsence, Object> {

    private AssertAbsence annotation;

    @Override
    public void initialize(AssertAbsence annotation) {
        this.annotation = annotation;
    }

    @Override
    public boolean isValid(Object validatedValue, ConstraintValidatorContext context) {

        // obtain indicationYesNo attribute value from EL
        Object ref = resolve( annotation.whenRef(), validatedValue );

        // determine whether thenThisFieldMustBeAbsent attribute should be absent
        boolean shouldBeAbsent;
        if ( NULL.equals( annotation.hasRefValue() ) ) {
            shouldBeAbsent = ref == null;
        }
        else if ( NOT_NULL.equals( annotation.hasRefValue() ) ) {
            shouldBeAbsent = ref != null;
        }
        else {
            shouldBeAbsent = annotation.hasRefValue().equals( ref );
        }

        // obtain thenThisFieldMustBeAbsent attribute value from EL
        Object targetObject = resolve( annotation.thenThisFieldMustBeAbsent(), validatedValue );

        boolean valid = true;

        // perform check(s) and determine message
        if ( shouldBeAbsent ) {
            valid = targetObject == null;
        }

        if ( !valid ) {
            // set message template
            String messageTemplate = null;
            if ( NULL.equals( annotation.hasRefValue() ) ) {
                messageTemplate = annotation.messagePresentOnNull();
            }
            else if ( NOT_NULL.equals( annotation.hasRefValue() ) ) {
                messageTemplate = annotation.messagePresentOnNotNull();
            }

            if ( messageTemplate != null ) {
                context.disableDefaultConstraintViolation();
                context.buildConstraintViolationWithTemplate( messageTemplate ).addConstraintViolation();
            }

            // replace custom fields in messages
            createSubstitutes( annotation, validatedValue ).addAttribute( "${refAttribute}", annotation.whenRef() )
                                                           .addAttribute( "${validatedAttribute}", annotation.thenThisFieldMustBeAbsent() )
                                                           .addValue( "${refValue}", ref );
        }

        return valid;
    }

}
