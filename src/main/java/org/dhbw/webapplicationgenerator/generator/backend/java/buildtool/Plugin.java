package org.dhbw.webapplicationgenerator.generator.backend.java.buildtool;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Plugin {
    private String groupId;
    private String artifactId;
    private String version;
    private List<PluginExecution> executions = new ArrayList<>();
}
