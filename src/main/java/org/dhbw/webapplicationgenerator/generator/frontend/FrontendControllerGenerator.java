package org.dhbw.webapplicationgenerator.generator.frontend;

import lombok.AllArgsConstructor;
import org.dhbw.webapplicationgenerator.generator.Project;
import org.dhbw.webapplicationgenerator.generator.base_project.FileFolderGenerator;
import org.dhbw.webapplicationgenerator.generator.entity.RelationType;
import org.dhbw.webapplicationgenerator.generator.model.ProjectDirectory;
import org.dhbw.webapplicationgenerator.generator.util.PackageNameResolver;
import org.dhbw.webapplicationgenerator.webclient.request.CreationRequest;
import org.dhbw.webapplicationgenerator.webclient.request.EntityAttribute;
import org.dhbw.webapplicationgenerator.webclient.request.EntityRelation;
import org.dhbw.webapplicationgenerator.webclient.request.RequestEntity;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Locale;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class FrontendControllerGenerator extends FileFolderGenerator {

    private static final String TMP_PATH = ".tmp/";
    private static final String JAVA_CLASS_ENDING = ".java";

    private final PackageNameResolver packageNameResolver;

    public Project create(Project project, CreationRequest request) {

        ProjectDirectory mainDir = getMainProjectDirectory(project, request);

        try {
            create(request, mainDir);
        } catch (IOException ex) {
            ex.printStackTrace();
        }
        return project;
    }

    private void create(CreationRequest request, ProjectDirectory parent) throws IOException {

        ProjectDirectory controllerDir = addDirectory("controller", Optional.of(parent));

        String packageName = packageNameResolver.resolveController(request);

        for (RequestEntity entity : request.getEntities()) {
            addFile(createFrontendController(entity, request, packageName), controllerDir);
        }

    }

    private File createFrontendController(RequestEntity entity, CreationRequest request, String packageName) throws IOException {
        String controllerName = capitalize(entity.getName()) + "Controller";
        String repositoryName = capitalize(entity.getName()) + "Repository";
        File file = new File(String.valueOf(Files.createFile(Path.of(TMP_PATH + controllerName + JAVA_CLASS_ENDING))));
        FileWriter fileWriter = new FileWriter(file);
        try (PrintWriter printWriter = new PrintWriter(fileWriter)) {
            printWriter.println("package " + packageName + ";");
            printWriter.println();

            // Basic Imports
            printWriter.println("import org.springframework.stereotype.Controller;");
            printWriter.println("import org.springframework.ui.Model;");
            printWriter.println("import org.springframework.web.bind.annotation.*;");
            printWriter.println("import org.springframework.web.servlet.view.RedirectView;");

            // Application Imports
            printWriter.println("import " + packageNameResolver.resolveEntity(request) + "." + capitalize(entity.getName()) + ";");
            printWriter.println("import " + packageNameResolver.resolveRepository(request) + "." + repositoryName + ";");
            printWriter.println("import " + packageNameResolver.resolveException(request) + ".NotFoundException;");
            printWriter.println("import " + packageNameResolver.resolveTransferObjects(request) + "." + capitalize(entity.getName()) + "Request;");

            // Also we need the RelationEntities and RelationRepositories if there is a any relation (toOne or toMany).
            for (EntityRelation relation : entity.getRelations()) {
                printWriter.println("import " + packageNameResolver.resolveRepository(request) + "." + capitalize(relation.getEntity()) + "Repository;");
                printWriter.println("import " + packageNameResolver.resolveEntity(request) + "." + capitalize(relation.getEntity()) + ";");
            }

            printWriter.println("");

            // Annotations
            printWriter.println("@Controller");
            printWriter.println("@RequestMapping(\"/" + plural(entity.getTitle().toLowerCase(Locale.ROOT)) + "\")");

            // Class Header & Attributes
            printWriter.println("public class " + controllerName + " {");
            printWriter.println("");

            // Add Repositories. We need the entity's repository as well as all Relation-Repositories (toOne or toMany)
            printWriter.println("private final " + capitalize(entity.getName()) + "Repository " +
                    entity.getName() + "Repository;");
            for (EntityRelation relation : entity.getRelations()) {
                printWriter.println("private final " + capitalize(relation.getEntity()) + "Repository " +
                        relation.getEntity() + "Repository;");
            }
            printWriter.println("");

            // Constructor
            String repositoryVariableName = entity.getTitle().toLowerCase(Locale.ROOT) + "Repository";
            printWriter.print("public " + controllerName + "(" + repositoryName + " " + repositoryVariableName);
            for (EntityRelation relation : entity.getRelations()) {
                printWriter.println(",");
                printWriter.print(capitalize(relation.getEntity()) + "Repository " + relation.getEntity() + "Repository");
            }
            printWriter.println(") {");
            printWriter.println("this." + repositoryVariableName + " = " + repositoryVariableName + ";");
            for (EntityRelation relation : entity.getRelations()) {
                printWriter.println("this." + relation.getEntity() + "Repository" + " = " + relation.getEntity() + "Repository" + ";");
            }
            printWriter.println("}");

            // Show all entities
            printWriter.println("@GetMapping()");
            printWriter.println("public String index(Model model) {");
            printWriter.println("model.addAttribute(\"" + plural(entity.getTitle().toLowerCase(Locale.ROOT)) +
                    "\", " + repositoryVariableName + ".findAll());");
            printWriter.println("model.addAttribute(\"title\", \"" + plural(entity.getTitle()) + " - Index\");");
            printWriter.println("return \"" + plural(entity.getTitle().toLowerCase(Locale.ROOT)) + "\";");
            printWriter.println("}");
            printWriter.println("");

            // Create new entity
            printWriter.println("@GetMapping(\"/create\")");
            printWriter.println("public String create(Model model) {");
            printWriter.println(entity.getTitle() + " " + entity.getTitle().toLowerCase(Locale.ROOT) + " = new " +
                    entity.getTitle() + "();");
            printWriter.println("model.addAttribute(\"" + entity.getTitle().toLowerCase(Locale.ROOT) + "\", " +
                    entity.getTitle().toLowerCase(Locale.ROOT) + ");");

            // For every relation (toOne and toMany), we have to add the whole relation data as well.
            for (EntityRelation relation : entity.getRelations()) {
                printWriter.println("model.addAttribute(\"" + plural(relation.getEntity()) + "\", " + relation.getEntity() + "Repository.findAll());");
            }
            printWriter.println("model.addAttribute(\"title\", \"" + plural(entity.getTitle()) + " - Create\");");

            printWriter.println("return \"" + entity.getTitle().toLowerCase(Locale.ROOT) + "Details\";");
            printWriter.println("}");
            printWriter.println("");

            // Update an existing entity
            printWriter.println("@GetMapping(\"/edit/{id}\")");
            printWriter.println("public String update(Model model, @PathVariable(\"id\") Long " +
                    entity.getTitle().toLowerCase(Locale.ROOT) + "Id)  throws NotFoundException {");
            printWriter.println(entity.getTitle() + " " + entity.getTitle().toLowerCase(Locale.ROOT) + " = " +
                    repositoryVariableName + ".findById(" + entity.getTitle().toLowerCase(Locale.ROOT) +
                    "Id).orElseThrow(() -> new NotFoundException(\"" + capitalize(entity.getTitle().toLowerCase(Locale.ROOT)) +
                    "\", " + entity.getTitle().toLowerCase(Locale.ROOT) + "Id));");
            printWriter.println("model.addAttribute(\"" + entity.getTitle().toLowerCase(Locale.ROOT) + "\", " +
                    entity.getTitle().toLowerCase(Locale.ROOT) + ");");

            // For every relation (toOne and toMany), we have to add the whole relation data as well.
            for (EntityRelation relation : entity.getRelations()) {
                printWriter.println("model.addAttribute(\"" + plural(relation.getEntity()) + "\", " + relation.getEntity() + "Repository.findAll());");
            }
            printWriter.println("model.addAttribute(\"title\", \"" + plural(entity.getTitle()) + " - Update\");");
            printWriter.println("return \"" + entity.getTitle().toLowerCase(Locale.ROOT) + "Details\";");
            printWriter.println("}");
            printWriter.println("");

            // Saving an entity
            printWriter.println("@Transactional");
            printWriter.println("@PostMapping(\"save\")");
            printWriter.println("public RedirectView save(@ModelAttribute " + entity.getTitle() + "Request " +
                    entity.getName() + "Request) throws NotFoundException {");
            printWriter.println("boolean isUpdate = " + entity.getName() + "Request.getId() != null;");
            printWriter.println(capitalize(entity.getName()) + " " + entity.getName() + " = new " + capitalize(entity.getName()) + "();");
            printWriter.println(entity.getName() + ".setId(" + entity.getName() + "Request.getId());");
            for (EntityAttribute attribute : entity.getAttributes()) {
                printWriter.println(entity.getName() + ".set" + capitalize(attribute.getName()) + "(" + entity.getName() + "Request.get" + capitalize(attribute.getName()) + "());");
            }

            // For all toOne relations we potentially add a single relation object to the entity.
            for (EntityRelation relation : entity.getRelations().stream().filter(relation -> !relation.getRelationType().isToMany()).collect(Collectors.toList())) {

                // First we deal with the case that the relation has been set.
                printWriter.println("if(" + entity.getName() + "Request.get" + capitalize(relation.getEntity()) + "Id() != null) {");

                if (relation.isOwning()) {
                    // If we are on the owing side, we only have to add the relation and can save that later on.
                    printWriter.println(entity.getName() + ".set" + capitalize(relation.getEntity()) + "(" + relation.getEntity() + "Repository.findById(" + entity.getName() + "Request.get" + capitalize(relation.getEntity()) + "Id()).orElse(null));");
                } else {
                    // If we are on the non-owning side, we have to get the owning entity and update it respectively.
                    printWriter.println(capitalize(relation.getEntity()) + " " + relation.getEntity() + " = " + relation.getEntity() + "Repository.findById(" + entity.getName() + "Request.get" + capitalize(relation.getEntity()) + "Id()).orElseThrow(() -> new NotFoundException(\"" + capitalize(relation.getEntity()) + "\", " + entity.getName() + "Request.get" + capitalize(relation.getName()) + "Id()));");
                    printWriter.println(relation.getEntity() + ".set" + capitalize(entity.getName()) + "(" + entity.getName() + ");");
                    printWriter.println(relation.getEntity() + "Repository.save(" + relation.getEntity() + ");");
                    printWriter.println(entity.getName() + ".set" + capitalize(relation.getEntity()) + "(" + relation.getEntity() + ");");
                }
                printWriter.println("}");

                // Next we deal with the case that we have an update process and the relation's id is set to null.
                if (relation.getRelationType().equals(RelationType.ONE_TO_ONE)) {
                    printWriter.println("else if(isUpdate) {");
                    printWriter.println(capitalize(entity.getName()) + " previous" + capitalize(entity.getName()) + " = " + entity.getName() + "Repository.findById(" + entity.getName() + "Request.getId()).orElseThrow(() -> new NotFoundException(\"" + capitalize(relation.getEntity()) + "\", " + entity.getName() + "Request.get" + capitalize(relation.getName()) + "Id()));");
                    printWriter.println(capitalize(relation.getEntity()) + " previous" + capitalize(relation.getEntity()) + " = previous" + capitalize(entity.getName()) + ".get" + capitalize(relation.getEntity()) + "();");
                    printWriter.println("if (previous" + capitalize(relation.getEntity()) + " != null) {");
                    printWriter.println("previous" + capitalize(relation.getEntity()) + ".set" + capitalize(entity.getName()) + "(null);");
                    printWriter.println(relation.getEntity() + "Repository.save(previous" + capitalize(relation.getEntity()) + ");");
                    printWriter.println("}");
                    printWriter.println("}");
                }

            }

            // For all toMany relations we potentially add multiple relation objects to the entity. However, because a List may contain the same element multiple times, we have to clear the list first.
            for (EntityRelation relation : entity.getRelations().stream().filter(relation -> relation.getRelationType().isToMany()).collect(Collectors.toList())) {
                printWriter.println(entity.getName() + ".get" + capitalize(plural(relation.getEntity())) + "().clear();");
                printWriter.println("for (Long " + relation.getEntity() + "Id : " + entity.getName() + "Request.get" + capitalize(relation.getEntity()) + "Ids()) {");
                printWriter.println(entity.getName() + ".get" + capitalize(plural(relation.getEntity())) + "().add(" + relation.getEntity() + "Repository.findById(" + relation.getEntity() + "Id).orElse(null));");
                printWriter.println("}");
            }

            printWriter.println(repositoryVariableName + ".save(" + entity.getName() + ");");
            printWriter.println("return new RedirectView(\"/" + plural(entity.getName())  + "\");");
            printWriter.println("}");
            printWriter.println("");

            // Deleting an entity
            printWriter.println("@GetMapping(\"delete/{id}\")");
            printWriter.println("public RedirectView delete(@PathVariable(\"id\") Long " +
                    entity.getTitle().toLowerCase(Locale.ROOT) + "Id) {");
            printWriter.println(repositoryVariableName + ".deleteById(" +entity.getTitle().toLowerCase(Locale.ROOT) + "Id);");
            printWriter.println("return new RedirectView(\"/" + plural(entity.getTitle().toLowerCase(Locale.ROOT))  + "\");");
            printWriter.println("}");

            // Close the final curly bracket
            printWriter.println("}");

        }

        return file;
    }

}
