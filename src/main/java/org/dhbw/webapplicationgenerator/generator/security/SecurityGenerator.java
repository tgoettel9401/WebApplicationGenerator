package org.dhbw.webapplicationgenerator.generator.security;

import lombok.AllArgsConstructor;
import org.dhbw.webapplicationgenerator.generator.security.backend.java.UserEntityGenerator;
import org.dhbw.webapplicationgenerator.generator.security.backend.java.spring.*;
import org.dhbw.webapplicationgenerator.generator.security.frontend.html.SecurityPagesGenerator;
import org.dhbw.webapplicationgenerator.generator.util.FileFolderGenerator;
import org.dhbw.webapplicationgenerator.model.request.ProjectRequest;
import org.dhbw.webapplicationgenerator.model.response.Project;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class SecurityGenerator extends FileFolderGenerator {

    private final WebSecurityConfigGenerator webSecurityConfigGenerator;
    private final UserControllerGenerator userControllerGenerator;
    private final RegistrationRequestGenerator registrationRequestGenerator;
    private final UserServiceGenerator userServiceGenerator;
    private final UserDataInitializationGenerator userDataInitializationGenerator;
    private final RoleEntityGenerator roleEntityGenerator;
    private final RoleRepositoryGenerator roleRepositoryGenerator;
    private final UserEntityGenerator userEntityGenerator;
    private final UserRepositoryGenerator userRepositoryGenerator;
    private final SecurityPagesGenerator securityPagesGenerator;


    /**
     * Creates the Project with Security integrated. This consists of a WebSecurityConfig, a UserDataInitializer,
     * a UserController (including RegistrationRequest), a UserEntity, as well as Thymeleaf Pages for Login and Register.
     * @param request Request containing the relevant information of the project to be created
     * @return Project with Security included
     */
    public Project create(Project project, ProjectRequest request) {
        if (request.isBackendEnabled()) {
            project = addSpringBackendSecurity(project, request);
        }
        if (request.isFrontendEnabled()) {
            project = addHtmlFrontendSecurity(project, request);
        }
        return project;
    }

    private Project addSpringBackendSecurity(Project project, ProjectRequest request) {
        project = webSecurityConfigGenerator.add(project, request);
        project = registrationRequestGenerator.add(project, request);
        project = userServiceGenerator.add(project, request);
        project = userDataInitializationGenerator.add(project, request);
        project = roleEntityGenerator.add(project,request);
        project = roleRepositoryGenerator.add(project,request);
        project = userEntityGenerator.add(project,request);
        project = userRepositoryGenerator.add(project,request);
        return project;
    }

    private Project addHtmlFrontendSecurity(Project project, ProjectRequest request) {
        // TODO: Handle multiple frontend strategies
        project = userControllerGenerator.add(project, request);
        project = securityPagesGenerator.add(project);
        return project;
    }

}
