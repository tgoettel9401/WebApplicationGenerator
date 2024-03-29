package org.dhbw.webapplicationgenerator.generator.backend.spring;

import lombok.AllArgsConstructor;
import org.dhbw.webapplicationgenerator.generator.backend.BackendStrategy;
import org.dhbw.webapplicationgenerator.generator.backend.java.buildtool.GradleGenerator;
import org.dhbw.webapplicationgenerator.generator.backend.java.buildtool.JavaBuildToolGenerator;
import org.dhbw.webapplicationgenerator.generator.backend.java.buildtool.MavenGenerator;
import org.dhbw.webapplicationgenerator.generator.backend.java.datamodel.EntityGenerator;
import org.dhbw.webapplicationgenerator.generator.backend.java.datamodel.TransferObjectGenerator;
import org.dhbw.webapplicationgenerator.generator.backend.java.other.ExceptionGenerator;
import org.dhbw.webapplicationgenerator.generator.baseproject.spring.ApplicationPropertiesGenerator;
import org.dhbw.webapplicationgenerator.generator.baseproject.spring.MainFileGenerator;
import org.dhbw.webapplicationgenerator.generator.baseproject.spring.MainTestFileGenerator;
import org.dhbw.webapplicationgenerator.generator.util.FileFolderGenerator;
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

    @Override
    public Project create(Project project, ProjectRequest request) {

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
        project = this.applicationPropertiesGenerator.addApplicationProperties(project, request, resourcesDir);
        project = this.mvcConfigGenerator.create(project, request, mainDir);
        project = this.mainFileGenerator.addMainFile(project, request, mainDir);
        project = this.mainTestFileGenerator.addMainTestFile(project, request, mainTestDir);
        project = this.exceptionGenerator.create(project, request, mainDir);

        // Create domain model
        project = this.entityGenerator.create(project, request, mainDir);
        project = this.transferObjectGenerator.create(project, request, mainDir);
        project = this.repositoryGenerator.create(project, request, mainDir);

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
