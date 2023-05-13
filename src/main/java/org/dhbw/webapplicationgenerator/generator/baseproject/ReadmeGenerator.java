package org.dhbw.webapplicationgenerator.generator.baseproject;

import lombok.AllArgsConstructor;
import org.dhbw.webapplicationgenerator.generator.util.FileFolderGenerator;
import org.dhbw.webapplicationgenerator.generator.util.FreemarkerTemplateProcessor;
import org.dhbw.webapplicationgenerator.model.request.ProjectRequest;
import org.dhbw.webapplicationgenerator.model.response.ProjectDirectory;
import org.dhbw.webapplicationgenerator.model.response.ProjectFile;
import org.springframework.stereotype.Service;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

@Service
@AllArgsConstructor
public class ReadmeGenerator extends FileFolderGenerator {

    private final FreemarkerTemplateProcessor freemarkerTemplateProcessor;

    /**
     * Creates your Readme-file based on the selections made.
     * @param parent Parent directory, usually this is the root directory of the project
     * @return Readme-File
     */
    public ProjectFile create(ProjectRequest request, ProjectDirectory parent) {
        return addFile(createReadmeFile(request), parent);
    }

    private File createReadmeFile(ProjectRequest request) {
        // Initialize Data Model for Freemarker
        Map<String, Object> dataModel = new HashMap<>();
        dataModel.put("isDeploymentEnabled", request.getDeployment() == null || request.getDeployment().isEnabled());
        dataModel.put("isSecurityEnabled", request.isSecurityEnabled());

        // Process the template and return the file
        String filename = "README.md";
        return freemarkerTemplateProcessor.process("Readme.ftl", dataModel, filename);
    }

}
