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

import static java.lang.annotation.ElementType.ANNOTATION_TYPE;
import static java.lang.annotation.ElementType.TYPE;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Repeatable;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import javax.validation.Constraint;
import javax.validation.Payload;

import nl.bro.dto.gef.validation.impl.AssertAbsenceValidator;

/**
 * Performs the following test(s): <br>
 ABSENCE: value of thenThisFieldMustBeAbsent attribute should be <code>null</code> if value of indicationYesNo attribute equals
 * <code>hasRefValue</code> value
 * <p>
 * <code>hasRefValue</code> value may be: a String value, {@link BroConstants#NOT_NULL} or {@link BroConstants#NULL} <br>
 * <code>hasRefValue</code> value may be: a String value, {@link BroConstants#NOT_NULL} or {@link BroConstants#NULL}
 */
@Target( { TYPE, ANNOTATION_TYPE } )
@Retention( RetentionPolicy.RUNTIME )
@Documented
@Constraint( validatedBy = AssertAbsenceValidator.class )
@Repeatable( AssertAbsence.List.class )
public @interface AssertAbsence {

    /**
     * @return Indicates that thenThisFieldMustBeAbsent attribute is present while indicationYesNo attribute equals hasRefValue() value
     */
    String message() default "{nl.bro.dto.gef.validation.AssertAbsence.message}";

    /**
     * @return Indicates that thenThisFieldMustBeAbsent attribute is present while indicationYesNo attribute is not present (null)
     */
    String messagePresentOnNull() default "{nl.bro.dto.gef.validation.AssertAbsence.messagePresentOnNull}";

    /**
     * @return Indicates that thenThisFieldMustBeAbsent attribute is present while indicationYesNo attribute is present (not null)
     */
    String messagePresentOnNotNull() default "{nl.bro.dto.gef.validation.AssertAbsence.messagePresentOnNotNull}";


    /**
     * @return Object attribute (. notation) which is compared to the ${@link #hasRefPresenceValue} and/or
     *         ${@link #hasRefValue} value.
     */
    String whenRef();

    /**
     * @return Object attribute (. notation) which is evaluated to be not null (PRESENCE) or null (ABSENCE)
     */
    String thenThisFieldMustBeAbsent();

    /**
     * The value of the attribute referenced by {@link AssertPresence#whenRef() } for which the presence of the
     * attribute / entity referenced by {@link AssertPresence#thenField() } should be absent.
     * <p>
     * Special value: {@link AssertPresence#NULL } means, when attribute {@link AssertPresence#whenRef() } is
     * {@code null} thenThisFieldMustBeAbsent the attribute / entity pointed out by {@link AssertPresence#thenField() } should be
     * absent.
     * <p>
     * Special value: {@link AssertPresence#NOT_NULL } means, when attribute {@link AssertPresence#whenRef() } is
     * {@code not null} thenThisFieldMustBeAbsent the attribute / entity pointed out by {@link AssertPresence#thenField() } should be
     * absent.
     *
     * @return
     */
    String hasRefValue();

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};

    @Target( { ElementType.TYPE, ElementType.ANNOTATION_TYPE } )
    @Retention( RUNTIME )
    @Documented
    @interface List {

        AssertAbsence[] value();
    }
}
