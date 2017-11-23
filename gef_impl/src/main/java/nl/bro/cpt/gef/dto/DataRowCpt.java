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

import nl.bro.dto.gef.validation.AssertSmallerOrEqual;

@AssertSmallerOrEqual( attribute = "${datablockci11}", referenceAttribute = "${datablockci1}" )
public class DataRowCpt extends DataRowFacade implements Serializable {

    private static final long serialVersionUID = -5909071347828866632L;

    /* datablock_columninfo1 */
    @NotNull
    @DecimalMin( value = "0.000" )
    @DecimalMax( value = "200.000" )
    private BigDecimal datablockci1;

    /* DATABLOCK_COLUMNINFO2 */
    @DecimalMin( value = "-1.000" )
    @DecimalMax( value = "200.000" )
    private BigDecimal datablockci2;

    /* DATABLOCK_COLUMNINFO3 */
    @DecimalMin( value = "-0.100" )
    @DecimalMax( value = "2.000" )
    private BigDecimal datablockci3;

    /* DATABLOCK_COLUMNINFO4 */
    @DecimalMin( value = "0.0" )
    @DecimalMax( value = "100.1" )
    private BigDecimal datablockci4;

    /* DATABLOCK_COLUMNINFO5 */
    @DecimalMin( value = "-1.000" )
    @DecimalMax( value = "10.000" )
    private BigDecimal datablockci5;

    /* DATABLOCK_COLUMNINFO6 */
    @DecimalMin( value = "-1.000" )
    @DecimalMax( value = "10.000" )
    private BigDecimal datablockci6;

    /* DATABLOCK_COLUMNINFO7 */
    @DecimalMin( value = "-1.000" )
    @DecimalMax( value = "10.000" )
    private BigDecimal datablockci7;

    /* DATABLOCK_COLUMNINFO8 */
    @DecimalMin( value = "0" )
    @DecimalMax( value = "20" )
    private BigDecimal datablockci8;

    /* DATABLOCK_COLUMNINFO9 */
    @DecimalMin( value = "-20" )
    @DecimalMax( value = "20" )
    private BigDecimal datablockci9;

    /* DATABLOCK_COLUMNINFO10 */
    @DecimalMin( value = "-20" )
    @DecimalMax( value = "20" )
    private BigDecimal datablockci10;

    /*
     * DATABLOCK_COLUMNINFO11 Controle: =< sondeerlengte in kolom 1
     */
    @DecimalMin( value = "0.000" )
    @DecimalMax( value = "200.000" )
    private BigDecimal datablockci11;

    /* DATABLOCK_COLUMNINFO12 */
    @DecimalMin( value = "0.0" )
    @DecimalMax( value = "68400.0" )
    private BigDecimal datablockci12;

    /* DATABLOCK_COLUMNINFO13 */
    @DecimalMin( value = "-1.000" )
    @DecimalMax( value = "200.000" )
    private BigDecimal datablockci13;

    /* DATABLOCK_COLUMNINFO14 */
    @DecimalMin( value = "-1.000" )
    @DecimalMax( value = "200.000" )
    private BigDecimal datablockci14;

    /* DATABLOCK_COLUMNINFO15 */
    @DecimalMin( value = "-1.000" )
    @DecimalMax( value = "20.000" )
    private BigDecimal datablockci15;

    /* DATABLOCK_COLUMNINFO21 */
    @DecimalMin( value = "-20" )
    @DecimalMax( value = "20" )
    private BigDecimal datablockci21;

    /* DATABLOCK_COLUMNINFO22 */
    @DecimalMin( value = "-20" )
    @DecimalMax( value = "20" )
    private BigDecimal datablockci22;

    /* DATABLOCK_COLUMNINFO23 */
    @DecimalMin( value = "0.000" )
    @DecimalMax( value = "10.000" )
    private BigDecimal datablockci23;

    /* DATABLOCK_COLUMNINFO31 */
    @DecimalMin( value = "-100000" )
    @DecimalMax( value = "100000" )
    private BigDecimal datablockci31;

    /* DATABLOCK_COLUMNINFO32 */
    @DecimalMin( value = "-100000" )
    @DecimalMax( value = "100000" )
    private BigDecimal datablockci32;

    /* DATABLOCK_COLUMNINFO33 */
    @DecimalMin( value = "-100000" )
    @DecimalMax( value = "100000" )
    private BigDecimal datablockci33;

    /* DATABLOCK_COLUMNINFO34 */
    @DecimalMin( value = "-100000" )
    @DecimalMax( value = "100000" )
    private BigDecimal datablockci34;

    /* DATABLOCK_COLUMNINFO35 */
    @DecimalMin( value = "-20" )
    @DecimalMax( value = "20" )
    private BigDecimal datablockci35;

    /* DATABLOCK_COLUMNINFO36 */
    @DecimalMin( value = "-20" )
    @DecimalMax( value = "20" )
    private BigDecimal datablockci36;

    /* DATABLOCK_COLUMNINFO39 */
    @DecimalMin( value = "-20.0" )
    @DecimalMax( value = "160.0" )
    private BigDecimal datablockci39;

