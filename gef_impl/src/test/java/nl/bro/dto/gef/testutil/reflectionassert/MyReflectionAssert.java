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

import org.junit.Assert;
import org.unitils.reflectionassert.ReflectionAssert;
import org.unitils.reflectionassert.ReflectionComparator;
import org.unitils.reflectionassert.ReflectionComparatorMode;
import org.unitils.reflectionassert.difference.Difference;

import junit.framework.AssertionFailedError;

public class MyReflectionAssert extends ReflectionAssert {

    public static void assertReflectionEquals(Object expected, Object actual, ReflectionComparatorMode... modes) throws AssertionFailedError {
        assertReflectionEquals( null, expected, actual, modes );
    }

    public static void assertReflectionEquals(String message, Object expected, Object actual, ReflectionComparatorMode... modes) throws AssertionFailedError {
        ReflectionComparator reflectionComparator = MyReflectionComparatorFactory.createRefectionComparator( modes );
        Difference difference = reflectionComparator.getDifference( expected, actual );
        if ( difference != null ) {
            Assert.fail( getFailureMessage( message, difference ) );
        }
    }
}
