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

import java.math.BigDecimal;
import java.util.List;
import java.util.Set;

public interface IfcCptFile extends IfcGefFile {

    void setGefId(String gefId);

    void setReportCode(String reportCode);

    void setMeasurementVar1(BigDecimal measurementVar1);

    void setMeasurementVar2(BigDecimal measurementVar2);

    void setMeasurementVar3(BigDecimal measurementVar3);

    void setMeasurementVar4(BigDecimal measurementVar4);

    void setMeasurementVar5(BigDecimal measurementVar5);

    void setMeasurementVar12(String measurementVar12);

    void setMeasurementVar13(BigDecimal measurementVar13);

    void setMeasurementVar14(BigDecimal measurementVar14);

    void setMeasurementVar16(BigDecimal measurementVar16);

    void setMeasurementVar17(String measurementVar17);

    void setMeasurementVar20(BigDecimal measurementVar20);

    void setMeasurementVar21(BigDecimal measurementVar21);

    void setMeasurementVar22(BigDecimal measurementVar22);

    void setMeasurementVar23(BigDecimal measurementVar23);

    void setMeasurementVar24(BigDecimal measurementVar24);

    void setMeasurementVar25(BigDecimal measurementVar25);

    void setMeasurementVar26(BigDecimal measurementVar26);

    void setMeasurementVar27(BigDecimal measurementVar27);

    void setMeasurementVar28(BigDecimal measurementVar28);

    void setMeasurementVar29(BigDecimal measurementVar29);

    void setMeasurementVar30(BigDecimal measurementVar30);

    void setMeasurementVar31(BigDecimal measurementVar31);

    void setMeasurementVar32(BigDecimal measurementVar32);

    void setMeasurementVar33(BigDecimal measurementVar33);

    void setMeasurementVar34(BigDecimal measurementVar34);

    void setMeasurementVar35(BigDecimal measurementVar35);

    void setMeasurementVar36(BigDecimal measurementVar36);

    void setMeasurementVar37(BigDecimal measurementVar37);

    void setMeasurementVar42(BigDecimal measurementVar42);

    void setMeasurementVar130(BigDecimal measurementVar130);

    void setMeasurementText4(String measurementText4);

    void setMeasurementText5(String measurementText5);

    void setMeasurementText9(String measurementText9);

    void setMeasurementText11(String measurementText11);

    void setMeasurementText20(String measurementText20);

    void setMeasurementText21(String measurementText21);

    void setMeasurementText42(String measurementText42);

    void setMeasurementText43(String measurementText43);

    void setMeasurementText101(String measurementText101);

    void setMeasurementText102(String measurementText102);

    void setMeasurementText103(String measurementText103);

    void setMeasurementText104(String measurementText104);

    void setMeasurementText105(String measurementText105);

    void setMeasurementText106(String measurementText106);

    void setMeasurementText107(String measurementText107);

    void setMeasurementText108(String measurementText108);

    void setMeasurementText109(String measurementText109);

    void setMeasurementText110(String measurementText110);

    void setMeasurementText111(String measurementText111);

    void setMeasurementText112(String measurementText112);

    void setMeasurementText113(String measurementText113);

    void setMeasurementText114(String measurementText114);

    void setMeasurementText6sondeernorm(String measurementText6sondeernorm);

    void setMeasurementText6kwaliteitsklasse(String measurementText6kwaliteitsklasse);

    String getMeasurementText4();

    String getMeasurementText5();

    String getMeasurementText6sondeernorm();

    String getMeasurementText6kwaliteitsklasse();

    String getMeasurementText9();

    String getMeasurementText11();

    String getMeasurementText20();

    String getMeasurementText21();

    String getMeasurementText43();

    String getMeasurementText102();

    String getMeasurementText103();

    String getMeasurementText104();

    String getMeasurementText105();

    String getMeasurementText106();

    String getMeasurementText107();

    String getMeasurementText108();

    String getMeasurementText109();

    String getMeasurementText110();

    String getMeasurementText111();

    String getMeasurementText113();

    String getMeasurementText114();

    String getMeasurementText42();

    String getMeasurementText101();

    String getMeasurementText112();

    String getTestId();

    String getProjectId();

    void setCompanyId(String companyId);

    void setSpecimenVar(List<SpecimenVar> specimenVar);

    void setStartDate(String startDate);

    void setStartTime(String startTime);

    String getStartDate();

    String getStartTime();

    void setProjectId(String projectId);

    void setTestId(String testId);

    void setDataBlock(List<DataRowCpt> dataBlock);

    List<DataRowCpt> getDataBlock();

    void setXyidCrs(String xyidCrs);

    void setXyidX(BigDecimal xyidX);

    void setXyidY(BigDecimal xyidY);

    void setZidVerticalDatum(String zidVerticalDatum);

    void setZidOffset(BigDecimal zidOffset);

    /**
     * @param fileDate the fileDate to set
     */
    void setFileDate(String fileDate);

    /**
     * @param parentFileName the parentFileName to set
     */
    void setParentFileName(String parentFileName);

    /**
     * @param expectedChildFileNameList the expectedChildFileNameList to set
     */
    void setExpectedChildFileNameList(List<String> expectedChildFileNameList);

    List<String> getExpectedChildFileNameList();

    List<SpecimenVar> getSpecimenVar();

    Set<String> getMeasuredParameters();

    String getXyidCrs();

    BigDecimal getXyidX();

    BigDecimal getXyidY();

    BigDecimal getZidOffset();

    String getZidVerticalDatum();

    BigDecimal getMeasurementVar1();

    BigDecimal getMeasurementVar2();

    BigDecimal getMeasurementVar3();

    BigDecimal getMeasurementVar4();

    BigDecimal getMeasurementVar5();

    BigDecimal getMeasurementVar13();

    BigDecimal getMeasurementVar14();

    BigDecimal getMeasurementVar15();

    BigDecimal getMeasurementVar16();

    BigDecimal getMeasurementVar42();

    String getMeasurementVar17();

    BigDecimal getMeasurementVar130();

    String getMeasurementVar12();

    BigDecimal getMeasurementVar20();

    BigDecimal getMeasurementVar21();

    BigDecimal getMeasurementVar22();

    BigDecimal getMeasurementVar23();

    BigDecimal getMeasurementVar24();

    BigDecimal getMeasurementVar25();

    BigDecimal getMeasurementVar26();

    BigDecimal getMeasurementVar27();

    BigDecimal getMeasurementVar28();

    BigDecimal getMeasurementVar29();

    BigDecimal getMeasurementVar30();

    BigDecimal getMeasurementVar31();

    BigDecimal getMeasurementVar32();

    BigDecimal getMeasurementVar33();

    BigDecimal getMeasurementVar34();

    BigDecimal getMeasurementVar35();

    BigDecimal getMeasurementVar36();

    BigDecimal getMeasurementVar37();

    String getCompanyId();

    String getFileDate();

    String getParentFileName();

    String getGefId();

    String getFileName();

    String getTransactionType();

}
