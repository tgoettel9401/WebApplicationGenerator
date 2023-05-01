package org.dhbw.webapplicationgenerator.generator.baseproject;

import lombok.AllArgsConstructor;
import org.dhbw.webapplicationgenerator.generator.model.ProjectDirectory;
import org.dhbw.webapplicationgenerator.generator.model.ProjectFile;
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
    public ProjectFile create(CreationRequest request, ProjectDirectory parent) {
        return addFile(createReadmeFile(request), parent);
    }

    private File createReadmeFile(CreationRequest request) {
        // Initialize Data Model for Freemarker
        Map<String, Object> dataModel = new HashMap<>();
        dataModel.put("isDockerEnabled", request.getDocker() == null || request.getDocker().isEnabled());

        // Process the template and return the file
        String filename = "README.md";
        return freemarkerTemplateProcessor.process("Readme.ftl", dataModel, filename);
    }

}
