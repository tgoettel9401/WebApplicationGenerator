package org.dhbw.webapplicationgenerator.generator.util;

import org.dhbw.webapplicationgenerator.model.request.ProjectRequest;
import org.dhbw.webapplicationgenerator.model.request.backend.JavaData;
import org.springframework.stereotype.Service;

@Service
public class PackageNameResolver {

    // TODO: Add to Java-part because only relevant for Java.

    public String resolveEntity(ProjectRequest request) {
        return basePackage(request) + ".domain";
    }

    public String resolveJavaFrontend(ProjectRequest request) {
        return basePackage(request) + ".frontend";
    }

    public String resolveTransferObjects(ProjectRequest request) {
        return basePackage(request) + ".transferObject";
    }

    public String resolveRepository(ProjectRequest request) {
        return basePackage(request) + ".repository";
    }

    public String resolveController(ProjectRequest request) {
        return basePackage(request) + ".controller";
    }

    public String resolveService(ProjectRequest request) {
        return basePackage(request) + ".service";
    }

    public String resolveConfig(ProjectRequest request) {
        return basePackage(request) + ".config";
    }

    public String resolveException(ProjectRequest request) {
        return basePackage(request) + ".exception";
    }

    private String basePackage(ProjectRequest request) {
        JavaData data = (JavaData) request.getBackend().getData();
        return data.getGroup() + "." + data.getArtifact();
    }

}
