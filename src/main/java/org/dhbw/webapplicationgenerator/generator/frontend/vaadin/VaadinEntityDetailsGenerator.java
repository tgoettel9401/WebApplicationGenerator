package org.dhbw.webapplicationgenerator.generator.frontend.vaadin;

import lombok.AllArgsConstructor;
import org.dhbw.webapplicationgenerator.generator.util.FileFolderGenerator;
import org.dhbw.webapplicationgenerator.generator.util.FreemarkerTemplateProcessor;
import org.dhbw.webapplicationgenerator.generator.util.PackageNameResolver;
import org.dhbw.webapplicationgenerator.generator.util.Utils;
import org.dhbw.webapplicationgenerator.model.request.ProjectRequest;
import org.dhbw.webapplicationgenerator.model.request.datamodel.Entity;
import org.dhbw.webapplicationgenerator.model.request.datamodel.EntityRelation;
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
public class VaadinEntityDetailsGenerator extends FileFolderGenerator {

    private final PackageNameResolver packageNameResolver;
    private final FreemarkerTemplateProcessor freemarkerTemplateProcessor;

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
        dataModel.put("entityRepositoryName", entity.getRepositoryVariableName());
        dataModel.put("entityRepositoryClassName", entity.getRepositoryClassName());
        dataModel.put("attributes", entity.getAttributes());
        dataModel.put("relations", entity.getRelations());

        // Process the template and return the file
        String filename = capitalize(entity.getName()) + "Details" + JAVA_CLASS_ENDING;
        return freemarkerTemplateProcessor.process("EntityDetails.ftl", dataModel, filename);
    }

    private List<String> getImports(ProjectRequest request, Entity entity) {
        List<String> imports = new ArrayList<>();
        imports.add(packageNameResolver.resolveEntity(request) + "." + entity.getClassName());
        imports.add(packageNameResolver.resolveRepository(request) + "." + entity.getRepositoryClassName());
        for (EntityRelation relation : entity.getRelations()) {
            imports.add(packageNameResolver.resolveEntity(request) + "." + relation.getEntityClassName());
            imports.add(packageNameResolver.resolveRepository(request) + "." + relation.getRepositoryClassName());
        }
        return imports;
    }

}
