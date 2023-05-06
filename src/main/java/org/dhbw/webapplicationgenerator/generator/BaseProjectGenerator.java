package org.dhbw.webapplicationgenerator.generator;

import lombok.AllArgsConstructor;
import org.dhbw.webapplicationgenerator.generator.baseproject.FileFolderGenerator;
import org.dhbw.webapplicationgenerator.generator.baseproject.MavenFolderGenerator;
import org.dhbw.webapplicationgenerator.generator.baseproject.RootFilesGenerator;
import org.dhbw.webapplicationgenerator.generator.baseproject.SourceFolderGenerator;
import org.dhbw.webapplicationgenerator.model.request.ProjectRequest;
import org.dhbw.webapplicationgenerator.model.response.ProjectDirectory;
import org.dhbw.webapplicationgenerator.model.response.Project;
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
    public Project createOld(CreationRequest request) {
        createTmpFolderIfNotExists();
        createTmp2FolderIfNotExists();
        Project baseProject = new Project();

        ProjectDirectory mainDirectory = addDirectory(request.getProject().getTitleWithoutSpaces(), Optional.empty());
        this.createTmpFolderIfNotExists();
        this.createTmp2FolderIfNotExists();
        this.mavenFolderGenerator.create(mainDirectory);
        this.rootFilesGenerator.createOld(request, mainDirectory);
        this.sourceFolderGenerator.createOld(request, mainDirectory);
        baseProject.setFileStructure(mainDirectory);
        return baseProject;
    }

    public Project create(ProjectRequest request) {
        Project baseProject = new Project();
        ProjectDirectory mainDirectory = addDirectory(request.getTitleWithoutSpaces(), Optional.empty());
        // TODO: Either create Maven or Gradle folder. Also extract to Maven-Generation step.
        this.mavenFolderGenerator.create(mainDirectory);
        this.rootFilesGenerator.create(request, mainDirectory);
        this.sourceFolderGenerator.create(request, mainDirectory);
        baseProject.setFileStructure(mainDirectory);
        return baseProject;
    }

}
