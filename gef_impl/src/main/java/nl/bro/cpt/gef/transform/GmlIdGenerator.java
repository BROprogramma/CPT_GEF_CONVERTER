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
package nl.bro.cpt.gef.transform;

import java.util.concurrent.atomic.AtomicInteger;

import org.mapstruct.AfterMapping;

import net.opengis.gml.v_3_2_1.AbstractFeatureType;

/**
 * The intention of this class is to generate unique ID's within the scope of one SOAP transaction. ID's will not be
 * globally or VM unique. The ID Generator must be reset at entry of web service method scope.
 *
 * @author derksenjpam
 */
public class GmlIdGenerator {

    private static final ThreadLocal<AtomicInteger> THREAD_COUNTER = new ThreadLocal<>();
    private static final String PREFIX = "BRO_";

    public static final String EXPRESSION = "java( nl.bro.cpt.gef.transform.GmlIdGenerator.getNewId() )";

    public static void reset() {
        THREAD_COUNTER.remove();
    }

    public static String getNewId() {
        if ( THREAD_COUNTER.get() == null ) {
            // init
            THREAD_COUNTER.set( new AtomicInteger() );
        }
        int counter = THREAD_COUNTER.get()
                                    .incrementAndGet();
        // format in 4 digits, leading zero
        return PREFIX + String.format( "%04d", counter );
    }

    /**
     * can only be used ICMW update methods
     *
     * @param feature
     */
    @AfterMapping
    public void setIdOn(AbstractFeatureType feature) {
        feature.setId( getNewId() );
    }

}
