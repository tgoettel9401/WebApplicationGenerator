package org.dhbw.webapplicationgenerator.generator.base_project;

import lombok.AllArgsConstructor;
import org.dhbw.webapplicationgenerator.generator.model.ProjectDirectory;
import org.dhbw.webapplicationgenerator.generator.model.ProjectFile;
import org.dhbw.webapplicationgenerator.util.ResourceFileHelper;
import org.dhbw.webapplicationgenerator.webclient.ProjectRequest;
import org.springframework.stereotype.Service;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.Objects;

@Service
@AllArgsConstructor
public class PomXmlGenerator extends FileFolderGenerator {

    private final ResourceFileHelper resourceFileHelper;

    public ProjectFile create(ProjectRequest request, ProjectDirectory parent) {
        createTmpFolderIfNotExists();
        return addFile(createPomXml(request), parent);
    }

    private File createPomXml(ProjectRequest request) {
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

    private void writeUpdatedPomXmlToStream(FileOutputStream stream, BufferedReader reader, ProjectRequest request) throws IOException {
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

    private void replaceAdditionalDependencies(StringBuilder builder, ProjectRequest request) {
        if (request.isHavingWeb()) {
            addDependency("org.springframework.boot", "spring-boot-starter-web", builder);
        }
        if (request.isHavingJpa()) {
            addDependency("org.springframework.boot", "spring-boot-starter-jpa", builder);
        }
        builder.append("\n");
    }

    private void replaceGroupId(StringBuilder builder, ProjectRequest request) {
        builder.append("\t<groupId>").append(request.getGroup()).append("</groupId>");
        builder.append("\n");
    }

    private void replaceArtifactId(StringBuilder builder, ProjectRequest request) {
        builder.append("\t<artifactId>").append(request.getArtifact()).append("</artifactId>");
        builder.append("\n");
    }

    private void replaceDescription(StringBuilder builder, ProjectRequest request) {
        builder.append("\t<description>").append(request.getDescription()).append("</description>");
        builder.append("\n");
    }

    private void replaceTitle(StringBuilder builder, ProjectRequest request) {
        builder.append("\t<name>").append(request.getTitle()).append("</name>");
        builder.append("\n");
    }

    private void addDependency(String groupId, String artifactId, StringBuilder builder) {
        builder.append("\n");
        builder.append("\t\t<dependency>");
        builder.append("\n");
        builder.append("\t\t\t<groupId>").append(groupId).append("</groupId>");
        builder.append("\n");
        builder.append("\t\t\t<artifactId>").append(artifactId).append("</artifactId>");
        builder.append("\n");
        builder.append("\t\t</dependency>");
        builder.append("\n");
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
