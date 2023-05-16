package org.dhbw.webapplicationgenerator.generator.deployment.docker;

import lombok.AllArgsConstructor;
import org.dhbw.webapplicationgenerator.generator.util.FileFolderGenerator;
import org.dhbw.webapplicationgenerator.generator.util.FreemarkerTemplateProcessor;
import org.dhbw.webapplicationgenerator.model.request.ProjectRequest;
import org.dhbw.webapplicationgenerator.model.request.backend.JavaData;
import org.dhbw.webapplicationgenerator.model.request.deployment.DockerData;
import org.springframework.stereotype.Service;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

@Service
@AllArgsConstructor
public class BuildAndRunScriptGenerator extends FileFolderGenerator {

    private final FreemarkerTemplateProcessor freemarkerTemplateProcessor;

    public File create(ProjectRequest request) {

        DockerData dockerData = (DockerData) request.getDeployment().getData();
        JavaData javaData = (JavaData) request.getBackend().getData();
        // TODO: Alternatives if other programming languages! Maybe therefore add a getBuildCommand to BackendData
        //  interface and implement this in JavaData?

        // Initialize Data Model for Freemarker
        Map<String, Object> dataModel = new HashMap<>();
        dataModel.put("imageName", dockerData.getImageName());
        dataModel.put("buildCommand", javaData.getJavaBuildTool().getBuildCommand());
        dataModel.put("cleanCommand", javaData.getJavaBuildTool().getCleanCommand());

        // Process the template and return the file
        String filename = "buildAndRun.sh";
        return freemarkerTemplateProcessor.process("BuildAndRun.ftl", dataModel, filename);
    }

}
