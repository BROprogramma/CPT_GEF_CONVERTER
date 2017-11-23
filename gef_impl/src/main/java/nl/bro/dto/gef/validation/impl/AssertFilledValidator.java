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

import java.util.Collection;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

import nl.bro.dto.gef.validation.AssertFilled;

public class AssertFilledValidator implements ConstraintValidator<AssertFilled, Object> {

    private AssertFilled annotation;

    @Override
    public void initialize(AssertFilled annotation) {
        this.annotation = annotation;
    }

    @Override
    public boolean isValid(Object validatedValue, ConstraintValidatorContext context) {

        // obtain the whenRef from its EL expression
        Object ref = resolve( annotation.whenRef(), validatedValue );

        // is the collection expected to be filled based on the validator and indicationYesNoAttribute
        boolean shouldBeFilled;
        if ( NOT_NULL.equals( annotation.hasRefValue() ) ) {
            shouldBeFilled = ref != null;
        }
        else {
            shouldBeFilled = annotation.hasRefValue().equals( ref );
        }

        // the target object is the presumed collection, obtain it from its EL expression
        Object targetObject = resolve( annotation.thenThisCollectionMustBeFilled(), validatedValue );
        String newMessageTemplate = null;

        // always start out with valid
        boolean valid = true;

        // so, if its indeed a collection, we do only checks on a non null one
        // skip if no check shouldBeEmpty or shouldbeFilled is executed.
        if ( null != targetObject && targetObject instanceof Collection && shouldBeFilled ) {

            Collection targetCollection = (Collection) targetObject;
            valid = !targetCollection.isEmpty();

            // perhaps we should replace the default message template with one of the other two
            if ( !targetCollection.isEmpty() ) {
                if ( NULL.equals( annotation.hasRefValue() ) ) {
                    newMessageTemplate = annotation.messageFilledOnNull();
                }
                else {
                    newMessageTemplate = annotation.messageFilledOnNo();
                }
            }
        }

        if ( !valid ) {
            if ( newMessageTemplate != null ) {
                // replace default message template with the new one
                context.disableDefaultConstraintViolation();
                context.buildConstraintViolationWithTemplate( newMessageTemplate ).addConstraintViolation();
            }
            createSubstitutes( annotation, validatedValue ).addAttribute( "${refAttribute}", annotation.whenRef() )
                                                           .addAttribute( "${validatedAttribute}", annotation.thenThisCollectionMustBeFilled() )
                                                           .addValue( "${refValue}", ref );

        }
        return valid;

    }
}
