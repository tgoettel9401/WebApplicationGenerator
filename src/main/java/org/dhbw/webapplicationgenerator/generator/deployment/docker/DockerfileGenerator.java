package org.dhbw.webapplicationgenerator.generator.deployment.docker;

import lombok.AllArgsConstructor;
import org.dhbw.webapplicationgenerator.generator.util.FileFolderGenerator;
import org.dhbw.webapplicationgenerator.generator.util.FreemarkerTemplateProcessor;
import org.dhbw.webapplicationgenerator.model.request.ProjectRequest;
import org.dhbw.webapplicationgenerator.model.request.deployment.DockerData;
import org.springframework.stereotype.Service;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

@Service
@AllArgsConstructor
public class DockerfileGenerator extends FileFolderGenerator {

    private final FreemarkerTemplateProcessor freemarkerTemplateProcessor;

    public File create(ProjectRequest request) {
        // Initialize Data Model for Freemarker
        DockerData data = (DockerData) request.getDeployment().getData();
        Map<String, Object> dataModel = new HashMap<>();
        dataModel.put("baseImage", data.getBaseImage());

        // Process the template and return the file
        String filename = "Dockerfile";
        return freemarkerTemplateProcessor.process("Dockerfile.ftl", dataModel, filename);
    }

}
