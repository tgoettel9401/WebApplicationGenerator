package org.dhbw.webapplicationgenerator.generator.util;

import org.dhbw.webapplicationgenerator.model.request.ProjectRequest;
import org.dhbw.webapplicationgenerator.model.request.backend.JavaData;
import org.dhbw.webapplicationgenerator.webclient.request.CreationRequest;
import org.springframework.stereotype.Service;

@Service
public class PackageNameResolver {

    // TODO: Add to Java-part because only relevant for Java.

    public String resolveEntity(CreationRequest request) {
        return basePackageOld(request) + ".domain";
    }

    public String resolveEntity(ProjectRequest request) {
        return basePackage(getJavaData(request)) + ".domain";
    }

    public String resolveTransferObjects(CreationRequest request) {
        return basePackageOld(request) + ".transferObject";
    }

    public String resolveTransferObjects(ProjectRequest request) {
        return basePackage(getJavaData(request)) + ".transferObject";
    }

    public String resolveRepository(CreationRequest request) {
        return basePackageOld(request) + ".repository";
    }

    public String resolveRepository(ProjectRequest request) {
        return basePackage(getJavaData(request)) + ".repository";
    }

    public String resolveController(CreationRequest request) {
        return basePackageOld(request) + ".controller";
    }

    public String resolveController(ProjectRequest request) {
        return basePackage(getJavaData(request)) + ".controller";
    }

    public String resolveService(CreationRequest request) {
        return basePackageOld(request) + ".service";
    }

    public String resolveService(ProjectRequest request) {
        return basePackage(getJavaData(request)) + ".service";
    }

    public String resolveConfig(CreationRequest request) {
        return basePackageOld(request) + ".config";
    }

    public String resolveConfig(ProjectRequest request) {
        return basePackage(getJavaData(request)) + ".config";
    }

    public String resolveException(CreationRequest request) {
        return basePackageOld(request) + ".exception";
    }

    public String resolveException(ProjectRequest request) {
        return basePackage(getJavaData(request)) + ".exception";
    }

    private String basePackageOld(CreationRequest request) {
        return request.getProject().getGroup() + "." + request.getProject().getArtifact();
    }

    private String basePackage(JavaData data) {
        return data.getGroup() + "." + data.getArtifact();
    }

    private JavaData getJavaData(ProjectRequest request) {
        return (JavaData) request.getBackend().getData();
    }

}
