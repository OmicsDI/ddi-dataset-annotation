package uk.ac.ebi.ddi.task.ddidatasetannotation.configuration;

import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.cloud.task.configuration.EnableTask;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;

@Configuration
@EnableTask
@EnableConfigurationProperties({ DatasetAnnotationTaskProperties.class })
@EnableMongoRepositories(value = "uk.ac.ebi.ddi.service.db.repo")
@ComponentScan({"uk.ac.ebi.ddi.service.db"})
public class TaskConfiguration {
}
