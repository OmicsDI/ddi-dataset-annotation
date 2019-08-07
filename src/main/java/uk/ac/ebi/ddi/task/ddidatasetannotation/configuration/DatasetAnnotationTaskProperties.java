package uk.ac.ebi.ddi.task.ddidatasetannotation.configuration;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties("annotation")
public class DatasetAnnotationTaskProperties {

    private String databaseName;

    /**
     * Force the task to annotate all datasets, whether or not it's annotated
     */
    private boolean force = false;

    public String getDatabaseName() {
        return databaseName;
    }

    public void setDatabaseName(String databaseName) {
        this.databaseName = databaseName;
    }

    public boolean isForce() {
        return force;
    }

    public void setForce(boolean force) {
        this.force = force;
    }

    @Override
    public String toString() {
        return "DatasetAnnotationTaskProperties{" +
                "databaseName='" + databaseName + '\'' +
                ", force=" + force +
                '}';
    }
}
