package org.dhbw.webapplicationgenerator.generator.backend.java.other;

import lombok.AllArgsConstructor;
import org.dhbw.webapplicationgenerator.generator.util.FileFolderGenerator;
import org.dhbw.webapplicationgenerator.model.request.ProjectRequest;
import org.dhbw.webapplicationgenerator.model.response.Project;
import org.dhbw.webapplicationgenerator.model.response.ProjectDirectory;
import org.dhbw.webapplicationgenerator.generator.util.FreemarkerTemplateProcessor;
import org.dhbw.webapplicationgenerator.generator.util.PackageNameResolver;
import org.springframework.stereotype.Service;

import java.io.File;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@Service
@AllArgsConstructor
public class ExceptionGenerator extends FileFolderGenerator {

    private static final String JAVA_CLASS_ENDING = ".java";

    private final PackageNameResolver packageNameResolver;
    private final FreemarkerTemplateProcessor freemarkerTemplateProcessor;

    /**
     * Creates the needed ExceptionClasses like NotFoundException.
     * @param project Project to be updated
     * @param request Request the project should be created/updated with
     * @return ExceptionClasses
     */
    public Project create(Project project, ProjectRequest request, ProjectDirectory parent) {
        ProjectDirectory exceptionDir = addDirectory("exception", Optional.of(parent));
        String packageName = packageNameResolver.resolveException(request);
        addFile(createNotFoundException(packageName), exceptionDir);
        return project;
    }

    private File createNotFoundException(String packageName) {
        Map<String, Object> dataModel = new HashMap<>();
        dataModel.put("packageName", packageName);
        String filename = "NotFoundException" + JAVA_CLASS_ENDING;
        return freemarkerTemplateProcessor.process("NotFoundException.ftl", dataModel, filename);
    }
}
