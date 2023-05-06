package org.dhbw.webapplicationgenerator.generator.security;

import lombok.AllArgsConstructor;
import org.dhbw.webapplicationgenerator.model.response.Project;
import org.dhbw.webapplicationgenerator.generator.baseproject.FileFolderGenerator;
import org.dhbw.webapplicationgenerator.webclient.request.CreationRequest;
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
    public Project create(Project project, CreationRequest request) {
        Project projectWithWebSecurityConfig = webSecurityConfigGenerator.create(project, request);
        Project projectWithUserDataInitializer = userDataInitializationGenerator.create(projectWithWebSecurityConfig, request);
        Project projectWithUserController = userControllerGenerator.create(projectWithUserDataInitializer, request);
        Project projectWithSecurityEntities = securityEntitiesGenerator.create(projectWithUserController, request);
        return securityPagesGenerator.create(projectWithSecurityEntities);
    }

}
