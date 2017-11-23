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
import java.util.Comparator;

public interface DataRow {

    class CompareOnLenght implements Comparator<DataRow>, Serializable {

        private static final long serialVersionUID = 3639165228562275820L;

        @Override
        public int compare( DataRow o1, DataRow o2 ) {
            if ( o1.getDatablockci1() != null && o2.getDatablockci1() != null ) {
                return o1.getDatablockci1().compareTo( o2.getDatablockci1() );
            }
            return 0;
        }

    }

    void setFilledEntry(String datablockIdx, Boolean value);

    Boolean getFilledEntry(String datablockIdx);

    /**
     * @param datablockci1 the datablockci1 to set
     */
    void setDatablockci1(BigDecimal datablockci1);

    BigDecimal getDatablockci1();

    /**
     * @param datablockci2 the datablockci2 to set
     */
    void setDatablockci2(BigDecimal datablockci2);

    BigDecimal getDatablockci2();

    /**
     * @param datablockci3 the datablockci3 to set
     */
    void setDatablockci3(BigDecimal datablockci3);

    BigDecimal getDatablockci3();

    /**
     * @param datablockci4 the datablockci4 to set
     */
    void setDatablockci4(BigDecimal datablockci4);

    BigDecimal getDatablockci4();

    /**
     * @param datablockci5 the datablockci5 to set
     */
    void setDatablockci5(BigDecimal datablockci5);

    BigDecimal getDatablockci5();

    /**
     * @param datablockci6 the datablockci6 to set
     */
    void setDatablockci6(BigDecimal datablockci6);

    BigDecimal getDatablockci6();

    /**
     * @param datablockci7 the datablockci7 to set
     */
    void setDatablockci7(BigDecimal datablockci7);

    BigDecimal getDatablockci7();

    /**
     * @param datablockci8 the datablockci8 to set
     */
    void setDatablockci8(BigDecimal datablockci8);

    BigDecimal getDatablockci8();

    /**
     * @param datablockci9 the datablockci9 to set
     */
    void setDatablockci9(BigDecimal datablockci9);

    BigDecimal getDatablockci9();

    /**
     * @param datablockci10 the datablockci10 to set
     */
    void setDatablockci10(BigDecimal datablockci10);

    BigDecimal getDatablockci10();

    /**
     * @param datablockci11 the datablockci11 to set
     */
    void setDatablockci11(BigDecimal datablockci11);

    BigDecimal getDatablockci11();

    /**
     * @param datablockci12 the datablockci12 to set
     */
    void setDatablockci12(BigDecimal datablockci12);

    BigDecimal getDatablockci12();

    /**
     * @param datablockci13 the datablockci13 to set
     */
    void setDatablockci13(BigDecimal datablockci13);

    BigDecimal getDatablockci13();

    /**
     * @param datablockci14 the datablockci14 to set
     */
    void setDatablockci14(BigDecimal datablockci14);

    BigDecimal getDatablockci14();

    /**
     * @param datablockci15 the datablockci15 to set
     */
    void setDatablockci15(BigDecimal datablockci15);

    BigDecimal getDatablockci15();

    /**
     * @param datablockci16 the datablockci16 to set
     */
    void setDatablockci16(BigDecimal datablockci16);

    BigDecimal getDatablockci16();

    /**
     * @param datablockci17 the datablockci17 to set
     */
    void setDatablockci17(BigDecimal datablockci17);

    BigDecimal getDatablockci17();

    /**
     * @param datablockci18 the datablockci18 to set
     */
    void setDatablockci18(BigDecimal datablockci18);

    BigDecimal getDatablockci18();

    /**
     * @param datablockci19 the datablockci19 to set
     */
    void setDatablockci19(BigDecimal datablockci19);

    BigDecimal getDatablockci19();

    /**
     * @param datablockci20 the datablockci20 to set
     */
    void setDatablockci20(BigDecimal datablockci20);

    BigDecimal getDatablockci20();

    /**
     * @param datablockci21 the datablockci21 to set
     */
    void setDatablockci21(BigDecimal datablockci21);

    BigDecimal getDatablockci21();

    /**
     * @param datablockci22 the datablockci22 to set
     */
    void setDatablockci22(BigDecimal datablockci22);

    BigDecimal getDatablockci22();

    /**
     * @param datablockci23 the datablockci23 to set
     */
    void setDatablockci23(BigDecimal datablockci23);

    BigDecimal getDatablockci23();

    /**
     * @param datablockci24 the datablockci24 to set
     */
    void setDatablockci24(BigDecimal datablockci24);

    BigDecimal getDatablockci24();

    /**
     * @param datablockci25 the datablockci25 to set
     */
    void setDatablockci25(BigDecimal datablockci25);

    BigDecimal getDatablockci25();

    /**
     * @param datablockci26 the datablockci26 to set
     */
    void setDatablockci26(BigDecimal datablockci26);

    BigDecimal getDatablockci26();

    /**
     * @param datablockci27 the datablockci27 to set
     */
    void setDatablockci27(BigDecimal datablockci27);

    BigDecimal getDatablockci27();

    /**
     * @param datablockci28 the datablockci28 to set
     */
    void setDatablockci28(BigDecimal datablockci28);

    BigDecimal getDatablockci28();

    /**
     * @param datablockci29 the datablockci29 to set
     */
    void setDatablockci29(BigDecimal datablockci29);

    BigDecimal getDatablockci29();

    /**
     * @param datablockci30 the datablockci30 to set
     */
    void setDatablockci30(BigDecimal datablockci30);

    BigDecimal getDatablockci30();

    /**
     * @param datablockci31 the datablockci31 to set
     */
    void setDatablockci31(BigDecimal datablockci31);

    BigDecimal getDatablockci31();

    /**
     * @param datablockci32 the datablockci32 to set
     */
    void setDatablockci32(BigDecimal datablockci32);

    BigDecimal getDatablockci32();

    /**
     * @param datablockci33 the datablockci33 to set
     */
    void setDatablockci33(BigDecimal datablockci33);

    BigDecimal getDatablockci33();

    /**
     * @param datablockci34 the datablockci34 to set
     */
    void setDatablockci34(BigDecimal datablockci34);

    BigDecimal getDatablockci34();

    /**
     * @param datablockci35 the datablockci35 to set
     */
    void setDatablockci35(BigDecimal datablockci35);

    BigDecimal getDatablockci35();

    /**
     * @param datablockci36 the datablockci36 to set
     */
    void setDatablockci36(BigDecimal datablockci36);

    BigDecimal getDatablockci36();

    /**
     * @param datablockci37 the datablockci37 to set
     */
    void setDatablockci37(BigDecimal datablockci37);

    BigDecimal getDatablockci37();

    /**
     * @param datablockci38 the datablockci38 to set
     */
    void setDatablockci38(BigDecimal datablockci38);

    BigDecimal getDatablockci38();

    /**
     * @param datablockci39 the datablockci39 to set
     */
    void setDatablockci39(BigDecimal datablockci39);

    BigDecimal getDatablockci39();

}
