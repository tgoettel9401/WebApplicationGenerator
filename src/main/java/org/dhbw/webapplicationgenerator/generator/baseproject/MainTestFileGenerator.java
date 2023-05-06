package org.dhbw.webapplicationgenerator.generator.baseproject;

import lombok.AllArgsConstructor;
import org.dhbw.webapplicationgenerator.model.request.ProjectRequest;
import org.dhbw.webapplicationgenerator.model.request.backend.SpringBootData;
import org.dhbw.webapplicationgenerator.model.response.ProjectDirectory;
import org.dhbw.webapplicationgenerator.model.response.ProjectFile;
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

    public ProjectFile createOld(CreationRequest request, ProjectDirectory parent) {

        // Initialize Data Model for Freemarker
        Map<String, Object> dataModel = new HashMap<>();
        dataModel.put("packageName", request.getProject().getGroup() + "." + request.getProject().getArtifact());
        dataModel.put("className", getClassNameOld(request));

        // Process the template and return the file
        String filename = getClassNameOld(request) + JAVA_CLASS_ENDING;
        return addFile(freemarkerTemplateProcessor.process(
                "MainApplicationTest.ftl", dataModel, filename),
                parent
        );
    }

    // TODO: Make this dependent on backend as well (as this is specific for SpringBoot here)
    public ProjectFile create(ProjectRequest request, ProjectDirectory parent) {

        // Initialize Data Model for Freemarker
        Map<String, Object> dataModel = new HashMap<>();
        SpringBootData data = (SpringBootData) request.getBackend().getData();
        dataModel.put("packageName", data.getGroup() + "." + data.getArtifact());
        dataModel.put("className", getClassName(request));

        // Process the template and return the file
        String filename = getClassName(request) + JAVA_CLASS_ENDING;
        return addFile(freemarkerTemplateProcessor.process(
                        "MainApplicationTest.ftl", dataModel, filename),
                parent
        );
    }

    private String getClassNameOld(CreationRequest request) {
        String className = request.getProject().getTitleWithoutSpaces() + "Application";
        className = className.replace("-", "");
        className = className + "Tests";
        return className;
    }

    private String getClassName(ProjectRequest request) {
        String className = request.getTitleWithoutSpaces() + "Application";
        className = className.replace("-", "");
        className = className + "Tests";
        return className;
    }

}
