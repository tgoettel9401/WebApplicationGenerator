package org.dhbw.webapplicationgenerator.webclient.validation;

import org.dhbw.webapplicationgenerator.model.request.ProjectRequest;
import org.dhbw.webapplicationgenerator.model.request.Strategy;
import org.dhbw.webapplicationgenerator.model.request.deployment.DockerData;
import org.dhbw.webapplicationgenerator.webclient.exception.ValidationException;
import org.springframework.stereotype.Service;

@Service
public class DockerValidator implements RequestValidator{

    /**
     * Validates the docker request
     * - imageName must be lowercase
     */
    public void validate(ProjectRequest request) throws ValidationException {
        Strategy strategy = request.getDeployment().getStrategy();
        if (request.isDeploymentEnabled() && strategy.equals(.equals(Strategy.DOCKER)) {
            DockerData data = (DockerData) request.getDeployment().getData();
            for (char letter : data.getImageName().toCharArray()) {
                if (Character.isUpperCase(letter)) {
                    throw new ValidationException("The provided imageName " + data.getImageName() +
                            "of docker-data has an uppercase letter but must only contain lowercase letters!");
                }
            }
        }
    }
}
