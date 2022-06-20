package org.dhbw.webapplicationgenerator.generator.base_project;

import org.dhbw.webapplicationgenerator.generator.model.ProjectDirectory;
import org.dhbw.webapplicationgenerator.generator.model.ProjectFile;
import org.dhbw.webapplicationgenerator.webclient.request.CreationRequest;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.file.Files;
import java.nio.file.Path;

@Service
public class MainFileGenerator extends FileFolderGenerator {

    private static final String TMP_PATH = ".tmp/";
    private static final String JAVA_CLASS_ENDING = ".java";

    public ProjectFile create(CreationRequest request, ProjectDirectory parent) throws IOException {
        String className = getClassName(request);
        createTmpFolderIfNotExists();
        File file = new File(String.valueOf(Files.createFile(Path.of(TMP_PATH + className + JAVA_CLASS_ENDING))));
        FileWriter fileWriter = new FileWriter(file);
        try (PrintWriter printWriter = new PrintWriter(fileWriter)){
            printWriter.println("package " + request.getProject().getGroup() + "." + request.getProject().getArtifact() + ";");
            printWriter.println();
            printWriter.println("import org.springframework.boot.SpringApplication;");
            printWriter.println("import org.springframework.boot.autoconfigure.SpringBootApplication;");
            printWriter.println();
            printWriter.println("@SpringBootApplication");
            printWriter.println("public class " + className + " {");
            printWriter.println();
            printWriter.println("   public static void main(String[] args) {");
            printWriter.println("       SpringApplication.run(" + className + ".class, args);");
            printWriter.println("   }");
            printWriter.println();
            printWriter.println("}");
        }
        return addFile(className + JAVA_CLASS_ENDING, file, parent);
    }

    public ProjectFile createTestFile(CreationRequest request, ProjectDirectory parent) throws IOException {
        String className = getClassName(request) + "Tests";
        createTmpFolderIfNotExists();
        File file = new File(String.valueOf(Files.createFile(Path.of(TMP_PATH + className + JAVA_CLASS_ENDING))));
        FileWriter fileWriter = new FileWriter(file);
        try (PrintWriter printWriter = new PrintWriter(fileWriter)){
            printWriter.println("package " + request.getProject().getGroup() + "." + request.getProject().getArtifact() + ";");
            printWriter.println();
            printWriter.println("import org.junit.jupiter.api.Test;");
            printWriter.println("import org.springframework.boot.test.context.SpringBootTest;");
            printWriter.println();
            printWriter.println("@SpringBootTest");
            printWriter.println("class " + className + " {");
            printWriter.println();
            printWriter.println("   @Test");
            printWriter.println("   void contextLoads() {");
            printWriter.println("   }");
            printWriter.println();
            printWriter.println("}");
        }
        return addFile(className + JAVA_CLASS_ENDING, file, parent);
    }

    private String getClassName(CreationRequest request) {
        String className = request.getProject().getTitleWithoutSpaces() + "Application";
        className = className.replace("-", "");
        return className;
    }

}
