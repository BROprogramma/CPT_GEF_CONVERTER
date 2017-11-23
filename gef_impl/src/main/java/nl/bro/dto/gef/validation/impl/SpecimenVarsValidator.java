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

import static nl.bro.dto.gef.validation.support.GefMessageInterpolator.createSubstitutes;

import java.util.List;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

import nl.bro.cpt.gef.dto.SpecimenVar;
import nl.bro.dto.gef.validation.support.GefMessageInterpolator.Substitutes;
import nl.bro.dto.gef.validation.SpecimenVarsValid;

public class SpecimenVarsValidator implements ConstraintValidator<SpecimenVarsValid, List<SpecimenVar>> {

    private SpecimenVarsValid annotation;

    @Override
    public void initialize(SpecimenVarsValid constraintAnnotation) {

        annotation = constraintAnnotation;
    }

    @Override
    public boolean isValid(List<SpecimenVar> valueList, ConstraintValidatorContext context) {
        if ( valueList == null || valueList.isEmpty() ) {
            return true;
        }

        boolean valid = true;
        // Wanneer de specimenvars zijn ingevuld, dan dienen de laag definities te kloppen (dwz: de onderdiepte van
        // een laag moet lager zijn dan de bovendiepte). Door de manier waarop GEF de verwijderde lagen definieert
        // hoeft dit alleen voor de onderste laag gecontroleerd te worden.
        SpecimenVar lastLayer = valueList.get( valueList.size() - 1 );
        if ( lastLayer.getOnderdiepte() != null && lastLayer.getBovendiepte() != null && lastLayer.getBovendiepte().compareTo( lastLayer.getOnderdiepte() ) > 0 ) {
            valid = false;
        }

        if ( !valid ) {
            // allways add the values, they will always be printed.
            Substitutes substitutes = createSubstitutes( annotation, valueList );
            substitutes.addValue( "${upperdepth}", lastLayer.getBovendiepte() );
            substitutes.addValue( "${predrilledDepth}", lastLayer.getOnderdiepte() );
        }

        return valid;
    }

}
