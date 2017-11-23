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

import java.math.BigDecimal;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

import nl.bro.cpt.gef.dto.GefCptFile;
import nl.bro.dto.gef.validation.ConeResistanceZeroLoadMeasurementsValid;

public class ConeResistanceZeroLoadMeasurementsValidator implements ConstraintValidator<ConeResistanceZeroLoadMeasurementsValid, GefCptFile> {

    @Override
    public void initialize(ConeResistanceZeroLoadMeasurementsValid validAdditionalInvestigation) {
        // method needs to be implemented due to contract - empty implementation suffices in this case.
    }

    @Override
    public boolean isValid(GefCptFile cpt, ConstraintValidatorContext context) {

        boolean valid = true;

        if ( cpt != null && ( cpt.getMeasurementVar20() == null || cpt.getMeasurementVar21() == null ) ) {
            // alle andere null metingen moeten null zijn wanneer cone resistance null metingen null zijn
            BigDecimal[] allMeaValues = new BigDecimal[] { cpt.getMeasurementVar20(), cpt.getMeasurementVar21(),  //
                cpt.getMeasurementVar22(), cpt.getMeasurementVar23(),  //
                cpt.getMeasurementVar24(), cpt.getMeasurementVar25(),  //
                cpt.getMeasurementVar26(), cpt.getMeasurementVar27(),  //
                cpt.getMeasurementVar28(), cpt.getMeasurementVar29(),  //
                cpt.getMeasurementVar30(), cpt.getMeasurementVar31(),  //
                cpt.getMeasurementVar32(), cpt.getMeasurementVar33(),  //
                cpt.getMeasurementVar34(), cpt.getMeasurementVar35(),  //
                cpt.getMeasurementVar36(), cpt.getMeasurementVar37() }; //

            for ( BigDecimal val : allMeaValues ) {
                valid = valid && val == null;
            }
        }
        return valid;
    }

}
