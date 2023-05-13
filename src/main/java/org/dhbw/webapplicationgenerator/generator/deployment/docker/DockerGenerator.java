package org.dhbw.webapplicationgenerator.generator.deployment.docker;

import lombok.AllArgsConstructor;
import org.dhbw.webapplicationgenerator.generator.deployment.DeploymentStrategy;
import org.dhbw.webapplicationgenerator.generator.util.FileFolderGenerator;
import org.dhbw.webapplicationgenerator.model.request.ProjectRequest;
import org.dhbw.webapplicationgenerator.model.response.Project;
import org.dhbw.webapplicationgenerator.model.response.ProjectDirectory;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class DockerGenerator extends FileFolderGenerator implements DeploymentStrategy {

    private final BuildAndRunScriptGenerator buildAndRunScriptGenerator;
    private final DockerfileGenerator dockerfileGenerator;

    /**
     * Creates all necessary files for the deployment
     * @param project Project that needs to be updated
     * @return Updated project (that has been enhanced with Docker)
     */
    @Override
    public Project create(Project project, ProjectRequest request) {
        ProjectDirectory rootDirectory = (ProjectDirectory) project.getFileStructure();
        addFile("buildAndRun.sh", buildAndRunScriptGenerator.create(request), rootDirectory);
        addFile("Dockerfile", dockerfileGenerator.create(request), rootDirectory);
        return project;
    }

}
