package uk.ac.ebi.ddi.task.ddidatasetannotation.models.taxonomy;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonIgnoreProperties(ignoreUnknown = true)
public class NCBIEResult {

    @JsonProperty("count")
    private int count;

    @JsonProperty("idlist")
    private String[] idList;

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public String[] getIdList() {
        return idList;
    }

    public void setIdList(String[] idList) {
        this.idList = idList;
    }
}

