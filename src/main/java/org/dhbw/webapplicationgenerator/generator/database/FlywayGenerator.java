package org.dhbw.webapplicationgenerator.generator.database;

import org.dhbw.webapplicationgenerator.model.request.ProjectRequest;
import org.dhbw.webapplicationgenerator.model.response.Project;
import org.springframework.stereotype.Service;

@Service
public class FlywayGenerator implements DatabaseStrategy {

    @Override
    public Project create(Project project, ProjectRequest request) {
        // TODO: Implement
        return project;
    }



}
