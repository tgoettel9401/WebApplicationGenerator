package org.dhbw.webapplicationgenerator.generator.base_project;

import lombok.AllArgsConstructor;
import org.dhbw.webapplicationgenerator.generator.Project;
import org.dhbw.webapplicationgenerator.generator.model.ProjectDirectory;
import org.dhbw.webapplicationgenerator.generator.util.PackageNameResolver;
import org.dhbw.webapplicationgenerator.webclient.request.CreationRequest;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Optional;

@Service
@AllArgsConstructor
public class ExceptionGenerator extends FileFolderGenerator{

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

        // Create exceptions package
        ProjectDirectory exceptionDir = addDirectory("exception", Optional.of(parent));
        String packageName = packageNameResolver.resolveException(request);

        // Add exception files
        addFile(createNotFoundException(packageName), exceptionDir);

    }

    private File createNotFoundException(String packageName) throws IOException {

        File file = new File(String.valueOf(Files.createFile(Path.of(TMP_PATH + "NotFoundException" + JAVA_CLASS_ENDING))));
        FileWriter fileWriter = new FileWriter(file);
        try (PrintWriter printWriter = new PrintWriter(fileWriter)) {
            printWriter.println("package " + packageName + ";");
            printWriter.println();

            printWriter.println("public class NotFoundException extends Exception {");
            printWriter.println("public NotFoundException(String title, Long id) {");
            printWriter.println("super(title + \"with ID \" + id + \" has not been found\");");
            printWriter.println("}");
            printWriter.println("}");

        }
        return file;
    }

}
