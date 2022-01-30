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
        try(BufferedReader reader = new BufferedReader(new FileReader(Objects.requireNonNull(baseFile)));
            FileOutputStream fileOutputStream = new FileOutputStream(".tmp/pom.xml")) {
            writeUpdatedPomXmlToStream(fileOutputStream, reader, request);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return new File(".tmp/pom.xml");
    }

    private void addAdditionalDependencies(StringBuilder builder, ProjectRequest request) {

        if (request.isHavingWeb()) {
            addDependency("org.springframework.boot", "spring-boot-starter-web", builder);
        }

        if (request.isHavingJpa()) {
            addDependency("org.springframework.boot", "spring-boot-starter-jpa", builder);
        }

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

    private void writeUpdatedPomXmlToStream(FileOutputStream stream, BufferedReader reader, ProjectRequest request) throws IOException {
        StringBuilder stringBuilder = new StringBuilder();

        String line;
        while ((line = reader.readLine()) != null) {
            if (line.equals("\t\t%ADDITIONAL_DEPENDENCIES")) {
                addAdditionalDependencies(stringBuilder, request);
            } else {
                stringBuilder.append(line);
                stringBuilder.append('\n');
            }
        }
        String inputString = stringBuilder.toString();
        stream.write(inputString.getBytes(StandardCharsets.UTF_8));
    }
}
