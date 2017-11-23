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

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class GefFile {

    private List<String> parseErrors;
    private List<String> validationErrors;

    private String transactionType;

    private Set<String> measuredParameters;

    public enum Types {
        DISS, CPT, UNKNOWN;
    }

    private String fileName;
    private Types fileType;

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public Types getFileType() {
        return fileType;
    }

    public void setFileType(Types fileType) {
        this.fileType = fileType;
    }

    public List<String> getParseErrors() {
        if ( parseErrors == null ) {
            parseErrors = new ArrayList<String>();
        }
        return parseErrors;
    }

    public void setParseErrors(List<String> parseErrors) {
        this.parseErrors = parseErrors;
    }

    public List<String> getValidationErrors() {
        if ( validationErrors == null ) {
            validationErrors = new ArrayList<String>();
        }
        return validationErrors;
    }

    public void setValidationErrors(List<String> validationErrors) {
        this.validationErrors = validationErrors;
    }

    public Set<String> getMeasuredParameters() {
        if ( measuredParameters == null ) {
            measuredParameters = new HashSet<String>();
        }
        return measuredParameters;
    }

    public void setMeasuredParameters(Set<String> measuredParameters) {
        this.measuredParameters = measuredParameters;
    }

    public void setTransactionType(String transactionType) {
        this.transactionType = transactionType;
    }

    public String getTransactionType() {
        return transactionType;
    }

}
