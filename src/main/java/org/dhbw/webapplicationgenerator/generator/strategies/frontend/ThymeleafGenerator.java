package org.dhbw.webapplicationgenerator.generator.strategies.frontend;

import org.dhbw.webapplicationgenerator.generator.thymeleaf.FrontendGenerator;
import org.dhbw.webapplicationgenerator.generator.thymeleaf.SecurityPagesGenerator;
import org.dhbw.webapplicationgenerator.model.request.ProjectRequest;
import org.dhbw.webapplicationgenerator.model.response.Project;
import org.dhbw.webapplicationgenerator.model.response.ProjectDirectory;
import org.dhbw.webapplicationgenerator.webclient.exception.WagException;
import org.springframework.stereotype.Service;

import java.util.function.Function;
import java.util.function.UnaryOperator;

@Service
public class ThymeleafGenerator implements FrontendStrategy {

    private final FrontendGenerator frontendGenerator;
    private final SecurityPagesGenerator securityPagesGenerator;

    private Function<ProjectDirectory, ProjectDirectory> frontendDirectoryFinder;

    public ThymeleafGenerator(FrontendGenerator frontendGenerator, SecurityPagesGenerator securityPagesGenerator) {
        this.frontendGenerator = frontendGenerator;
        this.securityPagesGenerator = securityPagesGenerator;
    }

    @Override
    public Project create(ProjectRequest request, Project project) {
        if (frontendDirectoryFinder == null) {
            throw new WagException("Used Thymeleaf Generator without setting the frontendDirectoryFinder before!");
        }
        ProjectDirectory frontendDirectory = frontendDirectoryFinder.apply((ProjectDirectory) project.getFileStructure());
        project = this.frontendGenerator.create(project, request, frontendDirectory);
        project = this.securityPagesGenerator.create(project);
        return project;
    }

    @Override
    public Function<ProjectDirectory, ProjectDirectory> getFrontendDirectoryFinder() {
        return this.frontendDirectoryFinder;
    }

    @Override
    public void setFrontendDirectoryFinder(UnaryOperator<ProjectDirectory> finder) {
        this.frontendDirectoryFinder = finder;
    }
}
