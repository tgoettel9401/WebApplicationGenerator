package org.dhbw.webapplicationgenerator.generator.baseproject;

import lombok.AllArgsConstructor;
import org.dhbw.webapplicationgenerator.model.request.ProjectRequest;
import org.dhbw.webapplicationgenerator.model.response.ProjectDirectory;
import org.dhbw.webapplicationgenerator.model.response.ProjectFile;
import org.dhbw.webapplicationgenerator.generator.util.FreemarkerTemplateProcessor;
import org.dhbw.webapplicationgenerator.webclient.request.CreationRequest;
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
    public ProjectFile createOld(CreationRequest request, ProjectDirectory parent) {
        return addFile(createReadmeFileOld(request), parent);
    }

    /**
     * Creates your Readme-file based on the selections made.
     * @param parent Parent directory, usually this is the root directory of the project
     * @return Readme-File
     */
    public ProjectFile create(ProjectRequest request, ProjectDirectory parent) {
        return addFile(createReadmeFile(request), parent);
    }

    private File createReadmeFileOld(CreationRequest request) {
        // Initialize Data Model for Freemarker
        Map<String, Object> dataModel = new HashMap<>();
        dataModel.put("isDockerEnabled", request.getDocker() == null || request.getDocker().isEnabled());

        // Process the template and return the file
        String filename = "README.md";
        return freemarkerTemplateProcessor.process("Readme.ftl", dataModel, filename);
    }

    private File createReadmeFile(ProjectRequest request) {
        // Initialize Data Model for Freemarker
        Map<String, Object> dataModel = new HashMap<>();
        dataModel.put("isDockerEnabled", request.getDeployment() == null || request.getDeployment().isEnabled()); // TODO: Add Deployment-part to Readme rather than Docker

        // Process the template and return the file
        String filename = "README.md";
        return freemarkerTemplateProcessor.process("Readme.ftl", dataModel, filename);
    }

}
