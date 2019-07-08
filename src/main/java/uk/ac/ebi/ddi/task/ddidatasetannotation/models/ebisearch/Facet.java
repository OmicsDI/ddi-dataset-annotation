package uk.ac.ebi.ddi.task.ddidatasetannotation.models.ebisearch;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonIgnoreProperties(ignoreUnknown = true)
public class Facet {

    @JsonProperty("id")
    private String id = null;

    @JsonProperty("label")
    private String label = null;

    @JsonProperty("total")
    private Integer total = null;

    @JsonProperty("facetValues")
    private FacetValue[] facetValues = null;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public FacetValue[] getFacetValues() {
        return facetValues;
    }

    public Integer getTotal() {
        return total;
    }

    public void setTotal(Integer total) {
        this.total = total;
    }

    public void setFacetValues(FacetValue[] facetValues) {
        this.facetValues = facetValues;
    }
}

