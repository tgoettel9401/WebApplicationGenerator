package org.dhbw.webapplicationgenerator.generator.repository;

import lombok.AllArgsConstructor;
import org.dhbw.webapplicationgenerator.generator.Project;
import org.dhbw.webapplicationgenerator.generator.baseproject.FileFolderGenerator;
import org.dhbw.webapplicationgenerator.generator.model.ProjectDirectory;
import org.dhbw.webapplicationgenerator.generator.util.FreemarkerTemplateProcessor;
import org.dhbw.webapplicationgenerator.generator.util.PackageNameResolver;
import org.dhbw.webapplicationgenerator.webclient.request.CreationRequest;
import org.dhbw.webapplicationgenerator.webclient.request.RequestEntity;
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

    public Project create(Project project, CreationRequest request) {
        ProjectDirectory artifactDir = getMainProjectDirectory(project, request);
        create(request, artifactDir);
        return project;
    }

    private void create(CreationRequest request, ProjectDirectory parent) {
        ProjectDirectory domainDir = addDirectory("repository", Optional.of(parent));
        for (RequestEntity entity : request.getEntities()) {
            addFile(createRepository(entity, request), domainDir);
        }
    }

    private File createRepository(RequestEntity entity, CreationRequest request) {

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

    private List<String> getImports(RequestEntity entity, CreationRequest request) {
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
