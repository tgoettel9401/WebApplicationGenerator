package org.dhbw.webapplicationgenerator.generator.entity;

import freemarker.template.Template;
import freemarker.template.TemplateException;
import lombok.AllArgsConstructor;
import org.dhbw.webapplicationgenerator.generator.Project;
import org.dhbw.webapplicationgenerator.generator.base_project.FileFolderGenerator;
import org.dhbw.webapplicationgenerator.generator.model.ProjectDirectory;
import org.dhbw.webapplicationgenerator.generator.util.PackageNameResolver;
import org.dhbw.webapplicationgenerator.generator.util.Utils;
import org.dhbw.webapplicationgenerator.webclient.exception.WagException;
import org.dhbw.webapplicationgenerator.webclient.request.CreationRequest;
import org.dhbw.webapplicationgenerator.webclient.request.EntityAttribute;
import org.dhbw.webapplicationgenerator.webclient.request.EntityRelation;
import org.dhbw.webapplicationgenerator.webclient.request.RequestEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.view.freemarker.FreeMarkerConfigurer;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.*;

@Service
@AllArgsConstructor
public class EntityGenerator extends FileFolderGenerator {

    private static final String TMP_PATH = ".tmp/";
    private static final String JAVA_CLASS_ENDING = ".java";

    private final PackageNameResolver packageNameResolver;
    private final FreeMarkerConfigurer freemarkerConfigurer;

    public Project create(Project project, CreationRequest request) {

        ProjectDirectory artifactDir = getMainProjectDirectory(project, request);
        try {
            create(request, artifactDir);
        } catch (IOException ex) {
            ex.printStackTrace();
        }
        return project;
    }

    private void create(CreationRequest request, ProjectDirectory parent) throws IOException {

        // Create the domain directory with entities first.
        ProjectDirectory domainDir = addDirectory("domain", Optional.of(parent));
        String entityPackageName = packageNameResolver.resolveEntity(request);

        // Second we also create the transferObjects directory with transferObjects.
        ProjectDirectory dtoDir = addDirectory("transferObject", Optional.of(parent));
        String transferObjectPackageName = packageNameResolver.resolveTransferObjects(request);

        // Finally we add entities and transferobjects to each of the directories.
        for (RequestEntity entity : request.getEntities()) {
            addFile(createEntityFreemarker(entity, entityPackageName), domainDir);
            addFile(createTransferObject(entity, transferObjectPackageName), dtoDir);
        }

    }

    private File createEntityFreemarker(RequestEntity entity, String packageName) {

        try {
            // Load template and process with Data Model to create the file.
            Template template = freemarkerConfigurer.getConfiguration().getTemplate("Entity.ftl");
            File file = new File(String.valueOf(Files.createFile(Path.of(TMP_PATH + capitalize(entity.getName()) + JAVA_CLASS_ENDING))));
            FileWriter teacherFileWriter = new FileWriter(file);

            // Initialize Data Model for Freemarker
            Map<String, Object> dataModel = new HashMap<>();
            dataModel.put("tableName", entity.getTableName() != null ? entity.getTableName() : plural(entity.getName().toLowerCase()));
            dataModel.put("className", Utils.capitalize(entity.getName()));
            dataModel.put("classNamePlural", Utils.capitalize(plural(entity.getName())));
            dataModel.put("packageName", packageName);
            dataModel.put("imports", getImports(entity));
            dataModel.put("entityName", entity.getName());
            dataModel.put("attributes", entity.getAttributes());
            dataModel.put("relations", entity.getRelations());

            // Process template and create file.
            template.process(dataModel, teacherFileWriter);
            return file;
        } catch (IOException | TemplateException ex) {
            ex.printStackTrace();
            throw new WagException("Processing template failed with exception " + ex.getCause());
        }

    }

