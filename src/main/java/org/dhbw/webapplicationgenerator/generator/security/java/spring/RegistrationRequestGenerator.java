package org.dhbw.webapplicationgenerator.generator.security.java.spring;

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
public class RegistrationRequestGenerator extends FileFolderGenerator {

    private final PackageNameResolver packageNameResolver;
    private final FreemarkerTemplateProcessor freemarkerTemplateProcessor;

    /**
     * Adds the RegistrationRequest to the project.
     * @param project Project to be updated
     * @param request Request to construct the project
     * @return Updated Project
     */
    public Project add(Project project, ProjectRequest request) {
        ProjectDirectory transferObjectDirectory = addDirectory("transferObject",
                Optional.of(getMainProjectDirectory(project, request)));
        Map<String, Object> dataModel = new HashMap<>();
        dataModel.put("packageName", packageNameResolver.resolveTransferObjects(request));
        String filename = "RegistrationRequest" + JAVA_CLASS_ENDING;
        File file = freemarkerTemplateProcessor.process("RegistrationRequest.ftl", dataModel, filename);
        addFile(file, transferObjectDirectory);
        return project;
    }

}
