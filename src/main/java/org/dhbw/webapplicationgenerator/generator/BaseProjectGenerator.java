package org.dhbw.webapplicationgenerator.generator;

import lombok.AllArgsConstructor;
import org.dhbw.webapplicationgenerator.generator.base_project.FolderGenerator;
import org.dhbw.webapplicationgenerator.generator.base_project.MavenFolderGenerator;
import org.dhbw.webapplicationgenerator.generator.model.ProjectDirectory;
import org.dhbw.webapplicationgenerator.webclient.ProjectRequest;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@AllArgsConstructor
public class BaseProjectGenerator extends FolderGenerator {

    private final MavenFolderGenerator mavenFolderGenerator;

    public Project create(ProjectRequest request) {
        Project baseProject = new Project();
        ProjectDirectory mainDirectory = addDirectory(request.getTitle(), Optional.empty());
        this.mavenFolderGenerator.create(mainDirectory);
        baseProject.setFileStructure(mainDirectory);
        return baseProject;
    }

}
