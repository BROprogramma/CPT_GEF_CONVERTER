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
import java.util.HashMap;
import java.util.Map;

public class DataRowFacade implements DataRow {

    private final Map<String, Boolean> filledEntriesMap;

    protected DataRowFacade() {

        filledEntriesMap = new HashMap<String, Boolean>();
    }

    @Override
    public void setFilledEntry(String datablockIdx, Boolean value) {
        filledEntriesMap.put( datablockIdx, value );
    }

    @Override
    public Boolean getFilledEntry(String datablockIdx) {
        Boolean value = filledEntriesMap.get( datablockIdx );
        // default: als waarde niet gevonden dan is ie niet gezet:
        if ( value == null ) {
            value = Boolean.FALSE;
        }
        return value;
    }

    @Override
    public void setDatablockci1(BigDecimal datablockci1) {

        throw new UnsupportedOperationException( "Setting datablock item with index 1 not implemented in subclass" );
    }

    @Override
    public void setDatablockci2(BigDecimal datablockci2) {
        throw new UnsupportedOperationException( "Setting datablock item with index 2 not implemented in subclass" );
    }

    @Override
    public void setDatablockci3(BigDecimal datablockci3) {
        throw new UnsupportedOperationException( "Setting datablock item with index 3 not implemented in subclass" );
    }

    @Override
    public void setDatablockci4(BigDecimal datablockci4) {
        throw new UnsupportedOperationException( "Setting datablock item with index 4 not implemented in subclass" );
    }

    @Override
    public void setDatablockci5(BigDecimal datablockci5) {
        throw new UnsupportedOperationException( "Setting datablock item with index 5 not implemented in subclass" );
    }

    @Override
    public void setDatablockci6(BigDecimal datablockci6) {
        throw new UnsupportedOperationException( "Setting datablock item with index 6 not implemented in subclass" );
    }

    @Override
    public void setDatablockci7(BigDecimal datablockci7) {
        throw new UnsupportedOperationException( "Setting datablock item with index 7 not implemented in subclass" );
    }

    @Override
    public void setDatablockci8(BigDecimal datablockci8) {
        throw new UnsupportedOperationException( "Setting datablock item with index 8 not implemented in subclass" );
    }

    @Override
    public void setDatablockci9(BigDecimal datablockci9) {
        throw new UnsupportedOperationException( "Setting datablock item with index 9 not implemented in subclass" );
    }

    @Override
    public void setDatablockci10(BigDecimal datablockci10) {
        throw new UnsupportedOperationException( "Setting datablock item with index 10 not implemented in subclass" );
    }

    @Override
    public void setDatablockci11(BigDecimal datablockci11) {
        throw new UnsupportedOperationException( "Setting datablock item with index 11 not implemented in subclass" );
    }

    @Override
    public void setDatablockci12(BigDecimal datablockci12) {
        throw new UnsupportedOperationException( "Setting datablock item with index 12 not implemented in subclass" );
    }

    @Override
    public void setDatablockci13(BigDecimal datablockci13) {
        throw new UnsupportedOperationException( "Setting datablock item with index 13 not implemented in subclass" );
    }

    @Override
    public void setDatablockci14(BigDecimal datablockci14) {
        throw new UnsupportedOperationException( "Setting datablock item with index 14 not implemented in subclass" );
    }

    @Override
    public void setDatablockci15(BigDecimal datablockci15) {
        throw new UnsupportedOperationException( "Setting datablock item with index 15 not implemented in subclass" );
    }

    @Override
    public void setDatablockci16(BigDecimal datablockci16) {
        throw new UnsupportedOperationException( "Setting datablock item with index 16 not implemented in subclass" );
    }

    @Override
    public void setDatablockci17(BigDecimal datablockci17) {
        throw new UnsupportedOperationException( "Setting datablock item with index 17 not implemented in subclass" );
    }

    @Override
    public void setDatablockci18(BigDecimal datablockci18) {
        throw new UnsupportedOperationException( "Setting datablock item with index 18 not implemented in subclass" );
    }

    @Override
    public void setDatablockci19(BigDecimal datablockci19) {
        throw new UnsupportedOperationException( "Setting datablock item with index 19 not implemented in subclass" );
    }

    @Override
    public void setDatablockci20(BigDecimal datablockci20) {
        throw new UnsupportedOperationException( "Setting datablock item with index 20 not implemented in subclass" );
    }

    @Override
    public void setDatablockci21(BigDecimal datablockci21) {
        throw new UnsupportedOperationException( "Setting datablock item with index 21 not implemented in subclass" );
    }

    @Override
    public void setDatablockci22(BigDecimal datablockci22) {
        throw new UnsupportedOperationException( "Setting datablock item with index 22 not implemented in subclass" );
    }

