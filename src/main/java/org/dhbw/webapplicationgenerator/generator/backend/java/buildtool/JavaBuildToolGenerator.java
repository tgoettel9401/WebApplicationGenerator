package org.dhbw.webapplicationgenerator.generator.backend.java.buildtool;

import org.dhbw.webapplicationgenerator.generator.util.FileFolderGenerator;
import org.dhbw.webapplicationgenerator.model.request.ProjectRequest;
import org.dhbw.webapplicationgenerator.model.request.Strategy;
import org.dhbw.webapplicationgenerator.model.request.backend.DatabaseProduct;
import org.dhbw.webapplicationgenerator.model.request.backend.JavaData;
import org.dhbw.webapplicationgenerator.model.request.backend.SpringBootData;
import org.dhbw.webapplicationgenerator.model.request.frontend.VaadinData;
import org.dhbw.webapplicationgenerator.model.response.Project;
import org.dhbw.webapplicationgenerator.model.response.ProjectDirectory;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public abstract class JavaBuildToolGenerator extends FileFolderGenerator {

    private static final String SPRING_BOOT_FRAMEWORK_GROUP_ID = "org.springframework.boot";
    private static final String VAADIN_GROUP_ID = "com.vaadin";

    /**
     * Creates the build-tool files (dependent on the selected JavaBuild-Tool)
     * @return
     */
    public abstract Project addBuildToolFiles(Project project, ProjectRequest request);

    /**
     * Creates the Src-Directory including test and main folder. The relevant base files like Main-Class are also contained.
     * @param parent Parent directory, usually this is the root directory of the project
     */
    public void createFolderStructure(ProjectRequest request, ProjectDirectory parent) {
        ProjectDirectory src = addDirectory("src", Optional.of(parent));

        // Main-Folder
        ProjectDirectory main = addDirectory("main", Optional.of(src));
        addDirectory("resources", Optional.of(main));
        ProjectDirectory java = addDirectory("java", Optional.of(main));
        addGroupPackage(request, java);

        // Test-Folder
        ProjectDirectory test = addDirectory("test", Optional.of(src));
        ProjectDirectory javaTest = addDirectory("java", Optional.of(test));
        addGroupPackage(request, javaTest);
    }

    /**
     * Returns the main project directory, usually in path /src/main/group/artifact
     * @param project current Project object
     * @param javaData javaData that has been provided in the request.
     * @return ProjectDirectory for main
     */
    public ProjectDirectory getMainDirectory(Project project, JavaData javaData) {
        ProjectDirectory rootDir = (ProjectDirectory) project.getFileStructure();
        return getMainDirectory(rootDir, javaData);
    }

    /**
     * Returns the main project directory, usually in path /src/main/group/artifact
     * @param rootDir rootFolder of the project
     * @param javaData javaData that has been provided in the request.
     * @return ProjectDirectory for main
     */
    public ProjectDirectory getMainDirectory(ProjectDirectory rootDir, JavaData javaData) {
        // TODO: Refactor, add findChild(String childName) to ProjectDirectory!
        ProjectDirectory srcDir = (ProjectDirectory) rootDir.getChildren().stream().filter(child -> child.getTitle().equals("src"))
                .findFirst().orElseThrow(() -> new RuntimeException("Finding main directory failed due to missing src folder"));
        ProjectDirectory mainDir = (ProjectDirectory) srcDir.getChildren().stream().filter(child -> child.getTitle().equals("main"))
                .findFirst().orElseThrow(() -> new RuntimeException("Finding main directory failed due to missing main folder"));
        ProjectDirectory groupDir = (ProjectDirectory) mainDir.getChildren().stream().filter(child1 -> child1.getTitle().equals("java"))
                .findFirst().orElseThrow(() -> new RuntimeException("Finding main directory failed due to missing java folder"));
        for (String groupPart : javaData.getGroup().split("\\.")) {
            groupDir = (ProjectDirectory) groupDir.getChildren().stream().filter(child -> child.getTitle().equals(groupPart)).findFirst()
                    .orElseThrow(() -> new RuntimeException("Finding main directory failed due to missing group folder"));
        }
        return (ProjectDirectory) groupDir.getChildren().stream().filter(child -> child.getTitle().equals(javaData.getArtifact()))
                .findFirst().orElseThrow(() -> new RuntimeException("Finding main directory failed due to missing artifact folder"));
    }

    /**
     * Returns the main test directory, usually in path /test/main/group/artifact
     * @param project current Project object
     * @param javaData javaData that has been provided in the request.
     * @return ProjectDirectory for main
     */
    public ProjectDirectory getMainTestDirectory(Project project, JavaData javaData) {
        ProjectDirectory rootDir = (ProjectDirectory) project.getFileStructure();
        // TODO: Refactor, add findChild(String childName) to ProjectDirectory!
        ProjectDirectory srcDir = (ProjectDirectory) rootDir.getChildren().stream().filter(child -> child.getTitle().equals("src"))
                .findFirst().orElseThrow(() -> new RuntimeException("Finding main directory failed due to missing test folder"));
        ProjectDirectory testDir = (ProjectDirectory) srcDir.getChildren().stream().filter(child -> child.getTitle().equals("test"))
                .findFirst().orElseThrow(() -> new RuntimeException("Finding main directory failed due to missing main folder"));
        ProjectDirectory groupDir = (ProjectDirectory) testDir.getChildren().stream().filter(child1 -> child1.getTitle().equals("java"))
                .findFirst().orElseThrow(() -> new RuntimeException("Finding main directory failed due to missing java folder"));
        for (String groupPart : javaData.getGroup().split("\\.")) {
            groupDir = (ProjectDirectory) groupDir.getChildren().stream().filter(child -> child.getTitle().equals(groupPart)).findFirst()
                    .orElseThrow(() -> new RuntimeException("Finding main directory failed due to missing group folder"));
        }
        return (ProjectDirectory) groupDir.getChildren().stream().filter(child -> child.getTitle().equals(javaData.getArtifact()))
                .findFirst().orElseThrow(() -> new RuntimeException("Finding main directory failed due to missing artifact folder"));
    }

    /**
     * Returns the resources directory, usually in path /src/main/resources
     * @param project current Project object
     * @return ProjectDirectory for resources
     */
    @Override
    public ProjectDirectory getResourcesDirectory(Project project) {
        ProjectDirectory rootDir = (ProjectDirectory) project.getFileStructure();
        ProjectDirectory srcDir = (ProjectDirectory) rootDir.getChildren().stream().filter(child -> child.getTitle().equals("src"))
                .findFirst().orElseThrow(() -> new RuntimeException("Creating entity failed due to missing src folder"));
        ProjectDirectory mainDir = (ProjectDirectory) srcDir.getChildren().stream().filter(child -> child.getTitle().equals("main"))
                .findFirst().orElseThrow(() -> new RuntimeException("Creating entity failed due to missing main folder"));
        return (ProjectDirectory) mainDir.getChildren().stream().filter(child -> child.getTitle().equals("resources"))
                .findFirst().orElseThrow(() -> new RuntimeException("Creating entity failed due to missing resources folder"));
    }

    private void addGroupPackage(ProjectRequest request, ProjectDirectory javaPackageDir) {
        JavaData data = (JavaData) request.getBackend().getData();
        String[] groupParts = data.getGroup().split("\\.");
        ProjectDirectory currentChildDir = javaPackageDir;
        for (String groupPart : groupParts) {
            currentChildDir = addDirectory(groupPart, Optional.of(currentChildDir));
        }
        addDirectory(data.getArtifact(), Optional.of(currentChildDir));
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
        dependencies.add(data.getDatabaseProduct().getDependency());

        // If H2 is not selected as DB-product, we stil have to add the dependency because of using it during tests.
        if (!data.getDatabaseProduct().equals(DatabaseProduct.H2)) {
            dependencies.add(DatabaseProduct.H2.getDependency());
        }

        if (request.isSecurityEnabled()) {
            dependencies.add(new Dependency(SPRING_BOOT_FRAMEWORK_GROUP_ID, "spring-boot-starter-security", "", "", "", false));
            if (request.getFrontend().getStrategy().equals(Strategy.THYMELEAF)) {
                dependencies.add(new Dependency("org.thymeleaf.extras", "thymeleaf-extras-springsecurity5", "", "", "", false));
            }
        }
        return dependencies;
    }

    protected List<Plugin> createPlugins(ProjectRequest request) {
        List<Plugin> plugins = new ArrayList<>();
        if (request.getFrontend().getStrategy().equals(Strategy.VAADIN)) {
            VaadinData data = (VaadinData) request.getFrontend().getData();
            String vaadinVersion = data.getVersion();
            plugins.add(getVaadinPlugin(vaadinVersion));
        }
        return plugins;
    }

    protected Plugin getVaadinPlugin(String vaadinVersion) {
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
