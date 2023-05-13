package org.dhbw.webapplicationgenerator.generator.security.backend.java.spring;

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
import java.util.Optional;

@Service
@AllArgsConstructor
public class WebSecurityConfigGenerator extends FileFolderGenerator {

    private final PackageNameResolver packageNameResolver;
    private final FreemarkerTemplateProcessor freemarkerTemplateProcessor;

    public Project add(Project project, ProjectRequest request) {
        ProjectDirectory serviceDirectory = addDirectory("config", Optional.of(getMainProjectDirectory(project, request)));
        Map<String, Object> dataModel = new HashMap<>();
        dataModel.put("packageName", packageNameResolver.resolveConfig(request));
        String filename = "WebSecurityConfig" + JAVA_CLASS_ENDING;
        File file = freemarkerTemplateProcessor.process("WebSecurityConfig.ftl", dataModel, filename);
        addFile(file, serviceDirectory);
        return project;
    }

}
