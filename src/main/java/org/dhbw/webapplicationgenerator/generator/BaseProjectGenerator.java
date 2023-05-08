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

    /**
     * Creates the Base Project, namely the root folder and files residing in that folder (.gitignore or README).
     * @param request Request containing the relevant information of the project to be created
     * @return BaseProject
     */
    public Project create(ProjectRequest request) {
        // TODO: Refactor and remove the rootFilesGenerator entirely.
        //  The files from rootFilesGenerator can also be created in this file.
        Project baseProject = new Project();
        ProjectDirectory mainDirectory = addDirectory(request.getTitleWithoutSpaces(), Optional.empty());
        this.rootFilesGenerator.create(request, mainDirectory);
        baseProject.setFileStructure(mainDirectory);
        return baseProject;
    }

}
