package org.dhbw.webapplicationgenerator.generator.baseproject;

import lombok.AllArgsConstructor;
import org.dhbw.webapplicationgenerator.generator.baseproject.deployment.BuildAndRunScriptGenerator;
import org.dhbw.webapplicationgenerator.generator.baseproject.deployment.DockerfileGenerator;
import org.dhbw.webapplicationgenerator.generator.model.ProjectDirectory;
import org.dhbw.webapplicationgenerator.generator.model.ProjectFile;
import org.dhbw.webapplicationgenerator.webclient.request.CreationRequest;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@AllArgsConstructor
public class DeploymentFilesGenerator extends FileFolderGenerator {

    private final BuildAndRunScriptGenerator buildAndRunScriptGenerator;
    private final DockerfileGenerator dockerfileGenerator;

    /**
     * Creates all necessary files for the deployment
     * @param parent Parent directory, usually this is the root directory of the project
     * @return List of Deployment Files
     */
    public List<ProjectFile> create(CreationRequest request, ProjectDirectory parent) {
        List<ProjectFile> files = new ArrayList<>();
        files.add(addFile("buildAndRun.sh", buildAndRunScriptGenerator.create(request), parent));
        files.add(addFile("Dockerfile", dockerfileGenerator.create(request), parent));
        return files;
    }
}
