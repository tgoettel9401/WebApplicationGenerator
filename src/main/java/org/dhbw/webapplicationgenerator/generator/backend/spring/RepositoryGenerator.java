package org.dhbw.webapplicationgenerator.generator.backend.spring;

import lombok.AllArgsConstructor;
import org.dhbw.webapplicationgenerator.generator.util.FileFolderGenerator;
import org.dhbw.webapplicationgenerator.generator.util.FreemarkerTemplateProcessor;
import org.dhbw.webapplicationgenerator.generator.util.PackageNameResolver;
import org.dhbw.webapplicationgenerator.model.request.ProjectRequest;
import org.dhbw.webapplicationgenerator.model.request.datamodel.DataModel;
import org.dhbw.webapplicationgenerator.model.request.datamodel.Entity;
import org.dhbw.webapplicationgenerator.model.response.Project;
import org.dhbw.webapplicationgenerator.model.response.ProjectDirectory;
import org.springframework.stereotype.Service;

import java.io.File;
import java.util.*;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class RepositoryGenerator extends FileFolderGenerator {

    private static final String JAVA_CLASS_ENDING = ".java";

    private final PackageNameResolver packageNameResolver;
    private final FreemarkerTemplateProcessor freemarkerTemplateProcessor;

    public Project create(Project project, ProjectRequest request, ProjectDirectory parent) {
        create(request, parent);
        return project;
    }

    private void create(ProjectRequest request, ProjectDirectory parent) {
        ProjectDirectory domainDir = addDirectory("repository", Optional.of(parent));
        DataModel dataModel = request.getDataModel();
        for (Entity entity : dataModel.getEntities()) {
            addFile(createRepository(entity, request), domainDir);
        }
    }

    private File createRepository(Entity entity, ProjectRequest request) {

        // Initialize Data Model for Freemarker
        Map<String, Object> dataModel = new HashMap<>();
        dataModel.put("packageName", packageNameResolver.resolveRepository(request));
        dataModel.put("imports", getImports(entity, request));
        dataModel.put("entityClassName", entity.getTitle());
        dataModel.put("relationsToOne", entity.getRelations().stream()
                .filter(relation -> !relation.getRelationType().isToMany())
                .collect(Collectors.toList())
        );

        // Process the template and return the file
        String filename = entity.getClassName() + "Repository" + JAVA_CLASS_ENDING;
        return freemarkerTemplateProcessor.process("Repository.ftl", dataModel, filename);
    }

    private List<String> getImports(Entity entity, ProjectRequest request) {
        List<String> imports = new ArrayList<>();

        // Basic imports
        imports.add("org.springframework.data.jpa.repository.JpaRepository");

        // Add the lists if there is any toOne-relation.
        if (entity.getRelations().stream().anyMatch(relation -> !relation.getRelationType().isToMany())) {
            imports.add("java.util.List");
        }

        // Entity import
        imports.add(packageNameResolver.resolveEntity(request) + "." + entity.getTitle());
        return imports;
    }

}
