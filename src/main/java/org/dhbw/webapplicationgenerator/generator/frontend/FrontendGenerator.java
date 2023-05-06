package org.dhbw.webapplicationgenerator.generator.frontend;

import lombok.AllArgsConstructor;
import org.dhbw.webapplicationgenerator.model.response.Project;
import org.dhbw.webapplicationgenerator.generator.baseproject.FileFolderGenerator;
import org.dhbw.webapplicationgenerator.model.response.ProjectDirectory;
import org.dhbw.webapplicationgenerator.generator.util.FreemarkerTemplateProcessor;
import org.dhbw.webapplicationgenerator.generator.util.Utils;
import org.dhbw.webapplicationgenerator.util.ResourceFileHelper;
import org.dhbw.webapplicationgenerator.webclient.request.CreationRequest;
import org.dhbw.webapplicationgenerator.webclient.request.RequestEntity;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class FrontendGenerator extends FileFolderGenerator {

    private static final String HTML_FILE_ENDING = ".html";

    private final ResourceFileHelper resourceFileHelper;
    private final FreemarkerTemplateProcessor freemarkerTemplateProcessor;

    public Project create(Project project, CreationRequest request) {

        ProjectDirectory resourcesDir = getResourcesDirectory(project);

        try {
            createFrontend(request, resourcesDir);
        } catch (IOException ex) {
            ex.printStackTrace();
        }
        return project;
    }

    private void createFrontend(CreationRequest request, ProjectDirectory parent) throws IOException {

        ProjectDirectory templatesDir = addDirectory("templates", Optional.of(parent));

        addFile(resourceFileHelper.getFile("main.html"), templatesDir);
        addFile(resourceFileHelper.getFile("footer.html"), templatesDir);
        addFile(createNavbarFile(request.getEntities()), templatesDir);

        addFile(createDashboardFile(request), templatesDir);

        for (RequestEntity entity : request.getEntities()) {
            addFile(createOverviewFile(entity), templatesDir);
            addFile(createDetailsFile(entity), templatesDir);
        }

    }

    private File createNavbarFile(Set<RequestEntity> entities) {
        // Initialize Data Model for Freemarker
        Map<String, Object> dataModel = new HashMap<>();
        dataModel.put("entities", entities);

        // Process the template and return the file
        String filename = "navbar" + HTML_FILE_ENDING;
        return freemarkerTemplateProcessor.process("Navbar.ftl", dataModel, filename);
    }

    private File createDashboardFile(CreationRequest request) {
        // Initialize Data Model for Freemarker
        Map<String, Object> dataModel = new HashMap<>();
        dataModel.put("entities", request.getEntities());

        // Process the template and return the file
        String filename = "dashboard" + HTML_FILE_ENDING;
        return freemarkerTemplateProcessor.process("Dashboard.ftl", dataModel, filename);
    }

    private File createOverviewFile(RequestEntity entity) {
        // Initialize Data Model for Freemarker
        Map<String, Object> dataModel = new HashMap<>();
        dataModel.put("entityVariableName", entity.getName());
        dataModel.put("entityVariableNamePlural", Utils.plural(entity.getName()));
        dataModel.put("entityClassName", entity.getTitle());
        dataModel.put("entityClassNamePlural", Utils.plural(entity.getTitle()));
        dataModel.put("attributes", entity.getAttributes());

        // Process the template and return the file
        String filename = Utils.plural(entity.getName()) + HTML_FILE_ENDING;
        return freemarkerTemplateProcessor.process("Overview.ftl", dataModel, filename);
    }

    private File createDetailsFile(RequestEntity entity) {
        // Initialize Data Model for Freemarker
        Map<String, Object> dataModel = new HashMap<>();
        dataModel.put("entityVariableName", entity.getName());
        dataModel.put("entityVariableNamePlural", Utils.plural(entity.getName()));
        dataModel.put("entityClassName", entity.getTitle());
        dataModel.put("entityClassNamePlural", Utils.plural(entity.getTitle()));
        dataModel.put("attributes", entity.getAttributes());
        dataModel.put("relationsToOne", entity.getRelations().stream()
                .filter(relation -> !relation.getRelationType().isToMany()).collect(Collectors.toList()));
        dataModel.put("relationsToMany", entity.getRelations().stream()
                .filter(relation -> relation.getRelationType().isToMany()).collect(Collectors.toList()));

        // Process the template and return the file
        String filename = entity.getName() + "Details" + HTML_FILE_ENDING;
        return freemarkerTemplateProcessor.process("Details.ftl", dataModel, filename);
    }

}
