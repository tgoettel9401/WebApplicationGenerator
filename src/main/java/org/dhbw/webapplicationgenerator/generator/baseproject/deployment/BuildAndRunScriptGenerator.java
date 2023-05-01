package org.dhbw.webapplicationgenerator.generator.baseproject.deployment;

import lombok.AllArgsConstructor;
import org.dhbw.webapplicationgenerator.generator.baseproject.FileFolderGenerator;
import org.dhbw.webapplicationgenerator.generator.util.FreemarkerTemplateProcessor;
import org.dhbw.webapplicationgenerator.webclient.request.CreationRequest;
import org.springframework.stereotype.Service;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

@Service
@AllArgsConstructor
public class BuildAndRunScriptGenerator extends FileFolderGenerator {

    private final FreemarkerTemplateProcessor freemarkerTemplateProcessor;

    public File create(CreationRequest request) {
        // Initialize Data Model for Freemarker
        Map<String, Object> dataModel = new HashMap<>();
        dataModel.put("imageName", request.getDocker().getImageName());
        dataModel.put("buildCommand", request.getDocker().getJavaBuildTool().getBuildCommand());

        // Process the template and return the file
        String filename = "buildAndRun.sh";
        return freemarkerTemplateProcessor.process("BuildAndRun.ftl", dataModel, filename);
    }
}
