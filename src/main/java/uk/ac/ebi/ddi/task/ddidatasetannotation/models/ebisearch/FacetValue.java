package uk.ac.ebi.ddi.task.ddidatasetannotation.models.ebisearch;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonIgnoreProperties(ignoreUnknown = true)
public class FacetValue {

    @JsonProperty("label")
    private String label = null;

    @JsonProperty("value")
    private String value = null;

    @JsonProperty("count")
    private String count = null;

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public String getCount() {
        return count;
    }

    public void setCount(String count) {
        this.count = count;
    }
}
