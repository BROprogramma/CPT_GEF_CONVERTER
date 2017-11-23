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
package nl.bro.dto.gef.validation.support;

import java.lang.annotation.Annotation;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.MissingResourceException;
import java.util.ResourceBundle;
import java.util.Set;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintViolation;
import javax.validation.MessageInterpolator;
import javax.validation.Path;
import javax.validation.Payload;
import javax.validation.metadata.ConstraintDescriptor;

import org.apache.log4j.Logger;
import org.hibernate.validator.messageinterpolation.ResourceBundleMessageInterpolator;
import org.hibernate.validator.messageinterpolation.ValueFormatterMessageInterpolator;
import org.hibernate.validator.resourceloading.PlatformResourceBundleLocator;
import org.hibernate.validator.spi.resourceloading.ResourceBundleLocator;

import com.google.common.base.Joiner;

/**
 * @author Sjaak Derksen
 */
public class GefMessageInterpolator implements MessageInterpolator {

    public static final String VALIDATED_ITEM = "${validatedEntityAttribute}";
    private static final Logger LOG = Logger.getLogger( GefMessageInterpolator.class );

    private static final ThreadLocal<MessagePostProcessor> THREAD_LOCAL_MP = new ThreadLocal<>();

    public static final String DEFAULT_VALIDATION_MESSAGES = "org.hibernate.validator.ValidationMessages";
    public static final String COMMON_VALIDATION_MESSAGES = "GefValidationMessages";

    private final GefResourceBundleLocator resourceBundleLocator;
    private final MessageInterpolator defaultMI;
    private Locale usedLocale;

    public GefMessageInterpolator() {
        this.resourceBundleLocator = new GefResourceBundleLocator();
        ResourceBundleMessageInterpolator rbmi = new ResourceBundleMessageInterpolator( resourceBundleLocator, true );
        this.defaultMI = new ValueFormatterMessageInterpolator( rbmi );
        this.usedLocale = Locale.getDefault();
    }

    public void init() {
        THREAD_LOCAL_MP.set( new MessagePostProcessor( this ) );
    }

    @Override
    public String interpolate(String message, Context ctx) {
        return interpolateMessage( message, ctx, Locale.getDefault() );
    }

    @Override
    public String interpolate(String message, Context ctx, Locale locale) {
        return interpolateMessage( message, ctx, locale );
    }

    private String interpolateMessage(String message, Context ctx, Locale locale) {
        usedLocale = locale;
        return defaultMI.interpolate( message, ctx, locale );
    }

    private ResourceBundle getUsedResourceBundle() {
        // return locator from cache
        return resourceBundleLocator.getResourceBundle( usedLocale );
    }

    private Locale getLocaleUsedInInterpolation() {
        return usedLocale;
    }

    private String get(String key, ResourceBundle rb) {
        try {
            return rb.getString( key );
        }
        catch ( MissingResourceException ex ) {
            LOG.debug( "Cannot find key: " + key, ex );
            return "{" + key + "}";
        }
    }

    public static Substitutes createSubstitutes(Annotation annotation, Object validatedObject) {
        Substitutes substitutes = new Substitutes();
        THREAD_LOCAL_MP.get().addMessageReplacement( annotation, validatedObject, substitutes );
        return substitutes;
    }

    public <T> Set<ConstraintViolation<T>> postProcess(Set<ConstraintViolation<T>> violations) {
        Set<ConstraintViolation<T>> outViolations = THREAD_LOCAL_MP.get().process( violations );
        // handle HV-655
        outViolations.addAll( THREAD_LOCAL_MP.get().applyHV665WorkAround( violations ) );
        THREAD_LOCAL_MP.remove();
        return outViolations;
    }

    public static class Substitutes {

        private final Map<String, List<Object>> replacements = new HashMap<>();

        private Substitutes() {
        }

        private Map<String, List<Object>> getSubstitutes() {
            return replacements;
        }

        public Substitutes addValue(String key, Object value) {
            if ( value != null ) {
                if ( !replacements.containsKey( key ) ) {
                    replacements.put( key, new ArrayList<>() );
                }
                replacements.get( key ).add( value );
            }
            return this;
        }

