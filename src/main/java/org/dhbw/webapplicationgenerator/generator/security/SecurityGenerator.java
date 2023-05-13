package org.dhbw.webapplicationgenerator.generator.security;

import lombok.AllArgsConstructor;
import org.dhbw.webapplicationgenerator.generator.baseproject.FileFolderGenerator;
import org.dhbw.webapplicationgenerator.generator.thymeleaf.SecurityPagesGenerator;
import org.dhbw.webapplicationgenerator.model.request.ProjectRequest;
import org.dhbw.webapplicationgenerator.model.response.Project;
import org.dhbw.webapplicationgenerator.model.response.ProjectDirectory;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class SecurityGenerator extends FileFolderGenerator {

    private final WebSecurityConfigGenerator webSecurityConfigGenerator;
    private final UserDataInitializationGenerator userDataInitializationGenerator;
    private final UserControllerGenerator userControllerGenerator;
    private final SecurityEntitiesGenerator securityEntitiesGenerator;
    private final SecurityPagesGenerator securityPagesGenerator;


    /**
     * Creates the Project with Security integrated. This consists of a WebSecurityConfig, a UserDataInitializer,
     * a UserController (including RegistrationRequest), a UserEntity, as well as Thymeleaf Pages for Login and Register.
     * @param request Request containing the relevant information of the project to be created
     * @return Project with Security included
     */
    public Project create(Project project, ProjectRequest request, ProjectDirectory parent) {
        Project projectWithWebSecurityConfig = webSecurityConfigGenerator.create(project, request, parent);
        Project projectWithUserDataInitializer = userDataInitializationGenerator.create(projectWithWebSecurityConfig, request, parent);
        Project projectWithUserController = userControllerGenerator.create(projectWithUserDataInitializer, request);
        return securityEntitiesGenerator.create(projectWithUserController, request, parent);
    }

}
