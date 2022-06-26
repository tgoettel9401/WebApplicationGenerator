package org.dhbw.webapplicationgenerator.generator.repository;

import lombok.AllArgsConstructor;
import org.dhbw.webapplicationgenerator.generator.Project;
import org.dhbw.webapplicationgenerator.generator.base_project.FileFolderGenerator;
import org.dhbw.webapplicationgenerator.generator.util.PackageNameResolver;
import org.dhbw.webapplicationgenerator.generator.model.ProjectDirectory;
import org.dhbw.webapplicationgenerator.webclient.request.CreationRequest;
import org.dhbw.webapplicationgenerator.webclient.request.EntityRelation;
import org.dhbw.webapplicationgenerator.webclient.request.RequestEntity;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class RepositoryGenerator extends FileFolderGenerator {

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

        ProjectDirectory domainDir = addDirectory("repository", Optional.of(parent));

        for (RequestEntity entity : request.getEntities()) {
            addFile(createRepository(entity, request), domainDir);
        }

    }

    private File createRepository(RequestEntity entity, CreationRequest request) throws IOException {
        File file = new File(String.valueOf(Files.createFile(Path.of(TMP_PATH + entity.getTitle() + "Repository" + JAVA_CLASS_ENDING))));
        String repositoryPackageName = packageNameResolver.resolveRepository(request);
        String entityPackageName = packageNameResolver.resolveEntity(request);
        FileWriter fileWriter = new FileWriter(file);
        try (PrintWriter printWriter = new PrintWriter(fileWriter)) {
            printWriter.println("package " + repositoryPackageName + ";");
            printWriter.println();
            printWriter.println("import " + entityPackageName + "." + entity.getTitle() + ";");
            printWriter.println("import org.springframework.data.jpa.repository.JpaRepository;");
            printWriter.println("import java.util.List;");
            printWriter.println();
            printWriter.println("public interface " + entity.getTitle() + "Repository extends JpaRepository<" + entity.getTitle() + ", Long> {");
            for (EntityRelation relation : entity.getRelations().stream().filter(rel -> !rel.getRelationType().isToMany()).collect(Collectors.toList())) {
                printWriter.println("List<" + capitalize(entity.getName()) + "> findBy" + capitalize(relation.getEntity()) + "Id(Long " + relation.getEntity() + "Id);");
            }
            printWriter.println("}");
            printWriter.println();
        }
        return file;
    }

}
