package org.dhbw.webapplicationgenerator.generator;

import lombok.AllArgsConstructor;
import org.dhbw.webapplicationgenerator.generator.model.ProjectDirectory;
import org.dhbw.webapplicationgenerator.generator.model.ProjectFile;
import org.dhbw.webapplicationgenerator.generator.model.StructureElement;
import org.dhbw.webapplicationgenerator.generator.model.StructureLevel;
import org.dhbw.webapplicationgenerator.util.ResourceFileHelper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

@Service
@AllArgsConstructor
public class ProjectGenerationService {

    private final Logger logger = LoggerFactory.getLogger(ProjectGenerationService.class);

    private final ResourceFileHelper resourceFileHelper;

    public ByteArrayOutputStream generate(GenerationRequest request) {
        logger.info("Generating the new project {}", request.getTitle());
        GeneratedProject baseProject = generateBaseProject();
        ByteArrayOutputStream zippedFile = zipProject(baseProject);
        return zippedFile;
    }

    private ByteArrayOutputStream zipProject(GeneratedProject project) {
        try {
            ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
            ZipOutputStream zipOut = new ZipOutputStream(byteArrayOutputStream);
            for (File file : getAllFiles(project.getFileStructure())) {
                FileInputStream fileIn = new FileInputStream(file);
                ZipEntry zipEntry = new ZipEntry(file.getName());
                zipOut.putNextEntry(zipEntry);
                byte[] bytes = new byte[1024];
                int length;
                while ((length = fileIn.read(bytes)) >= 0) {
                    zipOut.write(bytes, 0, length);
                }
                fileIn.close();
            }
            zipOut.close();
            byteArrayOutputStream.close();
            return byteArrayOutputStream;
        } catch (IOException ex) {
            ex.printStackTrace();
            return null;
        }
    }


    private GeneratedProject generateBaseProject() {
        GeneratedProject project = new GeneratedProject();
        ProjectDirectory mainDirectory = new ProjectDirectory();
        mainDirectory.setTitle("project");
        mainDirectory.addChild(generateMavenFolder());
        project.setFileStructure(mainDirectory);
        return project;
    }

    private StructureElement generateMavenFolder() {
        StructureElement mavenFolder = new ProjectDirectory();
        mavenFolder.setTitle(".mvn");
        mavenFolder.addChild(createMavenWrapperFolder());
        return mavenFolder;
    }

    private StructureElement createMavenWrapperFolder() {
        StructureElement mavenWrapperFolder = new ProjectDirectory();

        ProjectFile mavenWrapperJar = new ProjectFile();
        mavenWrapperJar.setTitle("maven-wrapper.jar");
        mavenWrapperJar.setFile(resourceFileHelper.getFile("maven-wrapper.jar"));

        ProjectFile mavenWrapperProperties = new ProjectFile();
        mavenWrapperProperties.setTitle("maven-wrapper.properties");
        mavenWrapperProperties.setFile(resourceFileHelper.getFile("maven-wrapper.properties"));

        mavenWrapperFolder.setTitle("wrapper");
        mavenWrapperFolder.addChild(mavenWrapperJar);
        mavenWrapperFolder.addChild(mavenWrapperProperties);
        return mavenWrapperFolder;
    }

    private List<File> getAllFiles(StructureElement element) {
        List<File> files = new ArrayList<>();
        if (element.getLevel() == StructureLevel.DIRECTORY) {
            element.getChildren().forEach(child -> files.addAll(getAllFiles(child)));
        } else { // Element is File and not DIRECTORY
            files.add(((ProjectFile) element).getFile());
        }
        return files;
    }

}
