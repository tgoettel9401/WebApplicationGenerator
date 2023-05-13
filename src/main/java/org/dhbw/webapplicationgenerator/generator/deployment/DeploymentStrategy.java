package org.dhbw.webapplicationgenerator.generator.deployment;

import org.dhbw.webapplicationgenerator.model.request.ProjectRequest;
import org.dhbw.webapplicationgenerator.model.response.Project;

public interface DeploymentStrategy {

    /**
     *
     * @param project CurrentProject that will be updated during this process.
     * @param request Request to create the project with.
     * @return Updated Project
     */
    Project create(Project project, ProjectRequest request);

}
