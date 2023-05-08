package org.dhbw.webapplicationgenerator.generator.strategies.database;

import org.dhbw.webapplicationgenerator.generator.strategies.GenerationStrategy;
import org.dhbw.webapplicationgenerator.model.request.ProjectRequest;
import org.dhbw.webapplicationgenerator.model.response.Project;
import org.springframework.stereotype.Service;

@Service
public class FlywayGenerator implements GenerationStrategy {

    @Override
    public Project create(ProjectRequest request, Project project) {
        return null;
    }



}
