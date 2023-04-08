package org.dhbw.webapplicationgenerator.generator.baseproject.pom;

import lombok.AllArgsConstructor;
import org.dhbw.webapplicationgenerator.generator.baseproject.FileFolderGenerator;
import org.dhbw.webapplicationgenerator.generator.model.ProjectDirectory;
import org.dhbw.webapplicationgenerator.generator.model.ProjectFile;
import org.dhbw.webapplicationgenerator.generator.util.FreemarkerTemplateProcessor;
import org.dhbw.webapplicationgenerator.webclient.request.CreationRequest;
import org.springframework.stereotype.Service;

import java.io.*;
import java.util.*;

@Service
@AllArgsConstructor
public class PomXmlGenerator extends FileFolderGenerator {

    private static final String SPRING_DOC_VERSION = "1.6.9";

    private static final String SPRING_BOOT_FRAMEWORK_GROUP_ID = "org.springframework.boot";

    private final FreemarkerTemplateProcessor freemarkerTemplateProcessor;

    public ProjectFile create(CreationRequest request, ProjectDirectory parent) {
        createTmpFolderIfNotExists();
        return addFile(createPomXmlWithFreemarker(request), parent);
    }

    private File createPomXmlWithFreemarker(CreationRequest request) {
        // Initialize Data Model for Freemarker
        Map<String, Object> dataModel = new HashMap<>();
        dataModel.put("groupId", request.getProject().getGroup());
        dataModel.put("artifactId", request.getProject().getArtifact());
        dataModel.put("projectTitle", request.getProject().getTitleWithoutSpaces());
        dataModel.put("projectDescription", request.getProject().getDescription());
        dataModel.put("springBootVersion", "2.6.3");
        dataModel.put("javaVersion", "11");
        dataModel.put("dependencies", createDependencies());

        // Process the template and return the file
        String filename = "pom.xml";
        return freemarkerTemplateProcessor.process("pom.ftl", dataModel, filename);
    }

    private List<PomXmlDependency> createDependencies() {
        List<PomXmlDependency> dependencies = new ArrayList<>();
        dependencies.add(new PomXmlDependency(SPRING_BOOT_FRAMEWORK_GROUP_ID, "spring-boot-starter-web", "", ""));
        dependencies.add(new PomXmlDependency(SPRING_BOOT_FRAMEWORK_GROUP_ID, "spring-boot-starter-thymeleaf", "", ""));
        dependencies.add(new PomXmlDependency(SPRING_BOOT_FRAMEWORK_GROUP_ID, "spring-boot-starter-data-jpa", "", ""));
        dependencies.add(new PomXmlDependency(SPRING_BOOT_FRAMEWORK_GROUP_ID, "spring-boot-starter-data-rest", "", ""));
        dependencies.add(new PomXmlDependency(SPRING_BOOT_FRAMEWORK_GROUP_ID, "spring-boot-starter-security", "", ""));
        dependencies.add(new PomXmlDependency("org.springdoc", "springdoc-openapi-ui", SPRING_DOC_VERSION, ""));
        dependencies.add(new PomXmlDependency("org.springdoc", "springdoc-openapi-data-rest", SPRING_DOC_VERSION, ""));
        dependencies.add(new PomXmlDependency("com.h2database", "h2", "", "runtime"));
        dependencies.add(new PomXmlDependency("org.thymeleaf.extras", "thymeleaf-extras-springsecurity5", "", ""));
        return dependencies;
    }
}