        public Substitutes addValues(String key, Collection<Object> values) {
            for ( Object value : values ) {
                addValue( key, value );
            }
            return this;
        }

        public Substitutes addAttribute(String key, String value) {
            if ( value != null ) {
                if ( !replacements.containsKey( key ) ) {
                    replacements.put( key, new ArrayList<>() );
                }
                replacements.get( key ).add( new AttributeExpression( value ) );
            }
            return this;
        }

        public Substitutes addAttributes(String key, Collection<String> values) {
            for ( String value : values ) {
                addAttribute( key, value );
            }
            return this;
        }
    }

    private static class AttributeExpression {

        private final String value;

        private AttributeExpression(String value) {
            if ( value.startsWith( "${" ) && value.endsWith( "}" ) ) {
                // its an EL value expression (attribute), relative to the baseNode.
                this.value = value.substring( 2, value.length() - 1 );
            }
            else {
                throw new IllegalArgumentException( "attribute is: " + value + " an attribute should be of form '${attribute.subattribute.etc}' " );
            }
        }

        private String formatWithBase(String base) {
            return "{" + base + "." + value + "}";
        }

    }

    private static final class GefResourceBundleLocator implements ResourceBundleLocator {

        private final ResourceBundleLocator commonResourceBundleLocator;
        private final ResourceBundleLocator defaultResourceBundleLocator;

        private final Map<Locale, ResourceBundle> bundles = new HashMap<>();

        private GefResourceBundleLocator() {
            this.commonResourceBundleLocator = new PlatformResourceBundleLocator( COMMON_VALIDATION_MESSAGES );
            this.defaultResourceBundleLocator = new PlatformResourceBundleLocator( DEFAULT_VALIDATION_MESSAGES );
        }

        @Override
        public ResourceBundle getResourceBundle(Locale locale) {

            if ( !bundles.containsKey( locale ) ) {
                ResourceBundle commonBundle = commonResourceBundleLocator.getResourceBundle( locale );
                ResourceBundle defaultBundle = defaultResourceBundleLocator.getResourceBundle( locale );
                bundles.put( locale, new GefResourceBundle( commonBundle, defaultBundle ) );
            }
            return bundles.get( locale );
        }
    }

    private static final class GefResourceBundle extends ResourceBundle {

        private final Set<String> keySet;
        private final Map<String, Object> values;

        private GefResourceBundle(ResourceBundle commonBundle, ResourceBundle defaultBundle) {

            keySet = new HashSet<>();

            keySet.addAll( Collections.list( commonBundle.getKeys() ) );
            keySet.addAll( Collections.list( defaultBundle.getKeys() ) );

            values = new HashMap<>();
            // entries are overridden in a hashmap. So now in reverse order
            Enumeration<String> defaultKeys = defaultBundle.getKeys();
            while ( defaultKeys.hasMoreElements() ) {
                String key = defaultKeys.nextElement();
                values.put( key, defaultBundle.getObject( key ) );
            }
            Enumeration<String> commonKeys = commonBundle.getKeys();
            while ( commonKeys.hasMoreElements() ) {
                String key = commonKeys.nextElement();
                values.put( key, commonBundle.getObject( key ) );
            }

        }

        @Override
        protected Object handleGetObject(String key) {
            if ( values.containsKey( key ) ) {
                return values.get( key );
            }
            else {
                throw new MissingResourceException( "missing", this.getClass().getName(), key );
            }
        }

        @Override
        public Enumeration<String> getKeys() {
            return new IteratorEnumeration<>( keySet.iterator() );
        }

    }

    private static class IteratorEnumeration<E> implements Enumeration<E> {

        private final Iterator<E> iterator;

        IteratorEnumeration(Iterator<E> iterator) {
            this.iterator = iterator;
        }

        @Override
        public E nextElement() {
            return iterator.next();
        }

        @Override
        public boolean hasMoreElements() {
            return iterator.hasNext();
        }

    }

    private static class MessagePostProcessor {

