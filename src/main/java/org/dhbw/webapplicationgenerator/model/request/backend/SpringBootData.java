package org.dhbw.webapplicationgenerator.model.request.backend;

import lombok.Data;

@Data
public class SpringBootData implements JavaData {
    private String group;
    private String artifact;
    private JavaBuildTool javaBuildTool;
    private String javaVersion = "11";
    private String springBootVersion = "2.6.3";
    private String springDocVersion = "1.6.9";
    private String apiPath = "/api";
    private String h2ConsolePath = "/h2-console";
    private boolean h2ConsoleEnabled = true;
    private String h2JdbcUrl = "jdbc:h2:mem:testdb";
}
