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
package nl.bro.dto.gef.testutil.reflectionassert;

import static java.lang.reflect.Modifier.isStatic;
import static java.lang.reflect.Modifier.isTransient;

import java.lang.reflect.AccessibleObject;
import java.lang.reflect.Field;

import org.unitils.reflectionassert.ReflectionComparator;
import org.unitils.reflectionassert.comparator.impl.ObjectComparator;
import org.unitils.reflectionassert.difference.Difference;
import org.unitils.reflectionassert.difference.ObjectDifference;

public class MyIdComparator extends ObjectComparator {

    private static final String ID = "id";

    @Override
    protected void compareFields(Object left, Object right, Class<?> clazz, ObjectDifference difference, boolean onlyFirstDifference, ReflectionComparator reflectionComparator) {
        Field[] fields = clazz.getDeclaredFields();
        AccessibleObject.setAccessible( fields, true );

        for ( Field field : fields ) {
            // skip transient and static fields
            if ( isTransient( field.getModifiers() ) || isStatic( field.getModifiers() ) || field.isSynthetic() ) {
                continue;
            }
            // skip id
            if ( String.class.getName().equals( field.getType().getName() ) && ID.equals( field.getName() ) ) {
                try {
                    Object leftVal =  field.get( left );
                    Object rightVal = field.get( right );
                    if ( ( leftVal == null && rightVal != null ) || ( leftVal != null && rightVal == null ) ) {
                        difference.addFieldDifference( field.getName(), new Difference( "IDs should be both 'null' or both 'not null': ", leftVal, rightVal) );
                        if ( onlyFirstDifference ) {
                            return;
                        }
                    }
                    continue;
                }
                catch ( IllegalArgumentException | IllegalAccessException ex ) {
                   throw new InternalError( "Unexpected IllegalAccessException" );
                }

            }

            try {
                // recursively check the value of the fields
                Difference innerDifference = reflectionComparator.getDifference( field.get( left ), field.get( right ), onlyFirstDifference );
                if ( innerDifference != null ) {
                    difference.addFieldDifference( field.getName(), innerDifference );
                    if ( onlyFirstDifference ) {
                        return;
                    }
                }
            }
            catch ( IllegalAccessException e ) {
                // this can't happen. Would get a Security exception instead
                // throw a runtime exception in case the impossible happens.
                throw new InternalError( "Unexpected IllegalAccessException" );
            }
        }

        // compare fields declared in superclass
        Class<?> superclazz = clazz.getSuperclass();
        while ( superclazz != null && !superclazz.getName().startsWith( "java.lang" ) ) {
            compareFields( left, right, superclazz, difference, onlyFirstDifference, reflectionComparator );
            superclazz = superclazz.getSuperclass();
        }
    }
}
