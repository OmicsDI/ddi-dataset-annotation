package uk.ac.ebi.ddi.task.ddidatasetannotation.models.pubmed;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * @author Yasset Perez-Riverol (ypriverol@gmail.com)
 * @date 18/05/2015
 */

@JsonIgnoreProperties(ignoreUnknown = true)
public class PubmedJSON {

    @JsonProperty("status")
    private String status;

    @JsonProperty("responseDate")
    private String responseDate;

    @JsonProperty("request")
    private String request;

    @JsonProperty("warning")
    private String warning;

    @JsonProperty("records")
    private Record[] records;

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getResponseDate() {
        return responseDate;
    }

    public void setResponseDate(String responseDate) {
        this.responseDate = responseDate;
    }

    public String getRequest() {
        return request;
    }

    public void setRequest(String request) {
        this.request = request;
    }

    public String getWarning() {
        return warning;
    }

    public void setWarning(String warning) {
        this.warning = warning;
    }

    public Record[] getRecords() {
        return records;
    }

    public void setRecords(Record[] records) {
        this.records = records;
    }
}
