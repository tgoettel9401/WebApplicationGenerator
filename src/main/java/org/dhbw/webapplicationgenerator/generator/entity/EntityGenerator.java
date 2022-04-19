package org.dhbw.webapplicationgenerator.generator.entity;

import org.dhbw.webapplicationgenerator.generator.Project;
import org.dhbw.webapplicationgenerator.generator.base_project.FileFolderGenerator;
import org.dhbw.webapplicationgenerator.generator.model.ProjectDirectory;
import org.dhbw.webapplicationgenerator.webclient.request.EntityAttribute;
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
public class EntityGenerator extends FileFolderGenerator {

    private static final String TMP_PATH = ".tmp/";
    private static final String JAVA_CLASS_ENDING = ".java";

    public Project create(Project project, ProjectRequest request) {
        ProjectDirectory rootDir = (ProjectDirectory) project.getFileStructure();
        ProjectDirectory srcDir = (ProjectDirectory) rootDir.getChildren().stream().filter(child -> child.getTitle().equals("src"))
                .findFirst().orElseThrow(() -> new RuntimeException("Creating entity failed due to missing src folder"));
        ProjectDirectory mainDir = (ProjectDirectory) srcDir.getChildren().stream().filter(child -> child.getTitle().equals("main"))
                .findFirst().orElseThrow(() -> new RuntimeException("Creating entity failed due to missing main folder"));
        ProjectDirectory groupDir = (ProjectDirectory) mainDir.getChildren().stream().filter(child1 -> child1.getTitle().equals("java"))
                .findFirst().orElseThrow(() -> new RuntimeException("Creating entity failed due to missing java folder"));
        for (String groupPart : request.getGroup().split("\\.")) {
            groupDir = (ProjectDirectory) groupDir.getChildren().stream().filter(child -> child.getTitle().equals(groupPart)).findFirst()
                    .orElseThrow(() -> new RuntimeException("Creating entity failed due to missing group folder"));
        }
        ProjectDirectory artifactDir = (ProjectDirectory) groupDir.getChildren().stream().filter(child -> child.getTitle().equals(request.getArtifact()))
                .findFirst().orElseThrow(() -> new RuntimeException("Creating entity failed due to missing artifact folder"));
        try {
            create(request, artifactDir);
        } catch (IOException ex) {
            ex.printStackTrace();
        }
        return project;
    }

    private void create(ProjectRequest request, ProjectDirectory parent) throws IOException {

        ProjectDirectory domainDir = addDirectory("domain", Optional.of(parent));

        String packageName = request.getGroup() + "." + request.getArtifact() + ".domain";

        for (RequestEntity entity : request.getEntities()) {
            addFile(createEntity(entity, packageName), domainDir);
        }

    }

    private File createEntity(RequestEntity entity, String packageName) throws IOException {
        File file = new File(String.valueOf(Files.createFile(Path.of(TMP_PATH + entity.getTitle() + JAVA_CLASS_ENDING))));
        FileWriter fileWriter = new FileWriter(file);
        try (PrintWriter printWriter = new PrintWriter(fileWriter)) {
            printWriter.println("package " + packageName + ";");
            printWriter.println();
            // TODO: Add imports
            // TODO: Add logic for table names in plural.
            printWriter.println("@Table(name = \"" + entity.getTitle().toLowerCase() + "\")");
            printWriter.println("@Entity");
            printWriter.println("public class " + entity.getTitle() + " implements Serializable {");
            printWriter.println();

            addIdAttribute(printWriter);

            for (EntityAttribute attribute : entity.getAttributes()) {
                addCustomAttribute(attribute, printWriter);
            }

            for (EntityAttribute attribute : entity.getAttributes()) {
                addGetter(attribute, printWriter);
                addSetter(attribute, printWriter);
            }

            printWriter.println("}");
        }
        return file;
    }

    private void addIdAttribute(PrintWriter writer) {
        writer.println("@Id");
        writer.println("@GeneratedValue");
        writer.println("@Column(name = \"id\", nullable = false)");
        writer.println("private Long id");
    }

    private void addCustomAttribute(EntityAttribute attribute, PrintWriter writer) {
        // TODO: Add logic for Dependencies as well. While doing so check that dependencies are existing in request as well.
        writer.println("@Column(name = \"" + attribute.getTitle().toLowerCase(Locale.ROOT) + "\")");
        writer.println("private " + attribute.getDataType() + " " + attribute.getTitle().toLowerCase(Locale.ROOT));
    }

    private void addGetter(EntityAttribute attribute, PrintWriter writer) {
        writer.println("private " + attribute.getDataType() + " get" + attribute.getTitle() + "() {");
        writer.println("    return " + attribute.getTitle().toLowerCase(Locale.ROOT));
        writer.println("}");
    }

    private void addSetter(EntityAttribute attribute, PrintWriter writer) {
        writer.println("private " + attribute.getDataType() + " set" + attribute.getTitle().toLowerCase(Locale.ROOT) + "(" +
                attribute.getDataType() + " " + attribute.getTitle() + ") {");
        writer.println("this." + attribute.getTitle().toLowerCase(Locale.ROOT) + " = " + attribute.getTitle().toLowerCase(Locale.ROOT));
        writer.println("}");
    }

}
