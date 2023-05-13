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
import java.util.*;

@Service
@AllArgsConstructor
public class UserServiceGenerator extends FileFolderGenerator {

    private static final String JAVA_CLASS_ENDING = ".java";

    private final PackageNameResolver packageNameResolver;
    private final FreemarkerTemplateProcessor freemarkerTemplateProcessor;

    /**
     * Adds the AppUserService to the project.
     * @param project Project to be updated
     * @param request Request to construct the project
     * @return Updated Project
     */
    public Project add(Project project, ProjectRequest request) {
        ProjectDirectory serviceDirectory = addDirectory("service", Optional.of(getMainProjectDirectory(project, request)));
        Map<String, Object> dataModel = new HashMap<>();
        dataModel.put("packageName", packageNameResolver.resolveService(request));
        dataModel.put("applicationImports", getApplicationImports(request));
        String filename = "AppUserService" + JAVA_CLASS_ENDING;
        File file = freemarkerTemplateProcessor.process("AppUserService.ftl", dataModel, filename);
        addFile(file, serviceDirectory);
        return project;
    }

    private List<String> getApplicationImports(ProjectRequest request) {
        String entityPackageName = packageNameResolver.resolveEntity(request);
        String transferObjectPackageName = packageNameResolver.resolveTransferObjects(request);
        String repositoryPackageName = packageNameResolver.resolveRepository(request);
        List<String> applicationImports = new ArrayList<>();
        applicationImports.add(transferObjectPackageName + ".RegistrationRequest");
        applicationImports.add(entityPackageName + ".Role");
        applicationImports.add(entityPackageName + ".AppUser");
        applicationImports.add(repositoryPackageName + ".AppUserRepository");
        return applicationImports;
    }

}
