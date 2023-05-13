package org.dhbw.webapplicationgenerator.generator;

import lombok.AllArgsConstructor;
import org.dhbw.webapplicationgenerator.generator.baseproject.ExceptionGenerator;
import org.dhbw.webapplicationgenerator.generator.baseproject.FileFolderGenerator;
import org.dhbw.webapplicationgenerator.generator.java.entity.EntityGenerator;
import org.dhbw.webapplicationgenerator.generator.java.entity.TransferObjectGenerator;
import org.dhbw.webapplicationgenerator.generator.frontend.FrontendControllerGenerator;
import org.dhbw.webapplicationgenerator.generator.thymeleaf.FrontendGenerator;
import org.dhbw.webapplicationgenerator.generator.frontend.WebMvcConfigGenerator;
import org.dhbw.webapplicationgenerator.generator.repository.RepositoryGenerator;
import org.dhbw.webapplicationgenerator.generator.security.SecurityGenerator;
import org.dhbw.webapplicationgenerator.generator.strategies.GenerationStrategy;
import org.dhbw.webapplicationgenerator.generator.strategies.backend.BackendStrategy;
import org.dhbw.webapplicationgenerator.generator.strategies.deployment.DockerGenerator;
import org.dhbw.webapplicationgenerator.generator.strategies.database.FlywayGenerator;
import org.dhbw.webapplicationgenerator.generator.strategies.backend.SpringBootGenerator;
import org.dhbw.webapplicationgenerator.generator.strategies.frontend.FrontendStrategy;
import org.dhbw.webapplicationgenerator.generator.strategies.frontend.ThymeleafGenerator;
import org.dhbw.webapplicationgenerator.model.request.ProjectRequest;
import org.dhbw.webapplicationgenerator.model.response.Project;
import org.dhbw.webapplicationgenerator.model.response.ProjectDirectory;
import org.dhbw.webapplicationgenerator.util.FileCleaner;
import org.dhbw.webapplicationgenerator.webclient.request.CreationRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.function.UnaryOperator;

@Service
@AllArgsConstructor
public class ProjectGenerator extends FileFolderGenerator {

    private final Logger logger = LoggerFactory.getLogger(ProjectGenerator.class);

    private final BaseProjectGenerator baseProjectGenerator;

    // Backend Strategies
    private final SpringBootGenerator springBootGenerator;

    // Frontend Strategies
    private final ThymeleafGenerator thymeleafGenerator;

    // Deployment Strategies
    private final DockerGenerator dockerGenerator;

    // Database Strategies
    private final FlywayGenerator flywayGenerator;

    private final EntityGenerator entityGenerator;
    private final TransferObjectGenerator transferObjectGenerator;
    private final RepositoryGenerator repositoryGenerator;
    private final WebMvcConfigGenerator mvcConfigGenerator;
    private final ExceptionGenerator exceptionGenerator;
    private final FrontendControllerGenerator frontendControllerGenerator;
    private final FrontendGenerator frontendGenerator;
    private final SecurityGenerator securityGenerator;
    private final FileCleaner fileCleaner;

    /**
     * Generates the Project based on the provided request
     * @param request Request for creating the project
     * @return Project based on the provided request
     */
    public Project generateOld(CreationRequest request) {

        // Create temp-folders
        prepareTmpFolders();

        logger.info("Generating new project with title {}", request.getProject().getTitle());
        fileCleaner.performCleanup(); // In case of previous errors, we perform a cleanup to be safe.
        Project baseProject = this.baseProjectGenerator.createOld(request);
        Project projectWithEntites = this.entityGenerator.createOld(baseProject, request);
        Project projectWithTransferObjects = this.transferObjectGenerator.create(projectWithEntites, request);
        Project projectWithRepositories =  this.repositoryGenerator.create(projectWithTransferObjects, request);
        Project projectWithMvcConfig = this.mvcConfigGenerator.create(projectWithRepositories, request);
        Project projectWithExceptions = this.exceptionGenerator.create(projectWithMvcConfig, request);
        Project projectWithFrontendController = this.frontendControllerGenerator.create(projectWithExceptions, request);
        Project projectWithFrontend = this.frontendGenerator.createOld(projectWithFrontendController, request);
        return this.securityGenerator.create(projectWithFrontend, request);
    }

    /**
     * Generates the Project based on the provided request
     * @param request Request for creating the project
     * @return Project based on the provided request
     */
    public Project generate(ProjectRequest request) {

        // Create temp-folders
        logger.info("Preparing temp-folders");
        prepareTmpFolders();

        // Assign strategies.
        logger.info("Assigning strategies");
        BackendStrategy backendStrategy = this.springBootGenerator;
        FrontendStrategy frontendStrategy = this.thymeleafGenerator;
        GenerationStrategy databaseStrategy = this.flywayGenerator;
        GenerationStrategy deploymentStrategy = this.dockerGenerator;

        logger.info("Setting frontendDirectoryFinder according to the BackendStrategy");
        UnaryOperator<ProjectDirectory> frontendDirectoryFinder = backendStrategy.getFrontendDirectoryFinder();
        frontendStrategy.setFrontendDirectoryFinder(frontendDirectoryFinder);

        // Create Builder and build project
        logger.info("Preparing the projectBuilder");
        ProjectBuilder builder = new ProjectBuilder()
                .baseProjectStrategy(baseProjectGenerator)
                .backendStrategy(backendStrategy)
                .frontendStrategy(frontendStrategy)
                .deploymentStrategy(deploymentStrategy)
                .databaseStrategy(databaseStrategy);
        logger.info("Generating new project with title {}", request.getTitle());
        Project project = builder.build(request);
        logger.info("Constructing of project done");

        return project;
    }

    /**
     * Prepares the Temp-Folders ./tmp and ./tmp2.
     */
    private void prepareTmpFolders() {
        // In case of previous errors, we perform a cleanup to be safe.
        fileCleaner.performCleanup();

        // Next we create the temp-folders from scratch.
        createTmpFolderIfNotExists();
        createTmp2FolderIfNotExists();
    }

}
