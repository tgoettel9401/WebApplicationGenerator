package org.dhbw.webapplicationgenerator.generator.strategies.backend;

import org.dhbw.webapplicationgenerator.generator.strategies.GenerationStrategy;
import org.dhbw.webapplicationgenerator.model.request.backend.BackendData;
import org.dhbw.webapplicationgenerator.model.response.ProjectDirectory;

import java.util.function.UnaryOperator;

public interface BackendStrategy extends GenerationStrategy {
    UnaryOperator<ProjectDirectory> getFrontendDirectoryFinder();
    UnaryOperator<ProjectDirectory> getMainSourceDirectoryFinder(BackendData data);
}
