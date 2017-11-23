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

import javax.el.BeanELResolver;
import javax.el.ELContext;
import javax.el.ExpressionFactory;
import javax.el.FunctionMapper;
import javax.el.ValueExpression;
import javax.el.VariableMapper;

/**
 * @author Sjaak Derksen
 */
public class ELResolver {

    private static final ExpressionFactory EF = ExpressionFactory.newInstance();

    private ELResolver() {
    }

    public static Object resolve(String expression, Object root) {
        ELContext ctx = new ELContextImpl( root );
        ValueExpression propertyExpr = EF.createValueExpression( ctx, expression, Object.class );
        return propertyExpr.getValue( ctx );
    }

    private static class BeanELResolverImpl extends BeanELResolver {

        private Object lastBean;

        private BeanELResolverImpl(Object base) {
            super();
            this.lastBean = base;
        }

        @Override
        public Object getValue(ELContext context, Object beanIn, Object property) {
            if ( beanIn != null ) {
                lastBean = beanIn;
            }
            return super.getValue( context, lastBean, property );
        }

    }

    private static class ELContextImpl extends ELContext {

        private final BeanELResolverImpl resolver;
        private final VariableMapperImpl mapper;

        private ELContextImpl(Object base) {
            resolver = new BeanELResolverImpl( base );
            mapper = new VariableMapperImpl();
        }

        @Override
        public BeanELResolverImpl getELResolver() {
            return resolver;
        }

        @Override
        public FunctionMapper getFunctionMapper() {
            return null;
        }

        @Override
        public VariableMapper getVariableMapper() {
            return mapper;
        }

    }

    private static class VariableMapperImpl extends VariableMapper {

        @Override
        public ValueExpression resolveVariable(String propertyName) {
            return null;
        }

        @Override
        public ValueExpression setVariable(String string, ValueExpression ve) {
            throw new UnsupportedOperationException( "Not supposed to be called." );
        }

    }

}
