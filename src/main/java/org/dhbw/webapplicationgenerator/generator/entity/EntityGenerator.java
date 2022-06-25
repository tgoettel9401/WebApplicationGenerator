package org.dhbw.webapplicationgenerator.generator.entity;

import lombok.AllArgsConstructor;
import org.dhbw.webapplicationgenerator.generator.Project;
import org.dhbw.webapplicationgenerator.generator.base_project.FileFolderGenerator;
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
import java.util.List;
import java.util.Locale;
import java.util.Optional;

@Service
@AllArgsConstructor
public class EntityGenerator extends FileFolderGenerator {

    private static final String TMP_PATH = ".tmp/";
    private static final String JAVA_CLASS_ENDING = ".java";

    private final PackageNameResolver packageNameResolver;

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
            addFile(createEntity(entity, entityPackageName), domainDir);
            addFile(createTransferObject(entity, transferObjectPackageName), dtoDir);
        }

    }

    private File createEntity(RequestEntity entity, String packageName) throws IOException {
        File file = new File(String.valueOf(Files.createFile(Path.of(TMP_PATH + capitalize(entity.getName()) + JAVA_CLASS_ENDING))));
        FileWriter fileWriter = new FileWriter(file);
        try (PrintWriter printWriter = new PrintWriter(fileWriter)) {
            printWriter.println("package " + packageName + ";");
            printWriter.println();

            addImports(printWriter, entity);
            printWriter.println();

            String tableName = entity.getTableName() != null ? entity.getTableName() : plural(entity.getName().toLowerCase());
            printWriter.println("@Table(name = \"" + tableName + "\")");
            printWriter.println("@Entity");
            printWriter.println("public class " + entity.getTitle() + " implements Serializable {");
            printWriter.println();

            addIdAttribute(printWriter);

            for (EntityRelation relation : entity.getRelations()) {
                addRelationAttribute(entity, relation, printWriter);
            }


            for (EntityAttribute attribute : entity.getAttributes()) {
                addCustomAttribute(attribute, printWriter);
            }

            // Getter and Setter for ID
            EntityAttribute idAttribute = new EntityAttribute();
            idAttribute.setName("id");
            idAttribute.setDataType("Long");
            addGetter(idAttribute, printWriter);
            addSetter(idAttribute, printWriter);

            for (EntityRelation relation : entity.getRelations()) {
                addRelationGetter(relation, printWriter);
                addRelationSetter(relation, printWriter);
            }

            // Getter and Setter for other attributes
            for (EntityAttribute attribute : entity.getAttributes()) {
                addGetter(attribute, printWriter);
                addSetter(attribute, printWriter);
            }

            printWriter.println("}");
        }
        return file;
    }

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
                    printWriter.println("private List<Long> " + relation.getEntity() + "Ids = new ArrayList<>();");
                } else {
                    printWriter.println("private Long " + relation.getEntity() + "Id;");
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

    private void addImports(PrintWriter writer, RequestEntity entity) {
        writer.println("import javax.persistence.*;");
        writer.println("import java.io.Serializable;");
        entity.getAttributes().stream()
                .map(EntityAttribute::getDataType)
                .map(DataType::fromName)
                .map(DataType::getPackageToImport)
                .flatMap(List::stream)
                .filter(packageToImport -> !packageToImport.isEmpty())
                .distinct()
                .forEach(packageToImport -> writer.println("import " + packageToImport + ";"));

        // If there is any relation with _TO_MANY, then we have to import a List.
        if (entity.getRelations().stream().map(EntityRelation::getRelationType).anyMatch(type -> type.equals(RelationType.MANY_TO_MANY) || type.equals(RelationType.ONE_TO_MANY))) {
            writer.println("import java.util.List;");
            writer.println("import java.util.ArrayList;");
        }
    }

    private void addIdAttribute(PrintWriter writer) {
        writer.println("@Id");
        writer.println("@GeneratedValue");
        writer.println("@Column(name = \"id\", nullable = false)");
        writer.println("private Long id;");
        writer.println("");
    }

    private void addCustomAttribute(EntityAttribute attribute, PrintWriter writer) {
        String columnName = attribute.getColumnName() != null ? attribute.getColumnName() : attribute.getName();
        writer.println("@Column(name = \"" + columnName.toLowerCase(Locale.ROOT) + "\")");

        // Add DateTimeFormat if the attribute is a LocalDate
        if (attribute.getDataType().equals("LocalDate")) {
            writer.println("@DateTimeFormat(pattern = \"yyyy-MM-dd\")");
        }

        writer.println("private " + attribute.getDataType() + " " + attribute.getName().toLowerCase(Locale.ROOT) + ";");
        writer.println("");
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

    private void addRelationAttribute(RequestEntity entity, EntityRelation relation, PrintWriter writer) {
        RelationType relationType = relation.getRelationType();

        if (relationType.equals(RelationType.ONE_TO_ONE)) {
            if (relation.isOwning()) {
                writer.println("@OneToOne");
                writer.println("@JoinColumn(");
                writer.println("name = \"" + relation.getEntity() + "_id\",");
                writer.println("referencedColumnName = \"id\")");
            } else {
                writer.println("@OneToOne(mappedBy=\"" + entity.getName() + "\")");
            }
            writer.println("private " + capitalize(relation.getEntity()) + " " + relation.getEntity() + ";");
        }

        if (relationType.equals(RelationType.ONE_TO_MANY)) {
            writer.println("@OneToMany(mappedBy = \"" + entity.getName() + "\")");
            writer.println("private List<" + capitalize(relation.getEntity()) + "> " + plural(relation.getEntity()) + " = new ArrayList<>();");
        }

        if (relationType.equals(RelationType.MANY_TO_ONE)) {
            writer.println("@ManyToOne"); // TODO: Add/Need JoinColumn?
            writer.println("private " + capitalize(relation.getEntity()) + " " + relation.getEntity() + ";");
        }

        if (relationType.equals(RelationType.MANY_TO_MANY)) {
            writer.println("@ManyToMany");
            writer.println("@JoinTable(");
            writer.println("name = \"" + relation.getJoinTable() + "\",");
            writer.println("joinColumns = @JoinColumn(name = \"" + entity.getName() + "_id\"),");
            writer.println("inverseJoinColumns = @JoinColumn(name = \"" + relation.getEntity() + "_id\")");
            writer.println(")");
            writer.println("private List<" + capitalize(relation.getEntity()) + "> " + plural(relation.getEntity()) + " = new ArrayList<>();");
        }

        writer.println("");

    }

    private void addRelationGetter(EntityRelation relation, PrintWriter writer) {

        RelationType relationType = relation.getRelationType();

        if (relationType.equals(RelationType.ONE_TO_ONE) || relationType.equals(RelationType.MANY_TO_ONE)) {
            writer.println("public " + capitalize(relation.getEntity()) + " get" + capitalize(relation.getEntity()) + "() {");
            writer.println("    return " + relation.getEntity() + ";");
        }

        if (relationType.equals(RelationType.ONE_TO_MANY) || relationType.equals(RelationType.MANY_TO_MANY)) {
            writer.println("public List<" + capitalize(relation.getEntity()) + "> get" + capitalize(plural(relation.getEntity())) + "() {");
            writer.println("    return " + plural(relation.getEntity()) + ";");
        }

        writer.println("}");
        writer.println("");
        writer.println("");

    }

    private void addRelationSetter(EntityRelation relation, PrintWriter writer) {

        RelationType relationType = relation.getRelationType();

        if (relationType.equals(RelationType.ONE_TO_ONE) || relationType.equals(RelationType.MANY_TO_ONE)) {
            writer.println("public void set" + capitalize(relation.getEntity()) + "(" + capitalize(relation.getEntity()) + " " + relation.getEntity() + ") {");
            writer.println("this." + relation.getEntity() + " = " + relation.getEntity() + ";");
        }

        if (relationType.equals(RelationType.ONE_TO_MANY) || relationType.equals(RelationType.MANY_TO_MANY)) {
            writer.println("public void set" + capitalize(plural(relation.getEntity())) + "(List<" + capitalize(relation.getEntity()) + "> " + plural(relation.getEntity()) + ") {");
            writer.println("this." + plural(relation.getEntity()) + " = " + plural(relation.getEntity()) + ";");
        }

        writer.println("}");
        writer.println("");

    }

    private void addRelationIdGetter(EntityRelation relation, PrintWriter writer) {
        if (relation.getRelationType().isToMany()) {
            writer.println("public List<Long> get" + capitalize(relation.getEntity()) + "Ids() {");
            writer.println("    return " + relation.getEntity() + "Ids;");
            writer.println("}");
            writer.println("");
        } else {
            writer.println("public Long get" + capitalize(relation.getEntity()) + "Id() {");
            writer.println("    return " + relation.getEntity() + "Id;");
            writer.println("}");
            writer.println("");
        }

    }

    private void addRelationIdSetter(EntityRelation relation, PrintWriter writer) {
        if (relation.getRelationType().isToMany()) {
            writer.println("public void set" + capitalize(relation.getEntity()) + "Ids(List<Long> " + relation.getEntity() + "Ids) {");
            writer.println("this." + relation.getEntity() + "Ids = " + relation.getEntity() + "Ids;");
            writer.println("}");
            writer.println("");
        } else {
            writer.println("public void set" + capitalize(relation.getEntity()) + "Id(Long " + relation.getEntity() + "Id) {");
            writer.println("this." + relation.getEntity() + "Id = " + relation.getEntity() + "Id;");
            writer.println("}");
            writer.println("");
        }

    }

}
