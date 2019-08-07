package uk.ac.ebi.ddi.task.ddidatasetannotation;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import uk.ac.ebi.ddi.ddidomaindb.dataset.DSField;
import uk.ac.ebi.ddi.service.db.model.dataset.Dataset;
import uk.ac.ebi.ddi.service.db.model.publication.PublicationDataset;
import uk.ac.ebi.ddi.service.db.service.dataset.IDatasetService;
import uk.ac.ebi.ddi.service.db.service.publication.IPublicationDatasetService;
import uk.ac.ebi.ddi.service.db.utils.DatasetCategory;
import uk.ac.ebi.ddi.service.db.utils.DatasetUtils;
import uk.ac.ebi.ddi.task.ddidatasetannotation.configuration.DatasetAnnotationTaskProperties;
import uk.ac.ebi.ddi.task.ddidatasetannotation.services.NCBITaxonomyService;
import uk.ac.ebi.ddi.task.ddidatasetannotation.services.PubmedService;

import java.util.*;
import java.util.stream.Collectors;

import static uk.ac.ebi.ddi.ddidomaindb.dataset.DSField.Additional.*;
import static uk.ac.ebi.ddi.ddidomaindb.dataset.DSField.Date.PUBLICATION;
import static uk.ac.ebi.ddi.ddidomaindb.dataset.DSField.Date.PUBLICATION_UPDATED;
import static uk.ac.ebi.ddi.service.db.utils.DatasetCategory.INSERTED;
import static uk.ac.ebi.ddi.service.db.utils.DatasetCategory.UPDATED;

@SpringBootApplication
public class DdiDatasetAnnotationApplication implements CommandLineRunner {

    @Autowired
    private IDatasetService datasetService;

    @Autowired
    private DatasetAnnotationTaskProperties taskProperties;

    @Autowired
    private PubmedService pubmedService;

    @Autowired
    private NCBITaxonomyService taxonomyService;

    @Autowired
    private IPublicationDatasetService publicationService;

    private static final Logger LOGGER = LoggerFactory.getLogger(DdiDatasetAnnotationApplication.class);

    public static void main(String[] args) {
        SpringApplication.run(DdiDatasetAnnotationApplication.class, args);
    }

    public boolean isDatasetNeedToAnnotate(Dataset dataset) {
        return dataset.getCurrentStatus().equals(INSERTED.getType())
                || dataset.getCurrentStatus().equals(UPDATED.getType())
                || taskProperties.isForce();
    }

    @Override
    public void run(String... args) throws Exception {
        List<Dataset> datasets = datasetService.readDatasetHashCode(taskProperties.getDatabaseName())
                .stream()
                .filter(this::isDatasetNeedToAnnotate)
                .collect(Collectors.toList());
        for (Dataset datasetShort : datasets) {
            try {
                Dataset dataset = datasetService.read(datasetShort.getAccession(), datasetShort.getDatabase());
                process(dataset);
            } catch (Exception e) {
                LOGGER.error("Exception occurred when processing dataset {}, ", datasetShort.getAccession(), e);
            }
        }
    }

    private void process(Dataset dataset) {

        if (DatasetUtils.hasDateType(dataset, PUBLICATION_UPDATED)) {
            dataset.getDates().put(PUBLICATION.getName(), DatasetUtils.getDateType(dataset, PUBLICATION_UPDATED));
        }

        List<String> ids = pubmedService.getPubMedIDsFromDataset(dataset);
        ids.forEach(x -> dataset.addCrossReferenceValue(DSField.CrossRef.PUBMED.getName(), x));
        List<Map<String, String[]>> information = pubmedService.getAbstractPublication(dataset);

        information.forEach(info -> info.forEach((key, values) -> {
            if (key.equalsIgnoreCase("description")) {
                Arrays.asList(values).forEach(x -> dataset.addAdditionalField(PUBMED_ABSTRACT.getName(), x));
            } else if (key.equalsIgnoreCase("name")) {
                Arrays.asList(values).forEach(x -> dataset.addAdditionalField(PUBMED_TITLE.getName(), x));
            } else if (key.equalsIgnoreCase("author")) {
                dataset.addAdditionalField(PUBMED_AUTHORS.getName(), String.join(", ", values));
            }
        }));

        Set<String> taxonomies = dataset.getCrossReference(DSField.CrossRef.TAXONOMY.key());
        Set<String> species = dataset.getAdditionalField(SPECIE_FIELD.key());

        if (taxonomies.isEmpty() && !species.isEmpty()) {
            List<String> taxs = taxonomyService.getNCBITaxonomy(new ArrayList<>(species));
            taxs.forEach(x -> dataset.addCrossReferenceValue(DSField.CrossRef.TAXONOMY.getName(), x));
        }

        if (!dataset.getCurrentStatus().equalsIgnoreCase(DatasetCategory.DELETED.getType())) {
            dataset.setCurrentStatus(DatasetCategory.ANNOTATED.getType());
        }

        datasetService.update(dataset.getId(), dataset);

        Set<String> pubmeds = dataset.getCrossReference(DSField.CrossRef.PUBMED.key());
        for (String pubmedId : pubmeds) {
            //Todo: In the future we need to check for providers that have multiple omics already.
            publicationService.save(new PublicationDataset(pubmedId, dataset.getAccession(), dataset.getDatabase(),
                    dataset.getAdditionalField(DSField.Additional.OMICS.key()).iterator().next()
            ));
        }
    }
}
