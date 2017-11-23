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

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

import nl.bro.dto.gef.validation.ValidCode;

public class CodeValidator implements ConstraintValidator<ValidCode, String> {

    private Set<String> validCodes;
    private boolean caseSensitive;

    @Override
    public void initialize(ValidCode constraintAnnotation) {
        validCodes = new HashSet<>( Arrays.asList( constraintAnnotation.allowedCodes() ) );
        caseSensitive = constraintAnnotation.isCaseSensitive();
    }

    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        boolean isValid;
        if ( value == null ) {
            isValid = true;
        }
        else {
            if ( caseSensitive ) {
                isValid = caseSensitiveSearch( value );
            }
            else {
                isValid = caseInsensitiveSearch( value );
            }
        }
        return isValid;
    }

    private boolean caseInsensitiveSearch(String value) {
        boolean isValid;
        isValid = false;
        for ( String codeObj : validCodes ) {
            if ( codeObj.equalsIgnoreCase( value ) ) {
                isValid = true;
                break;
            }
        }
        return isValid;
    }

    private boolean caseSensitiveSearch(String value) {
        boolean isValid;
        isValid = validCodes.contains( value );
        return isValid;
    }

}
