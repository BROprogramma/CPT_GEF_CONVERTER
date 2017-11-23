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
import java.util.ArrayList;
import java.util.List;

public class GefCptSurvey implements Serializable {

    private static final long serialVersionUID = 1L;

    private String cptFileName;
    private String objectName;

    private String qualityRegime;
    private boolean orphanaged;

    private IfcCptFile gefCptFile = new GefCptFile();
    private List<IfcDisFile> gefDisFiles = new ArrayList<IfcDisFile>();

    public IfcCptFile getGefCptFile() {
        return gefCptFile;
    }

    public List<IfcDisFile> getGefDisFiles() {
        if ( gefDisFiles == null ) {
            gefDisFiles = new ArrayList<IfcDisFile>();
        }
        return gefDisFiles;
    }

    public String getCptFileName() {
        return cptFileName;
    }

    public void setGefCptFile(IfcCptFile gefCptFile) {
        this.gefCptFile = gefCptFile;
    }

    public String getObjectName() {
        return objectName;
    }

    public void setObjectName(String objectName) {
        this.objectName = objectName;
    }

    public void setGefDisFiles(List<IfcDisFile> getDisFiles) {
        this.gefDisFiles = getDisFiles;
    }

    public void setCptFileName(String cptFileName) {
        this.cptFileName = cptFileName;
    }

    @Override
    public String toString() {
        return "GefCptSurvey [fileName=" + cptFileName + "]";
    }

    public String getQualityRegime() {
        return qualityRegime;
    }

    public void setQualityRegime(String qualityRegime) {
        this.qualityRegime = qualityRegime;
    }

    public boolean isOrphanaged() {
        return orphanaged;
    }

    public void setOrphanaged(boolean orphanaged) {
        this.orphanaged = orphanaged;
    }

}
