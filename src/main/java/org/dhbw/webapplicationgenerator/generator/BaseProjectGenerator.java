package org.dhbw.webapplicationgenerator.generator;

import lombok.AllArgsConstructor;
import org.dhbw.webapplicationgenerator.generator.base_project.FileFolderGenerator;
import org.dhbw.webapplicationgenerator.generator.base_project.MavenFolderGenerator;
import org.dhbw.webapplicationgenerator.generator.base_project.RootFilesGenerator;
import org.dhbw.webapplicationgenerator.generator.base_project.SourceFolderGenerator;
import org.dhbw.webapplicationgenerator.generator.model.ProjectDirectory;
import org.dhbw.webapplicationgenerator.webclient.request.CreationRequest;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@AllArgsConstructor
public class BaseProjectGenerator extends FileFolderGenerator {

    private final MavenFolderGenerator mavenFolderGenerator;
    private final RootFilesGenerator rootFilesGenerator;
    private final SourceFolderGenerator sourceFolderGenerator;

    /**
     * Creates the Base Project
     * @param request Request containing the relevant information of the project to be created
     * @return BaseProject
     */
    public Project create(CreationRequest request) {
        Project baseProject = new Project();

        ProjectDirectory mainDirectory = addDirectory(request.getProject().getTitleWithoutSpaces(), Optional.empty());
        this.createTmpFolderIfNotExists();
        this.createTmp2FolderIfNotExists();
        this.mavenFolderGenerator.create(mainDirectory);
        this.rootFilesGenerator.create(request, mainDirectory);
        this.sourceFolderGenerator.create(request, mainDirectory);
        baseProject.setFileStructure(mainDirectory);
        return baseProject;
    }

}
