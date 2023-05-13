package org.dhbw.webapplicationgenerator.generator;

import org.dhbw.webapplicationgenerator.generator.backend.BackendStrategy;
import org.dhbw.webapplicationgenerator.generator.baseproject.BaseProjectGenerator;
import org.dhbw.webapplicationgenerator.generator.database.DatabaseStrategy;
import org.dhbw.webapplicationgenerator.generator.deployment.DeploymentStrategy;
import org.dhbw.webapplicationgenerator.generator.frontend.FrontendStrategy;
import org.dhbw.webapplicationgenerator.generator.security.SecurityGenerator;
import org.dhbw.webapplicationgenerator.model.request.ProjectRequest;
import org.dhbw.webapplicationgenerator.model.response.Project;
import org.dhbw.webapplicationgenerator.model.response.ProjectDirectory;
import org.dhbw.webapplicationgenerator.webclient.exception.WagException;

import java.util.function.UnaryOperator;

public class ProjectBuilder {

    private BaseProjectGenerator baseProjectStrategy;
    private BackendStrategy backendStrategy;
    private FrontendStrategy frontendStrategy;
    private DatabaseStrategy databaseStrategy;
    private DeploymentStrategy deploymentStrategy;
    private SecurityGenerator securityGenerator;

    // Directory Finder
    private UnaryOperator<ProjectDirectory> frontendDirectoryFinder;
    private UnaryOperator<ProjectDirectory> mainSourceDirectoryFinder;

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
        addSecurity(request);
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
    public ProjectBuilder backendStrategy(BackendStrategy strategy) {
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
    public ProjectBuilder databaseStrategy(DatabaseStrategy strategy) {
        this.databaseStrategy = strategy;
        return this;
    }

    /**
     * Set the deploymentStrategy
     * @param strategy strategy constructing the deployment
     * @return builder
     */
    public ProjectBuilder deploymentStrategy(DeploymentStrategy strategy) {
        this.deploymentStrategy = strategy;
        return this;
    }

    /**
     * Set the frontendDirectoryFinder
     * @param frontendDirectoryFinder function to find the frontendDirectory with rootDir of project as parameter
     * @return builder
     */
    public ProjectBuilder frontendDirectoryFinder(UnaryOperator<ProjectDirectory> frontendDirectoryFinder) {
        this.frontendDirectoryFinder = frontendDirectoryFinder;
        return this;
    }

    /**
     * Set the mainSourceDirectoryFinder
     * @param mainSourceDirectoryFinder function to find the mainSourceDirectory (of backend)
     *                                  with rootDir of project as parameter
     * @return builder
     */
    public ProjectBuilder mainSourceDirectoryFinder(UnaryOperator<ProjectDirectory> mainSourceDirectoryFinder) {
        this.mainSourceDirectoryFinder = mainSourceDirectoryFinder;
        return this;
    }

    /**
     * Set the deploymentStrategy
     * @param securityGenerator strategy constructing the security-files.
     * @return builder
     */
    public ProjectBuilder securityGenerator(SecurityGenerator securityGenerator) {
        this.securityGenerator = securityGenerator;
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
            this.currentProject = backendStrategy.create(this.currentProject, request);
        }
    }

    private void addFrontend(ProjectRequest request) {
        if (request.isFrontendEnabled()) {
            if (frontendStrategy == null) {
                throw new WagException("Using frontend strategy without setting the strategy.");
            }
            this.currentProject = frontendStrategy.create(request, this.currentProject, frontendDirectoryFinder, mainSourceDirectoryFinder);
        }
    }

    private void addDatabase(ProjectRequest request) {
        if (request.isDatabaseEnabled()) {
            if (databaseStrategy == null) {
                throw new WagException("Using database strategy without setting the strategy.");
            }
            this.currentProject = databaseStrategy.create(this.currentProject, request);
        }
    }

    private void addDeployment(ProjectRequest request) {
        if (request.isDeploymentEnabled()) {
            if (deploymentStrategy == null) {
                throw new WagException("Using deployment strategy without setting the strategy.");
            }
            this.currentProject = deploymentStrategy.create(this.currentProject, request);
        }
    }

    private void addSecurity(ProjectRequest request) {
        if (request.isSecurityEnabled()) {
            if (securityGenerator == null) {
                throw new WagException("Using security strategy without setting the strategy.");
            }
            this.currentProject = securityGenerator.create(this.currentProject, request);
        }
    }

}
