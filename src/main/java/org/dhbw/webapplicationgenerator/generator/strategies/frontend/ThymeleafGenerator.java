package org.dhbw.webapplicationgenerator.generator.strategies.frontend;

import org.dhbw.webapplicationgenerator.model.request.ProjectRequest;
import org.dhbw.webapplicationgenerator.model.response.Project;
import org.dhbw.webapplicationgenerator.model.response.ProjectDirectory;
import org.springframework.stereotype.Service;

@Service
public class ThymeleafGenerator implements FrontendStrategy {

    private ProjectDirectory frontendDirectory;

    @Override
    public Project create(ProjectRequest request, Project project) {
        // TODO: Implement
        // Project projectWithFrontend = this.frontendGenerator.create(projectWithFrontendController, request);
        return null;
    }

    @Override
    public ProjectDirectory getFrontendDirectory() {
        return this.frontendDirectory;
    }

    @Override
    public void setFrontendDirectory(ProjectDirectory dir) {
        this.frontendDirectory = dir;
    }
}
