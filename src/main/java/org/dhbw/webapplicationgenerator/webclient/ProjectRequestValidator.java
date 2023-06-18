package org.dhbw.webapplicationgenerator.webclient;

import lombok.AllArgsConstructor;
import org.dhbw.webapplicationgenerator.generator.util.Utils;
import org.dhbw.webapplicationgenerator.model.request.ProjectRequest;
import org.dhbw.webapplicationgenerator.model.request.Strategy;
import org.dhbw.webapplicationgenerator.model.request.backend.SpringBootData;
import org.dhbw.webapplicationgenerator.model.request.frontend.VaadinData;
import org.dhbw.webapplicationgenerator.webclient.exception.ValidationException;
import org.dhbw.webapplicationgenerator.webclient.exception.WagException;
import org.dhbw.webapplicationgenerator.webclient.validation.DataModelValidator;
import org.dhbw.webapplicationgenerator.webclient.validation.DockerValidator;
import org.dhbw.webapplicationgenerator.webclient.validation.Version;
import org.springframework.stereotype.Service;

import java.util.Objects;

@Service
@AllArgsConstructor
public class ProjectRequestValidator {

    private final DockerValidator dockerValidator;
    private final DataModelValidator dataModelValidator;

    /**
     * Performs necessary Validations for a ProjectRequest
     *
     * @param request ProjectRequest
     * @throws ValidationException Exception providing the validation error
     */
    public void validate(ProjectRequest request) throws ValidationException {
        validateProject(request);
        validateDeployment(request);
        validateBackend(request);
        validateFrontend(request);
        validateDatabase();
        this.dataModelValidator.validate(request);
        validateSecurity(request);
        validateRoles();
    }

    private void validateProject(ProjectRequest request) throws ValidationException {
        if (request.getTitle() == null) {
            throw new ValidationException("Project-Title is missing");
        }
    }

    private void validateDeployment(ProjectRequest request) throws ValidationException {
        // If not enabled, then skip validation.
        if (!request.isDeploymentEnabled()) {
            return;
        }

        Strategy strategy = request.getDeployment().getStrategy();
        if (Objects.requireNonNull(strategy) == Strategy.DOCKER) {
            dockerValidator.validate(request);
        } else {
            throwUnknownException("deployment", strategy);
        }
    }

    private void validateBackend(ProjectRequest request) throws ValidationException {
        // If not enabled, then skip validation.
        if (!request.isBackendEnabled()) {
            return;
        }

        Strategy strategy = request.getBackend().getStrategy();
        if (Objects.requireNonNull(strategy) == Strategy.SPRING_BOOT) {
            SpringBootData data = (SpringBootData) request.getBackend().getData();
            if (!parseVersion(data.getSpringBootVersion()).isSmallerThan(new Version(3,0,0))) {
                throw new ValidationException("Only Spring Boot versions < 3.0.0 are supported");
            }
            if (data.getArtifact() == null || data.getArtifact().length() == 0) {
                throw new ValidationException("Artifact is empty");
            }
            if (data.getGroup() == null || data.getGroup().length() == 0) {
                throw new ValidationException("Group is empty");
            }
            if (data.getJavaBuildTool() == null) {
                throw new ValidationException("JavaBuildTool is empty");
            }
        } else {
            throwUnknownException("backend", strategy);
        }
    }

    private void validateFrontend(ProjectRequest request) throws ValidationException {
        if (request.getBackend().getStrategy().equals(Strategy.SPRING_BOOT) && request.getFrontend().getStrategy().equals(Strategy.VAADIN)) {
            VaadinData vaadinData = (VaadinData) request.getFrontend().getData();
            SpringBootData springBootData = (SpringBootData) request.getBackend().getData();
            validateVaadinAndSpringBootVersionCompatibility(springBootData, vaadinData);
        }
    }

    private void validateDatabase() {
        // Functionality not available, hence skipping validation.
    }

    private void validateSecurity(ProjectRequest request) throws ValidationException {
        // Skip validation if security is disabled
        if (request.getSecurity() == null || !request.getSecurity().isEnabled()) {
            return;
        }

        // Password must not be empty.
        if (request.getSecurity().getDefaultPassword().isBlank()) {
            throw new ValidationException("Default-Password must not be empty");
        }

        // Security must only be enabled if Backend and Frontend are both enabled
        if (request.isSecurityEnabled() && (!request.isFrontendEnabled() || !request.isBackendEnabled())) {
            throw new ValidationException("If security is enabled, you have to activate both frontend and backend as well!");
        }
    }

    private void validateRoles() {
        // Currently not in use
        // TODO: Implement?
        // - mentioned entities have to exist
        // - also the functionality for this has yet to be implemented
    }

    private void throwUnknownException(String type, Strategy strategy) throws ValidationException {
        throw new ValidationException("Unknown " + Utils.capitalize(type) + "-Strategy " + strategy + " supplied");
    }

    private void validateVaadinAndSpringBootVersionCompatibility(SpringBootData springBootData, VaadinData vaadinData) throws ValidationException {
        // Vaadin version 23 must use Spring-Boot version >2.6.6 and <3.0.0

        Version vaadinVersion = parseVersion(vaadinData.getVersion());
        Version springBootVersion = parseVersion(springBootData.getSpringBootVersion());

        if (vaadinVersion.isSmallerThan(new Version(23, 0, 0))) {
            throw new ValidationException("Only Vaadin versions >= 23.0.0 and < 24.0.0 are supported");
        }

        if (vaadinVersion.isGreaterThan(new Version(24,0,0))) {
            throw new ValidationException("Only Vaadin versions >= 23.0.0 and < 24.0.0 are supported");
        }

        if (vaadinVersion.isGreaterThan(new Version(23,0,0))
                && springBootVersion.isSmallerThan(new Version(2,6,6))) {
            throw new ValidationException("Using Vaadin version 23, you must use Spring Boot version >= 2.6.6 and < 3.0.0");
        }

        if (vaadinVersion.isGreaterThan(new Version(23,0,0))
                && !springBootVersion.isSmallerThan(new Version(3,0,0))) {
            throw new ValidationException("Using Vaadin version 23, you must use Spring Boot version >= 2.6.6 and < 3.0.0");
        }
    }

    private Version parseVersion(String versionString) {
        String[] versionParts = versionString.split("\\.");
        Version version = new Version();
        int index = 1;
        for (String versionPart : versionParts) {
            switch(index) {
                case 1:
                    version.setFirstPart(Integer.valueOf(versionPart));
                    break;
                case 2:
                    version.setSecondPart(Integer.valueOf(versionPart));
                    break;
                case 3:
                    version.setThirdPart(Integer.valueOf(versionPart));
                    break;
                default:
                    throw new WagException("Version with more than 3 parts has been supplied");
            }
            index++;
        }
        return version;
    }

}
