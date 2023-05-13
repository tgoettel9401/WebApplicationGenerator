package org.dhbw.webapplicationgenerator.webclient.request;

import lombok.Data;
import org.dhbw.webapplicationgenerator.model.request.backend.JavaBuildTool;

@Data
public class CreationRequestDocker {
    private boolean enabled = false;
    private String imageName;
    private String baseImage;
    private JavaBuildTool javaBuildTool;
}
