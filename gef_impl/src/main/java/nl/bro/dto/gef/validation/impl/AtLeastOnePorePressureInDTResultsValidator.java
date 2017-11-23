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
package nl.bro.dto.gef.validation.impl;

import java.util.List;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

import nl.bro.cpt.gef.dto.DataRowDiss;
import nl.bro.dto.gef.validation.AtLeastOnePorePressureInDTResults;

/**
 * @author derksenjpam
 */
public class AtLeastOnePorePressureInDTResultsValidator implements ConstraintValidator<AtLeastOnePorePressureInDTResults, List<DataRowDiss>> {

    @Override
    public void initialize(AtLeastOnePorePressureInDTResults a) {
        // method needs to be implemented due to contract - empty implementation suffices in this case.
    }

    @Override
    public boolean isValid(List<DataRowDiss> dts, ConstraintValidatorContext cvc) {

        boolean valid = false;
        if ( dts == null ) {
            return valid;
        }

        for ( DataRowDiss dtRes : dts ) {

            if ( ( dtRes.getDatablockci5() != null ) || ( dtRes.getDatablockci6() != null ) || ( dtRes.getDatablockci7() != null ) ) {
                valid = true;
                break;
            }
        }

        return valid;

    }

}
