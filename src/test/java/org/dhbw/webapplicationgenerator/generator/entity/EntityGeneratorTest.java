package org.dhbw.webapplicationgenerator.generator.entity;

import org.dhbw.webapplicationgenerator.generator.Project;
import org.dhbw.webapplicationgenerator.generator.model.ProjectDirectory;
import org.dhbw.webapplicationgenerator.generator.model.ProjectFile;
import org.dhbw.webapplicationgenerator.webclient.request.EntityAttribute;
import org.dhbw.webapplicationgenerator.webclient.request.ProjectRequest;
import org.dhbw.webapplicationgenerator.webclient.request.RequestEntity;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.util.FileSystemUtils;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.HashSet;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
class EntityGeneratorTest {

    @Autowired
    private EntityGenerator entityGenerator;

    @BeforeEach
    void setUp() throws IOException {
        FileSystemUtils.deleteRecursively(Path.of(".tmp"));
        Files.createDirectory(Path.of(".tmp"));
    }

    @AfterEach
    void cleanUp() throws IOException {
        FileSystemUtils.deleteRecursively(Path.of(".tmp"));
    }

    @Test
    void create() {

        ProjectDirectory artifactDir = new ProjectDirectory();
        artifactDir.setTitle("artifact");
        artifactDir.setPath("/src/main/java/group/artifact/");

        ProjectDirectory groupDir = new ProjectDirectory();
        groupDir.setTitle("group");
        groupDir.getChildren().add(artifactDir);
        groupDir.setPath("/src/main/java/group/");

        ProjectDirectory javaDir = new ProjectDirectory();
        javaDir.setTitle("java");
        javaDir.getChildren().add(groupDir);
        javaDir.setPath("/src/main/java/");

        ProjectDirectory mainDir = new ProjectDirectory();
        mainDir.setTitle("main");
        mainDir.getChildren().add(javaDir);
        mainDir.setPath("/src/main/");

        ProjectDirectory srcDir = new ProjectDirectory();
        srcDir.setTitle("src");
        srcDir.getChildren().add(mainDir);
        srcDir.setPath("/src/");

        ProjectDirectory fileStructure = new ProjectDirectory();
        fileStructure.setTitle("title");
        fileStructure.getChildren().add(srcDir);
        fileStructure.setPath("/");

        Project baseProject = new Project();
        baseProject.setFileStructure(fileStructure);

        ProjectRequest request = new ProjectRequest();
        request.setArtifact("artifact");
        request.setGroup("group");
        request.setTitle("title");

        Set<RequestEntity> entities = new HashSet<>();
        RequestEntity entity1 = new RequestEntity();
        entity1.setTitle("FirstEntityClass");

        Set<EntityAttribute> attributesEntity1 = new HashSet<>();
        EntityAttribute entity1Attribute1 = new EntityAttribute();
        entity1Attribute1.setTitle("attribute1");
        entity1Attribute1.setDataType("String");
        attributesEntity1.add(entity1Attribute1);
        EntityAttribute entity1Attribute2 = new EntityAttribute();
        entity1Attribute2.setTitle("attribute2");
        entity1Attribute2.setDataType("Integer");
        attributesEntity1.add(entity1Attribute2);
        entity1.setAttributes(attributesEntity1);
        entities.add(entity1);

        RequestEntity entity2 = new RequestEntity();
        entity2.setTitle("SecondEntityClass");

        Set<EntityAttribute> attributesEntity2 = new HashSet<>();
        EntityAttribute entity2Attribute1 = new EntityAttribute();
        entity2Attribute1.setTitle("attribute1");
        entity2Attribute1.setDataType("String");
        attributesEntity2.add(entity2Attribute1);
        EntityAttribute entity2Attribute2 = new EntityAttribute();
        entity2Attribute2.setTitle("attribute2");
        entity2Attribute2.setDataType("Integer");
        attributesEntity2.add(entity2Attribute2);
        entity2.setAttributes(attributesEntity2);
        entities.add(entity2);

        request.setEntities(entities);

        Project createdProject = entityGenerator.create(baseProject, request);

        ProjectDirectory createdRootDir = (ProjectDirectory) createdProject.getFileStructure();
        ProjectDirectory createdSrcDir = (ProjectDirectory) createdRootDir.getChildren().stream()
                .filter(child -> child.getTitle().equals("src"))
                .findFirst().get();
        ProjectDirectory createdMainDir = (ProjectDirectory) srcDir.getChildren().stream()
                .filter(child -> child.getTitle().equals("main"))
                .findFirst().get();
        ProjectDirectory createdJavaDir = (ProjectDirectory) mainDir.getChildren().stream()
                .filter(child -> child.getTitle().equals("java"))
                .findFirst().get();
        ProjectDirectory createdGroupDir = javaDir;
        for (String groupPart : request.getGroup().split("\\.")) {
            createdGroupDir = (ProjectDirectory) createdGroupDir.getChildren().stream()
                    .filter(child -> child.getTitle().equals(groupPart)).findFirst()
                    .orElseThrow(() -> new RuntimeException("Creating entity failed due to missing src folder"));
        }
        ProjectDirectory createdArtifactDir = (ProjectDirectory) createdGroupDir.getChildren().stream()
                .filter(child -> child.getTitle().equals(request.getArtifact()))
                .findFirst().get();
        ProjectDirectory createdDomainDir = (ProjectDirectory) createdArtifactDir.getChildren().stream()
                .filter(child -> child.getTitle().equals("domain"))
                .findFirst().get();
        ProjectFile createdEntityFile1 = (ProjectFile) createdDomainDir.getChildren().stream()
                .filter(child -> child.getTitle().equals("FirstEntityClass.java"))
                .findFirst().get();
        ProjectFile createdEntityFile2 = (ProjectFile) createdDomainDir.getChildren().stream()
                .filter(child -> child.getTitle().equals("SecondEntityClass.java"))
                .findFirst().get();

        assertThat(createdArtifactDir).isNotNull();
        assertThat(createdArtifactDir.getPath()).isEqualTo("/src/main/java/group/artifact/");
        assertThat(createdDomainDir).isNotNull();
        assertThat(createdDomainDir.getPath()).isEqualTo("/src/main/java/group/artifact/domain/");
        assertThat(createdEntityFile1).isNotNull();
        assertThat(createdEntityFile1.getPath()).isEqualTo("/src/main/java/group/artifact/domain/FirstEntityClass.java");
        assertThat(createdEntityFile2).isNotNull();
        assertThat(createdEntityFile2.getPath()).isEqualTo("/src/main/java/group/artifact/domain/SecondEntityClass.java");

        // TODO: Test file content for entity classes.

    }
}