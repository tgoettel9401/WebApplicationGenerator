package org.dhbw.webapplicationgenerator.generator.baseproject.spring;

import lombok.AllArgsConstructor;
import org.dhbw.webapplicationgenerator.generator.util.FileFolderGenerator;
import org.dhbw.webapplicationgenerator.generator.util.FreemarkerTemplateProcessor;
import org.dhbw.webapplicationgenerator.model.request.ProjectRequest;
import org.dhbw.webapplicationgenerator.model.request.backend.SpringBootData;
import org.dhbw.webapplicationgenerator.model.response.Project;
import org.dhbw.webapplicationgenerator.model.response.ProjectDirectory;
import org.springframework.stereotype.Service;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

@Service
@AllArgsConstructor
public class ApplicationPropertiesGenerator extends FileFolderGenerator {

    private final FreemarkerTemplateProcessor freemarkerTemplateProcessor;

    public Project addApplicationProperties(Project project, ProjectRequest request, ProjectDirectory parent) {
        SpringBootData data = (SpringBootData) request.getBackend().getData();
        // Initialize Data Model for Freemarker
        Map<String, Object> dataModel = new HashMap<>();
        dataModel.put("apiPath", data.getApiPath());
        dataModel.put("h2ConsoleEnabled", data.isEmbeddedH2() && data.isH2ConsoleEnabled());
        dataModel.put("h2ConsolePath", data.getH2ConsolePath());
        dataModel.put("databaseUsername", data.getDatabaseUsername());
        dataModel.put("databasePassword", data.getDatabasePassword());
        dataModel.put("databaseDialect", data.getDatabaseProduct().getDriverDialect());
        dataModel.put("databaseConnectionString", data.getDatabaseConnectionString());

        // Process the template and return the file
        String filename = "application.properties";
        File file = freemarkerTemplateProcessor.process("ApplicationProperties.ftl", dataModel, filename);
        addFile(file, parent);
        return project;
    }

    public Project addApplicationTestProperties(Project project, ProjectRequest request, ProjectDirectory parent) {
        SpringBootData data = (SpringBootData) request.getBackend().getData();
        // Initialize Data Model for Freemarker
        Map<String, Object> dataModel = new HashMap<>();
        dataModel.put("apiPath", data.getApiPath());
        dataModel.put("h2ConsoleEnabled", data.isEmbeddedH2() && data.isH2ConsoleEnabled());
        dataModel.put("h2ConsolePath", data.getH2ConsolePath());
        dataModel.put("databaseUsername", data.getDatabaseUsername());
        dataModel.put("databasePassword", data.getDatabasePassword());
        dataModel.put("databaseDialect", data.getDatabaseProduct().getDriverDialect());
        dataModel.put("databaseConnectionString", data.getDatabaseConnectionString());

        // Process the template and return the file
        String filename = "application-test.properties";
        File file = freemarkerTemplateProcessor.process("ApplicationPropertiesTest.ftl", dataModel, filename);
        addFile(file, parent);
        return project;
    }

}
