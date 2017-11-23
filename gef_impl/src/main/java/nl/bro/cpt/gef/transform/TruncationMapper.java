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

import static java.math.BigDecimal.ROUND_DOWN;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.math.BigDecimal;

import org.mapstruct.Qualifier;

/**
 * @author J.M. van Ommeren
 */
public class TruncationMapper {

    private TruncationMapper() {
        // deliberately empty
    }

    @Qualifier
    @Target( ElementType.METHOD )
    @Retention( RetentionPolicy.CLASS )
    public static @interface Fraction0 {
    }

    @Qualifier
    @Target( ElementType.METHOD )
    @Retention( RetentionPolicy.CLASS )
    public static @interface Fraction1 {
    }

    @Qualifier
    @Target( ElementType.METHOD )
    @Retention( RetentionPolicy.CLASS )
    public static @interface Fraction2 {
    }

    @Qualifier
    @Target( ElementType.METHOD )
    @Retention( RetentionPolicy.CLASS )
    public static @interface Fraction3 {
    }

    @Fraction0
    public static BigDecimal toBigDecimal0(BigDecimal val) {
        return val != null ? truncate( val, 0 ) : null;
    }

    @Fraction1
    public static BigDecimal toBigDecimal1(BigDecimal val) {
        return val != null ? truncate( val, 1 ) : null;
    }

    @Fraction2
    public static BigDecimal toBigDecimal2(BigDecimal val) {
        return val != null ? truncate( val, 2 ) : null;
    }

    @Fraction3
    public static BigDecimal toBigDecimal3(BigDecimal val) {
        return val != null ? truncate( val, 3 ) : null;
    }

    /**
     * @param in value to be truncated or complemented with zeros
     * @param scale required scale (number of digits to the right of the decimal point)
     * @return BigDecimal with the specified scale; the returned object may or may not be newly allocated (the original
     *         object is never modified)
     */
    public static BigDecimal truncate(BigDecimal in, int scale) {
        BigDecimal result = null;
        if ( in != null ) {
            if ( in.scale() > scale ) {
                result = in.setScale( scale, ROUND_DOWN );
            }
            else {
                result = in.setScale( scale );
            }
        }

        return result;
    }

}
