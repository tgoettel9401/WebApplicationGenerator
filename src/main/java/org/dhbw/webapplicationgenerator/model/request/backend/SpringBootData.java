package org.dhbw.webapplicationgenerator.model.request.backend;

import lombok.Data;

@Data
public class SpringBootData implements JavaData {
    private String group;
    private String artifact;
    private JavaBuildTool javaBuildTool;
}
