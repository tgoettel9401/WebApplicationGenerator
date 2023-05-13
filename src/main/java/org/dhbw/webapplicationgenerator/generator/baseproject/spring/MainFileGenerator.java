package org.dhbw.webapplicationgenerator.generator.baseproject.spring;

import lombok.AllArgsConstructor;
import org.dhbw.webapplicationgenerator.generator.util.FileFolderGenerator;
import org.dhbw.webapplicationgenerator.generator.util.FreemarkerTemplateProcessor;
import org.dhbw.webapplicationgenerator.model.request.ProjectRequest;
import org.dhbw.webapplicationgenerator.model.request.backend.SpringBootData;
import org.dhbw.webapplicationgenerator.model.response.Project;
import org.dhbw.webapplicationgenerator.model.response.ProjectDirectory;
import org.dhbw.webapplicationgenerator.model.response.ProjectFile;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
@AllArgsConstructor
public class MainFileGenerator extends FileFolderGenerator {

    private static final String JAVA_CLASS_ENDING = ".java";

    private final FreemarkerTemplateProcessor freemarkerTemplateProcessor;

    public Project addMainFile(Project project, ProjectRequest request, ProjectDirectory parent) {
        create(request, parent);
        return project;
    }

    // TODO: Make specific to Backend as this is only correct for SpringBoot!
    public ProjectFile create(ProjectRequest request, ProjectDirectory parent) {

        // Initialize Data Model for Freemarker
        Map<String, Object> dataModel = new HashMap<>();
        SpringBootData data = (SpringBootData) request.getBackend().getData();
        dataModel.put("packageName", data.getGroup() + "." + data.getArtifact());
        dataModel.put("className", getClassName(request));

        // Process the template and return the file
        String filename = getClassName(request) + JAVA_CLASS_ENDING;
        return addFile(freemarkerTemplateProcessor.process(
                        "MainApplication.ftl", dataModel, filename),
                parent
        );
    }

    private String getClassName(ProjectRequest request) {
        String className = request.getTitleWithoutSpaces() + "Application";
        className = className.replace("-", "");
        return className;
    }

}