        /**
         * There can be more replacements for one combination constraintValidatorClass / validatedObject
         */
        private final Map<CompositeKey, Substitutes> replacementMap = new HashMap<>();

        private final GefMessageInterpolator mi;

        private MessagePostProcessor(GefMessageInterpolator interpolator) {
            this.mi = interpolator;
        }

        private void addMessageReplacement(Annotation annotation, Object validatedObject, Substitutes substitutes) {
            // init when empty.
            CompositeKey key = new CompositeKey( annotation, validatedObject );
            if ( !replacementMap.containsKey( key ) ) {
                replacementMap.put( key, substitutes );
            }
        }

        private <T> Set<ConstraintViolation<T>> process(Set<ConstraintViolation<T>> violations) {

            Set<ConstraintViolation<T>> result = new HashSet<>();
            for ( ConstraintViolation<T> violation : violations ) {

                // create the key, agnostic from lists (indices)
                StringBuilder baseNodeBuilder = new StringBuilder( violation.getRootBean().getClass().getSimpleName() );
                for ( Path.Node node : violation.getPropertyPath() ) {
                    if ( node.getName() != null ) {
                        baseNodeBuilder.append( "." ).append( node.getName() );
                    }
                }
                String baseNode = baseNodeBuilder.toString();

                // replace with all, by validators defined replacments
                String newMessage = replaceCustomMessages( violation, baseNode );

                // resolve the actual value of the base entityAttribute
                ResourceBundle rb = mi.getUsedResourceBundle();
                String validatedAttribute = mi.get( baseNode, rb );
                newMessage = newMessage.replace( VALIDATED_ITEM, validatedAttribute );

                // interpolate message again
                Context ctx = new GefContextWrapper( violation.getConstraintDescriptor(), violation.getLeafBean() );
                newMessage = mi.interpolateMessage( newMessage, ctx, mi.getLocaleUsedInInterpolation() );

                // create wrapper
                result.add( new GefConstraintViolationWrapper<>( violation, newMessage ) );
            }
            return result;
        }

        private String replaceCustomMessages(ConstraintViolation<?> violation, String baseNode) {

            String newMessage = violation.getMessage();
            Substitutes replacement = replacementMap.remove( new CompositeKey( violation ) );
            if ( replacement != null ) {
                for ( Map.Entry<String, List<Object>> entry : replacement.getSubstitutes().entrySet() ) {

                    String toBeSubstituted = entry.getKey();
                    List<String> substitions = new ArrayList<>();
                    for ( Object candidateSubstitution : entry.getValue() ) {
                        // there's a list when there are more values to be replaced for one key, normally there's
                        // only one entry.
                        addSubstitution( baseNode, substitions, candidateSubstitution );
                    }
                    newMessage = newMessage.replace( toBeSubstituted, Joiner.on( ", " ).join( substitions ) );
                }
            }
            return newMessage;

        }

        private void addSubstitution(String baseNode, List<String> substitions, Object candidateSubstitution) {
            if ( candidateSubstitution != null ) {

                if ( candidateSubstitution instanceof AttributeExpression ) {
                    substitions.add( ( (AttributeExpression) candidateSubstitution ).formatWithBase( baseNode ) );
                }
                else {
                    // in the future, we might consider adding some formatting syntax, just as the standard
                    // validatedValue message mi.
                    String candidateSubstitionStr = candidateSubstitution.toString();
                    substitions.add( candidateSubstitionStr );
                }
            }
        }

