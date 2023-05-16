package org.dhbw.webapplicationgenerator.generator.backend.java.buildtool;

import lombok.AllArgsConstructor;
import org.dhbw.webapplicationgenerator.generator.util.FreemarkerTemplateProcessor;
import org.dhbw.webapplicationgenerator.model.request.ProjectRequest;
import org.dhbw.webapplicationgenerator.model.request.Strategy;
import org.dhbw.webapplicationgenerator.model.request.backend.SpringBootData;
import org.dhbw.webapplicationgenerator.model.request.frontend.VaadinData;
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
    private static final String VAADIN_GROUP_ID = "com.vaadin";
    private static final String VAADIN_VERSION = "23.3.12";

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
        dataModel.put("dependencyManagementNeeded", createDependencies(request).stream().anyMatch(Dependency::isDependencyManagement));
        dataModel.put("plugins", createPlugins(request));

        // Process the template and return the file
        String filename = "pom.xml";
        return freemarkerTemplateProcessor.process("pom.ftl", dataModel, filename);
    }

    protected List<Dependency> createDependencies(ProjectRequest request) {
        SpringBootData data = (SpringBootData) request.getBackend().getData();
        List<Dependency> dependencies = new ArrayList<>();
        dependencies.add(new Dependency(SPRING_BOOT_FRAMEWORK_GROUP_ID, "spring-boot-starter-web", "", "", "", false));
        dependencies.add(new Dependency(SPRING_BOOT_FRAMEWORK_GROUP_ID, "spring-boot-starter-data-jpa", "", "", "", false));
        dependencies.add(new Dependency(SPRING_BOOT_FRAMEWORK_GROUP_ID, "spring-boot-starter-data-rest", "", "", "", false));

        if (request.isFrontendEnabled() && request.getFrontend().getStrategy().equals(Strategy.THYMELEAF)) {
            dependencies.add(new Dependency(SPRING_BOOT_FRAMEWORK_GROUP_ID, "spring-boot-starter-thymeleaf", "", "", "", false));
        }

        if (request.isFrontendEnabled() && request.getFrontend().getStrategy().equals(Strategy.VAADIN)) {
            VaadinData vaadinData = (VaadinData) request.getFrontend().getData();
            String vaadinVersion = vaadinData.getVersion();
            dependencies.add(new Dependency(VAADIN_GROUP_ID, "vaadin-bom", vaadinVersion, "import", "pom", true));
            dependencies.add(new Dependency(VAADIN_GROUP_ID, "vaadin-spring-boot-starter", vaadinVersion, "", "", false));
        }

        dependencies.add(new Dependency("org.springdoc", "springdoc-openapi-ui", data.getSpringDocVersion(), "", "", false));
        dependencies.add(new Dependency("org.springdoc", "springdoc-openapi-data-rest", data.getSpringDocVersion(), "", "", false));
        dependencies.add(new Dependency("com.h2database", "h2", "", "runtime", "", false));

        if (request.isSecurityEnabled()) {
            dependencies.add(new Dependency(SPRING_BOOT_FRAMEWORK_GROUP_ID, "spring-boot-starter-security", "", "", "", false));
            if (request.getFrontend().getStrategy().equals(Strategy.THYMELEAF)) {
                dependencies.add(new Dependency("org.thymeleaf.extras", "thymeleaf-extras-springsecurity5", "", "", "", false));
            }
        }
        return dependencies;
    }

    private List<Plugin> createPlugins(ProjectRequest request) {
        VaadinData data = (VaadinData) request.getFrontend().getData();
        List<Plugin> plugins = new ArrayList<>();
        String vaadinVersion = data.getVersion();
        plugins.add(getVaadinPlugin(vaadinVersion));
        return plugins;
    }

    private Plugin getVaadinPlugin(String vaadinVersion) {
        Plugin plugin = new Plugin();
        plugin.setGroupId(VAADIN_GROUP_ID);
        plugin.setArtifactId("vaadin-maven-plugin");
        plugin.setVersion(vaadinVersion);

        List<PluginExecution> executions = new ArrayList<>();
        PluginExecution execution = new PluginExecution();

        List<PluginExecutionGoal> goals = new ArrayList<>();
        PluginExecutionGoal goalPrepare = new PluginExecutionGoal("prepare-frontend");
        PluginExecutionGoal goalBuild = new PluginExecutionGoal("build-frontend");
        goals.add(goalPrepare);
        goals.add(goalBuild);
        execution.setGoals(goals);
        executions.add(execution);
        plugin.setExecutions(executions);
        return plugin;
    }

}
