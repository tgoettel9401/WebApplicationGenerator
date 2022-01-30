package org.dhbw.webapplicationgenerator.generator;

import lombok.AllArgsConstructor;
import org.dhbw.webapplicationgenerator.webclient.ProjectRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class ProjectGenerator {

    private final Logger logger = LoggerFactory.getLogger(ProjectGenerator.class);

    private final BaseProjectGenerator baseProjectGenerator;

    /**
     * Generates the Project based on the provided request
     * @param request Request for creating the project
     * @return Project based on the provided request
     */
    public Project generate(ProjectRequest request) {
        logger.info("Generating new project with title {}", request.getTitle());
        return this.baseProjectGenerator.create(request);
    }

}