    // TODO: Replace by Freemarker template
    private File createTransferObject(RequestEntity entity, String packageName) throws IOException {
        File file = new File(String.valueOf(Files.createFile(Path.of(TMP_PATH + capitalize(entity.getName()) + "Request" + JAVA_CLASS_ENDING))));
        FileWriter fileWriter = new FileWriter(file);
        try (PrintWriter printWriter = new PrintWriter(fileWriter)) {
            printWriter.println("package " + packageName + ";");
            printWriter.println();

            printWriter.println("import java.io.Serializable;");
            if (entity.getRelations().stream().anyMatch(relation -> relation.getRelationType().isToMany())) {
                printWriter.println("import java.util.List;");
                printWriter.println("import java.util.ArrayList;");
            }
            entity.getAttributes().stream()
                    .map(EntityAttribute::getDataType)
                    .map(DataType::fromName)
                    .map(DataType::getPackageToImport)
                    .flatMap(List::stream)
                    .filter(packageToImport -> !packageToImport.isEmpty())
                    .distinct()
                    .forEach(packageToImport -> printWriter.println("import " + packageToImport + ";"));
            printWriter.println();

            printWriter.println("public class " + entity.getTitle() + "Request" + " implements Serializable {");
            printWriter.println();

            printWriter.println("private Long id;");
            for (EntityAttribute attribute : entity.getAttributes()) {
                // Add DateTimeFormat if the attribute is a LocalDate
                if (attribute.getDataType().equals("LocalDate")) {
                    printWriter.println("@DateTimeFormat(pattern = \"yyyy-MM-dd\")");
                }
                printWriter.println("private " + attribute.getDataType() + " " + attribute.getName() + ";");
            }
            printWriter.println("");

            for (EntityRelation relation : entity.getRelations()) {
                if (relation.getRelationType().isToMany()) {
                    printWriter.println("private List<Long> " + relation.getEntityName() + "Ids = new ArrayList<>();");
                } else {
                    printWriter.println("private Long " + relation.getEntityName() + "Id;");
                }

            }
            printWriter.println("");

            // Add Getter and Setter for Id-Attribute
            printWriter.println("public Long getId() {");
            printWriter.println("return this.id;");
            printWriter.println("}");
            printWriter.println("");
            printWriter.println("public void setId(Long id) {");
            printWriter.println("this.id = id;");
            printWriter.println("}");
            printWriter.println("");

            // Getter and Setter for normal attributes
            for (EntityAttribute attribute : entity.getAttributes()) {
                addGetter(attribute, printWriter);
                addSetter(attribute, printWriter);
            }

            for (EntityRelation relation : entity.getRelations()) {
                addRelationIdGetter(relation, printWriter);
                addRelationIdSetter(relation, printWriter);
            }

            printWriter.println("}");
        }
        return file;
    }

    private List<String> getImports(RequestEntity entity) {
        List<String> imports = new ArrayList<>();
        imports.add("javax.persistence.*");
        imports.add("java.io.Serializable");
        entity.getAttributes().stream()
                .map(EntityAttribute::getDataType)
                .map(DataType::fromName)
                .map(DataType::getPackageToImport)
                .flatMap(List::stream)
                .filter(packageToImport -> !packageToImport.isEmpty())
                .distinct()
                .forEach(imports::add);

        // If there is any relation with _TO_MANY, then we have to import a List.
        if (entity.getRelations().stream().map(EntityRelation::getRelationType).anyMatch(type -> type.equals(RelationType.MANY_TO_MANY) || type.equals(RelationType.ONE_TO_MANY))) {
            imports.add("java.util.List");
            imports.add("java.util.ArrayList");
        }
        return imports;
    }

    private void addGetter(EntityAttribute attribute, PrintWriter writer) {
        writer.println("public " + attribute.getDataType() + " get" + capitalize(attribute.getName()) + "() {");
        writer.println("    return " + attribute.getName().toLowerCase(Locale.ROOT) + ";");
        writer.println("}");
        writer.println("");
    }

    private void addSetter(EntityAttribute attribute, PrintWriter writer) {
        writer.println("public void set" + capitalize(attribute.getName()) + "(" +
                attribute.getDataType() + " " + attribute.getName() + ") {");
        writer.println("this." + attribute.getName().toLowerCase(Locale.ROOT) + " = " + attribute.getName().toLowerCase(Locale.ROOT) + ";");
        writer.println("}");
        writer.println("");
    }

    private void addRelationIdGetter(EntityRelation relation, PrintWriter writer) {
        if (relation.getRelationType().isToMany()) {
            writer.println("public List<Long> get" + capitalize(relation.getEntityName()) + "Ids() {");
            writer.println("    return " + relation.getEntityName() + "Ids;");
            writer.println("}");
            writer.println("");
        } else {
            writer.println("public Long get" + capitalize(relation.getEntityName()) + "Id() {");
            writer.println("    return " + relation.getEntityName() + "Id;");
            writer.println("}");
            writer.println("");
        }

    }

    private void addRelationIdSetter(EntityRelation relation, PrintWriter writer) {
        if (relation.getRelationType().isToMany()) {
            writer.println("public void set" + capitalize(relation.getEntityName()) + "Ids(List<Long> " + relation.getEntityName() + "Ids) {");
            writer.println("this." + relation.getEntityName() + "Ids = " + relation.getEntityName() + "Ids;");
            writer.println("}");
            writer.println("");
        } else {
            writer.println("public void set" + capitalize(relation.getEntityName()) + "Id(Long " + relation.getEntityName() + "Id) {");
            writer.println("this." + relation.getEntityName() + "Id = " + relation.getEntityName() + "Id;");
            writer.println("}");
            writer.println("");
        }

    }

}
