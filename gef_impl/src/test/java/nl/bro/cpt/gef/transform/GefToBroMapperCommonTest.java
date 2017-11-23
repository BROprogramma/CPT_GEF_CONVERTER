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

import static javax.xml.datatype.DatatypeConstants.FIELD_UNDEFINED;
import static org.fest.assertions.Assertions.assertThat;

import java.text.ParseException;

import javax.xml.datatype.DatatypeConfigurationException;
import javax.xml.datatype.DatatypeFactory;
import javax.xml.datatype.XMLGregorianCalendar;

import org.junit.Test;

import nl.bro.cpt.gef.dto.GefCptFile;
import nl.bro.cpt.gef.dto.GefCptSurvey;
import nl.broservices.xsd.brocommon.v_3_0.IndicationYesNoEnumeration;
import nl.broservices.xsd.brocommon.v_3_0.PartialDateType;

import net.opengis.gml.v_3_2_1.TimeInstantPropertyType;

public class GefToBroMapperCommonTest {


    @Test
    public void testGetObjectIdAccountableParty() {
        GefCptSurvey survey = new GefCptSurvey();
        GefCptFile file = new GefCptFile();
        survey.setGefCptFile( file );

        file.setProjectId( MappingConstants.NEGEREN );
        file.setTestId( "Test" );

        String result =  GefToBroMapperCommon.getInstance().getObjectIdAccountableParty( survey.getGefCptFile() );
        assertThat( result ).isEqualTo( "Test" );

        file.setProjectId( "Project" );
        result =  GefToBroMapperCommon.getInstance().getObjectIdAccountableParty( survey.getGefCptFile() );
        assertThat( result ).isEqualTo( "Project/Test" );
    }

    @Test
    public void testMeasuredParamsToBro() {
        GefCptSurvey survey = new GefCptSurvey();
        GefCptFile file = new GefCptFile();
        survey.setGefCptFile( file );

        file.getMeasuredParameters().add( "1" );

        IndicationYesNoEnumeration result = GefToBroMapperCommon.getInstance().measuredParamToBro( survey, 1 );
        assertThat( result ).isEqualTo( IndicationYesNoEnumeration.JA );

        result = GefToBroMapperCommon.getInstance().measuredParamToBro( survey, 2 );
        assertThat( result ).isEqualTo( IndicationYesNoEnumeration.NEE );
    }

    @Test
    public void testDateMapping() throws ParseException, DatatypeConfigurationException {

        GefToBroMapperCommon mapper = GefToBroMapperCommon.getInstance();
        PartialDateType date = mapper.gefToPartialDate( "1975, 01, 25" );

        XMLGregorianCalendar xcal = DatatypeFactory.newInstance().newXMLGregorianCalendar(
            1975, //
            1, //
            25,  //
            FIELD_UNDEFINED, //
            FIELD_UNDEFINED, //
            FIELD_UNDEFINED, //
            FIELD_UNDEFINED, //
            FIELD_UNDEFINED ); //

        assertThat( date ).isNotNull();
        assertThat( date.getDate() ).isEqualTo( xcal );
    }

    @Test
    public void testDateTimeMapping() throws ParseException, DatatypeConfigurationException {

        GefToBroMapperCommon mapper = GefToBroMapperCommon.getInstance();
        TimeInstantPropertyType dateTime = mapper.gefToTimeInstantProperty( "1975, 01, 25", "16, 00, 00" );

        XMLGregorianCalendar xcal = DatatypeFactory.newInstance().newXMLGregorianCalendar(
            1975, //
            1, //
            25, //
            16, //
            0, //
            0, //
            FIELD_UNDEFINED, //
            60 ); //

        assertThat( dateTime ).isNotNull();
        assertThat( dateTime.getTimeInstant().getTimePosition().getValue().get( 0 ) ).isEqualTo( xcal.toXMLFormat() );
    }

}
