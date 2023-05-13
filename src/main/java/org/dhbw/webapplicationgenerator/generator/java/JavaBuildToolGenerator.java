package org.dhbw.webapplicationgenerator.generator.java;

import org.dhbw.webapplicationgenerator.generator.baseproject.FileFolderGenerator;
import org.dhbw.webapplicationgenerator.model.request.ProjectRequest;
import org.dhbw.webapplicationgenerator.model.request.backend.JavaData;
import org.dhbw.webapplicationgenerator.model.response.Project;
import org.dhbw.webapplicationgenerator.model.response.ProjectDirectory;

import java.util.Optional;

public abstract class JavaBuildToolGenerator extends FileFolderGenerator {

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

}
