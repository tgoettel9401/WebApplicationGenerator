package org.dhbw.webapplicationgenerator.webclient;

import lombok.AllArgsConstructor;
import org.dhbw.webapplicationgenerator.generator.util.Utils;
import org.dhbw.webapplicationgenerator.model.request.ProjectRequest;
import org.dhbw.webapplicationgenerator.model.request.Strategy;
import org.dhbw.webapplicationgenerator.model.request.backend.SpringBootData;
import org.dhbw.webapplicationgenerator.webclient.exception.ValidationException;
import org.dhbw.webapplicationgenerator.webclient.validation.DataModelValidator;
import org.dhbw.webapplicationgenerator.webclient.validation.DockerValidator;
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
        validateDatabase(request);
        this.dataModelValidator.validate(request);
        validateRoles(request);
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

    private void validateFrontend() {
        // Currently frontend data is empty, hence nothing needs to be validated here.
    }

    private void validateDatabase() {
        // Functionality not available, hence skipping validation.
    }

    private void validateRoles() {
        // Currently not in use
        // TODO: Implement?
        // - mentioned entities have to exist
    }

    private void throwUnknownException(String type, Strategy strategy) throws ValidationException {
        throw new ValidationException("Unknown " + Utils.capitalize(type) + "-Strategy " + strategy + " supplied");
    }

}