    @Override
    public void setDatablockci23(BigDecimal datablockci23) {
        throw new UnsupportedOperationException( "Setting datablock item with index 23 not implemented in subclass" );
    }

    @Override
    public void setDatablockci24(BigDecimal datablockci24) {
        throw new UnsupportedOperationException( "Setting datablock item with index 24 not implemented in subclass" );
    }

    @Override
    public void setDatablockci25(BigDecimal datablockci25) {
        throw new UnsupportedOperationException( "Setting datablock item with index 25 not implemented in subclass" );
    }

    @Override
    public void setDatablockci26(BigDecimal datablockci26) {
        throw new UnsupportedOperationException( "Setting datablock item with index 26 not implemented in subclass" );
    }

    @Override
    public void setDatablockci27(BigDecimal datablockci27) {
        throw new UnsupportedOperationException( "Setting datablock item with index 27 not implemented in subclass" );
    }

    @Override
    public void setDatablockci28(BigDecimal datablockci28) {
        throw new UnsupportedOperationException( "Setting datablock item with index 28 not implemented in subclass" );
    }

    @Override
    public void setDatablockci29(BigDecimal datablockci29) {
        throw new UnsupportedOperationException( "Setting datablock item with index 29 not implemented in subclass" );
    }

    @Override
    public void setDatablockci30(BigDecimal datablockci30) {
        throw new UnsupportedOperationException( "Setting datablock item with index 30 not implemented in subclass" );
    }

    @Override
    public void setDatablockci31(BigDecimal datablockci31) {
        throw new UnsupportedOperationException( "Setting datablock item with index 31 not implemented in subclass" );
    }

    @Override
    public void setDatablockci32(BigDecimal datablockci32) {
        throw new UnsupportedOperationException( "Setting datablock item with index 32 not implemented in subclass" );
    }

    @Override
    public void setDatablockci33(BigDecimal datablockci33) {
        throw new UnsupportedOperationException( "Setting datablock item with index 33 not implemented in subclass" );
    }

    @Override
    public void setDatablockci34(BigDecimal datablockci34) {
        throw new UnsupportedOperationException( "Setting datablock item with index 34 not implemented in subclass" );
    }

    @Override
    public void setDatablockci35(BigDecimal datablockci35) {
        throw new UnsupportedOperationException( "Setting datablock item with index 35 not implemented in subclass" );
    }

    @Override
    public void setDatablockci36(BigDecimal datablockci36) {
        throw new UnsupportedOperationException( "Setting datablock item with index 36 not implemented in subclass" );
    }

    @Override
    public void setDatablockci37(BigDecimal datablockci37) {
        throw new UnsupportedOperationException( "Setting datablock item with index 37 not implemented in subclass" );
    }

    @Override
    public void setDatablockci38(BigDecimal datablockci38) {
        throw new UnsupportedOperationException( "Setting datablock item with index 38 not implemented in subclass" );
    }

    @Override
    public BigDecimal getDatablockci39() {
        throw new UnsupportedOperationException( "Getting datablock item with index 39 not implemented in subclass" );
    }

    @Override
    public void setDatablockci39(BigDecimal datablockci39) {
        throw new UnsupportedOperationException( "Setting datablock item with index 39 not implemented in subclass" );
    }

    @Override
    public BigDecimal getDatablockci1() {
        throw new UnsupportedOperationException( "Getting datablock item with index 1 not implemented in subclass" );
    }

    @Override
    public BigDecimal getDatablockci11() {
        throw new UnsupportedOperationException( "Getting datablock item with index 11 not implemented in subclass" );
    }

    @Override
    public BigDecimal getDatablockci2() {
        throw new UnsupportedOperationException( "Getting datablock item with index 2 not implemented in subclass" );
    }

    @Override
    public BigDecimal getDatablockci5() {
        throw new UnsupportedOperationException( "Getting datablock item with index 5 not implemented in subclass" );
    }

    @Override
    public BigDecimal getDatablockci6() {
        throw new UnsupportedOperationException( "Getting datablock item with index 6 not implemented in subclass" );
    }

    @Override
    public BigDecimal getDatablockci7() {
        throw new UnsupportedOperationException( "Getting datablock item with index 7 not implemented in subclass" );
    }

    @Override
    public BigDecimal getDatablockci3() {
        throw new UnsupportedOperationException( "Getting datablock item with index 3 not implemented in subclass" );
    }

    @Override
    public BigDecimal getDatablockci4() {
        throw new UnsupportedOperationException( "Getting datablock item with index 4 not implemented in subclass" );
    }

    @Override
    public BigDecimal getDatablockci8() {
        throw new UnsupportedOperationException( "Getting datablock item with index 8 not implemented in subclass" );
    }

