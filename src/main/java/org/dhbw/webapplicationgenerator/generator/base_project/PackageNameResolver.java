package org.dhbw.webapplicationgenerator.generator.base_project;

import org.dhbw.webapplicationgenerator.webclient.request.ProjectRequest;
import org.springframework.stereotype.Service;

@Service
public class PackageNameResolver {

    public String resolveEntity(ProjectRequest request) {
        return request.getGroup() + "." + request.getArtifact() + ".domain";
    }

    public String resolveRepository(ProjectRequest request) {
        return request.getGroup() + "." + request.getArtifact() + ".repository";
    }

    public String resolveController(ProjectRequest request) {
        return request.getGroup() + "." + request.getArtifact() + ".controller";
    }

    public String resolveConfig(ProjectRequest request) {
        return request.getGroup() + "." + request.getArtifact() + ".config";
    }

}