        /**
         * see https://hibernate.atlassian.net/browse/HV-665
         *
         * @param t <T>
         * @param reportedViolations set
         * @return the set additional violations that were missed due to HV-665
         */
        private <T> Set<ConstraintViolation<T>> applyHV665WorkAround(Set<ConstraintViolation<T>> reportedViolations) {
            Set<ConstraintViolation<T>> hv665Violations = new HashSet<>();
            for ( Map.Entry<CompositeKey, Substitutes> entry : replacementMap.entrySet() ) {
                for ( ConstraintViolation<T> reportedViolation : reportedViolations ) {

                    // The reported constraint is reported only once (instead of one time per annotation)
                    // So, there could be more violations to report. These will be based on the unhandled constraints
                    // in the replacement map.
                    Annotation reportedConstraint = reportedViolation.getConstraintDescriptor().getAnnotation();
                    Annotation actualConstraint = entry.getKey().constraintDescriptionAnnotation;
                    if ( actualConstraint.getClass().equals( reportedConstraint.getClass() ) ) {

                        // interpolate message again
                        ConstraintDescriptor cd = new HV655ConstraintDescriptorWrapper( reportedViolation.getConstraintDescriptor(), actualConstraint );
                        Context ctx = new GefContextWrapper( cd, reportedViolation.getLeafBean() );
                        String newMessage = mi.interpolateMessage( reportedViolation.getMessageTemplate(), ctx, mi.getLocaleUsedInInterpolation() );

                        // create new violation and add this to the set. This will be possible because the
                        // BroConstraintViolationWrapper adds an additional criteria to the equals test (the constraint
                        // descriptor, containing the actual constraint annotation)
                        ConstraintViolation hv665Violation = new GefConstraintViolationWrapper<>( reportedViolation, newMessage, cd );
                        hv665Violations.add( hv665Violation );
                    }
                }
            }
            return THREAD_LOCAL_MP.get().process( hv665Violations );

        }
    }

    /**
     * This composite key is designed in such a way that in encompasses both 2 state determining objects in a validator:
     * {@link CompositeKey#constraintDescriptionAnnotation} and {@link CompositeKey#validatedObject} There is no other
     * way to bring about a state change in a validator that could result into a different message unless statics or a
     * thread-local are used to bypass the {@link ConstraintValidator} methods, therefore these keys should be
     * sufficient to uniquely identify a message to be changed.
     */
    private static class CompositeKey {

        private final Annotation constraintDescriptionAnnotation;
        private final Object validatedObject;

        private CompositeKey(Annotation annotation, Object validatedObject) {
            this.constraintDescriptionAnnotation = annotation;
            this.validatedObject = validatedObject;
        }

        private CompositeKey(ConstraintViolation violation) {
            this.constraintDescriptionAnnotation = violation.getConstraintDescriptor().getAnnotation();
            this.validatedObject = violation.getInvalidValue();
        }

        @Override
        public int hashCode() {
            int hash = 5;
            hash = 59 * hash + ( this.constraintDescriptionAnnotation != null ? this.constraintDescriptionAnnotation.hashCode() : 0 );
            hash = 59 * hash + ( this.validatedObject != null ? this.validatedObject.hashCode() : 0 );
            return hash;
        }

        @Override
        public boolean equals(Object obj) {
            if ( obj == null ) {
                return false;
            }
            if ( getClass() != obj.getClass() ) {
                return false;
            }
            final CompositeKey other = (CompositeKey) obj;
            return areEqualByObjectReference( this.validatedObject, other.validatedObject );
        }

        // zie artf52743. Hier wordt soms de ${dateValue} niet ingevuld, omdat de BroDate.equals niet gebruikt dient te
        // worden
        // voor deze controle.
        private boolean areEqualByObjectReference(Object in1, Object in2) {
            if ( in1 != null && in2 != null ) {
                return in1 == in2;
            }
            else {
                return in1 == null && in2 == null;
            }
        }
    }

    private static class GefConstraintViolationWrapper<T> implements ConstraintViolation<T> {

        private final ConstraintViolation<T> wrapped;
        private final String message;
        private final ConstraintDescriptor<?> descriptor; // HV-665

        private GefConstraintViolationWrapper(ConstraintViolation<T> wrapped, String message) {
            this.wrapped = wrapped;
            this.message = message;
            this.descriptor = null;
        }

        // HV665
        private GefConstraintViolationWrapper(ConstraintViolation<T> wrapped, String message, ConstraintDescriptor descriptor) {
            this.wrapped = wrapped;
            this.message = message;
            this.descriptor = descriptor;
        }

        @Override
        public String getMessage() {
            return message;
        }

        @Override
        public String getMessageTemplate() {
            return wrapped.getMessageTemplate();
        }

