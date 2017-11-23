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
import java.util.ArrayList;
import java.util.List;

import javax.validation.Valid;
import javax.validation.constraints.DecimalMax;
import javax.validation.constraints.DecimalMin;
import javax.validation.constraints.NotNull;

import org.hibernate.validator.constraints.NotBlank;

import nl.bro.dto.gef.groups.Imbro;
import nl.bro.dto.gef.groups.ImbroA;
import nl.bro.dto.gef.validation.AtLeastOnePorePressureInDTResults;
import nl.bro.dto.gef.validation.DateValid;
import nl.bro.dto.gef.validation.IncompleteDateValid;
import nl.bro.dto.gef.validation.IsCptStartBeforeDis;
import nl.bro.dto.gef.validation.IsNotBefore01011930;
import nl.bro.dto.gef.validation.TimeValid;
import nl.bro.dto.gef.validation.ValidCode;

@IsCptStartBeforeDis
public class GefDisFile extends GefFileFacade<DataRowDiss> implements Serializable, IfcDisFile {

    private static final long serialVersionUID = -5909071347828866632L;

    @NotNull
    @NotBlank
    private String gefId;

    @NotNull
    @ValidCode( allowedCodes = { "GEF-DISS-Report.1.0.0" }, isCaseSensitive = false )
    private String reportCode;

    /* #PARENT: */
    private String parentFileName;

    /* childFileName */
    private String childFileName;

    /* PARENTDEPTH */
    @NotNull
    @DecimalMin( value = "0.000", groups = Imbro.class )
    @DecimalMax( value = "200.000", groups = Imbro.class )
    private BigDecimal parentDepth;

    /* STARTDATE */
    @NotNull( groups = Imbro.class )
    @DateValid( groups = Imbro.class )
    @IncompleteDateValid( groups = ImbroA.class )
    @IsNotBefore01011930
    private String startDate;

    /* STARTTIME */
    @NotNull( groups = Imbro.class )
    @TimeValid( groups = Imbro.class )
    private String startTime;

    /* alleen validatie */
    private String cptStartDate;
    private String cptStartTime;

    /*
     * DATABLOCK
     */
    @Valid
    @AtLeastOnePorePressureInDTResults
    private List<DataRowDiss> dataBlock;

    /**
     * @return the parentDepth
     */
    public BigDecimal getParentDepth() {
        return parentDepth;
    }

    /*
     * (non-Javadoc)
     * @see nl.bro.cpt.gef.dto.IfcDisFile#setParentDepth(java.math.BigDecimal)
     */
    @Override
    public void setParentDepth(BigDecimal parentDepth) {
        this.parentDepth = parentDepth;
    }

    /**
     * @return the startDate
     */
    public String getStartDate() {
        return startDate;
    }

    /*
     * (non-Javadoc)
     * @see nl.bro.cpt.gef.dto.IfcDisFile#setStartDate(java.lang.String)
     */
    @Override
    public void setStartDate(String startDate) {
        this.startDate = startDate;
    }

    /**
     * @return the startTime
     */
    public String getStartTime() {
        return startTime;
    }

    /*
     * (non-Javadoc)
     * @see nl.bro.cpt.gef.dto.IfcDisFile#setStartTime(java.lang.String)
     */
    @Override
    public void setStartTime(String startTime) {
        this.startTime = startTime;
    }

    public String getCptStartDate() {
        return cptStartDate;
    }

    public void setCptStartDate( String cptStartDate ) {
        this.cptStartDate = cptStartDate;
    }

    public String getCptStartTime() {
        return cptStartTime;
    }

    public void setCptStartTime( String cptStartTime ) {
        this.cptStartTime = cptStartTime;
    }

    /**
     * @return the dataBlock
     */
    @Override
    public List<DataRowDiss> getDataBlock() {
        if ( dataBlock == null ) {
            dataBlock = new ArrayList<DataRowDiss>();
        }
        return dataBlock;
    }

    /*
     * (non-Javadoc)
     * @see nl.bro.cpt.gef.dto.IfcDisFile#setDataBlockDiss(java.util.List)
     */
    @Override
    public void setDataBlock(List<DataRowDiss> dataBlock) {
        this.dataBlock = (List<DataRowDiss>) dataBlock;
    }

    /**
     * @return the childFileName
     */
    public String getChildFileName() {
        return childFileName;
    }

    /*
     * (non-Javadoc)
     * @see nl.bro.cpt.gef.dto.IfcDisFile#setChildFileName(java.lang.String)
     */
    @Override
    public void setChildFileName(String childFileName) {
        this.childFileName = childFileName;
    }

    public String getParentFileName() {
        return parentFileName;
    }

    /*
     * (non-Javadoc)
     * @see nl.bro.cpt.gef.dto.IfcDisFile#setParentFileName(java.lang.String)
     */
    @Override
    public void setParentFileName(String parentFileName) {
        this.parentFileName = parentFileName;
    }

    public String getGefId() {
        return gefId;
    }

    public void setGefId(String gefId) {
        this.gefId = gefId;
    }

    public String getReportCode() {
        return reportCode;
    }

    public void setReportCode(String reportCode) {
        this.reportCode = reportCode;
    }
}
