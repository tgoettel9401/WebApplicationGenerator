package org.dhbw.webapplicationgenerator.generator.baseproject;

import lombok.AllArgsConstructor;
import org.dhbw.webapplicationgenerator.generator.util.FileFolderGenerator;
import org.dhbw.webapplicationgenerator.model.request.ProjectRequest;
import org.dhbw.webapplicationgenerator.model.response.Project;
import org.dhbw.webapplicationgenerator.model.response.ProjectDirectory;
import org.dhbw.webapplicationgenerator.util.ResourceFileHelper;
import org.springframework.stereotype.Service;

import java.io.FileNotFoundException;
import java.util.Optional;

@Service
@AllArgsConstructor
public class BaseProjectGenerator extends FileFolderGenerator {

    private final ResourceFileHelper resourceFileHelper;
    private final ReadmeGenerator readmeGenerator;

    /**
     * Creates the Base Project, namely the root folder and files residing in that folder (.gitignore or README).
     * @param request Request containing the relevant information of the project to be created
     * @return BaseProject
     */
    public Project create(ProjectRequest request) {
        Project baseProject = new Project();
        ProjectDirectory rootDirectory = addRootDirectory(request);
        addGitIgnoreFile(rootDirectory);
        addReadmeFile(request, rootDirectory);
        baseProject.setFileStructure(rootDirectory);
        return baseProject;
    }

    private ProjectDirectory addRootDirectory(ProjectRequest request) {
        return addDirectory(request.getTitleWithoutSpaces(), Optional.empty());
    }

    private void addGitIgnoreFile(ProjectDirectory rootDirectory) {
        try {
            addFile(".gitignore", resourceFileHelper.getFile("gitignore"), rootDirectory);
        } catch (FileNotFoundException ex) {
            ex.printStackTrace();
        }
    }

    private void addReadmeFile(ProjectRequest request, ProjectDirectory rootDirectory) {
        readmeGenerator.create(request, rootDirectory);
    }

}
