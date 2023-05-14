package org.dhbw.webapplicationgenerator.model.request.backend;

import lombok.Getter;

@Getter
public enum JavaBuildTool {
    MAVEN("mvn install", "mvn clean"),
    GRADLE("gradle build", "gradle clean");

    private final String buildCommand;
    private final String cleanCommand;

    JavaBuildTool(String buildCommand, String cleanCommand) {
        this.buildCommand = buildCommand;
        this.cleanCommand = cleanCommand;
    }
}
