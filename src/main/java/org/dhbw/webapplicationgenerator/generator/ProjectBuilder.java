package org.dhbw.webapplicationgenerator.generator;

import org.dhbw.webapplicationgenerator.generator.strategies.BackendStrategy;
import org.dhbw.webapplicationgenerator.generator.strategies.DatabaseStrategy;
import org.dhbw.webapplicationgenerator.generator.strategies.DeploymentStrategy;
import org.dhbw.webapplicationgenerator.generator.strategies.FrontendStrategy;
import org.dhbw.webapplicationgenerator.model.request.ProjectRequest;
import org.dhbw.webapplicationgenerator.model.response.Project;

public class ProjectBuilder {
    private Project currentProject;

    private final BaseProjectGenerator baseProjectGenerator;

    private BackendStrategy backendStrategy;
    private FrontendStrategy frontendStrategy;
    private DeploymentStrategy deploymentStrategy;
    private DatabaseStrategy databaseStrategy;

    public ProjectBuilder(Project project, BaseProjectGenerator baseProjectGenerator) {
        this.baseProjectGenerator = baseProjectGenerator;
        this.currentProject = project;
    }

    public ProjectBuilder(BaseProjectGenerator baseProjectGenerator) {
        this.baseProjectGenerator = baseProjectGenerator;
        this.currentProject = new Project();
    }

    // TODO: Add

    /**
     * Builds the Project based on the defined strategies and the specified request.
     * @return
     */
    public Project build() {
        return this.currentProject;
    }

    public ProjectBuilder setBackendStrategy(BackendStrategy backendStrategy) {
        this.backendStrategy = backendStrategy;
        return this;
    }

    public ProjectBuilder setFrontendStrategy(FrontendStrategy frontendStrategy) {
        this.frontendStrategy = frontendStrategy;
        return this;
    }

    public ProjectBuilder setDeploymentStrategy(DeploymentStrategy deploymentStrategy) {
        this.deploymentStrategy = deploymentStrategy;
        return this;
    }

    public ProjectBuilder setDatabaseStrategy(DatabaseStrategy databaseStrategy) {
        this.databaseStrategy = databaseStrategy;
        return this;
    }

    // TODO: Add
    public ProjectBuilder addBackend() {
        // TODO: Move baseProject creation here as this mainly depends on the selected Backend!
        // TODO: Add backend-steps potentially in the strategy as e.g. MvcConfig is specific to SpringBoot.
        /*
        Project projectWithMvcConfig = this.mvcConfigGenerator.create(projectWithRepositories, request);
        Project projectWithExceptions = this.exceptionGenerator.create(projectWithMvcConfig, request);
         */
        return this;
    }

    // TODO: Add
    public ProjectBuilder addFrontend() {
        return this;
    }

    // TODO: Add
    public ProjectBuilder addDatabase() {
        return this;
    }

    // TODO: Add
    public ProjectBuilder addDocker() {
        return this;
    }

    public ProjectBuilder addBaseProject(ProjectRequest request) {
        this.currentProject = this.baseProjectGenerator.create(request);
        return this;
    }

    public ProjectBuilder addDataModel(ProjectRequest request) {
        // TODO: Implement
        /*Project projectWithEntites = this.entityGenerator.create(baseProject, request);
        Project projectWithTransferObjects = this.transferObjectGenerator.create(projectWithEntites, request);
        Project projectWithRepositories =  this.repositoryGenerator.create(projectWithTransferObjects, request);
        */
        // TODO: Add logic.
        return this;
    }

}
