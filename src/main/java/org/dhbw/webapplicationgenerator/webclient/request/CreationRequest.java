package org.dhbw.webapplicationgenerator.webclient.request;

import lombok.Data;

import java.util.HashSet;
import java.util.Set;

@Data
public class CreationRequest {
    private CreationRequestProject project;
    private CreationRequestDocker docker;
    private Set<RequestEntity> entities = new HashSet<>();

    /**
     * Deployment is enabled and will be created during build-step of project build.
     * @return true = enabled / false = disabled
     */
    public boolean isDeploymentEnabled() {
        return docker != null && docker.isEnabled();
        // TODO: Rename docker in request to deployment and then specifiy strategy to Docker.
    }
}
