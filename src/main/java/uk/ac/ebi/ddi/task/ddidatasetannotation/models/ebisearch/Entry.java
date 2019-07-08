package uk.ac.ebi.ddi.task.ddidatasetannotation.models.ebisearch;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.HashMap;
import java.util.Map;

@JsonIgnoreProperties(ignoreUnknown = true)
public class Entry {

    @JsonProperty("id")
    private String id = null;

    @JsonProperty("score")
    private String score;

    @JsonProperty("source")
    private String source = null;

    @JsonProperty("fields")
    private Map<String, String[]> fields = new HashMap<>();

    public Map<String, String[]> getFields() {
        return fields;
    }

    public void setFields(Map<String, String[]> fields) {
        this.fields = fields;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source;
    }

    public String getScore() {
        return score;
    }

    public void setScore(String score) {
        this.score = score;
    }
}
