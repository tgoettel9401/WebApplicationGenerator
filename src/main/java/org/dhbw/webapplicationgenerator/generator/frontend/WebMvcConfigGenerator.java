package org.dhbw.webapplicationgenerator.generator.frontend;

import org.dhbw.webapplicationgenerator.generator.Project;
import org.dhbw.webapplicationgenerator.generator.base_project.FileFolderGenerator;
import org.dhbw.webapplicationgenerator.generator.model.ProjectDirectory;
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
import java.util.Set;

@Service
public class WebMvcConfigGenerator extends FileFolderGenerator {

    private static final String TMP_PATH = ".tmp/";
    private static final String JAVA_CLASS_ENDING = ".java";

    public Project create(Project project, ProjectRequest request) {

        ProjectDirectory artifactDir = getMainProjectDirectory(project, request);
        try {
            create(request, artifactDir);
        } catch (IOException ex) {
            ex.printStackTrace();
        }
        return project;
    }

    private void create(ProjectRequest request, ProjectDirectory parent) throws IOException {

        ProjectDirectory configDir = addDirectory("config", Optional.of(parent));

        String packageName = request.getGroup() + "." + request.getArtifact() + ".config";

        addFile(createWebMvcConfigFile(request.getEntities(), packageName), configDir);

    }

    private File createWebMvcConfigFile(Set<RequestEntity> entities, String packageName) throws IOException {
        File file = new File(String.valueOf(Files.createFile(Path.of(TMP_PATH + "MvcConfig" + JAVA_CLASS_ENDING))));
        FileWriter fileWriter = new FileWriter(file);
        try (PrintWriter printWriter = new PrintWriter(fileWriter)) {
            printWriter.println("package " + packageName + ";");
            printWriter.println();
            printWriter.println("import org.springframework.context.annotation.Configuration;");
            printWriter.println("import org.springframework.web.servlet.config.annotation.ViewControllerRegistry;");
            printWriter.println("import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;");
            printWriter.println();
            printWriter.println("@Configuration");
            printWriter.println("public class MvcConfig implements WebMvcConfigurer {");
            printWriter.println("public void addViewControllers(ViewControllerRegistry registry) {");

            for (RequestEntity entity : entities) {
                printWriter.println("registry.addViewController(\"/" +
                        plural(entity.getEntityName().toLowerCase(Locale.ROOT)) +
                        "\").setViewName(\"" +
                        plural(entity.getEntityName().toLowerCase(Locale.ROOT)) +
                        "\");");
            }

            printWriter.println("}");
            printWriter.println("}");
        }
        return file;
    }

}
