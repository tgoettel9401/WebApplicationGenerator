package org.dhbw.webapplicationgenerator.generator.strategies;

import org.dhbw.webapplicationgenerator.model.request.ProjectRequest;
import org.dhbw.webapplicationgenerator.model.response.Project;

public interface GenerationStrategy {
    Project create(ProjectRequest request, Project project);
}
