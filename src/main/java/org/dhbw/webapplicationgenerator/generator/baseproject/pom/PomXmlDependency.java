package org.dhbw.webapplicationgenerator.generator.baseproject.pom;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PomXmlDependency {
    private String groupId;
    private String artifactId;
    private String version;
    private String scope;
}
