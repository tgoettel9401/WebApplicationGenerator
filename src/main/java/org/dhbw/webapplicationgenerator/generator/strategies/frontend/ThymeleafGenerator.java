package org.dhbw.webapplicationgenerator.generator.strategies.frontend;

import org.dhbw.webapplicationgenerator.generator.thymeleaf.FrontendControllerGenerator;
import org.dhbw.webapplicationgenerator.generator.thymeleaf.FrontendGenerator;
import org.dhbw.webapplicationgenerator.generator.thymeleaf.SecurityPagesGenerator;
import org.dhbw.webapplicationgenerator.model.request.ProjectRequest;
import org.dhbw.webapplicationgenerator.model.response.Project;
import org.dhbw.webapplicationgenerator.model.response.ProjectDirectory;
import org.dhbw.webapplicationgenerator.webclient.exception.WagException;
import org.springframework.stereotype.Service;

import java.util.function.UnaryOperator;

@Service
public class ThymeleafGenerator implements FrontendStrategy {

    private final FrontendGenerator frontendGenerator;
    private final SecurityPagesGenerator securityPagesGenerator;
    private final FrontendControllerGenerator frontendControllerGenerator;

    public ThymeleafGenerator(FrontendGenerator frontendGenerator,
                              SecurityPagesGenerator securityPagesGenerator,
                              FrontendControllerGenerator frontendControllerGenerator) {
        this.frontendGenerator = frontendGenerator;
        this.securityPagesGenerator = securityPagesGenerator;
        this.frontendControllerGenerator = frontendControllerGenerator;
    }

    @Override
    public Project create(ProjectRequest request, Project project, UnaryOperator<ProjectDirectory> frontendDirectoryFinder,
                          UnaryOperator<ProjectDirectory> mainSourceFolderFinder) {

        // Throw exception if a mandatory finder-function has not been set before
        validateFindersArePresent(frontendDirectoryFinder, mainSourceFolderFinder);

        // Extract needed directories
        ProjectDirectory rootDirectory = (ProjectDirectory) project.getFileStructure();
        ProjectDirectory frontendDirectory = frontendDirectoryFinder.apply(rootDirectory);
        ProjectDirectory mainSourceDirectory = mainSourceFolderFinder.apply(rootDirectory);

        // Create files and finally return updated project.
        project = this.frontendGenerator.create(project, request, frontendDirectory);
        project = this.frontendControllerGenerator.create(project, request, mainSourceDirectory);
        project = this.securityPagesGenerator.create(project);
        return project;
    }

    private void validateFindersArePresent(UnaryOperator<ProjectDirectory> frontendDirectoryFinder,
                                           UnaryOperator<ProjectDirectory> mainSourceFolderFinder) {
        if (frontendDirectoryFinder == null || mainSourceFolderFinder == null) {
            throw new WagException("Used Thymeleaf Generator without setting the frontendDirectoryFinder or " +
                    "mainSourceFolderFinder before!");
        }
    }

}
