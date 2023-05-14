package org.dhbw.webapplicationgenerator.generator.frontend.vaadin;

import lombok.AllArgsConstructor;
import org.dhbw.webapplicationgenerator.generator.util.FileFolderGenerator;
import org.dhbw.webapplicationgenerator.generator.util.FreemarkerTemplateProcessor;
import org.dhbw.webapplicationgenerator.generator.util.PackageNameResolver;
import org.dhbw.webapplicationgenerator.generator.util.Utils;
import org.dhbw.webapplicationgenerator.model.request.ProjectRequest;
import org.dhbw.webapplicationgenerator.model.request.datamodel.Entity;
import org.dhbw.webapplicationgenerator.model.response.Project;
import org.dhbw.webapplicationgenerator.model.response.ProjectDirectory;
import org.springframework.stereotype.Service;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@AllArgsConstructor
public class VaadinEntityOverviewGenerator extends FileFolderGenerator {

    private PackageNameResolver packageNameResolver;
    private FreemarkerTemplateProcessor freemarkerTemplateProcessor;

    public Project add(ProjectRequest request, Project project, ProjectDirectory frontendDirectory) {
        for (Entity entity : request.getDataModel().getEntities()) {
            addFile(createFile(request, entity), frontendDirectory);
        }
        return project;
    }

    private File createFile(ProjectRequest request, Entity entity) {

        // Initialize Data Model for Freemarker
        Map<String, Object> dataModel = new HashMap<>();
        dataModel.put("packageName", packageNameResolver.resolveJavaFrontend(request));
        dataModel.put("applicationImports", getImports(request, entity));
        dataModel.put("entityVariableName", entity.getName());
        dataModel.put("entityVariableNamePlural", Utils.plural(entity.getName()));
        dataModel.put("entityClassName", entity.getTitle());
        dataModel.put("entityClassNamePlural", Utils.plural(entity.getTitle()));
        dataModel.put("repositoryVariableName", entity.getRepositoryVariableName());
        dataModel.put("repositoryClassName", entity.getRepositoryClassName());
        dataModel.put("attributes", entity.getAttributes());

        // Process the template and return the file
        String filename = capitalize(entity.getName()) + "Overview" + JAVA_CLASS_ENDING;
        return freemarkerTemplateProcessor.process("EntityOverview.ftl", dataModel, filename);
    }

    private List<String> getImports(ProjectRequest request, Entity entity) {
        List<String> imports = new ArrayList<>();
        imports.add(packageNameResolver.resolveEntity(request) + "." + entity.getClassName());
        imports.add(packageNameResolver.resolveRepository(request) + "." + entity.getRepositoryClassName());
        return imports;
    }

}
