package org.dhbw.webapplicationgenerator.generator.strategies.deployment;

import lombok.AllArgsConstructor;
import org.dhbw.webapplicationgenerator.generator.baseproject.FileFolderGenerator;
import org.dhbw.webapplicationgenerator.generator.baseproject.deployment.BuildAndRunScriptGenerator;
import org.dhbw.webapplicationgenerator.generator.baseproject.deployment.DockerfileGenerator;
import org.dhbw.webapplicationgenerator.generator.strategies.GenerationStrategy;
import org.dhbw.webapplicationgenerator.model.request.ProjectRequest;
import org.dhbw.webapplicationgenerator.model.response.Project;
import org.dhbw.webapplicationgenerator.model.response.ProjectDirectory;
import org.dhbw.webapplicationgenerator.model.response.ProjectFile;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@AllArgsConstructor
public class DockerGenerator extends FileFolderGenerator implements GenerationStrategy {

    private final BuildAndRunScriptGenerator buildAndRunScriptGenerator;
    private final DockerfileGenerator dockerfileGenerator;

    /**
     * Creates all necessary files for the deployment
     * @param project Project that needs to be updated
     * @return Updated project (that has been enhanced with Docker)
     */
    @Override
    public Project create(ProjectRequest request, Project project) {
        ProjectDirectory rootDirectory = (ProjectDirectory) project.getFileStructure();
        addFile("buildAndRun.sh", buildAndRunScriptGenerator.create(request), rootDirectory);
        addFile("Dockerfile", dockerfileGenerator.create(request), rootDirectory);
        return project;
    }

}
