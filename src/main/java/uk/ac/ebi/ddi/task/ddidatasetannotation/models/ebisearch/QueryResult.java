package uk.ac.ebi.ddi.task.ddidatasetannotation.models.ebisearch;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@JsonIgnoreProperties(ignoreUnknown = true)
public class QueryResult {

    @JsonProperty("hitCount")
    private Integer count;

    @JsonProperty("entries")
    private Entry[] entries = null;

    @JsonProperty("facets")
    private Facet[] facets;

    @JsonProperty("domains")
    private List<Domains> domains;

    public QueryResult() {
        count = 0;
    }

    public Integer getCount() {
        return count;
    }

    public void setCount(Integer count) {
        this.count = count;
    }

    public Entry[] getEntries() {
        return entries;
    }

    public void setEntries(Entry[] entries) {
        this.entries = entries;
    }

    public Facet[] getFacets() {
        return facets;
    }

    public void setFacets(Facet[] facets) {
        this.facets = facets;
    }

    public List<Domains> getDomains() {
        return domains;
    }

    public void setDomains(List<Domains> domains) {
        this.domains = domains;
    }

    public void addResults(QueryResult results) {

        Set<Entry> entries = new HashSet<>();
        Set<Facet> facets = new HashSet<>();

        if (results != null) {

            if (this.entries != null) {
                Collections.addAll(entries, this.entries);
            }
            if (results.entries != null) {
                Collections.addAll(entries, results.entries);
            }
            if (this.facets != null) {
                Collections.addAll(facets, this.facets);
            }
            if (results.facets != null) {
                Collections.addAll(facets, results.facets);
            }

            this.facets = new Facet[facets.size()];
            this.entries = new Entry[entries.size()];

            int i = 0;
            for (Entry entry : entries) {
                this.entries[i] = entry;
                i++;
            }

            i = 0;
            for (Facet entry : facets) {
                this.facets[i] = entry;
                i++;
            }

            count = entries.size();
        }
    }

}
