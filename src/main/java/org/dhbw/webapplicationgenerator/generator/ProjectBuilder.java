package org.dhbw.webapplicationgenerator.generator;

import org.dhbw.webapplicationgenerator.generator.strategies.GenerationStrategy;
import org.dhbw.webapplicationgenerator.generator.strategies.frontend.FrontendStrategy;
import org.dhbw.webapplicationgenerator.model.request.ProjectRequest;
import org.dhbw.webapplicationgenerator.model.response.Project;
import org.dhbw.webapplicationgenerator.webclient.exception.WagException;

public class ProjectBuilder {

    private BaseProjectGenerator baseProjectStrategy;
    private GenerationStrategy backendStrategy;
    private FrontendStrategy frontendStrategy;
    private GenerationStrategy databaseStrategy;
    private GenerationStrategy deploymentStrategy;

    private Project currentProject;

    /**
     * Builds the project using the defined strategies, strategies are only executed (and also are only mandatory to
     * be set prior to this action) if they are enabled in the request.
     * @param request Request to construct the project.
     * @return Constructed Project.
     */
    public Project build(ProjectRequest request) {
        createBaseProject(request);
        addBackend(request);
        addFrontend(request);
        addDatabase(request);
        addDeployment(request);
        return currentProject;
    }

    /**
     * Set the baseProjectStrategy
     * @param strategy strategy constructing the baseProject
     * @return builder
     */
    public ProjectBuilder baseProjectStrategy(BaseProjectGenerator strategy) {
        this.baseProjectStrategy = strategy;
        return this;
    }

    /**
     * Set the backendStrategy
     * @param strategy strategy constructing the backend
     * @return builder
     */
    public ProjectBuilder backendStrategy(GenerationStrategy strategy) {
        this.backendStrategy = strategy;
        return this;
    }

    /**
     * Set the frontendStrategy
     * @param strategy strategy constructing the frontend
     * @return builder
     */
    public ProjectBuilder frontendStrategy(FrontendStrategy strategy) {
        this.frontendStrategy = strategy;
        return this;
    }

    /**
     * Set the databaseStrategy
     * @param strategy strategy constructing the database
     * @return builder
     */
    public ProjectBuilder databaseStrategy(GenerationStrategy strategy) {
        this.databaseStrategy = strategy;
        return this;
    }

    /**
     * Set the deploymentStrategy
     * @param strategy strategy constructing the deployment
     * @return builder
     */
    public ProjectBuilder deploymentStrategy(GenerationStrategy strategy) {
        this.deploymentStrategy = strategy;
        return this;
    }

    private void createBaseProject(ProjectRequest request) {
        if (baseProjectStrategy == null) {
            throw new WagException("Using baseproject strategy without setting the strategy.");
        }
        this.currentProject = baseProjectStrategy.create(request);
    }

    private void addBackend(ProjectRequest request) {
        if (request.isBackendEnabled()) {
            if (backendStrategy == null) {
                throw new WagException("Using backend strategy without setting the strategy.");
            }
            this.currentProject = backendStrategy.create(request, this.currentProject);
        }
    }

    private void addFrontend(ProjectRequest request) {
        if (request.isFrontendEnabled()) {
            if (frontendStrategy == null) {
                throw new WagException("Using frontend strategy without setting the strategy.");
            }
            // TOOD: this.currentProject = frontendStrategy.create(request, this.currentProject);
        }
    }

    private void addDatabase(ProjectRequest request) {
        if (request.isDatabaseEnabled()) {
            if (databaseStrategy == null) {
                throw new WagException("Using database strategy without setting the strategy.");
            }
            // TODO: this.currentProject = databaseStrategy.create(request, this.currentProject);
        }
    }

    private void addDeployment(ProjectRequest request) {
        if (request.isDeploymentEnabled()) {
            if (deploymentStrategy == null) {
                throw new WagException("Using deployment strategy without setting the strategy.");
            }
            // TODO: this.currentProject = deploymentStrategy.create(request, this.currentProject);
        }
    }

}
