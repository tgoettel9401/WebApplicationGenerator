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
import java.util.*;

@Service
@AllArgsConstructor
public class UserDataInitializationGenerator extends FileFolderGenerator {

    private static final String JAVA_CLASS_ENDING = ".java";

    private final PackageNameResolver packageNameResolver;
    private final FreemarkerTemplateProcessor freemarkerTemplateProcessor;

    /**
     * Adds the UserDataInitializer to the project.
     * @param project Project to be updated
     * @param request Request to construct the project
     * @return Updated Project
     */
    public Project add(Project project, ProjectRequest request) {
        ProjectDirectory configDirectory = addDirectory("config", Optional.of(getMainProjectDirectory(project, request)));
        Map<String, Object> dataModel = new HashMap<>();
        dataModel.put("packageName", packageNameResolver.resolveConfig(request));
        dataModel.put("applicationImports", getApplicationImports(request));
        dataModel.put("defaultUsername", request.getSecurity().getDefaultUsername());
        dataModel.put("defaultPassword", request.getSecurity().getDefaultPassword());
        dataModel.put("defaultAdminEmail", request.getSecurity().getDefaultAdminEmail());
        String filename = "UserDataInitializer" + JAVA_CLASS_ENDING;
        File file = freemarkerTemplateProcessor.process("UserDataInitializer.ftl", dataModel, filename);
        addFile(file, configDirectory);
        return project;
    }

    private List<String> getApplicationImports(ProjectRequest request) {
        String entityPackageName = packageNameResolver.resolveEntity(request);
        String repositoryPackageName = packageNameResolver.resolveRepository(request);
        List<String> applicationImports = new ArrayList<>();
        applicationImports.add(entityPackageName + ".Role");
        applicationImports.add(entityPackageName + ".AppUser");
        applicationImports.add(repositoryPackageName + ".RoleRepository");
        applicationImports.add(repositoryPackageName + ".AppUserRepository");
        return applicationImports;
    }

}
