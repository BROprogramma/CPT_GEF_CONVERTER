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
import javax.validation.constraints.NotNull;

public class DataRowDiss extends DataRowFacade implements Serializable {

    private static final long serialVersionUID = -5909071347828866632L;

    /* DATABLOCK_COLUMNINFO2 (conusweerstand) */
    @DecimalMin( value = "-1.000" )
    @DecimalMax( value = "200.000" )
    private BigDecimal datablockci2;

    /* DATABLOCK_COLUMNINFO5 (waterspanning u1) */
    @DecimalMin( value = "-1.000" )
    @DecimalMax( value = "10.000" )
    private BigDecimal datablockci5;

    /* DATABLOCK_COLUMNINFO6 (waterspanning u2) */
    @DecimalMin( value = "-1.000" )
    @DecimalMax( value = "10.000" )
    private BigDecimal datablockci6;

    /* DATABLOCK_COLUMNINFO7 (waterspanning u3) */
    @DecimalMin( value = "-1.000" )
    @DecimalMax( value = "10.000" )
    private BigDecimal datablockci7;

    /* DATABLOCK_COLUMNINFO12 */
    @NotNull
    @DecimalMin( value = "0.0" )
    @DecimalMax( value = "68400.0" )
    private BigDecimal datablockci12;

    /**
     * @return the datablockci2
     */
    @Override
    public BigDecimal getDatablockci2() {
        return datablockci2;
    }

    /**
     * @param datablockci2 the datablockci2 to set
     */
    @Override
    public void setDatablockci2(BigDecimal datablockci2) {
        this.datablockci2 = datablockci2;
    }

    /**
     * @return the datablockci5
     */
    @Override
    public BigDecimal getDatablockci5() {
        return datablockci5;
    }

    /**
     * @param datablockci5 the datablockci5 to set
     */
    @Override
    public void setDatablockci5(BigDecimal datablockci5) {
        this.datablockci5 = datablockci5;
    }

    /**
     * @return the datablockci6
     */
    @Override
    public BigDecimal getDatablockci6() {
        return datablockci6;
    }

    /**
     * @param datablockci6 the datablockci6 to set
     */
    @Override
    public void setDatablockci6(BigDecimal datablockci6) {
        this.datablockci6 = datablockci6;
    }

    /**
     * @return the datablockci7
     */
    @Override
    public BigDecimal getDatablockci7() {
        return datablockci7;
    }

    /**
     * @param datablockci7 the datablockci7 to set
     */
    @Override
    public void setDatablockci7(BigDecimal datablockci7) {
        this.datablockci7 = datablockci7;
    }

    @Override
    public BigDecimal getDatablockci12() {
        return datablockci12;
    }

    @Override
    public void setDatablockci12(BigDecimal datablockci21) {
        this.datablockci12 = datablockci21;
    }

}
