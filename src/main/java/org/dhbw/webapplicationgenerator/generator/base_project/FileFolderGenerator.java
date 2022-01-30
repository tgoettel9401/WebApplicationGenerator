package org.dhbw.webapplicationgenerator.generator.base_project;

import org.dhbw.webapplicationgenerator.generator.model.ProjectDirectory;
import org.dhbw.webapplicationgenerator.generator.model.ProjectFile;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Objects;
import java.util.Optional;

@Service
public class FileFolderGenerator {

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

}
