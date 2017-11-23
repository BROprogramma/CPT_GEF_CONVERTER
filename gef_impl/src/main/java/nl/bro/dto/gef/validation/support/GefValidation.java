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
/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package nl.bro.dto.gef.validation.support;

import java.util.Set;

import javax.validation.ConstraintValidatorFactory;
import javax.validation.ConstraintViolation;
import javax.validation.MessageInterpolator;
import javax.validation.TraversableResolver;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorContext;
import javax.validation.ValidatorFactory;
import javax.validation.metadata.BeanDescriptor;

/**
 * @author Sjaak Derksen
 */
public class GefValidation {

    private GefValidation() {
    }

    public static ValidatorFactory buildFactory() {
        GefMessageInterpolator interpolator = new GefMessageInterpolator();
        ValidatorFactory factory = Validation.byDefaultProvider()
                                             .configure()
                                             .messageInterpolator( interpolator )
                                             .buildValidatorFactory();

        return new ValidatorFactoryDelegator( factory, interpolator );
    }

    public static class ValidatorDelegator implements Validator {

        private final Validator delegee;
        private final GefMessageInterpolator messageInterpolator;

        public ValidatorDelegator(Validator validator, GefMessageInterpolator messageInterpolator) {
            this.delegee = validator;
            this.messageInterpolator = messageInterpolator;
        }

        @Override
        public <T> Set<ConstraintViolation<T>> validate(T t, Class<?>... types) {
            messageInterpolator.init();
            Set<ConstraintViolation<T>> orig = delegee.validate( t, types );
            return messageInterpolator.postProcess( orig );
        }

        @Override
        public <T> Set<ConstraintViolation<T>> validateProperty(T t, String string, Class<?>... types) {
            messageInterpolator.init();
            Set<ConstraintViolation<T>> orig = delegee.validateProperty( t, string, types );
            return messageInterpolator.postProcess( orig );
        }

        @Override
        public <T> Set<ConstraintViolation<T>> validateValue(Class<T> type, String string, Object o, Class<?>... types) {
            messageInterpolator.init();
            Set<ConstraintViolation<T>> orig = delegee.validateValue( type, string, o, types );
            return messageInterpolator.postProcess( orig );
        }

        @Override
        public BeanDescriptor getConstraintsForClass(Class<?> type) {
            return delegee.getConstraintsForClass( type );
        }

        @Override
        public <T> T unwrap(Class<T> type) {
            return delegee.unwrap( type );
        }

    }

    public static class ValidatorFactoryDelegator implements ValidatorFactory {

        private final ValidatorFactory delegee;
        private final GefMessageInterpolator interpolator;

        public ValidatorFactoryDelegator(ValidatorFactory validatorFactory, GefMessageInterpolator interpolator) {
            this.delegee = validatorFactory;
            this.interpolator = interpolator;
        }

        @Override
        public Validator getValidator() {
            return new ValidatorDelegator( delegee.getValidator(), interpolator );
        }

        @Override
        public ValidatorContext usingContext() {
            return delegee.usingContext();
        }

        @Override
        public MessageInterpolator getMessageInterpolator() {
            return delegee.getMessageInterpolator();
        }

        @Override
        public TraversableResolver getTraversableResolver() {
            return delegee.getTraversableResolver();
        }

        @Override
        public ConstraintValidatorFactory getConstraintValidatorFactory() {
            return delegee.getConstraintValidatorFactory();
        }

        @Override
        public <T> T unwrap(Class<T> type) {
            return delegee.unwrap( type );
        }
    }

}
