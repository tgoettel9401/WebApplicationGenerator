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
public class GradleGenerator extends JavaBuildToolGenerator {

    private final ResourceFileHelper resourceFileHelper;
    private final FreemarkerTemplateProcessor freemarkerTemplateProcessor;

    /**
     * Implementation for adding build-tool-files for Maven-projects. First it adds a folder .mvn with the necessary
     * files and second it adds a pom.xml file.
     * @return Project enhanced by Maven-Files.
     */
    @Override
    public Project addBuildToolFiles(Project project, ProjectRequest request) {
        ProjectDirectory rootDir = (ProjectDirectory) project.getFileStructure();
        ProjectDirectory gradleDir = addDirectory("gradle", Optional.of(rootDir));
        ProjectDirectory gradleWrapperDir = addDirectory("wrapper", Optional.of(gradleDir));

        try {
            addFile(resourceFileHelper.getFile("gradlew"), rootDir);
            addFile(resourceFileHelper.getFile("gradlew.bat"), rootDir);
            addFile(resourceFileHelper.getFile("gradle-wrapper.jar"), gradleWrapperDir);
            addFile(resourceFileHelper.getFile("gradle-wrapper.properties"), gradleWrapperDir);
            addFile(createSettingsGradle(request), rootDir);
            addFile(createBuildGradle(request), rootDir);
        } catch(FileNotFoundException exception) {
            exception.printStackTrace();
        }

        return project;
    }

    private File createSettingsGradle(ProjectRequest request) {
        Map<String, Object> dataModel = new HashMap<>();
        SpringBootData data = (SpringBootData) request.getBackend().getData();
        dataModel.put("artifactId", data.getArtifact());
        String filename = "settings.gradle";
        return freemarkerTemplateProcessor.process("settings.ftl", dataModel, filename);
    }

    private File createBuildGradle(ProjectRequest request) {
        // Initialize Data Model for Freemarker
        Map<String, Object> dataModel = new HashMap<>();
        SpringBootData data = (SpringBootData) request.getBackend().getData();
        dataModel.put("groupId", data.getGroup());
        dataModel.put("artifactId", data.getArtifact());
        dataModel.put("projectTitle", request.getTitleWithoutSpaces());
        dataModel.put("projectDescription", request.getDescription());
        dataModel.put("springBootVersion", data.getSpringBootVersion());
        dataModel.put("springDependencyManagementVersion", data.getSpringDependencyManagementVersion());
        dataModel.put("javaVersion", data.getJavaVersion());
        dataModel.put("dependencies", createDependencies(request));
        dataModel.put("dependencyManagementNeeded", createDependencies(request).stream().anyMatch(Dependency::isDependencyManagement));
        dataModel.put("plugins", createPlugins(request));

        // Process the template and return the file
        String filename = "build.gradle";
        return freemarkerTemplateProcessor.process("build.ftl", dataModel, filename);
    }

    @Override
    protected Plugin getVaadinPlugin(String vaadinVersion) {
        Plugin plugin = new Plugin();
        plugin.setArtifactId("com.vaadin");
        plugin.setVersion(vaadinVersion);
        return plugin;
    }

}
