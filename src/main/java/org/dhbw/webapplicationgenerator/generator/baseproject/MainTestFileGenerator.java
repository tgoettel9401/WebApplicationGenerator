package org.dhbw.webapplicationgenerator.generator.baseproject;

import lombok.AllArgsConstructor;
import org.dhbw.webapplicationgenerator.generator.model.ProjectDirectory;
import org.dhbw.webapplicationgenerator.generator.model.ProjectFile;
import org.dhbw.webapplicationgenerator.generator.util.FreemarkerTemplateProcessor;
import org.dhbw.webapplicationgenerator.webclient.request.CreationRequest;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
@AllArgsConstructor
public class MainTestFileGenerator extends FileFolderGenerator {

    private static final String JAVA_CLASS_ENDING = ".java";

    private final FreemarkerTemplateProcessor freemarkerTemplateProcessor;

    public ProjectFile create(CreationRequest request, ProjectDirectory parent) {

        // Initialize Data Model for Freemarker
        Map<String, Object> dataModel = new HashMap<>();
        dataModel.put("packageName", request.getProject().getGroup() + "." + request.getProject().getArtifact());
        dataModel.put("className", getClassName(request));

        // Process the template and return the file
        String filename = getClassName(request) + JAVA_CLASS_ENDING;
        return addFile(freemarkerTemplateProcessor.process(
                "MainApplicationTest.ftl", dataModel, filename),
                parent
        );
    }

    private String getClassName(CreationRequest request) {
        String className = request.getProject().getTitleWithoutSpaces() + "Application";
        className = className.replace("-", "");
        className = className + "Tests";
        return className;
    }

}