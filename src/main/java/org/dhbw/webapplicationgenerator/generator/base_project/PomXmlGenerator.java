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

    private void addAdditionalDependencies(StringBuilder buffer, ProjectRequest request) {
        if (request.isIncludeWeb()) {
            buffer.append("\t\t<dependency>");
            buffer.append("\n");
            buffer.append("\t\t\t<groupId>org.springframework.boot</groupId>");
            buffer.append("\n");
            buffer.append("\t\t\t<artifactId>spring-boot-starter-web</artifactId>");
            buffer.append("\n");
            buffer.append("\t\t</dependency>");
            buffer.append("\n");
        } else {
            buffer.append("\n");
        }
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
