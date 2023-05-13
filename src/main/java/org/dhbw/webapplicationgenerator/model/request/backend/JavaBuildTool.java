package org.dhbw.webapplicationgenerator.model.request.backend;

import lombok.Getter;

@Getter
public enum JavaBuildTool {
    MAVEN("mvn install"), GRADLE("gradle build");

    private final String buildCommand;

    JavaBuildTool(String buildCommand) {
        this.buildCommand = buildCommand;
    }
}
