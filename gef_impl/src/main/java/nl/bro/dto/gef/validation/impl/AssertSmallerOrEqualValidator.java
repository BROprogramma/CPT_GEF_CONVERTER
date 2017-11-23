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



import static nl.bro.dto.gef.validation.support.ELResolver.resolve;
import static nl.bro.dto.gef.validation.support.GefMessageInterpolator.createSubstitutes;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

import nl.bro.dto.gef.validation.AssertSmallerOrEqual;

public class AssertSmallerOrEqualValidator implements ConstraintValidator<AssertSmallerOrEqual, Object> {

    private AssertSmallerOrEqual annotation;

    @Override
    public void initialize(AssertSmallerOrEqual annotation) {
        this.annotation = annotation;
    }

    @Override
    public boolean isValid(Object validatedObject, ConstraintValidatorContext context) {

        Comparable refValue = (Comparable) resolve( annotation.referenceAttribute(), validatedObject );
        Comparable value = (Comparable) resolve( annotation.attribute(), validatedObject );

        boolean valid = true;

        if ( validatedObject != null && refValue != null && value != null ) {
            valid = value.compareTo( refValue ) <= 0;
        }

        if ( !valid ) {

            createSubstitutes( annotation, validatedObject ).addAttribute( "${referencedAttribute}", annotation.referenceAttribute() )
                                                            .addValue( "${referencedValue}", refValue )
                                                            .addAttribute( "${attrib}", annotation.attribute() )
                                                            .addValue( "${attribValue}", value );

        }

        return valid;
    }

}
