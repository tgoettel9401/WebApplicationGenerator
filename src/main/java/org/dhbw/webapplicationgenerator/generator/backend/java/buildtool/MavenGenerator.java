package org.dhbw.webapplicationgenerator.generator.backend.java.buildtool;

import lombok.AllArgsConstructor;
import org.dhbw.webapplicationgenerator.generator.util.FreemarkerTemplateProcessor;
import org.dhbw.webapplicationgenerator.model.request.ProjectRequest;
import org.dhbw.webapplicationgenerator.model.request.backend.SpringBootData;
import org.dhbw.webapplicationgenerator.model.response.Project;
import org.dhbw.webapplicationgenerator.model.response.ProjectDirectory;
import org.dhbw.webapplicationgenerator.util.ResourceFileHelper;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.*;

@Service
@AllArgsConstructor
public class MavenGenerator extends JavaBuildToolGenerator {

    private final ResourceFileHelper resourceFileHelper;
    private final FreemarkerTemplateProcessor freemarkerTemplateProcessor;

    private static final String SPRING_BOOT_FRAMEWORK_GROUP_ID = "org.springframework.boot";

    /**
     * Implementation for adding build-tool-files for Maven-projects. First it adds a folder .mvn with the necessary
     * files and second it adds a pom.xml file.
     * @return Project enhanced by Maven-Files.
     */
    @Override
    public Project addBuildToolFiles(Project project, ProjectRequest request) {
        ProjectDirectory rootDir = (ProjectDirectory) project.getFileStructure();
        ProjectDirectory mvnDir = addDirectory(".mvn", Optional.of(rootDir));
        ProjectDirectory mvnWrapperDir = addDirectory("wrapper", Optional.of(mvnDir));

        try {
            addFile(resourceFileHelper.getFile("mvnw"), rootDir);
            addFile(resourceFileHelper.getFile("mvnw.cmd"), rootDir);
            addFile(createPomXml(request), rootDir);
            addFile(resourceFileHelper.getFile("maven-wrapper.jar"), mvnWrapperDir);
            addFile(resourceFileHelper.getFile("maven-wrapper.properties"), mvnWrapperDir);
        } catch(FileNotFoundException exception) {
            exception.printStackTrace();
        }

        return project;
    }

    private File createPomXml(ProjectRequest request) {
        // Initialize Data Model for Freemarker
        Map<String, Object> dataModel = new HashMap<>();
        SpringBootData data = (SpringBootData) request.getBackend().getData();
        dataModel.put("groupId", data.getGroup());
        dataModel.put("artifactId", data.getArtifact());
        dataModel.put("projectTitle", request.getTitleWithoutSpaces());
        dataModel.put("projectDescription", request.getDescription());
        dataModel.put("springBootVersion", data.getSpringBootVersion());
        dataModel.put("javaVersion", data.getJavaVersion());
        dataModel.put("dependencies", createDependencies(request));

        // Process the template and return the file
        String filename = "pom.xml";
        return freemarkerTemplateProcessor.process("pom.ftl", dataModel, filename);
    }

    private List<Dependency> createDependencies(ProjectRequest request) {
        SpringBootData data = (SpringBootData) request.getBackend().getData();
        List<Dependency> dependencies = new ArrayList<>();
        dependencies.add(new Dependency(SPRING_BOOT_FRAMEWORK_GROUP_ID, "spring-boot-starter-web", "", ""));
        dependencies.add(new Dependency(SPRING_BOOT_FRAMEWORK_GROUP_ID, "spring-boot-starter-data-jpa", "", ""));
        dependencies.add(new Dependency(SPRING_BOOT_FRAMEWORK_GROUP_ID, "spring-boot-starter-data-rest", "", ""));

        // TODO: Only add Thymeleaf it is needed, otherwise e.g. add Vaadin-Dependencies. Hence add a dependency list to the strategy potentially?
        dependencies.add(new Dependency(SPRING_BOOT_FRAMEWORK_GROUP_ID, "spring-boot-starter-thymeleaf", "", ""));

        dependencies.add(new Dependency("org.springdoc", "springdoc-openapi-ui", data.getSpringDocVersion(), ""));
        dependencies.add(new Dependency("org.springdoc", "springdoc-openapi-data-rest", data.getSpringDocVersion(), ""));
        dependencies.add(new Dependency("com.h2database", "h2", "", "runtime"));

        if (request.isSecurityEnabled()) {
            dependencies.add(new Dependency(SPRING_BOOT_FRAMEWORK_GROUP_ID, "spring-boot-starter-security", "", ""));
            dependencies.add(new Dependency("org.thymeleaf.extras", "thymeleaf-extras-springsecurity5", "", ""));
        }
        return dependencies;
    }

}
