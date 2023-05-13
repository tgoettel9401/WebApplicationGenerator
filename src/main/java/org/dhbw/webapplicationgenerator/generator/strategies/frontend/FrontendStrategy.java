package org.dhbw.webapplicationgenerator.generator.strategies.frontend;

import org.dhbw.webapplicationgenerator.generator.strategies.GenerationStrategy;
import org.dhbw.webapplicationgenerator.model.response.ProjectDirectory;

import java.util.function.Function;
import java.util.function.UnaryOperator;

public interface FrontendStrategy extends GenerationStrategy {
    Function<ProjectDirectory, ProjectDirectory> getFrontendDirectoryFinder();
    void setFrontendDirectoryFinder(UnaryOperator<ProjectDirectory> frontendDirectory);
}
