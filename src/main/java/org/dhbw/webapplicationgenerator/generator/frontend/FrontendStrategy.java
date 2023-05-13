package org.dhbw.webapplicationgenerator.generator.frontend;

import org.dhbw.webapplicationgenerator.model.request.ProjectRequest;
import org.dhbw.webapplicationgenerator.model.response.Project;
import org.dhbw.webapplicationgenerator.model.response.ProjectDirectory;

import java.util.function.UnaryOperator;

public interface FrontendStrategy {

    /**
     *
     * @param project CurrentProject that will be updated during this process.
     * @param request Request to create the project with.
     * @param frontendDirectoryFinder function to find the frontend directory.
     * @param mainSourceDirectoryFinder function to find the main source directory.
     * @return Updated Project
     */
    Project create(ProjectRequest request, Project project, UnaryOperator<ProjectDirectory> frontendDirectoryFinder,
                   UnaryOperator<ProjectDirectory> mainSourceDirectoryFinder);
}
