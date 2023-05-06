package org.dhbw.webapplicationgenerator.model.request.backend;

import lombok.Data;

@Data
public class SpringBootData {
    private String group;
    private String artifact;
    private JavaBuildTool javaBuildTool;
}
