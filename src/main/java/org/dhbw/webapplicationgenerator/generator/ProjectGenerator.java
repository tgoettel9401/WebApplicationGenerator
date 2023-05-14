package org.dhbw.webapplicationgenerator.generator;

import lombok.AllArgsConstructor;
import org.dhbw.webapplicationgenerator.generator.backend.BackendStrategy;
import org.dhbw.webapplicationgenerator.generator.backend.spring.SpringBootGenerator;
import org.dhbw.webapplicationgenerator.generator.baseproject.BaseProjectGenerator;
import org.dhbw.webapplicationgenerator.generator.database.DatabaseStrategy;
import org.dhbw.webapplicationgenerator.generator.database.FlywayGenerator;
import org.dhbw.webapplicationgenerator.generator.deployment.DeploymentStrategy;
import org.dhbw.webapplicationgenerator.generator.deployment.docker.DockerGenerator;
import org.dhbw.webapplicationgenerator.generator.frontend.FrontendStrategy;
import org.dhbw.webapplicationgenerator.generator.frontend.thymeleaf.ThymeleafGenerator;
import org.dhbw.webapplicationgenerator.generator.frontend.vaadin.VaadinGenerator;
import org.dhbw.webapplicationgenerator.generator.security.SecurityGenerator;
import org.dhbw.webapplicationgenerator.generator.util.FileFolderGenerator;
import org.dhbw.webapplicationgenerator.model.request.ProjectRequest;
import org.dhbw.webapplicationgenerator.model.request.backend.BackendData;
import org.dhbw.webapplicationgenerator.model.response.Project;
import org.dhbw.webapplicationgenerator.model.response.ProjectDirectory;
import org.dhbw.webapplicationgenerator.util.FileCleaner;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.function.UnaryOperator;

@Service
@AllArgsConstructor
public class ProjectGenerator extends FileFolderGenerator {

    private final Logger logger = LoggerFactory.getLogger(ProjectGenerator.class);

    private final BaseProjectGenerator baseProjectGenerator;
    private final SecurityGenerator securityGenerator;
    private final FileCleaner fileCleaner;

    // Backend Strategies
    private final SpringBootGenerator springBootGenerator;

    // Frontend Strategies
    private final ThymeleafGenerator thymeleafGenerator;
    private VaadinGenerator vaadinGenerator;

    // Deployment Strategies
    private final DockerGenerator dockerGenerator;

    // Database Strategies
    private final FlywayGenerator flywayGenerator;

    /**
     * Generates the Project based on the provided request
     * @param request Request for creating the project
     * @return Project based on the provided request
     */
    public Project generate(ProjectRequest request) {

        // Create temp-folders
        logger.info("Preparing temp-folders");
        prepareTmpFolders();

        logger.info("Extracting data and converting to BackendData");
        BackendData data = (BackendData) request.getBackend().getData();

        // Assign strategies.
        logger.info("Assigning strategies");
        BackendStrategy backendStrategy = this.springBootGenerator; // TODO: Set generator according to request

        FrontendStrategy frontendStrategy;
        switch(request.getFrontend().getStrategy()) {
            case THYMELEAF:
                frontendStrategy = this.thymeleafGenerator;
                break;
            case VAADIN:
                frontendStrategy = this.vaadinGenerator;
                break;
            default:
                frontendStrategy = null;
        }
        DatabaseStrategy databaseStrategy = this.flywayGenerator;
        DeploymentStrategy deploymentStrategy = this.dockerGenerator;
        UnaryOperator<ProjectDirectory> frontendDirectoryFinder = backendStrategy.getFrontendDirectoryFinder();
        UnaryOperator<ProjectDirectory> mainSourceDirectoryFinder = backendStrategy.getMainSourceDirectoryFinder(data);

        // Create Builder and build project
        logger.info("Preparing the projectBuilder");
        ProjectBuilder builder = new ProjectBuilder()
                .baseProjectStrategy(baseProjectGenerator)
                .backendStrategy(backendStrategy)
                .frontendStrategy(frontendStrategy)
                .deploymentStrategy(deploymentStrategy)
                .databaseStrategy(databaseStrategy)
                .securityGenerator(securityGenerator)
                .frontendDirectoryFinder(frontendDirectoryFinder)
                .mainSourceDirectoryFinder(mainSourceDirectoryFinder);

        // Build the project
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
