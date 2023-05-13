package org.dhbw.webapplicationgenerator.generator.strategies.frontend;

import org.dhbw.webapplicationgenerator.model.request.ProjectRequest;
import org.dhbw.webapplicationgenerator.model.response.Project;
import org.dhbw.webapplicationgenerator.model.response.ProjectDirectory;

import java.util.function.UnaryOperator;

public interface FrontendStrategy {
    Project create(ProjectRequest request, Project project, UnaryOperator<ProjectDirectory> frontendDirectoryFinder,
                   UnaryOperator<ProjectDirectory> mainSourceDirectoryFinder);
}
