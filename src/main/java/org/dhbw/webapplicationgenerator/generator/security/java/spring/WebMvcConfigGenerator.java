package org.dhbw.webapplicationgenerator.generator.security.java.spring;

import lombok.AllArgsConstructor;
import org.dhbw.webapplicationgenerator.generator.util.FileFolderGenerator;
import org.dhbw.webapplicationgenerator.generator.util.FreemarkerTemplateProcessor;
import org.dhbw.webapplicationgenerator.generator.util.PackageNameResolver;
import org.dhbw.webapplicationgenerator.model.request.ProjectRequest;
import org.dhbw.webapplicationgenerator.model.request.datamodel.DataModel;
import org.dhbw.webapplicationgenerator.model.request.datamodel.Entity;
import org.dhbw.webapplicationgenerator.model.response.Project;
import org.dhbw.webapplicationgenerator.model.response.ProjectDirectory;
import org.springframework.stereotype.Service;

import java.io.File;
import java.util.*;

@Service
@AllArgsConstructor
public class WebMvcConfigGenerator extends FileFolderGenerator {

    private static final String JAVA_CLASS_ENDING = ".java";

    private final PackageNameResolver packageNameResolver;
    private final FreemarkerTemplateProcessor freemarkerTemplateProcessor;

    // TODO: Move to backend?! Or rather frontend because it is only needed due to frontend?

    public Project create(Project project, ProjectRequest request, ProjectDirectory parent) {
        create(request, parent);
        return project;
    }

    private void create(ProjectRequest request, ProjectDirectory parent) {
        ProjectDirectory configDir = addDirectory("config", Optional.of(parent));
        String packageName = packageNameResolver.resolveConfig(request);
        DataModel dataModel = request.getDataModel();
        addFile(createWebMvcConfigFile(dataModel.getEntities(), packageName), configDir);
    }

    private File createWebMvcConfigFile(List<Entity> entities, String packageName) {
        // TODO: Freemarker

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
