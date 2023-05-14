package org.dhbw.webapplicationgenerator.generator.frontend.vaadin;

import lombok.AllArgsConstructor;
import org.dhbw.webapplicationgenerator.generator.frontend.FrontendStrategy;
import org.dhbw.webapplicationgenerator.generator.util.FileFolderGenerator;
import org.dhbw.webapplicationgenerator.model.request.ProjectRequest;
import org.dhbw.webapplicationgenerator.model.response.Project;
import org.dhbw.webapplicationgenerator.model.response.ProjectDirectory;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.function.UnaryOperator;

@Service
@AllArgsConstructor
public class VaadinGenerator extends FileFolderGenerator implements FrontendStrategy {

    private final VaadinEntityOverviewGenerator vaadinEntityOverviewGenerator;
    private final VaadinEntityDetailsGenerator vaadinEntityDetailsGenerator;
    private final VaadinMainViewGenerator vaadinMainViewGenerator;
    private final VaadinNavigationGenerator vaadinNavigationGenerator;

    /**
     * Adds the frontend-files for Vaadin
     * @param request Request to create the project with.
     * @param project CurrentProject that will be updated during this process.
     * @param frontendDirectoryFinder function to find the frontend directory.
     * @param mainSourceDirectoryFinder function to find the main source directory.
     * @return Updated Project
     */
    // TODO: Refactor to remove directoryFinders.
    public Project create(ProjectRequest request, Project project,
                          UnaryOperator<ProjectDirectory> frontendDirectoryFinder,
                          UnaryOperator<ProjectDirectory> mainSourceDirectoryFinder) {
        ProjectDirectory mainSourceDirectory = mainSourceDirectoryFinder
                .apply((ProjectDirectory) project.getFileStructure());
        ProjectDirectory frontendDirectory = addDirectory("frontend", Optional.of(mainSourceDirectory));
        project = vaadinEntityOverviewGenerator.add(request, project, frontendDirectory);
        project = vaadinEntityDetailsGenerator.add(request, project, frontendDirectory);
        project = vaadinMainViewGenerator.add(request, project, frontendDirectory);
        project = vaadinNavigationGenerator.add(request, project, frontendDirectory);
        return project;
    }

}
