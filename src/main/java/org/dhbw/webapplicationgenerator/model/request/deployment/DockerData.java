package org.dhbw.webapplicationgenerator.model.request.deployment;

import lombok.Data;

@Data
public class DockerData {
    private String imageName;
    private String baseImage;
}
