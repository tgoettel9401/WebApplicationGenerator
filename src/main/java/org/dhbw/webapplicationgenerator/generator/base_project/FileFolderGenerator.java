package org.dhbw.webapplicationgenerator.generator.base_project;

import org.dhbw.webapplicationgenerator.generator.Project;
import org.dhbw.webapplicationgenerator.generator.model.ProjectDirectory;
import org.dhbw.webapplicationgenerator.generator.model.ProjectFile;
import org.dhbw.webapplicationgenerator.webclient.request.ProjectRequest;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Locale;
import java.util.Objects;
import java.util.Optional;

@Service
public class FileFolderGenerator {

    /**
     * Returns the main project directory, usually in path /src/main/group/artifact
     * @param project
     * @param request
     * @return
     */
    public ProjectDirectory getMainProjectDirectory(Project project, ProjectRequest request) {
        ProjectDirectory rootDir = (ProjectDirectory) project.getFileStructure();
        ProjectDirectory srcDir = (ProjectDirectory) rootDir.getChildren().stream().filter(child -> child.getTitle().equals("src"))
                .findFirst().orElseThrow(() -> new RuntimeException("Creating entity failed due to missing src folder"));
        ProjectDirectory mainDir = (ProjectDirectory) srcDir.getChildren().stream().filter(child -> child.getTitle().equals("main"))
                .findFirst().orElseThrow(() -> new RuntimeException("Creating entity failed due to missing main folder"));
        ProjectDirectory groupDir = (ProjectDirectory) mainDir.getChildren().stream().filter(child1 -> child1.getTitle().equals("java"))
                .findFirst().orElseThrow(() -> new RuntimeException("Creating entity failed due to missing java folder"));
        for (String groupPart : request.getGroup().split("\\.")) {
            groupDir = (ProjectDirectory) groupDir.getChildren().stream().filter(child -> child.getTitle().equals(groupPart)).findFirst()
                    .orElseThrow(() -> new RuntimeException("Creating entity failed due to missing group folder"));
        }
        return (ProjectDirectory) groupDir.getChildren().stream().filter(child -> child.getTitle().equals(request.getArtifact()))
                .findFirst().orElseThrow(() -> new RuntimeException("Creating entity failed due to missing artifact folder"));
    }

    /**
     * Returns the resources directory, usually in path /src/main/resources
     * @param project
     * @return
     */
    public ProjectDirectory getResourcesDirectory(Project project) {
        ProjectDirectory rootDir = (ProjectDirectory) project.getFileStructure();
        ProjectDirectory srcDir = (ProjectDirectory) rootDir.getChildren().stream().filter(child -> child.getTitle().equals("src"))
                .findFirst().orElseThrow(() -> new RuntimeException("Creating entity failed due to missing src folder"));
        ProjectDirectory mainDir = (ProjectDirectory) srcDir.getChildren().stream().filter(child -> child.getTitle().equals("main"))
                .findFirst().orElseThrow(() -> new RuntimeException("Creating entity failed due to missing main folder"));
        ProjectDirectory resourcesDir = (ProjectDirectory) mainDir.getChildren().stream().filter(child -> child.getTitle().equals("resources"))
                .findFirst().orElseThrow(() -> new RuntimeException("Creating entity failed due to missing resources folder"));
        return resourcesDir;
    }

    /**
     * Adds a directory to the specified parent folder
     * @param title String
     * @param parent Optional parent directory the folder should be added to. If the Optional is empty, it will be
     *               added as root directory
     * @return newly created directory
     */
    public ProjectDirectory addDirectory(String title, Optional<ProjectDirectory> parent) {
        ProjectDirectory projectDirectory = new ProjectDirectory();
        projectDirectory.setTitle(title);

        if (parent.isPresent()) {
            projectDirectory.setPath(parent.get().getPath() + title + "/");
            parent.get().addChild(projectDirectory);
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
        if (!Files.exists(Path.of(".tmp"))) {
            try {
                Files.createDirectory(Path.of(".tmp"));
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    protected String capitalize(String value) {
        value = value.toLowerCase(Locale.ROOT);
        return value.substring(0,1).toUpperCase() + value.substring(1).toLowerCase();
    }

    protected String plural(String value) {
        return value + "s";
    }

}
