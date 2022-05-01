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

        ProjectDirectory artifactDir = getMainProjectDirectory(project, request);
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

            addImports(printWriter, entity);
            printWriter.println();

            printWriter.println("@Table(name = \"" + plural(entity.getTitle().toLowerCase()) + "\")");
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

    private void addImports(PrintWriter writer, RequestEntity entity) {
        writer.println("import javax.persistence.*;");
        writer.println("import java.io.Serializable;");

        entity.getAttributes().stream()
                .map(EntityAttribute::getDataType)
                .map(DataType::fromName)
                .distinct()
                .forEach(type -> writer.println("import " + type.getPackageToImport() + ";"));
    }

    private void addIdAttribute(PrintWriter writer) {
        writer.println("@Id");
        writer.println("@GeneratedValue");
        writer.println("@Column(name = \"id\", nullable = false)");
        writer.println("private Long id;");
    }

    private void addCustomAttribute(EntityAttribute attribute, PrintWriter writer) {
        writer.println("@Column(name = \"" + attribute.getTitle().toLowerCase(Locale.ROOT) + "\")");
        writer.println("private " + attribute.getDataType() + " " + attribute.getTitle().toLowerCase(Locale.ROOT) + ";");
    }

    private void addGetter(EntityAttribute attribute, PrintWriter writer) {
        writer.println("private " + attribute.getDataType() + " get" + capitalize(attribute.getTitle()) + "() {");
        writer.println("    return " + attribute.getTitle().toLowerCase(Locale.ROOT) + ";");
        writer.println("}");
    }

    private void addSetter(EntityAttribute attribute, PrintWriter writer) {
        writer.println("private void set" + capitalize(attribute.getTitle()) + "(" +
                attribute.getDataType() + " " + attribute.getTitle() + ") {");
        writer.println("this." + attribute.getTitle().toLowerCase(Locale.ROOT) + " = " + attribute.getTitle().toLowerCase(Locale.ROOT) + ";");
        writer.println("}");
    }

    private String capitalize(String value) {
        value = value.toLowerCase(Locale.ROOT);
        return value.substring(0,1).toUpperCase() + value.substring(1).toLowerCase();
    }

    private String plural(String value) {
        return value + "s";
    }

}