    /**
     * @return the datablockci1
     */
    @Override
    public BigDecimal getDatablockci1() {
        return datablockci1;
    }

    /*
     * (non-Javadoc)
     * @see nl.bro.cpt.gef.dto.DataRow#setDatablockci1(java.math.BigDecimal)
     */
    @Override
    public void setDatablockci1(BigDecimal datablockci1) {
        this.datablockci1 = datablockci1;
    }

    /**
     * @return the datablockci2
     */
    @Override
    public BigDecimal getDatablockci2() {
        return datablockci2;
    }

    /*
     * (non-Javadoc)
     * @see nl.bro.cpt.gef.dto.DataRow#setDatablockci2(java.math.BigDecimal)
     */
    @Override
    public void setDatablockci2(BigDecimal datablockci2) {
        this.datablockci2 = datablockci2;
    }

    /**
     * @return the datablockci3
     */
    @Override
    public BigDecimal getDatablockci3() {
        return datablockci3;
    }

    /*
     * (non-Javadoc)
     * @see nl.bro.cpt.gef.dto.DataRow#setDatablockci3(java.math.BigDecimal)
     */
    @Override
    public void setDatablockci3(BigDecimal datablockci3) {
        this.datablockci3 = datablockci3;
    }

    /**
     * @return the datablockci4
     */
    @Override
    public BigDecimal getDatablockci4() {
        return datablockci4;
    }

    /*
     * (non-Javadoc)
     * @see nl.bro.cpt.gef.dto.DataRow#setDatablockci4(java.math.BigDecimal)
     */
    @Override
    public void setDatablockci4(BigDecimal datablockci4) {
        this.datablockci4 = datablockci4;
    }

    /**
     * @return the datablockci5
     */
    @Override
    public BigDecimal getDatablockci5() {
        return datablockci5;
    }

    /*
     * (non-Javadoc)
     * @see nl.bro.cpt.gef.dto.DataRow#setDatablockci5(java.math.BigDecimal)
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

    /*
     * (non-Javadoc)
     * @see nl.bro.cpt.gef.dto.DataRow#setDatablockci6(java.math.BigDecimal)
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

    /*
     * (non-Javadoc)
     * @see nl.bro.cpt.gef.dto.DataRow#setDatablockci7(java.math.BigDecimal)
     */
    @Override
    public void setDatablockci7(BigDecimal datablockci7) {
        this.datablockci7 = datablockci7;
    }

    /**
     * @return the datablockci8
     */
    @Override
    public BigDecimal getDatablockci8() {
        return datablockci8;
    }

    /*
     * (non-Javadoc)
     * @see nl.bro.cpt.gef.dto.DataRow#setDatablockci8(java.math.BigDecimal)
     */
    @Override
    public void setDatablockci8(BigDecimal datablockci8) {
        this.datablockci8 = datablockci8;
    }

    /**
     * @return the datablockci9
     */
    @Override
    public BigDecimal getDatablockci9() {
        return datablockci9;
    }

    /*
     * (non-Javadoc)
     * @see nl.bro.cpt.gef.dto.DataRow#setDatablockci9(java.math.BigDecimal)
     */
    @Override
    public void setDatablockci9(BigDecimal datablockci9) {
        this.datablockci9 = datablockci9;
    }

    /**
     * @return the datablockci10
     */
    @Override
    public BigDecimal getDatablockci10() {
        return datablockci10;
    }

    /*
     * (non-Javadoc)
     * @see nl.bro.cpt.gef.dto.DataRow#setDatablockci10(java.math.BigDecimal)
     */
    @Override
    public void setDatablockci10(BigDecimal datablockci10) {
        this.datablockci10 = datablockci10;
    }

    /**
     * @return the datablockci11
     */
    @Override
    public BigDecimal getDatablockci11() {
        return datablockci11;
    }

    /*
     * (non-Javadoc)
     * @see nl.bro.cpt.gef.dto.DataRow#setDatablockci11(java.math.BigDecimal)
     */
    @Override
    public void setDatablockci11(BigDecimal datablockci11) {
        this.datablockci11 = datablockci11;
    }

    /**
     * @return the datablockci12
     */
    @Override
    public BigDecimal getDatablockci12() {
        return datablockci12;
    }

    /*
     * (non-Javadoc)
     * @see nl.bro.cpt.gef.dto.DataRow#setDatablockci12(java.math.BigDecimal)
     */
    @Override
    public void setDatablockci12(BigDecimal datablockci12) {
        this.datablockci12 = datablockci12;
    }

    /**
     * @return the datablockci13
     */
    @Override
    public BigDecimal getDatablockci13() {
        return datablockci13;
    }

    /*
     * (non-Javadoc)
     * @see nl.bro.cpt.gef.dto.DataRow#setDatablockci13(java.math.BigDecimal)
     */
    @Override
    public void setDatablockci13(BigDecimal datablockci13) {
        this.datablockci13 = datablockci13;
    }

