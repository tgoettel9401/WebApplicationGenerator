package org.dhbw.webapplicationgenerator.generator.util;

import org.dhbw.webapplicationgenerator.webclient.request.ProjectRequest;
import org.springframework.stereotype.Service;

@Service
public class PackageNameResolver {

    public String resolveEntity(ProjectRequest request) {
        return basePackage(request) + ".domain";
    }

    public String resolveRepository(ProjectRequest request) {
        return basePackage(request) + ".repository";
    }

    public String resolveController(ProjectRequest request) {
        return basePackage(request) + ".controller";
    }

    public String resolveConfig(ProjectRequest request) {
        return basePackage(request) + ".config";
    }

    public String resolveException(ProjectRequest request) {
        return basePackage(request) + ".exception";
    }

    private String basePackage(ProjectRequest request) {
        return request.getGroup() + "." + request.getArtifact();
    }

}
