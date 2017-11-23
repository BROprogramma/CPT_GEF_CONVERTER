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

import static org.unitils.reflectionassert.ReflectionComparatorMode.IGNORE_DEFAULTS;
import static org.unitils.reflectionassert.ReflectionComparatorMode.LENIENT_DATES;
import static org.unitils.reflectionassert.ReflectionComparatorMode.LENIENT_ORDER;
import static org.unitils.util.CollectionUtils.asSet;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import org.unitils.reflectionassert.ReflectionComparator;
import org.unitils.reflectionassert.ReflectionComparatorFactory;
import org.unitils.reflectionassert.ReflectionComparatorMode;
import org.unitils.reflectionassert.comparator.Comparator;

public class MyReflectionComparatorFactory extends ReflectionComparatorFactory {

    public static ReflectionComparator createRefectionComparator(ReflectionComparatorMode... modes) {
        List<Comparator> comparators = getComparatorChain( asSet( modes ) );
        return new ReflectionComparator( comparators );
    }

    protected static List<Comparator> getComparatorChain(Set<ReflectionComparatorMode> modes) {
        List<Comparator> comparatorChain = new ArrayList<>();
        if ( modes.contains( LENIENT_DATES ) ) {
            comparatorChain.add( LENIENT_DATES_COMPARATOR );
        }
        if ( modes.contains( IGNORE_DEFAULTS ) ) {
            comparatorChain.add( IGNORE_DEFAULTS_COMPARATOR );
        }
        comparatorChain.add( LENIENT_NUMBER_COMPARATOR );
        comparatorChain.add( SIMPLE_CASES_COMPARATOR );
        if ( modes.contains( LENIENT_ORDER ) ) {
            comparatorChain.add( LENIENT_ORDER_COMPARATOR );
        }
        else {
            comparatorChain.add( COLLECTION_COMPARATOR );
        }
        comparatorChain.add( MAP_COMPARATOR );
        comparatorChain.add( HIBERNATE_PROXY_COMPARATOR );
        comparatorChain.add( new MyIdComparator() );
        return comparatorChain;
    }
}
