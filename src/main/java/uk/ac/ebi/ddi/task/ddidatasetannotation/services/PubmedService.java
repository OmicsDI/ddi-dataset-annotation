package uk.ac.ebi.ddi.task.ddidatasetannotation.services;

import com.google.common.collect.Lists;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;
import uk.ac.ebi.ddi.ddidomaindb.dataset.DSField;
import uk.ac.ebi.ddi.service.db.model.dataset.Dataset;
import uk.ac.ebi.ddi.task.ddidatasetannotation.models.ebisearch.QueryResult;
import uk.ac.ebi.ddi.task.ddidatasetannotation.models.pubmed.PubmedJSON;
import uk.ac.ebi.ddi.task.ddidatasetannotation.models.pubmed.Record;
import uk.ac.ebi.ddi.task.ddidatasetannotation.utils.DDIUtils;
import uk.ac.ebi.ddi.task.ddidatasetannotation.utils.DOIUtils;
import uk.ac.ebi.ddi.task.ddidatasetannotation.utils.RetryClient;
import uk.ac.ebi.ddi.task.ddidatasetannotation.models.ebisearch.Entry;
import java.net.URI;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class PubmedService extends RetryClient {

    private RestTemplate restTemplate = new RestTemplate();

    private static final int MAX_IDENTIFIER_PER_REQUEST = 100;

    private static final String PUBMED_ENDPOINT = "https://www.ncbi.nlm.nih.gov";

    private static final String EBI_SEARCH_ENDPOINT = "https://www.ebi.ac.uk";

    public PubmedJSON getPubmedIds(List<String> dois) throws RestClientException {

        if (dois.size() > 0) {
            UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(PUBMED_ENDPOINT)
                    .path("/pmc/utils/idconv/v1.0/")
                    .queryParam("tool", "my_tool")
                    .queryParam("ids", String.join(",", dois))
                    .queryParam("format", "json");

            URI uri = builder.build().encode().toUri();
            return execute(ctx -> restTemplate.getForObject(uri, PubmedJSON.class));
        }
        return null;
    }

    public List<String> getPubMedIDsFromDataset(Dataset dataset) {
        List<String> dois = DOIUtils.getDOIListFromText(Collections.singletonList(dataset.toString()));
        return getPubMedIDsFromDOIList(dois);
    }

    public List<String> getPubMedIDsFromDOIList(List<String> doiList) throws RestClientException {
        List<String> pubmedIds = new ArrayList<>();
        PubmedJSON resultJSON = getPubmedIds(doiList);
        if (resultJSON != null && resultJSON.getRecords() != null && resultJSON.getRecords().length > 0) {
            for (Record record: resultJSON.getRecords()) {
                if (record != null && record.getPmid() != null && !record.getPmid().isEmpty()) {
                    pubmedIds.add(record.getPmid());
                }
            }
        }

        return pubmedIds;
    }

    /**
     * This function retrieve a set of publications by Ids and the corresponding fields
     * @param fields The fields to be retrieved
     * @param ids The pubmed ids
     * @return A set of publications
     */
    public QueryResult getPublications(String[] fields, Set<String> ids) {

        QueryResult queryResult = new QueryResult();
        List<List<String>> parts = Lists.partition(Lists.newArrayList(ids), MAX_IDENTIFIER_PER_REQUEST);

        for (List<String> part : parts) {
            String finalIds = String.join(",", part);
            UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(EBI_SEARCH_ENDPOINT)
                    .path("/ebisearch/ws/rest/pubmed/entry")
                    .path("/" + finalIds)
                    .queryParam("fields", DDIUtils.getConcatenatedField(fields))
                    .queryParam("format", "JSON");

            URI uri = builder.build().encode().toUri();

            QueryResult tmp = getRetryTemplate().execute(context -> restTemplate.getForObject(uri, QueryResult.class));
            queryResult.addResults(tmp);
        }

        return queryResult;
    }

    /**
     * This function retrieve from the web service the publication information to be index in the database,
     * also we will generate all the information
     * about the publication reference from the dataset.
     * @param idList
     * @return
     */
    public List<Map<String, String[]>> getAbstractPublication(List<String> idList) throws RestClientException {
        String[] fields = {"description", "name", "author"};
        List<Map<String, String[]>> publications = new ArrayList<>();
        Set<String> finalIds = new HashSet<>(idList);
        finalIds = finalIds.stream().filter(x -> !x.trim().isEmpty()).collect(Collectors.toSet());
        QueryResult pride = getPublications(fields, finalIds);
        if (pride != null && pride.getEntries() != null && pride.getEntries().length > 0) {
            for (Entry entry: pride.getEntries()) {
                if (entry.getFields() != null) {
                    publications.add(entry.getFields());
                }
            }
        }
        return publications;
    }

    public List<Map<String, String[]>> getAbstractPublication(Dataset dataset) throws RestClientException {
        Set<String> pubmedIds = dataset.getCrossReference(DSField.CrossRef.PUBMED.getName());
        return getAbstractPublication(new ArrayList<>(pubmedIds));
    }
}
