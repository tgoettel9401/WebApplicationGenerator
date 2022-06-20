package org.dhbw.webapplicationgenerator.generator.util;

import org.dhbw.webapplicationgenerator.webclient.request.CreationRequest;
import org.springframework.stereotype.Service;

@Service
public class PackageNameResolver {

    public String resolveEntity(CreationRequest request) {
        return basePackage(request) + ".domain";
    }

    public String resolveTransferObjects(CreationRequest request) {
        return basePackage(request) + ".transferObjects";
    }

    public String resolveRepository(CreationRequest request) {
        return basePackage(request) + ".repository";
    }

    public String resolveController(CreationRequest request) {
        return basePackage(request) + ".controller";
    }

    public String resolveConfig(CreationRequest request) {
        return basePackage(request) + ".config";
    }

    public String resolveException(CreationRequest request) {
        return basePackage(request) + ".exception";
    }

    private String basePackage(CreationRequest request) {
        return request.getProject().getGroup() + "." + request.getProject().getArtifact();
    }

}
