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
public class RoleEntityGenerator extends FileFolderGenerator {

    private final PackageNameResolver packageNameResolver;
    private final FreemarkerTemplateProcessor freemarkerTemplateProcessor;

    /**
     * Adds the Role-Entity to the project.
     * @param project Project to be updated
     * @param request Request to construct the project
     * @return Updated Project
     */
    public Project add(Project project, ProjectRequest request) {
        ProjectDirectory entityDirectory = addDirectory("domain",
                Optional.of(getMainProjectDirectory(project, request)));
        Map<String, Object> dataModel = new HashMap<>();
        dataModel.put("packageName", packageNameResolver.resolveEntity(request));
        dataModel.put("roleTableName", request.getSecurity().getRoleTableName());
        String filename = "Role" + JAVA_CLASS_ENDING;
        File file = freemarkerTemplateProcessor.process("RoleEntity.ftl", dataModel, filename);
        addFile(file, entityDirectory);
        return project;
    }

}
