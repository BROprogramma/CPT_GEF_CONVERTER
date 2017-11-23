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

import nl.bro.dto.gef.validation.support.ValidationUtil;

import static nl.bro.dto.gef.validation.support.GefMessageInterpolator.createSubstitutes;

import java.util.Date;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

import nl.bro.cpt.gef.dto.GefDisFile;
import nl.bro.dto.gef.validation.support.GefMessageInterpolator.Substitutes;
import nl.bro.dto.gef.validation.IsCptStartBeforeDis;

/**
 * For more information on this solution, see {@link http
 * ://illegalargumentexception.blogspot.nl/2008/04/java-using-el-outside-j2ee.html}
 */
public class IsCptStartBeforeDisValidator implements ConstraintValidator<IsCptStartBeforeDis, GefDisFile> {

    private static final String ATTRIBUTE_NAME_CPT_START_DATE = "${cptStartDate}";
    private static final String ATTRIBUTE_NAME_CPT_START_TIME = "${cptStartTime}";
    private static final String ATTRIBUTE_NAME_DIS_START_DATE = "${startDate}";
    private static final String ATTRIBUTE_NAME_DIS_START_TIME = "${startTime}";

    private IsCptStartBeforeDis annotation;

    @Override
    public void initialize(IsCptStartBeforeDis annotation) {
        this.annotation = annotation;
    }

    @Override
    public boolean isValid(GefDisFile validatedObject, ConstraintValidatorContext context) {

        // init
        boolean valid = true;

        if ( validatedObject != null ) {

            // setup error message to date attributes / values
            String offendingCptAttributeName = ATTRIBUTE_NAME_CPT_START_DATE;
            String offendingCptValue = validatedObject.getCptStartDate();
            String offendingDisAttributeName = ATTRIBUTE_NAME_DIS_START_DATE;
            String offendingDisValue = validatedObject.getStartDate();

            // get date
            Date cptDate = ValidationUtil.parseIncompleteDate( offendingCptValue );
            Date disDate = ValidationUtil.parseIncompleteDate( offendingDisValue );

            if ( cptDate != null && disDate != null ) {

                // so, these are valid and non null dates, lets see if they are equal
                Date cptTime = ValidationUtil.parseTime( validatedObject.getCptStartTime() );
                Date disTime = ValidationUtil.parseTime( validatedObject.getStartTime() );

                if ( cptDate.equals( disDate ) && cptTime != null && disTime != null ) {
                    // date's are equal, there is time, so lets take time into consideration

                    // setup offending attribute names and values
                    offendingCptAttributeName = ATTRIBUTE_NAME_CPT_START_TIME;
                    offendingCptValue = validatedObject.getCptStartTime();
                    offendingDisAttributeName = ATTRIBUTE_NAME_DIS_START_TIME;
                    offendingDisValue = validatedObject.getStartTime();

                    valid = cptTime.before( disTime ) || cptTime.equals( disTime );
                }
                else {
                    valid = cptDate.before( disDate ) || disDate.equals( cptDate );
                }
            }

            if ( !valid ) {
                // allways add the values, they will always be printed.
                Substitutes substitutes = createSubstitutes( annotation, validatedObject );
                substitutes.addValue( "${futureValue}", offendingDisValue );
                substitutes.addValue( "${pastValue}", offendingCptValue );
                substitutes.addAttribute( "${futureAttribute}", offendingDisAttributeName );
                substitutes.addAttribute( "${pastAttribute}", offendingCptAttributeName );
            }
        }
        return valid;
    }
}
