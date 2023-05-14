package org.dhbw.webapplicationgenerator.model.request.backend;

public interface JavaData extends BackendData {
    String getGroup();
    String getArtifact();
    JavaBuildTool getJavaBuildTool();
    String getJavaVersion();
}
