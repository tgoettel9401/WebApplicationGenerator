package org.dhbw.webapplicationgenerator.generator.java;

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
}
