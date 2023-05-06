package org.dhbw.webapplicationgenerator.generator;

import lombok.AllArgsConstructor;
import org.dhbw.webapplicationgenerator.generator.baseproject.ExceptionGenerator;
import org.dhbw.webapplicationgenerator.generator.baseproject.FileFolderGenerator;
import org.dhbw.webapplicationgenerator.generator.entity.EntityGenerator;
import org.dhbw.webapplicationgenerator.generator.entity.TransferObjectGenerator;
import org.dhbw.webapplicationgenerator.generator.frontend.FrontendControllerGenerator;
import org.dhbw.webapplicationgenerator.generator.frontend.FrontendGenerator;
import org.dhbw.webapplicationgenerator.generator.frontend.WebMvcConfigGenerator;
import org.dhbw.webapplicationgenerator.generator.repository.RepositoryGenerator;
import org.dhbw.webapplicationgenerator.generator.security.SecurityGenerator;
import org.dhbw.webapplicationgenerator.model.request.ProjectRequest;
import org.dhbw.webapplicationgenerator.model.response.Project;
import org.dhbw.webapplicationgenerator.util.FileCleaner;
import org.dhbw.webapplicationgenerator.webclient.request.CreationRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class ProjectGenerator extends FileFolderGenerator {

    private final Logger logger = LoggerFactory.getLogger(ProjectGenerator.class);

    private final BaseProjectGenerator baseProjectGenerator;
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
        Project projectWithEntites = this.entityGenerator.create(baseProject, request);
        Project projectWithTransferObjects = this.transferObjectGenerator.create(projectWithEntites, request);
        Project projectWithRepositories =  this.repositoryGenerator.create(projectWithTransferObjects, request);
        Project projectWithMvcConfig = this.mvcConfigGenerator.create(projectWithRepositories, request);
        Project projectWithExceptions = this.exceptionGenerator.create(projectWithMvcConfig, request);
        Project projectWithFrontendController = this.frontendControllerGenerator.create(projectWithExceptions, request);
        Project projectWithFrontend = this.frontendGenerator.create(projectWithFrontendController, request);
        return this.securityGenerator.create(projectWithFrontend, request);
    }

    /**
     * Generates the Project based on the provided request
     * @param request Request for creating the project
     * @return Project based on the provided request
     */
    public Project generate(ProjectRequest request) {

        // Create temp-folders
        prepareTmpFolders();

        // Create project builder and provide the needed necessary generators.
        ProjectBuilder builder = new ProjectBuilder(baseProjectGenerator);

        logger.info("Generating new project with title {}", request.getTitle());

        return builder
                .addBaseProject(request)
                .addDataModel(request)
                .addFrontend()
                .build();

        // TODO: Properly implement, also check if the step is enabled or not.
        /*
        Project projectWithFrontendController = this.frontendControllerGenerator.create(projectWithExceptions, request);
        Project projectWithFrontend = this.frontendGenerator.create(projectWithFrontendController, request);
        return this.securityGenerator.create(projectWithFrontend, request);*/
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
