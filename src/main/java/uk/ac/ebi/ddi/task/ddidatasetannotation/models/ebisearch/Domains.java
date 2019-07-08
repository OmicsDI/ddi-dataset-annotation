package uk.ac.ebi.ddi.task.ddidatasetannotation.models.ebisearch;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonIgnoreProperties(ignoreUnknown = true)
public class Domains {

    @JsonProperty("id")
    private String id;

    @JsonProperty("hitCount")
    private Integer hitCount;

    @JsonProperty("subdomains")
    private Domains[] subdomains;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Integer getHitCount() {
        return hitCount;
    }

    public void setHitCount(Integer hitCount) {
        this.hitCount = hitCount;
    }

    public Domains[] getSubdomains() {
        return subdomains;
    }

    public void setSubdomains(Domains[] subdomains) {
        this.subdomains = subdomains;
    }
}
