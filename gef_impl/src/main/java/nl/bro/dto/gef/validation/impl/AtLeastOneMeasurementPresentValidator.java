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

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

import nl.bro.cpt.gef.dto.DataRowCpt;
import nl.bro.cpt.gef.dto.GefCptFile;
import nl.bro.dto.gef.validation.AtLeastOneMeasurementPresent;

/**
 * @author derksenjpam
 */
public class AtLeastOneMeasurementPresentValidator
    implements ConstraintValidator<AtLeastOneMeasurementPresent, GefCptFile> {

    private AtLeastOneMeasurementPresent constraint;

    @Override
    public void initialize(AtLeastOneMeasurementPresent annotation) {
        this.constraint = annotation;
    }

    @Override
    public boolean isValid(GefCptFile cptFile, ConstraintValidatorContext cvc) {

        boolean valid = true;
        if ( cptFile != null
            && ( cptFile.getMeasuredParameters().contains( constraint.column() ) || constraint.mandatory() ) ) {
            valid = checkDataBlock( cptFile );
        }
        return valid;
    }

    private boolean checkDataBlock(GefCptFile cptFile) {
        boolean valid = false;
        for ( DataRowCpt dataRow : cptFile.getDataBlock() ) {
            if ( dataRow.getFilledEntry( constraint.column() ) ) {
                valid = true;
                break;
            }
        }
        return valid;
    }

}
