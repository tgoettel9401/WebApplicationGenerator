package org.dhbw.webapplicationgenerator.generator.base_project;

import org.dhbw.webapplicationgenerator.generator.model.ProjectDirectory;
import org.dhbw.webapplicationgenerator.generator.model.ProjectFile;
import org.springframework.stereotype.Service;

import java.io.File;
import java.util.Optional;

@Service
public class FolderGenerator {

    public ProjectDirectory addDirectory(String title, ProjectDirectory parent) {
        ProjectDirectory projectDirectory = new ProjectDirectory();
        projectDirectory.setTitle(title);
        projectDirectory.setPath(parent.getPath() + title + "/");
        parent.addChild(projectDirectory);
        return projectDirectory;
    }

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

    public ProjectFile addFile(File file, ProjectDirectory parent) {
        return addFile(file.getName(), file, parent);
    }

    public ProjectFile addFile(String title, File file, ProjectDirectory parent) {
        ProjectFile projectFile = new ProjectFile();
        projectFile.setTitle(title);
        projectFile.setPath(parent.getPath() + title);
        projectFile.setFile(file);
        parent.addChild(projectFile);
        return projectFile;
    }

}
