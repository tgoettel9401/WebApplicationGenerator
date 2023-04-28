package org.dhbw.webapplicationgenerator.generator.entity;

import lombok.AllArgsConstructor;
import org.dhbw.webapplicationgenerator.generator.Project;
import org.dhbw.webapplicationgenerator.generator.baseproject.FileFolderGenerator;
import org.dhbw.webapplicationgenerator.generator.model.ProjectDirectory;
import org.dhbw.webapplicationgenerator.generator.util.FreemarkerTemplateProcessor;
import org.dhbw.webapplicationgenerator.generator.util.PackageNameResolver;
import org.dhbw.webapplicationgenerator.generator.util.Utils;
import org.dhbw.webapplicationgenerator.webclient.request.CreationRequest;
import org.dhbw.webapplicationgenerator.webclient.request.EntityAttribute;
import org.dhbw.webapplicationgenerator.webclient.request.RequestEntity;
import org.springframework.stereotype.Service;

import java.io.File;
import java.util.*;

@Service
@AllArgsConstructor
public class TransferObjectGenerator extends FileFolderGenerator {

    private static final String JAVA_CLASS_ENDING = ".java";

    private final PackageNameResolver packageNameResolver;
    private final FreemarkerTemplateProcessor freemarkerTemplateProcessor;

    public Project create(Project project, CreationRequest request) {
        ProjectDirectory artifactDir = getMainProjectDirectory(project, request);
        create(request, artifactDir);
        return project;
    }

    private void create(CreationRequest request, ProjectDirectory parent) {

        // Create the transferObjects directory with transferObjects.
        ProjectDirectory dtoDir = addDirectory("transferObject", Optional.of(parent));
        String transferObjectPackageName = packageNameResolver.resolveTransferObjects(request);

        // Finally we add transferobjects to each of the directories.
        for (RequestEntity entity : request.getEntities()) {
            addFile(createTransferObjectWithFreemarker(entity, transferObjectPackageName), dtoDir);
        }

    }

    private File createTransferObjectWithFreemarker(RequestEntity entity, String packageName) {

        // Initialize Data Model for Freemarker
        Map<String, Object> dataModel = new HashMap<>();
        dataModel.put("packageName", packageName);
        dataModel.put("imports", getImports(entity));
        dataModel.put("className", Utils.capitalize(entity.getName()));
        dataModel.put("attributes", entity.getAttributes());
        dataModel.put("relations", entity.getRelations());

        // Process the template and return the file
        String filename = capitalize(entity.getName()) + "Request" + JAVA_CLASS_ENDING;
        return freemarkerTemplateProcessor.process("TransferObject.ftl", dataModel, filename);
    }

    private List<String> getImports(RequestEntity entity) {
        List<String> imports = new ArrayList<>();
        imports.add("java.io.Serializable");

        if (entity.getRelations().stream().anyMatch(relation -> relation.getRelationType().isToMany())) {
            imports.add("java.util.List");
            imports.add("java.util.ArrayList");
        }

        entity.getAttributes().stream()
                .map(EntityAttribute::getDataType)
                .map(DataType::fromName)
                .map(DataType::getPackageToImport)
                .flatMap(List::stream)
                .filter(packageToImport -> !packageToImport.isEmpty())
                .distinct()
                .forEach(imports::add);
        return imports;
    }
}
