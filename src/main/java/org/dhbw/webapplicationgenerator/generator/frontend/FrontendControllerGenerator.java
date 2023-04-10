package org.dhbw.webapplicationgenerator.generator.frontend;

import lombok.AllArgsConstructor;
import org.dhbw.webapplicationgenerator.generator.Project;
import org.dhbw.webapplicationgenerator.generator.baseproject.FileFolderGenerator;
import org.dhbw.webapplicationgenerator.generator.model.ProjectDirectory;
import org.dhbw.webapplicationgenerator.generator.util.FreemarkerTemplateProcessor;
import org.dhbw.webapplicationgenerator.generator.util.PackageNameResolver;
import org.dhbw.webapplicationgenerator.generator.util.Utils;
import org.dhbw.webapplicationgenerator.webclient.request.CreationRequest;
import org.dhbw.webapplicationgenerator.webclient.request.EntityRelation;
import org.dhbw.webapplicationgenerator.webclient.request.RequestEntity;
import org.springframework.stereotype.Service;

import java.io.File;
import java.util.*;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class FrontendControllerGenerator extends FileFolderGenerator {

    private static final String JAVA_CLASS_ENDING = ".java";

    private final PackageNameResolver packageNameResolver;
    private final FreemarkerTemplateProcessor freemarkerTemplateProcessor;

    public Project create(Project project, CreationRequest request) {
        ProjectDirectory mainDir = getMainProjectDirectory(project, request);
        create(request, mainDir);
        return project;
    }

    private void create(CreationRequest request, ProjectDirectory parent) {

        ProjectDirectory controllerDir = addDirectory("controller", Optional.of(parent));

        String packageName = packageNameResolver.resolveController(request);

        for (RequestEntity entity : request.getEntities()) {
            addFile(createFrontendControllerWithFreemarker(entity, request, packageName), controllerDir);
        }

    }

    private File createFrontendControllerWithFreemarker(RequestEntity entity, CreationRequest request, String packageName) {
        // Initialize Data Model for Freemarker
        Map<String, Object> dataModel = new HashMap<>();
        dataModel.put("packageName", packageName);
        dataModel.put("imports", getImports(entity, request));
        dataModel.put("requestPath", Utils.plural(entity.getName()));
        dataModel.put("controllerClassName", entity.getTitle() + "Controller");
        dataModel.put("repositoryClassName", entity.getTitle() + "Repository");
        dataModel.put("repositoryVariableName", entity.getName() + "Repository");
        dataModel.put("entityVariableName", entity.getName());
        dataModel.put("entityVariableNamePlural", Utils.plural(entity.getName()));
        dataModel.put("entityClassName", entity.getTitle());
        dataModel.put("entityClassNamePlural", Utils.plural(entity.getTitle()));
        dataModel.put("attributes", entity.getAttributes());
        dataModel.put("relations", entity.getRelations());
        dataModel.put("relationsToOne", entity.getRelations().stream()
                .filter(relation -> !relation.getRelationType().isToMany())
                .collect(Collectors.toList())
        );
        dataModel.put("relationsToMany", entity.getRelations().stream()
                .filter(relation -> relation.getRelationType().isToMany())
                .collect(Collectors.toList())
        );

        // Process the template and return the file
        String filename = entity.getClassName() + "Controller" + JAVA_CLASS_ENDING;
        return freemarkerTemplateProcessor.process("FrontendController.ftl", dataModel, filename);
    }

    private List<String> getImports(RequestEntity entity, CreationRequest request) {
        List<String> imports = new ArrayList<>();

        // Basic imports
        imports.add("org.springframework.stereotype.Controller");
        imports.add("org.springframework.ui.Model");
        imports.add("org.springframework.web.bind.annotation.*");
        imports.add("org.springframework.web.servlet.view.RedirectView");
        imports.add("javax.transaction.Transactional");

        // Application specific imports for needed entities and transfer objects
        imports.add(packageNameResolver.resolveEntity(request) + "." + entity.getClassName());
        imports.add(packageNameResolver.resolveRepository(request) + "." + entity.getClassName() + "Repository");
        imports.add(packageNameResolver.resolveException(request) + ".NotFoundException");
        imports.add(packageNameResolver.resolveTransferObjects(request) + "." + entity.getClassName() + "Request");

        // Imports for Relation Entities
        for (EntityRelation relation : entity.getRelations()) {
            imports.add(packageNameResolver.resolveEntity(request) + "." + relation.getEntityClassName());
            imports.add(packageNameResolver.resolveRepository(request) + "." + relation.getEntityClassName() + "Repository");
        }

        return imports;

    }
}
