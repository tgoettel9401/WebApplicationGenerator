package org.dhbw.webapplicationgenerator.generator.baseproject;

import lombok.AllArgsConstructor;
import org.dhbw.webapplicationgenerator.model.response.ProjectDirectory;
import org.dhbw.webapplicationgenerator.util.ResourceFileHelper;
import org.springframework.stereotype.Service;

import java.io.FileNotFoundException;
import java.util.Optional;

@Service
@AllArgsConstructor
public class MavenFolderGenerator extends FileFolderGenerator {

    private final ResourceFileHelper resourceFileHelper;

    /**
     * Creates the Project Directory .mvn
     * @param parent Parent directory, usually this is the root directory of the project
     * @return .mvn Directory
     */
    public ProjectDirectory create(ProjectDirectory parent) {
        ProjectDirectory mavenFolder = addDirectory(".mvn", Optional.of(parent));
        ProjectDirectory mavenWrapperFolder = addDirectory("wrapper", Optional.of(mavenFolder));
        try {
            addFile(resourceFileHelper.getFile("maven-wrapper.jar"), mavenWrapperFolder);
            addFile(resourceFileHelper.getFile("maven-wrapper.properties"), mavenWrapperFolder);
        } catch (FileNotFoundException ex) {
            ex.printStackTrace();
        }
        return mavenFolder;
    }
}
