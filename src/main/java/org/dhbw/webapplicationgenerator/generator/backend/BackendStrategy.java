package org.dhbw.webapplicationgenerator.generator.backend;

import org.dhbw.webapplicationgenerator.model.request.ProjectRequest;
import org.dhbw.webapplicationgenerator.model.request.backend.BackendData;
import org.dhbw.webapplicationgenerator.model.response.Project;
import org.dhbw.webapplicationgenerator.model.response.ProjectDirectory;

import java.util.function.UnaryOperator;

public interface BackendStrategy {

    /**
     *
     * @param project CurrentProject that will be updated during this process.
     * @param request Request to create the project with.
     * @return Updated Project
     */
    Project create(Project project, ProjectRequest request);

    /**
     * Gives a function to find the frontend directory in the folder structure.
     * @return function that can be applied later on
     */
    UnaryOperator<ProjectDirectory> getFrontendDirectoryFinder();

    /**
     * Gives a function to find the main source directory in the folder structure.
     * @param data BackendData (needed to be cast later on)
     * @return function that can be applied later on
     */
    UnaryOperator<ProjectDirectory> getMainSourceDirectoryFinder(BackendData data);
}
