package org.dhbw.webapplicationgenerator.generator.base_project;

import lombok.AllArgsConstructor;
import org.dhbw.webapplicationgenerator.generator.model.ProjectDirectory;
import org.dhbw.webapplicationgenerator.util.ResourceFileHelper;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class MavenFolderGenerator extends FolderGenerator {

    private final ResourceFileHelper resourceFileHelper;

    public ProjectDirectory create(ProjectDirectory parent) {
        ProjectDirectory mavenFolder = addDirectory(".mvn", parent);
        ProjectDirectory mavenWrapperFolder = addDirectory("wrapper", mavenFolder);
        addFile(resourceFileHelper.getFile("maven-wrapper.jar"), mavenWrapperFolder);
        addFile(resourceFileHelper.getFile("maven-wrapper.properties"), mavenWrapperFolder);
        return mavenFolder;
    }

}
