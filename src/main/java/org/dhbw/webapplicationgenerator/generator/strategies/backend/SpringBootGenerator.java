package org.dhbw.webapplicationgenerator.generator.strategies.backend;

import lombok.AllArgsConstructor;
import org.dhbw.webapplicationgenerator.generator.baseproject.ExceptionGenerator;
import org.dhbw.webapplicationgenerator.generator.baseproject.FileFolderGenerator;
import org.dhbw.webapplicationgenerator.generator.baseproject.spring.ApplicationPropertiesGenerator;
import org.dhbw.webapplicationgenerator.generator.baseproject.spring.MainFileGenerator;
import org.dhbw.webapplicationgenerator.generator.baseproject.spring.MainTestFileGenerator;
import org.dhbw.webapplicationgenerator.generator.frontend.FrontendControllerGenerator;
import org.dhbw.webapplicationgenerator.generator.frontend.WebMvcConfigGenerator;
import org.dhbw.webapplicationgenerator.generator.java.GradleGenerator;
import org.dhbw.webapplicationgenerator.generator.java.JavaBuildToolGenerator;
import org.dhbw.webapplicationgenerator.generator.java.MavenGenerator;
import org.dhbw.webapplicationgenerator.generator.java.entity.EntityGenerator;
import org.dhbw.webapplicationgenerator.generator.java.entity.TransferObjectGenerator;
import org.dhbw.webapplicationgenerator.generator.repository.RepositoryGenerator;
import org.dhbw.webapplicationgenerator.generator.security.SecurityGenerator;
import org.dhbw.webapplicationgenerator.generator.strategies.GenerationStrategy;
import org.dhbw.webapplicationgenerator.model.request.ProjectRequest;
import org.dhbw.webapplicationgenerator.model.request.backend.JavaBuildTool;
import org.dhbw.webapplicationgenerator.model.request.backend.SpringBootData;
import org.dhbw.webapplicationgenerator.model.response.Project;
import org.dhbw.webapplicationgenerator.model.response.ProjectDirectory;
import org.dhbw.webapplicationgenerator.webclient.exception.WagException;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class SpringBootGenerator extends FileFolderGenerator implements GenerationStrategy {

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
    private final FrontendControllerGenerator frontendControllerGenerator;
    private final SecurityGenerator securityGenerator;

    @Override
    public Project create(ProjectRequest request, Project project) {

        ProjectDirectory rootDir = (ProjectDirectory) project.getFileStructure();

        // Create Source Folder (JavaBuildToolGenerator)
        SpringBootData data = (SpringBootData) request.getBackend().getData();
        JavaBuildToolGenerator javaBuildToolGenerator = getBuildToolGenerator(data);
        javaBuildToolGenerator.createFolderStructure(request, rootDir);

        // Find main and resources directory for project
        ProjectDirectory mainDir = javaBuildToolGenerator.getMainDirectory(project, data);
        ProjectDirectory mainTestDir = javaBuildToolGenerator.getMainTestDirectory(project, data);
        ProjectDirectory resourcesDir = javaBuildToolGenerator.getResourcesDirectory(project);

        // Create basic files dependent to either the Spring-Framework or the selected Build-Tool
        project = javaBuildToolGenerator.addBuildToolFiles(project, request);
        project = this.applicationPropertiesGenerator.addApplicationProperties(project, resourcesDir);
        project = this.mainFileGenerator.addMainFile(project, request, mainDir);
        project = this.mainTestFileGenerator.addMainTestFile(project, request, mainTestDir);

        // Create domain model TODO: Extract this whole step to data model generation potentially?
        project = this.entityGenerator.create(project, request, mainDir);
        project = this.transferObjectGenerator.create(project, request, mainDir);
        project = this.repositoryGenerator.create(project, request, mainDir);
        project = this.mvcConfigGenerator.create(project, request, mainDir);
        project = this.exceptionGenerator.create(project, request, mainDir);
        project = this.frontendControllerGenerator.create(project, request, mainDir);
        project = this.securityGenerator.create(project, request, mainDir);
        // TODO: Only generate Controllers like so if frontend is Thymeleaf. Maybe even move to FrontendGenerator,
        //  even though it is actually part of the Backend?

        return project;

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
