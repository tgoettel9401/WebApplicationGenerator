package org.dhbw.webapplicationgenerator.generator.frontend;

import lombok.AllArgsConstructor;
import org.dhbw.webapplicationgenerator.generator.Project;
import org.dhbw.webapplicationgenerator.generator.base_project.FileFolderGenerator;
import org.dhbw.webapplicationgenerator.generator.base_project.PackageNameResolver;
import org.dhbw.webapplicationgenerator.generator.model.ProjectDirectory;
import org.dhbw.webapplicationgenerator.util.ResourceFileHelper;
import org.dhbw.webapplicationgenerator.webclient.request.ProjectRequest;
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

@Service
@AllArgsConstructor
public class FrontendControllerGenerator extends FileFolderGenerator {

    private static final String TMP_PATH = ".tmp/";
    private static final String JAVA_CLASS_ENDING = ".java";

    private final ResourceFileHelper resourceFileHelper;
    private final PackageNameResolver packageNameResolver;

    public Project create(Project project, ProjectRequest request) {

        ProjectDirectory mainDir = getMainProjectDirectory(project, request);

        try {
            create(request, mainDir);
        } catch (IOException ex) {
            ex.printStackTrace();
        }
        return project;
    }

    private void create(ProjectRequest request, ProjectDirectory parent) throws IOException {

        ProjectDirectory controllerDir = addDirectory("controller", Optional.of(parent));

        String packageName = packageNameResolver.resolveController(request);

        for (RequestEntity entity : request.getEntities()) {
            addFile(createFrontendController(entity, request, packageName), controllerDir);
        }

    }

    private File createFrontendController(RequestEntity entity, ProjectRequest request, String packageName) throws IOException {
        String controllerName = entity.getTitle() + "Controller";
        String repositoryName = entity.getTitle() + "Repository";
        File file = new File(String.valueOf(Files.createFile(Path.of(TMP_PATH + controllerName + JAVA_CLASS_ENDING))));
        FileWriter fileWriter = new FileWriter(file);
        try (PrintWriter printWriter = new PrintWriter(fileWriter)) {
            printWriter.println("package " + packageName + ";");
            printWriter.println();

            printWriter.println("import " + packageNameResolver.resolveEntity(request) + "." + entity.getTitle() + ";");
            printWriter.println("import " + packageNameResolver.resolveRepository(request) + "." + repositoryName + ";");
            printWriter.println("import org.springframework.stereotype.Controller;");
            printWriter.println("import org.springframework.ui.Model;");
            printWriter.println("import org.springframework.web.bind.annotation.*;");
            printWriter.println("import org.springframework.web.servlet.view.RedirectView;");

            // Annotations
            printWriter.println("@Controller");
            printWriter.println("@RequestMapping(\"/" + plural(entity.getTitle().toLowerCase(Locale.ROOT)) + "\")");

            // Class Header & Attributes
            printWriter.println("public class " + controllerName + " {");
            printWriter.println("");
            printWriter.println("private final " + entity.getTitle() + "Repository " +
                    entity.getTitle().toLowerCase(Locale.ROOT) + "Repository;");
            printWriter.println("");

            String repositoryVariableName = entity.getTitle().toLowerCase(Locale.ROOT) + "Repository";
            printWriter.println("public " + controllerName + "(" + repositoryName + repositoryVariableName + ") {");
            printWriter.println("this." + repositoryVariableName + " = " + repositoryVariableName + ";");
            printWriter.println("}");

            printWriter.println("@GetMapping()");
            printWriter.println("public String index(Model model) {");
            printWriter.println("model.addAttribute(\"" + plural(entity.getTitle().toLowerCase(Locale.ROOT)) +
                    "\", " + repositoryVariableName + ".findAll());");
            printWriter.println("return \"" + plural(entity.getTitle().toLowerCase(Locale.ROOT)) + "\";");
            printWriter.println("}");
            printWriter.println("");

            printWriter.println("@GetMapping(\"/create\")");
            printWriter.println("public String create(Model model) {");
            printWriter.println(entity.getTitle() + " " + entity.getTitle().toLowerCase(Locale.ROOT) + " = new " +
                    entity.getTitle() + "();");
            printWriter.println("model.addAttribute(\"" + entity.getTitle().toLowerCase(Locale.ROOT) + "\", " +
                    entity.getTitle().toLowerCase(Locale.ROOT) + ");");
            printWriter.println("return \"" + entity.getTitle().toLowerCase(Locale.ROOT) + "Details\";");
            printWriter.println("}");
            printWriter.println("");

            printWriter.println("}");

        }

        return file;
    }

}
