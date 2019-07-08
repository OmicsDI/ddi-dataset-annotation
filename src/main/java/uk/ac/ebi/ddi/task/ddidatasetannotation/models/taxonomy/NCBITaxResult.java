package uk.ac.ebi.ddi.task.ddidatasetannotation.models.taxonomy;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
public class NCBITaxResult {

    @JsonProperty("header")
    private NCBIHeader header;

    @JsonProperty("esearchresult")
    private NCBIEResult result;

    public NCBIHeader getHeader() {
        return header;
    }

    public void setHeader(NCBIHeader header) {
        this.header = header;
    }

    public NCBIEResult getResult() {
        return result;
    }

    public void setResult(NCBIEResult result) {
        this.result = result;
    }

    public List<String> getNCBITaxonomy() {
        if (getResult() != null && getResult().getIdList() != null && getResult().getIdList().length == 1) {
            return Arrays.asList(getResult().getIdList());
        }
        return new ArrayList<>();
    }
}