        @Override
        public T getRootBean() {
            return wrapped.getRootBean();
        }

        @Override
        public Class<T> getRootBeanClass() {
            return wrapped.getRootBeanClass();
        }

        @Override
        public Object getLeafBean() {
            return wrapped.getLeafBean();
        }

        @Override
        public Path getPropertyPath() {
            return wrapped.getPropertyPath();
        }

        @Override
        public Object getInvalidValue() {
            return wrapped.getInvalidValue();
        }

        @Override
        public ConstraintDescriptor<?> getConstraintDescriptor() {
            if ( descriptor == null ) {
                return wrapped.getConstraintDescriptor();
            }
            else {
                // HV665
                return descriptor;
            }
        }

        @Override
        public int hashCode() {
            int hash = 7;
            hash = 59 * hash + ( this.wrapped != null ? this.wrapped.hashCode() : 0 );
            return hash;
        }

        @Override
        public boolean equals(Object obj) {
            if ( obj == null ) {
                return false;
            }
            if ( getClass() != obj.getClass() ) {
                return false;
            }
            final GefConstraintViolationWrapper<?> other = (GefConstraintViolationWrapper<?>) obj;
            if ( this.wrapped != other.wrapped && ( this.wrapped == null || !this.wrapped.equals( other.wrapped ) ) ) {
                return false;
            }
            if ( this.message == null ? other.message != null : !this.message.equals( other.message ) ) {
                return false;
            }
            // HV665
            return this.descriptor == other.descriptor || ( this.descriptor != null && this.descriptor.equals( other.descriptor ) );
        }

    }

    private static class GefContextWrapper implements Context {

        private final ConstraintDescriptor<?> constraintDescriptor;
        private final Object validatedValue;

        GefContextWrapper(ConstraintDescriptor<?> constraintDescriptor, Object validatedValue) {
            this.constraintDescriptor = constraintDescriptor;
            this.validatedValue = validatedValue;
        }

        @Override
        public ConstraintDescriptor<?> getConstraintDescriptor() {
            return constraintDescriptor;
        }

        @Override
        public Object getValidatedValue() {
            return validatedValue;
        }

    }

    /**
     * Work-around, See https://hibernate.atlassian.net/browse/HV-665
     */
    private static class HV655ConstraintDescriptorWrapper implements ConstraintDescriptor<Annotation> {

        private final ConstraintDescriptor wrapped;
        private final Annotation annotation;

        private HV655ConstraintDescriptorWrapper(ConstraintDescriptor wrapped, Annotation annotation) {
            this.wrapped = wrapped;
            this.annotation = annotation;
        }

        @Override
        public Annotation getAnnotation() {
            return annotation;
        }

        @Override
        public Set<Class<?>> getGroups() {
            return wrapped.getGroups();
        }

        @Override
        public Set<Class<? extends Payload>> getPayload() {
            return wrapped.getPayload();
        }

        @Override
        public List<Class<? extends ConstraintValidator<Annotation, ?>>> getConstraintValidatorClasses() {
            return wrapped.getConstraintValidatorClasses();
        }

        @Override
        public Map<String, Object> getAttributes() {
            return wrapped.getAttributes();
        }

        @Override
        public Set<ConstraintDescriptor<?>> getComposingConstraints() {
            return wrapped.getComposingConstraints();
        }

        @Override
        public boolean isReportAsSingleViolation() {
            return wrapped.isReportAsSingleViolation();
        }

        @Override
        public int hashCode() {
            return 3;
        }

        @Override
        public boolean equals(Object obj) {
            if ( obj == null ) {
                return false;
            }
            if ( getClass() != obj.getClass() ) {
                return false;
            }
            final HV655ConstraintDescriptorWrapper other = (HV655ConstraintDescriptorWrapper) obj;
            boolean wrappedEquals = this.wrapped == other.wrapped || ( this.wrapped != null && this.wrapped.equals( other.wrapped ) );
            boolean annotationEquals = this.annotation == other.annotation || ( this.annotation != null && this.annotation.equals( other.annotation ) );
            return wrappedEquals && annotationEquals;
        }

    }

}
