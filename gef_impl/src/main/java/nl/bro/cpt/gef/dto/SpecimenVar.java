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
package nl.bro.cpt.gef.dto;

import java.io.Serializable;
import java.math.BigDecimal;

import javax.validation.constraints.DecimalMax;
import javax.validation.constraints.DecimalMin;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import nl.bro.dto.gef.groups.Imbro;

public class SpecimenVar implements Serializable {

    private static final long serialVersionUID = -5909071347828866632L;

    /* specimenvarvolgnummer */
    @NotNull
    @Min( value = 1, groups = Imbro.class )
    @Max( value = 99, groups = Imbro.class )
    private Integer specimenvarVolgnummer;

    /* specimenvarbovendiepte */
    @NotNull
    @DecimalMin( value = "0.00" )
    @DecimalMax( value = "99.99" )
    private BigDecimal specimenvarBovendiepte;

    /* specimenvaronderdiepte */
    @NotNull
    @DecimalMin( value = "0.00" )
    @DecimalMax( value = "99.99" )
    private BigDecimal specimenvarOnderdiepte;

    /* specimenvarbeschrijving */
    @NotNull
    @Size( max = 200 )
    private String specimenvarBeschrijving;

    /**
     * @return the specimenvarvolgnummer
     */
    public Integer getVolgnummer() {
        return specimenvarVolgnummer;
    }

    /**
     * @param specimenvarvolgnummer the specimenvarvolgnummer to set
     */
    public void setVolgnummer(Integer specimenvarVolgnummer) {
        this.specimenvarVolgnummer = specimenvarVolgnummer;
    }

    /**
     * @return the specimenvarbovendiepte
     */
    public BigDecimal getBovendiepte() {
        return specimenvarBovendiepte;
    }

    /**
     * @param specimenvarbovendiepte the specimenvarbovendiepte to set
     */
    public void setBovendiepte(BigDecimal specimenvarBovendiepte) {
        this.specimenvarBovendiepte = specimenvarBovendiepte;
    }

    /**
     * @return the specimenvaronderdiepte
     */
    public BigDecimal getOnderdiepte() {
        return specimenvarOnderdiepte;
    }

    /**
     * @param specimenvaronderdiepte the specimenvaronderdiepte to set
     */
    public void setOnderdiepte(BigDecimal specimenvarOnderdiepte) {
        this.specimenvarOnderdiepte = specimenvarOnderdiepte;
    }

    /**
     * @return the specimenvarbeschrijving
     */
    public String getBeschrijving() {
        return specimenvarBeschrijving;
    }

    /**
     * @param specimenvarbeschrijving the specimenvarbeschrijving to set
     */
    public void setBeschrijving(String specimenvarBeschrijving) {
        this.specimenvarBeschrijving = specimenvarBeschrijving;
    }

}
