package org.dhbw.webapplicationgenerator.generator.base_project;

import lombok.AllArgsConstructor;
import org.dhbw.webapplicationgenerator.generator.model.ProjectDirectory;
import org.dhbw.webapplicationgenerator.generator.model.ProjectFile;
import org.dhbw.webapplicationgenerator.util.ResourceFileHelper;
import org.dhbw.webapplicationgenerator.webclient.request.CreationRequest;
import org.springframework.stereotype.Service;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.Objects;

@Service
@AllArgsConstructor
public class PomXmlGenerator extends FileFolderGenerator {

    private final String springdocVersion = "1.6.9";

    private final ResourceFileHelper resourceFileHelper;

    public ProjectFile create(CreationRequest request, ProjectDirectory parent) {
        createTmpFolderIfNotExists();
        return addFile(createPomXml(request), parent);
    }

    private File createPomXml(CreationRequest request) {
        createTmpFolderIfNotExists();
        File baseFile = getBasePomXml();
        try (BufferedReader reader = new BufferedReader(new FileReader(Objects.requireNonNull(baseFile)));
             FileOutputStream fileOutputStream = new FileOutputStream(".tmp/pom.xml")) {
            writeUpdatedPomXmlToStream(fileOutputStream, reader, request);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return new File(".tmp/pom.xml");
    }

    private void writeUpdatedPomXmlToStream(FileOutputStream stream, BufferedReader reader, CreationRequest request) throws IOException {
        StringBuilder stringBuilder = new StringBuilder();

        String line;
        while ((line = reader.readLine()) != null) {
            switch (line) {
                case "\t<groupId>$GROUP_ID</groupId>":
                    replaceGroupId(stringBuilder, request);
                    break;
                case "\t<artifactId>$ARTIFACT_ID</artifactId>":
                    replaceArtifactId(stringBuilder, request);
                    break;
                case "\t<name>$TITLE</name>":
                    replaceTitle(stringBuilder, request);
                    break;
                case "\t<description>$DESCRIPTION</description>":
                    replaceDescription(stringBuilder, request);
                    break;
                case "\t\t$ADDITIONAL_DEPENDENCIES":
                    replaceAdditionalDependencies(stringBuilder, request);
                    break;
                default:
                    // Line has not been replaced.
                    stringBuilder.append(line);
                    stringBuilder.append('\n');
                    break;
            }
        }
        String inputString = stringBuilder.toString();
        stream.write(inputString.getBytes(StandardCharsets.UTF_8));
    }

    private void replaceAdditionalDependencies(StringBuilder builder, CreationRequest request) {

        // Web and Thymeleaf
        addDependency("org.springframework.boot", "spring-boot-starter-web", builder);
        addDependency("org.springframework.boot", "spring-boot-starter-thymeleaf", builder);

        // Data, H2, Data Rest and Springdoc
        addDependency("org.springframework.boot", "spring-boot-starter-data-jpa", builder);
        addDependency("org.springframework.boot", "spring-boot-starter-data-rest", builder);
        addDependency("org.springframework.boot", "spring-boot-starter-security", builder);
        addDependency("org.springdoc", "springdoc-openapi-ui", springdocVersion, builder);
        addDependency("org.springdoc", "springdoc-openapi-data-rest", springdocVersion, builder);
        addDependency("com.h2database", "h2", "runtime", "", builder);
        addDependency("org.thymeleaf.extras", "thymeleaf-extras-springsecurity5", builder);

        builder.append("\n");
    }

    private void replaceGroupId(StringBuilder builder, CreationRequest request) {
        builder.append("\t<groupId>").append(request.getProject().getGroup()).append("</groupId>");
        builder.append("\n");
    }

    private void replaceArtifactId(StringBuilder builder, CreationRequest request) {
        builder.append("\t<artifactId>").append(request.getProject().getArtifact()).append("</artifactId>");
        builder.append("\n");
    }

    private void replaceDescription(StringBuilder builder, CreationRequest request) {
        builder.append("\t<description>").append(request.getProject().getDescription()).append("</description>");
        builder.append("\n");
    }

    private void replaceTitle(StringBuilder builder, CreationRequest request) {
        builder.append("\t<name>").append(request.getProject().getTitleWithoutSpaces()).append("</name>");
        builder.append("\n");
    }

    private void addDependency(String groupId, String artifactId, String scope, String version, StringBuilder builder) {
        builder.append("\n");
        builder.append("\t\t<dependency>");
        builder.append("\n");
        builder.append("\t\t\t<groupId>").append(groupId).append("</groupId>");
        builder.append("\n");
        builder.append("\t\t\t<artifactId>").append(artifactId).append("</artifactId>");
        builder.append("\n");
        if (!version.isEmpty()) {
            builder.append("\t\t\t<version>").append(version).append("</version>");
            builder.append("\n");
        }
        if (!scope.isEmpty()) {
            builder.append("\t\t\t<scope>").append(scope).append("</scope>");
            builder.append("\n");
        }
        builder.append("\t\t</dependency>");
        builder.append("\n");
    }

    private void addDependency(String groupId, String artifactId, String version, StringBuilder builder) {
        addDependency(groupId, artifactId, "", version, builder);
    }

    private void addDependency(String groupId, String artifactId, StringBuilder builder) {
        addDependency(groupId, artifactId, "", "", builder);
    }

    private File getBasePomXml() {
        try {
            return resourceFileHelper.getFile("pom.xml");
        } catch (FileNotFoundException ex) {
            ex.printStackTrace();
            return null;
        }
    }

}
