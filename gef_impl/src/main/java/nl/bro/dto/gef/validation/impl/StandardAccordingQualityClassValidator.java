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

import java.util.Map;
import java.util.Set;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

import com.google.common.collect.ImmutableMap;
import com.google.common.collect.ImmutableSet;

import nl.bro.cpt.gef.dto.GefCptFile;
import nl.bro.dto.gef.validation.StandardAccordingQualityClass;

public class StandardAccordingQualityClassValidator implements ConstraintValidator<StandardAccordingQualityClass, GefCptFile> {

    private static final String UNKNOWN = "onbekend";

    private static final String CLASS1 = "1";
    private static final String CLASS2 = "2";
    private static final String CLASS3 = "3";
    private static final String CLASS4 = "4";
    private static final String CLASS5 = "5";
    private static final String CLASS6 = "6";
    private static final String CLASS7 = "7";

    // @formatter:off
    private static final Map<String, Set<String>> ALLOWED_COMBINATIONS = ImmutableMap.<String, Set<String>>builder()
        .put( "5140", ImmutableSet.<String>builder()
            .add( CLASS1 )
            .add( CLASS2 )
            .add( CLASS3 )
            .add( CLASS4 )
             // wordt niet afgewezen onder IMBRO/A regime:
            .add( UNKNOWN )
            .build() )
        .put( "22476-1", ImmutableSet.<String>builder()
            .add( CLASS1 )
            .add( CLASS2 )
            .add( CLASS3 )
            .add( CLASS4 )
            // wordt niet afgewezen onder IMBRO/A regime:
            .add( UNKNOWN )
            .build() )
        .put( "22476-12", ImmutableSet.<String>builder()
            .add( CLASS5 )
            .add( CLASS6 )
            .add( CLASS7 )
            // wordt niet afgewezen onder IMBRO/A regime
            .add( UNKNOWN )
            .build() )
        .put( "3680", ImmutableSet.<String>builder()
            .add( "nvt" )
            .build() )
        .put( UNKNOWN, ImmutableSet.<String>builder()
            // wordt niet afgewezen onder IMBRO/A regime
            .add( UNKNOWN )
            .build() )
        .build();
    // @formatter:on

    private StandardAccordingQualityClass annotation;

    @Override
    public void initialize(StandardAccordingQualityClass annotation) {
        this.annotation = annotation;
    }

    @Override
    public boolean isValid(GefCptFile cpt, ConstraintValidatorContext context) {

        boolean valid = true;

        if ( cpt != null ) {

            String klasse = cpt.getMeasurementText6kwaliteitsklasse();
            String qualityClass = klasse == null || klasse.isEmpty() ? UNKNOWN : klasse;

            String norm = cpt.getMeasurementText6sondeernorm();
            String standard = norm == null || norm.isEmpty() ? UNKNOWN : norm;

            Set<String> allowedCombinations = ALLOWED_COMBINATIONS.get( standard );
            if ( allowedCombinations != null ) {
                valid = allowedCombinations.contains( qualityClass );
            }
            else {
                valid = true;
            }

            if ( !valid ) {
                // just to add attribute / entity
                createSubstitutes( annotation, cpt ).addValue( "${qualityClassValue}", qualityClass ).addValue( "${standardValue}", standard );

            }
        }

        return valid;
    }
}