    /**
     * @return the datablockci14
     */
    @Override
    public BigDecimal getDatablockci14() {
        return datablockci14;
    }

    /*
     * (non-Javadoc)
     * @see nl.bro.cpt.gef.dto.DataRow#setDatablockci14(java.math.BigDecimal)
     */
    @Override
    public void setDatablockci14(BigDecimal datablockci14) {
        this.datablockci14 = datablockci14;
    }

    /**
     * @return the datablockci15
     */
    @Override
    public BigDecimal getDatablockci15() {
        return datablockci15;
    }

    /*
     * (non-Javadoc)
     * @see nl.bro.cpt.gef.dto.DataRow#setDatablockci15(java.math.BigDecimal)
     */
    @Override
    public void setDatablockci15(BigDecimal datablockci15) {
        this.datablockci15 = datablockci15;
    }

    /**
     * @return the datablockci21
     */
    @Override
    public BigDecimal getDatablockci21() {
        return datablockci21;
    }

    /*
     * (non-Javadoc)
     * @see nl.bro.cpt.gef.dto.DataRow#setDatablockci21(java.math.BigDecimal)
     */
    @Override
    public void setDatablockci21(BigDecimal datablockci21) {
        this.datablockci21 = datablockci21;
    }

    /**
     * @return the datablockci22
     */
    @Override
    public BigDecimal getDatablockci22() {
        return datablockci22;
    }

    /*
     * (non-Javadoc)
     * @see nl.bro.cpt.gef.dto.DataRow#setDatablockci22(java.math.BigDecimal)
     */
    @Override
    public void setDatablockci22(BigDecimal datablockci22) {
        this.datablockci22 = datablockci22;
    }

    /**
     * @return the datablockci23
     */
    @Override
    public BigDecimal getDatablockci23() {
        return datablockci23;
    }

    /*
     * (non-Javadoc)
     * @see nl.bro.cpt.gef.dto.DataRow#setDatablockci23(java.math.BigDecimal)
     */
    @Override
    public void setDatablockci23(BigDecimal datablockci23) {
        this.datablockci23 = datablockci23;
    }

    /**
     * @return the datablockci31
     */
    @Override
    public BigDecimal getDatablockci31() {
        return datablockci31;
    }

    /*
     * (non-Javadoc)
     * @see nl.bro.cpt.gef.dto.DataRow#setDatablockci31(java.math.BigDecimal)
     */
    @Override
    public void setDatablockci31(BigDecimal datablockci31) {
        this.datablockci31 = datablockci31;
    }

    /**
     * @return the datablockci32
     */
    @Override
    public BigDecimal getDatablockci32() {
        return datablockci32;
    }

    /*
     * (non-Javadoc)
     * @see nl.bro.cpt.gef.dto.DataRow#setDatablockci32(java.math.BigDecimal)
     */
    @Override
    public void setDatablockci32(BigDecimal datablockci32) {
        this.datablockci32 = datablockci32;
    }

    /**
     * @return the datablockci33
     */
    @Override
    public BigDecimal getDatablockci33() {
        return datablockci33;
    }

    /*
     * (non-Javadoc)
     * @see nl.bro.cpt.gef.dto.DataRow#setDatablockci33(java.math.BigDecimal)
     */
    @Override
    public void setDatablockci33(BigDecimal datablockci33) {
        this.datablockci33 = datablockci33;
    }

    /**
     * @return the datablockci34
     */
    @Override
    public BigDecimal getDatablockci34() {
        return datablockci34;
    }

    /*
     * (non-Javadoc)
     * @see nl.bro.cpt.gef.dto.DataRow#setDatablockci34(java.math.BigDecimal)
     */
    @Override
    public void setDatablockci34(BigDecimal datablockci34) {
        this.datablockci34 = datablockci34;
    }

    /**
     * @return the datablockci35
     */
    @Override
    public BigDecimal getDatablockci35() {
        return datablockci35;
    }

    /*
     * (non-Javadoc)
     * @see nl.bro.cpt.gef.dto.DataRow#setDatablockci35(java.math.BigDecimal)
     */
    @Override
    public void setDatablockci35(BigDecimal datablockci35) {
        this.datablockci35 = datablockci35;
    }

    /**
     * @return the datablockci36
     */
    @Override
    public BigDecimal getDatablockci36() {
        return datablockci36;
    }

    /*
     * (non-Javadoc)
     * @see nl.bro.cpt.gef.dto.DataRow#setDatablockci36(java.math.BigDecimal)
     */
    @Override
    public void setDatablockci36(BigDecimal datablockci36) {
        this.datablockci36 = datablockci36;
    }

    /**
     * @return the datablockci39
     */
    @Override
    public BigDecimal getDatablockci39() {
        return datablockci39;
    }

    /*
     * (non-Javadoc)
     * @see nl.bro.cpt.gef.dto.DataRow#setDatablockci39(java.math.BigDecimal)
     */
    @Override
    public void setDatablockci39(BigDecimal datablockci39) {
        this.datablockci39 = datablockci39;
    }

}
