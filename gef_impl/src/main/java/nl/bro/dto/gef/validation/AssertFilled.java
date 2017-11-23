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

import nl.bro.dto.gef.validation.impl.AssertFilledValidator;


@Target( { TYPE, ANNOTATION_TYPE } )
@Retention( RetentionPolicy.RUNTIME )
@Documented
@Constraint( validatedBy = AssertFilledValidator.class )
@Repeatable( AssertFilled.List.class )
public @interface AssertFilled {

    /**
     * @return Default message. Indicates that an expected ({@link AssertFilled#whenRef()} equals 'hasRefValue') filled
     *         collection is empty
     */
    String message() default "{nl.bro.dto.gef.validation.AssertFilled.message}";

    /**
     * @return Indicates that an expected ({@link AssertFilled#whenRef()} equals 'no') empty collection is actually
     *         empty
     */
    String messageFilledOnNo() default "{nl.bro.dto.gef.validation.AssertFilled.messageFilledOnNo}";

    /**
     * @return Special case {@link AssertFilled#messageFilledOnNo()} of 'Indicates that an expected
     *         ({@link AssertFilled#whenRef()} equals null) empty collection is actually empty. Since there's a null
     *         value for the {@link AssertFilled#whenRefValue()}, the message cannot print this value as part of the
     *         message.
     */
    String messageFilledOnNull() default "{nl.bro.dto.gef.validation.AssertFilled.messageFilledOnNull}";

    /**
     * @return String pointing out an 'String attribute' (. notation) which is compared to the ${@link #hasRefValue} and
     *         ${@link #hasRefValue}. When ${@link #hasRefValue} the attribute pointed out with ${@link #notNull} should
     *         be non empty.
     */
    String whenRef();

    /**
     * @return Object attribute (. notation) which is evaluated not to be empty
     */
    String thenThisCollectionMustBeFilled();

    /**
     * @return The value of the attribute referenced by {@link AssertFilled#whenRef() } for which the presence of the
     *         attribute / entity referenced by {@link AssertFilled#hasRefValue() } should be filled. Special value:
     *         {@link BroConstants#NOT_NULL } means, when attribute {@link AssertFilled#whenRef() } is {@code <> null}
     *         then the attribute / entity pointed out by {@link AssertFilled#hasRefValue() } should not be empty. *
     */
    String hasRefValue();

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};

    @Target( { ElementType.TYPE, ElementType.ANNOTATION_TYPE } )
    @Retention( RUNTIME )
    @Documented
    @interface List {

        AssertFilled[] value();
    }
}
