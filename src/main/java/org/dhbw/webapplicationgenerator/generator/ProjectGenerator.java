package org.dhbw.webapplicationgenerator.generator;

import lombok.AllArgsConstructor;
import org.dhbw.webapplicationgenerator.generator.entity.EntityGenerator;
import org.dhbw.webapplicationgenerator.generator.frontend.FrontendGenerator;
import org.dhbw.webapplicationgenerator.generator.frontend.WebMvcConfigGenerator;
import org.dhbw.webapplicationgenerator.generator.repository.RepositoryGenerator;
import org.dhbw.webapplicationgenerator.webclient.request.ProjectRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class ProjectGenerator {

    private final Logger logger = LoggerFactory.getLogger(ProjectGenerator.class);

    private final BaseProjectGenerator baseProjectGenerator;
    private final EntityGenerator entityGenerator;
    private final RepositoryGenerator repositoryGenerator;
    private final WebMvcConfigGenerator mvcConfigGenerator;
    private final FrontendGenerator frontendGenerator;

    /**
     * Generates the Project based on the provided request
     * @param request Request for creating the project
     * @return Project based on the provided request
     */
    public Project generate(ProjectRequest request) {
        logger.info("Generating new project with title {}", request.getTitle());
        Project baseProject = this.baseProjectGenerator.create(request);
        Project projectWithEntites = this.entityGenerator.create(baseProject, request);
        Project projectWithRepositories =  this.repositoryGenerator.create(projectWithEntites, request);
        Project projectWithMvcConfig = this.mvcConfigGenerator.create(projectWithRepositories, request);
        return this.frontendGenerator.create(projectWithMvcConfig, request);
    }

}
