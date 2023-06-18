package org.dhbw.webapplicationgenerator.model.request.backend;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import lombok.Data;

@Data
public class SpringBootData implements JavaData {
    private String group;
    private String artifact;
    private JavaBuildTool javaBuildTool;
    private String javaVersion = "11";
    private String springBootVersion = "2.6.3";
    private String springDependencyManagementVersion = "1.1.0";
    private String springDocVersion = "1.6.9";
    private String apiPath = "/api";
    private boolean embeddedH2 = false;
    @JsonDeserialize(using = DatabaseProductSerializer.class)
    private DatabaseProduct databaseProduct;
    private String databaseConnectionString;
    private String databaseUsername;
    private String databasePassword;
    private String h2ConsolePath = "/h2-console";
    private boolean h2ConsoleEnabled = true;
}
