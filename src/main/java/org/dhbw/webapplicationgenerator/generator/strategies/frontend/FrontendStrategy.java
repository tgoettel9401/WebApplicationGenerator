package org.dhbw.webapplicationgenerator.generator.strategies.frontend;

import org.dhbw.webapplicationgenerator.generator.strategies.GenerationStrategy;
import org.dhbw.webapplicationgenerator.model.response.ProjectDirectory;

public interface FrontendStrategy extends GenerationStrategy {

    public ProjectDirectory getFrontendDirectory();

    public void setFrontendDirectory(ProjectDirectory dir);

}
