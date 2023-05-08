package org.dhbw.webapplicationgenerator.generator.java.entity;

import lombok.AllArgsConstructor;
import org.dhbw.webapplicationgenerator.model.request.ProjectRequest;
import org.dhbw.webapplicationgenerator.model.request.datamodel.Attribute;
import org.dhbw.webapplicationgenerator.model.request.datamodel.Entity;
import org.dhbw.webapplicationgenerator.model.response.Project;
import org.dhbw.webapplicationgenerator.generator.baseproject.FileFolderGenerator;
import org.dhbw.webapplicationgenerator.model.response.ProjectDirectory;
import org.dhbw.webapplicationgenerator.generator.util.FreemarkerTemplateProcessor;
import org.dhbw.webapplicationgenerator.generator.util.PackageNameResolver;
import org.dhbw.webapplicationgenerator.generator.util.Utils;
import org.dhbw.webapplicationgenerator.model.request.datamodel.DataType;
import org.dhbw.webapplicationgenerator.webclient.request.CreationRequest;
import org.dhbw.webapplicationgenerator.webclient.request.EntityAttribute;
import org.dhbw.webapplicationgenerator.webclient.request.RequestEntity;
import org.springframework.stereotype.Service;

import java.io.File;
import java.util.*;

@Service
@AllArgsConstructor
public class EntityGenerator extends FileFolderGenerator {

    private final PackageNameResolver packageNameResolver;
    private final FreemarkerTemplateProcessor freemarkerTemplateProcessor;

    /**
     *
     * @param project Project to be updated
     * @param request Request to construct the project
     * @param parent main-directory for the sourcecode
     * @return
     */
    public Project create(Project project, ProjectRequest request, ProjectDirectory parent) {

        // Create the domain directory with entities.
        ProjectDirectory domainDir = addDirectory("domain", Optional.of(parent));
        String entityPackageName = packageNameResolver.resolveEntity(request);

        // Finally we add entities to each of the directories.
        for (Entity entity : request.getDataModel().getEntities()) {
            addFile(createEntity(entity, entityPackageName), domainDir);
        }

        return project;

    }

    public Project createOld(Project project, CreationRequest request) {
        ProjectDirectory artifactDir = getMainProjectDirectoryOld(project, request);
        createOld(request, artifactDir);
        return project;
    }

    private void createOld(CreationRequest request, ProjectDirectory parent) {

        // Create the domain directory with entities.
        ProjectDirectory domainDir = addDirectory("domain", Optional.of(parent));
        String entityPackageName = packageNameResolver.resolveEntity(request);

        // Finally we add entities to each of the directories.
        for (RequestEntity entity : request.getEntities()) {
            addFile(createEntity(entity, entityPackageName), domainDir);
        }

    }

    private File createEntity(RequestEntity entity, String packageName) {

        // Initialize Data Model for Freemarker
        Map<String, Object> dataModel = new HashMap<>();
        dataModel.put("tableName", entity.getTableName() != null ? entity.getTableName() : plural(entity.getName().toLowerCase()));
        dataModel.put("className", Utils.capitalize(entity.getName()));
        dataModel.put("classNamePlural", Utils.capitalize(plural(entity.getName())));
        dataModel.put("packageName", packageName);
        dataModel.put("imports", getEntityImports(entity));
        dataModel.put("entityName", entity.getName());
        dataModel.put("attributes", entity.getAttributes());
        dataModel.put("relations", entity.getRelations());

        // Process the template and return the file
        String filename = capitalize(entity.getName()) + JAVA_CLASS_ENDING;
        return freemarkerTemplateProcessor.process("Entity.ftl", dataModel, filename);
    }

    private File createEntity(Entity entity, String packageName) {

        // Initialize Data Model for Freemarker
        Map<String, Object> dataModel = new HashMap<>();
        dataModel.put("tableName", entity.getTableName() != null ? entity.getTableName() : plural(entity.getName().toLowerCase()));
        dataModel.put("className", Utils.capitalize(entity.getName()));
        dataModel.put("classNamePlural", Utils.capitalize(plural(entity.getName())));
        dataModel.put("packageName", packageName);
        dataModel.put("imports", getEntityImports(entity));
        dataModel.put("entityName", entity.getName());
        dataModel.put("attributes", entity.getAttributes());
        dataModel.put("relations", entity.getRelations());

        // Process the template and return the file
        String filename = capitalize(entity.getName()) + JAVA_CLASS_ENDING;
        return freemarkerTemplateProcessor.process("Entity.ftl", dataModel, filename);
    }

    private List<String> getEntityImports(RequestEntity entity) {
        List<String> imports = new ArrayList<>();
        imports.add("javax.persistence.*");
        imports.add("java.io.Serializable");
        entity.getAttributes().stream()
                .map(EntityAttribute::getDataType)
                .map(DataType::fromName)
                .map(DataType::getPackageToImport)
                .flatMap(List::stream)
                .filter(packageToImport -> !packageToImport.isEmpty())
                .distinct()
                .forEach(imports::add);

        // If there is any relation with _TO_MANY, then we have to import a List.
        if (entity.getRelations().stream().anyMatch(relation -> relation.getRelationType().isToMany())) {
            imports.add("java.util.List");
            imports.add("java.util.ArrayList");
        }
        return imports;
    }

    private List<String> getEntityImports(Entity entity) {
        List<String> imports = new ArrayList<>();
        imports.add("javax.persistence.*");
        imports.add("java.io.Serializable");
        entity.getAttributes().stream()
                .map(Attribute::getDataType)
                .map(DataType::getPackageToImport)
                .flatMap(List::stream)
                .filter(packageToImport -> !packageToImport.isEmpty())
                .distinct()
                .forEach(imports::add);

        // If there is any relation with _TO_MANY, then we have to import a List.
        if (entity.getRelations().stream().anyMatch(relation -> relation.getRelationType().isToMany())) {
            imports.add("java.util.List");
            imports.add("java.util.ArrayList");
        }
        return imports;
    }

}
