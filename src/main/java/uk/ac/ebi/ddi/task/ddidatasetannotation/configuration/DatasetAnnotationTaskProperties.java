package uk.ac.ebi.ddi.task.ddidatasetannotation.configuration;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties("annotation")
public class DatasetAnnotationTaskProperties {

    private String databaseName;

    public String getDatabaseName() {
        return databaseName;
    }

    public void setDatabaseName(String databaseName) {
        this.databaseName = databaseName;
    }

    @Override
    public String toString() {
        return "DatasetAnnotationTaskProperties{" +
                "databaseName='" + databaseName + '\'' +
                '}';
    }
}
