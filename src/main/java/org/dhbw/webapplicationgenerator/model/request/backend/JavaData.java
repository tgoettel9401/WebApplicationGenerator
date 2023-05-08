package org.dhbw.webapplicationgenerator.model.request.backend;

public interface JavaData {
    String getGroup();
    String getArtifact();
    JavaBuildTool getJavaBuildTool();

    void setGroup(String group);
    void setArtifact(String artifact);
    void setJavaBuildTool(JavaBuildTool buildTool);
}
