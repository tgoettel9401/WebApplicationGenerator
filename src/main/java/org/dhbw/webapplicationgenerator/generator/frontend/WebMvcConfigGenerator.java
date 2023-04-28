package org.dhbw.webapplicationgenerator.generator.frontend;

import lombok.AllArgsConstructor;
import org.dhbw.webapplicationgenerator.generator.Project;
import org.dhbw.webapplicationgenerator.generator.baseproject.FileFolderGenerator;
import org.dhbw.webapplicationgenerator.generator.model.ProjectDirectory;
import org.dhbw.webapplicationgenerator.generator.util.FreemarkerTemplateProcessor;
import org.dhbw.webapplicationgenerator.generator.util.PackageNameResolver;
import org.dhbw.webapplicationgenerator.webclient.request.CreationRequest;
import org.dhbw.webapplicationgenerator.webclient.request.RequestEntity;
import org.springframework.stereotype.Service;

import java.io.File;
import java.util.*;

@Service
@AllArgsConstructor
public class WebMvcConfigGenerator extends FileFolderGenerator {

    private static final String JAVA_CLASS_ENDING = ".java";

    private final PackageNameResolver packageNameResolver;
    private final FreemarkerTemplateProcessor freemarkerTemplateProcessor;

    public Project create(Project project, CreationRequest request) {
        ProjectDirectory artifactDir = getMainProjectDirectory(project, request);
        create(request, artifactDir);
        return project;
    }

    private void create(CreationRequest request, ProjectDirectory parent) {
        ProjectDirectory configDir = addDirectory("config", Optional.of(parent));
        String packageName = packageNameResolver.resolveConfig(request);
        addFile(createWebMvcConfigFile(request.getEntities(), packageName), configDir);
    }

    private File createWebMvcConfigFile(Set<RequestEntity> entities, String packageName) {

        // Initialize imports
        List<String> imports = new ArrayList<>();
        imports.add("org.springframework.context.annotation.Configuration");
        imports.add("org.springframework.web.servlet.config.annotation.ViewControllerRegistry");
        imports.add("org.springframework.web.servlet.config.annotation.WebMvcConfigurer");

        // Initialize Data Model for Freemarker
        Map<String, Object> dataModel = new HashMap<>();
        dataModel.put("packageName", packageName);
        dataModel.put("imports", imports);
        dataModel.put("entities", entities);

        // Process the template and return the file
        String filename = "MvcConfig" + JAVA_CLASS_ENDING;
        return freemarkerTemplateProcessor.process("MvcConfig.ftl", dataModel, filename);
    }

}