    @Override
    public BigDecimal getDatablockci9() {
        throw new UnsupportedOperationException( "Getting datablock item with index 9 not implemented in subclass" );
    }

    @Override
    public BigDecimal getDatablockci10() {
        throw new UnsupportedOperationException( "Getting datablock item with index 10 not implemented in subclass" );
    }

    @Override
    public BigDecimal getDatablockci12() {
        throw new UnsupportedOperationException( "Getting datablock item with index 12 not implemented in subclass" );
    }

    @Override
    public BigDecimal getDatablockci13() {
        throw new UnsupportedOperationException( "Getting datablock item with index 13 not implemented in subclass" );
    }

    @Override
    public BigDecimal getDatablockci14() {
        throw new UnsupportedOperationException( "Getting datablock item with index 14 not implemented in subclass" );
    }

    @Override
    public BigDecimal getDatablockci15() {
        throw new UnsupportedOperationException( "Getting datablock item with index 15 not implemented in subclass" );
    }

    @Override
    public BigDecimal getDatablockci16() {
        throw new UnsupportedOperationException( "Getting datablock item with index 16 not implemented in subclass" );
    }

    @Override
    public BigDecimal getDatablockci17() {
        throw new UnsupportedOperationException( "Getting datablock item with index 17 not implemented in subclass" );
    }

    @Override
    public BigDecimal getDatablockci18() {
        throw new UnsupportedOperationException( "Getting datablock item with index 18 not implemented in subclass" );
    }

    @Override
    public BigDecimal getDatablockci19() {
        throw new UnsupportedOperationException( "Getting datablock item with index 19 not implemented in subclass" );
    }

    @Override
    public BigDecimal getDatablockci20() {
        throw new UnsupportedOperationException( "Getting datablock item with index 20 not implemented in subclass" );
    }

    @Override
    public BigDecimal getDatablockci21() {
        throw new UnsupportedOperationException( "Getting datablock item with index 21 not implemented in subclass" );
    }

    @Override
    public BigDecimal getDatablockci22() {
        throw new UnsupportedOperationException( "Getting datablock item with index 22 not implemented in subclass" );
    }

    @Override
    public BigDecimal getDatablockci23() {
        throw new UnsupportedOperationException( "Getting datablock item with index 23 not implemented in subclass" );
    }

    @Override
    public BigDecimal getDatablockci24() {
        throw new UnsupportedOperationException( "Getting datablock item with index 24 not implemented in subclass" );
    }

    @Override
    public BigDecimal getDatablockci25() {
        throw new UnsupportedOperationException( "Getting datablock item with index 25 not implemented in subclass" );
    }

    @Override
    public BigDecimal getDatablockci26() {
        throw new UnsupportedOperationException( "Getting datablock item with index 26 not implemented in subclass" );
    }

    @Override
    public BigDecimal getDatablockci27() {
        throw new UnsupportedOperationException( "Getting datablock item with index 27 not implemented in subclass" );
    }

    @Override
    public BigDecimal getDatablockci28() {
        throw new UnsupportedOperationException( "Getting datablock item with index 28 not implemented in subclass" );
    }

    @Override
    public BigDecimal getDatablockci29() {
        throw new UnsupportedOperationException( "Getting datablock item with index 29 not implemented in subclass" );
    }

    @Override
    public BigDecimal getDatablockci30() {
        throw new UnsupportedOperationException( "Getting datablock item with index 30 not implemented in subclass" );
    }

    @Override
    public BigDecimal getDatablockci31() {
        throw new UnsupportedOperationException( "Getting datablock item with index 31 not implemented in subclass" );
    }

    @Override
    public BigDecimal getDatablockci32() {
        throw new UnsupportedOperationException( "Getting datablock item with index 32 not implemented in subclass" );
    }

    @Override
    public BigDecimal getDatablockci33() {
        throw new UnsupportedOperationException( "Getting datablock item with index 33 not implemented in subclass" );
    }

    @Override
    public BigDecimal getDatablockci34() {
        throw new UnsupportedOperationException( "Getting datablock item with index 34 not implemented in subclass" );
    }

    @Override
    public BigDecimal getDatablockci35() {
        throw new UnsupportedOperationException( "Getting datablock item with index 35 not implemented in subclass" );
    }

    @Override
    public BigDecimal getDatablockci36() {
        throw new UnsupportedOperationException( "Getting datablock item with index 36 not implemented in subclass" );
    }

    @Override
    public BigDecimal getDatablockci37() {
        throw new UnsupportedOperationException( "Getting datablock item with index 37 not implemented in subclass" );
    }

    @Override
    public BigDecimal getDatablockci38() {
        throw new UnsupportedOperationException( "Getting datablock item with index 38 not implemented in subclass" );
    }
}
