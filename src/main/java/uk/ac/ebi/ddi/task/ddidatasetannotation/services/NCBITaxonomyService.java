package uk.ac.ebi.ddi.task.ddidatasetannotation.services;

import com.google.common.collect.Lists;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;
import uk.ac.ebi.ddi.task.ddidatasetannotation.models.taxonomy.NCBIEResult;
import uk.ac.ebi.ddi.task.ddidatasetannotation.models.taxonomy.NCBITaxResult;
import uk.ac.ebi.ddi.task.ddidatasetannotation.utils.RetryClient;

import java.net.URI;
import java.util.*;
import java.util.stream.Stream;

@Service
public class NCBITaxonomyService extends RetryClient {

    private RestTemplate restTemplate = new RestTemplate();

    private static final String NCBI_API_ENDPOINT = "https://eutils.ncbi.nlm.nih.gov";

    private static final int MAX_TAX_PER_REQUEST = 30;

    public NCBITaxResult getNCBITax(String term) {
        if (term != null && term.length() > 0) {
            UriComponentsBuilder uriComponentsBuilder = UriComponentsBuilder.fromHttpUrl(NCBI_API_ENDPOINT)
                    .path("/entrez/eutils/esearch.fcgi")
                    .queryParam("db", "taxonomy")
                    .queryParam("term", term)
                    .queryParam("retmode", "JSON");
            URI uri = uriComponentsBuilder.build().encode().toUri();
            return execute(context -> restTemplate.getForObject(uri, NCBITaxResult.class));
        }
        return null;
    }

    public NCBITaxResult getNCBITax(Set<String> terms) {
        if (terms == null || terms.size() == 0) {
            return null;
        }
        List<List<String>> partitions = Lists.partition(new ArrayList<>(terms), MAX_TAX_PER_REQUEST);
        NCBITaxResult ncbiTaxResult = null;
        for (List<String> partition : partitions) {
            String query = String.join("+OR+", partition);
            if (ncbiTaxResult == null) {
                ncbiTaxResult = getNCBITax(query);
            } else {
                NCBITaxResult tmp = getNCBITax(query);
                NCBIEResult oldResult = ncbiTaxResult.getResult();
                oldResult.setCount(tmp.getResult().getCount() + oldResult.getCount());
                oldResult.setIdList(Stream.of(oldResult.getIdList(), tmp.getResult().getIdList())
                        .flatMap(Stream::of).toArray(String[]::new));
                ncbiTaxResult.setResult(oldResult);
            }
        }
        return ncbiTaxResult;
    }

    public List<String> getNCBITaxonomy(List<String> term) {
        if (term != null && !term.isEmpty()) {
            Set<String> terms = new HashSet<>(term);
            NCBITaxResult ncbiTax = getNCBITax(terms);
            if (ncbiTax != null && ncbiTax.getNCBITaxonomy().size() > 0) {
                return getTaxonomyArr(ncbiTax.getNCBITaxonomy());
            }
        }
        return new ArrayList<>();
    }

    private List<String> getTaxonomyArr(List<String> taxonomy) {
        List<String> taxonomies = new ArrayList<>();
        if (taxonomy.size() > 0) {
            for (String tax: taxonomy) {
                if (tax != null && !tax.isEmpty()) {
                    taxonomies.add(tax);
                }
            }
        }
        return taxonomies;
    }
}
