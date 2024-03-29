package org.dhbw.webapplicationgenerator.generator.util;

import org.dhbw.webapplicationgenerator.model.request.ProjectRequest;
import org.dhbw.webapplicationgenerator.model.request.backend.JavaData;
import org.dhbw.webapplicationgenerator.model.response.Project;
import org.dhbw.webapplicationgenerator.model.response.ProjectDirectory;
import org.dhbw.webapplicationgenerator.model.response.ProjectFile;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Objects;
import java.util.Optional;

@Service
public class FileFolderGenerator {

    protected static final String JAVA_CLASS_ENDING = ".java";
    protected static final String TMP_PATH = ".tmp/";
    protected static final String TMP_2_PATH = ".tmp2/";


    // TODO: Migrate directory calculation to some more specific place, probably Java.

    // TODO: Refactor methods to use Java-Streams and flatMap.

    /**
     * Returns the main project directory, usually in path /src/main/group/artifact
     * @param project current Project object
     * @param request CreationRequest that has been transmitted from consumer
     * @return ProjectDirectory for main
     */
    public ProjectDirectory getMainProjectDirectory(Project project, ProjectRequest request) {
        ProjectDirectory rootDir = (ProjectDirectory) project.getFileStructure();
        ProjectDirectory srcDir = rootDir.findChildDirectory("src");
        ProjectDirectory mainDir = srcDir.findChildDirectory("main");
        ProjectDirectory groupDir = mainDir.findChildDirectory("java");

        JavaData javaData = (JavaData) request.getBackend().getData();
        for (String groupPart : javaData.getGroup().split("\\.")) {
            groupDir = groupDir.findChildDirectory(groupPart);
        }
        return groupDir.findChildDirectory(javaData.getArtifact());
    }

    /**
     * Returns the resources directory, usually in path /src/main/resources
     * @param project current Project object
     * @return ProjectDirectory for resources
     */
    public ProjectDirectory getResourcesDirectory(Project project) {
        ProjectDirectory rootDir = (ProjectDirectory) project.getFileStructure();
        ProjectDirectory srcDir = (ProjectDirectory) rootDir.getChildren().stream().filter(child -> child.getTitle().equals("src"))
                .findFirst().orElseThrow(() -> new RuntimeException("Creating entity failed due to missing src folder"));
        ProjectDirectory mainDir = (ProjectDirectory) srcDir.getChildren().stream().filter(child -> child.getTitle().equals("main"))
                .findFirst().orElseThrow(() -> new RuntimeException("Creating entity failed due to missing main folder"));
        return (ProjectDirectory) mainDir.getChildren().stream().filter(child -> child.getTitle().equals("resources"))
                .findFirst().orElseThrow(() -> new RuntimeException("Creating entity failed due to missing resources folder"));
    }

    /**
     * Returns the templates directory, usually in path /src/main/resources/templates
     * @param project current Project object
     * @return ProjectDirectory for templates
     */
    public ProjectDirectory getTemplatesDirectory(Project project) {
        ProjectDirectory resourcesDir = getResourcesDirectory(project);
        return (ProjectDirectory) resourcesDir.getChildren().stream().filter(child -> child.getTitle().equals("templates"))
                .findFirst().orElseThrow(() -> new RuntimeException("Creating template failed due to missing templates folder"));
    }

    /**
     * Returns the controller directory, usually in path {mainDir}/controller
     * @param project current Project object
     * @return ProjectDirectory for resources
     */
    public ProjectDirectory getControllerDirectory(Project project, ProjectRequest request) {
        ProjectDirectory artifactDir = getMainProjectDirectory(project, request);
        return (ProjectDirectory) artifactDir.getChildren().stream().filter(child -> child.getTitle().equals("controller"))
                .findFirst().orElseThrow(() -> new RuntimeException("Creating controller failed due to missing controller folder"));
    }

    /**
     * Returns the transferObject directory, usually in path {mainDir}/transferObject
     * @param project current Project object
     * @return ProjectDirectory for resources
     */
    public ProjectDirectory getTransferObjectDirectory(Project project, ProjectRequest request) {
        ProjectDirectory artifactDir = getMainProjectDirectory(project, request);
        return (ProjectDirectory) artifactDir.getChildren().stream().filter(child -> child.getTitle().equals("transferObject"))
                .findFirst().orElseThrow(() -> new RuntimeException("Creating transferObject failed due to missing transferObject folder"));
    }

    /**
     * Adds a directory to the specified parent folder. If the directory already exists, it returns the existing
     * directory.
     * @param title String
     * @param parent Optional parent directory the folder should be added to. If the Optional is empty, it will be
     *               added as root directory
     * @return newly created directory (or existing directory)
     */
    public ProjectDirectory addDirectory(String title, Optional<ProjectDirectory> parent) {

        // Prepare directory.
        ProjectDirectory projectDirectory = new ProjectDirectory();
        projectDirectory.setTitle(title);

        // Parent exists => directory should be added in the parent
        if (parent.isPresent()) {

            // Check if the directory to be added already exists
            Optional<ProjectDirectory> existingDirectory = parent.get().getDirectoryChildren().stream()
                    .filter(dir -> dir.getTitle().equals(title))
                    .findAny();

            // If the directory already exists though, it should be returned
            if (existingDirectory.isPresent()) {
                return existingDirectory.get();
            }

            // Otherwise a new directory is created
            else {
                projectDirectory.setPath(parent.get().getPath() + title + "/");
                parent.get().addChild(projectDirectory);
            }

        } else {
            projectDirectory.setPath(title + "/");
        }

        return projectDirectory;
    }

    /**
     * Adds a file to the specified parent folder
     * @param parent Parent directory the file should be added to. Must not be null
     * @return newly created file that resides in the parent directory
     */
    public ProjectFile addFile(File file, ProjectDirectory parent) {
        Objects.requireNonNull(parent);
        return addFile(file.getName(), file, parent);
    }

    /**
     * Adds a file to the specified parent folder
     * @param title Filename of the created File
     * @param parent Parent directory the file should be added to. Must not be null
     * @return newly created file that resides in the parent directory
     */
    public ProjectFile addFile(String title, File file, ProjectDirectory parent) {
        Objects.requireNonNull(parent);
        ProjectFile projectFile = new ProjectFile();
        projectFile.setTitle(title);
        projectFile.setPath(parent.getPath() + title);
        projectFile.setFile(file);
        parent.addChild(projectFile);
        return projectFile;
    }

    /**
     * Creates the .tmp folder if it does not exist yet.
     */
    public void createTmpFolderIfNotExists() {
        Path tmpPath = Path.of(TMP_PATH);
        if (!Files.exists(tmpPath)) {
            try {

                Files.createDirectory(tmpPath);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * Creates the .tmp2 folder if it does not exist yet.
     */
    public void createTmp2FolderIfNotExists() {
        Path tmp2Path = Path.of(TMP_2_PATH);
        if (!Files.exists(tmp2Path)) {
            try {
                Files.createDirectory(tmp2Path);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    protected String capitalize(String value) {
        return Utils.capitalize(value);
    }

    protected String plural(String value) {
        return value + "s";
    }

}
