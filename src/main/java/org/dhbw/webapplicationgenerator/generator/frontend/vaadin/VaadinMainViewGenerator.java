package org.dhbw.webapplicationgenerator.generator.frontend.vaadin;

import lombok.AllArgsConstructor;
import org.dhbw.webapplicationgenerator.generator.util.FileFolderGenerator;
import org.dhbw.webapplicationgenerator.generator.util.FreemarkerTemplateProcessor;
import org.dhbw.webapplicationgenerator.generator.util.PackageNameResolver;
import org.dhbw.webapplicationgenerator.model.request.ProjectRequest;
import org.dhbw.webapplicationgenerator.model.response.Project;
import org.dhbw.webapplicationgenerator.model.response.ProjectDirectory;
import org.springframework.stereotype.Service;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

@Service
@AllArgsConstructor
public class VaadinMainViewGenerator extends FileFolderGenerator {

    private PackageNameResolver packageNameResolver;
    private FreemarkerTemplateProcessor freemarkerTemplateProcessor;

    public Project add(ProjectRequest request, Project project, ProjectDirectory frontendDirectory) {
        addFile(createFile(request), frontendDirectory);
        return project;
    }

    private File createFile(ProjectRequest request) {

        // Initialize Data Model for Freemarker
        Map<String, Object> dataModel = new HashMap<>();
        dataModel.put("packageName", packageNameResolver.resolveJavaFrontend(request));

        // Process the template and return the file
        String filename = "MainView" + JAVA_CLASS_ENDING;
        return freemarkerTemplateProcessor.process("MainView.ftl", dataModel, filename);
    }

}
