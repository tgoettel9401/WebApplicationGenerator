package org.dhbw.webapplicationgenerator.generator.baseproject.spring;

import lombok.AllArgsConstructor;
import org.dhbw.webapplicationgenerator.generator.baseproject.FileFolderGenerator;
import org.dhbw.webapplicationgenerator.generator.util.FreemarkerTemplateProcessor;
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

    public Project addApplicationProperties(Project project, ProjectDirectory parent) {
        // Initialize Data Model for Freemarker
        Map<String, Object> dataModel = new HashMap<>();
        // TODO: Add parameter api-path according to request.

        // Process the template and return the file
        String filename = "application.properties";
        File file = freemarkerTemplateProcessor.process("ApplicationProperties.ftl", dataModel, filename);
        addFile(file, parent);
        return project;
    }

}
