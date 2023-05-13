package org.dhbw.webapplicationgenerator.generator.strategies.backend;

import lombok.AllArgsConstructor;
import org.dhbw.webapplicationgenerator.generator.baseproject.ExceptionGenerator;
import org.dhbw.webapplicationgenerator.generator.baseproject.FileFolderGenerator;
import org.dhbw.webapplicationgenerator.generator.baseproject.spring.ApplicationPropertiesGenerator;
import org.dhbw.webapplicationgenerator.generator.baseproject.spring.MainFileGenerator;
import org.dhbw.webapplicationgenerator.generator.baseproject.spring.MainTestFileGenerator;
import org.dhbw.webapplicationgenerator.generator.frontend.WebMvcConfigGenerator;
import org.dhbw.webapplicationgenerator.generator.java.GradleGenerator;
import org.dhbw.webapplicationgenerator.generator.java.JavaBuildToolGenerator;
import org.dhbw.webapplicationgenerator.generator.java.MavenGenerator;
import org.dhbw.webapplicationgenerator.generator.java.entity.EntityGenerator;
import org.dhbw.webapplicationgenerator.generator.java.entity.TransferObjectGenerator;
import org.dhbw.webapplicationgenerator.generator.repository.RepositoryGenerator;
import org.dhbw.webapplicationgenerator.generator.security.SecurityGenerator;
import org.dhbw.webapplicationgenerator.model.request.ProjectRequest;
import org.dhbw.webapplicationgenerator.model.request.backend.BackendData;
import org.dhbw.webapplicationgenerator.model.request.backend.JavaBuildTool;
import org.dhbw.webapplicationgenerator.model.request.backend.SpringBootData;
import org.dhbw.webapplicationgenerator.model.response.Project;
import org.dhbw.webapplicationgenerator.model.response.ProjectDirectory;
import org.dhbw.webapplicationgenerator.webclient.exception.WagException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.function.UnaryOperator;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class SpringBootGenerator extends FileFolderGenerator implements BackendStrategy {

    private final MavenGenerator mavenGenerator;
    private final GradleGenerator gradleGenerator;
    private final ApplicationPropertiesGenerator applicationPropertiesGenerator;
    private final MainFileGenerator mainFileGenerator;
    private final MainTestFileGenerator mainTestFileGenerator;

    private final EntityGenerator entityGenerator;
    private final TransferObjectGenerator transferObjectGenerator;
    private final RepositoryGenerator repositoryGenerator;
    private final WebMvcConfigGenerator mvcConfigGenerator;
    private final ExceptionGenerator exceptionGenerator;
    private final SecurityGenerator securityGenerator;

    @Override
    public Project create(ProjectRequest request, Project project) {

        ProjectDirectory rootDir = (ProjectDirectory) project.getFileStructure();

        // Create Source Folder (JavaBuildToolGenerator)
        SpringBootData springBootData = (SpringBootData) request.getBackend().getData();
        JavaBuildToolGenerator javaBuildToolGenerator = getBuildToolGenerator(springBootData);
        javaBuildToolGenerator.createFolderStructure(request, rootDir);

        // Find main and resources directory for project
        ProjectDirectory mainDir = javaBuildToolGenerator.getMainDirectory(project, springBootData);
        ProjectDirectory mainTestDir = javaBuildToolGenerator.getMainTestDirectory(project, springBootData);
        ProjectDirectory resourcesDir = javaBuildToolGenerator.getResourcesDirectory(project);

        // Create basic files dependent to either the Spring-Framework or the selected Build-Tool
        project = javaBuildToolGenerator.addBuildToolFiles(project, request);
        project = this.applicationPropertiesGenerator.addApplicationProperties(project, resourcesDir);
        project = this.mvcConfigGenerator.create(project, request, mainDir);
        project = this.mainFileGenerator.addMainFile(project, request, mainDir);
        project = this.mainTestFileGenerator.addMainTestFile(project, request, mainTestDir);
        project = this.exceptionGenerator.create(project, request, mainDir);

        // Create domain model
        project = this.entityGenerator.create(project, request, mainDir);
        project = this.transferObjectGenerator.create(project, request, mainDir);
        project = this.repositoryGenerator.create(project, request, mainDir);

        // Create SecurityController, entities and
        project = this.securityGenerator.create(project, request, mainDir);
        // TODO: Add security to its own strategy, even though this mainly depends on the backend.

        return project;

    }

    @Override
    public UnaryOperator<ProjectDirectory> getFrontendDirectoryFinder() {
        return projectDirectory -> {
            List<ProjectDirectory> directories = projectDirectory.getChildren().stream()
                    .filter(dir -> dir.getTitle().equals("src"))
                    .flatMap(dir -> dir.getDirectoryChildren().stream())
                    .filter(dir -> dir.getTitle().equals("main"))
                    .flatMap(dir -> dir.getDirectoryChildren().stream())
                    .filter(dir -> dir.getTitle().equals("resources"))
                    .collect(Collectors.toList());
            if (directories.isEmpty()) {
                throw new WagException("Resources directory not found");
            } else if (directories.size() >= 2) {
                throw new WagException("Multiple resources directories found");
            }
            return directories.get(0);
        };
    }

    @Override
    public UnaryOperator<ProjectDirectory> getMainSourceDirectoryFinder(BackendData data) {
        SpringBootData springBootData = (SpringBootData) data;
        JavaBuildToolGenerator buildToolGenerator = getBuildToolGenerator(springBootData);
        return projectDirectory -> buildToolGenerator.getMainDirectory(projectDirectory, springBootData);
    }

    private JavaBuildToolGenerator getBuildToolGenerator(SpringBootData data) {
        if (data.getJavaBuildTool().equals(JavaBuildTool.MAVEN)) {
            return this.mavenGenerator;
        } else if (data.getJavaBuildTool().equals(JavaBuildTool.GRADLE)){
            return this.gradleGenerator;
        } else {
            throw new WagException("Not supported JavaBuildTool has bee supplied");
        }
    }

}
