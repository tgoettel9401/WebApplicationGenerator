package org.dhbw.webapplicationgenerator.generator.backend.java.buildtool;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Dependency {
    private String groupId;
    private String artifactId;
    private String version;
    private String scope;
    private String type;
    private boolean dependencyManagement = false;

    public String getGradleLine() {
        String line = "";
        if (type.equals("test")) {
            line = line.concat("testImplementation");
        } else {
            line = line.concat("implementation");
        }
        line = line.concat(" '");
        line = line.concat(groupId + ":" + artifactId);
        if (!version.isEmpty()) {
            line = line.concat(":" + version);
        }
        line = line.concat("';");
        return line;
    }

}
