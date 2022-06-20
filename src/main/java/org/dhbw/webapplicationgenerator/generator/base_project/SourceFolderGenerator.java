package org.dhbw.webapplicationgenerator.generator.base_project;

import lombok.AllArgsConstructor;
import org.dhbw.webapplicationgenerator.generator.model.ProjectDirectory;
import org.dhbw.webapplicationgenerator.util.ResourceFileHelper;
import org.dhbw.webapplicationgenerator.webclient.request.CreationRequest;
import org.springframework.stereotype.Service;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Optional;

@Service
@AllArgsConstructor
public class SourceFolderGenerator extends FileFolderGenerator {

    private final MainFileGenerator mainFileGenerator;
    private final ResourceFileHelper resourceFileHelper;

    /**
     * Creates the Src-Directory including test and main folder. The relevant base files like Main-Class are also contained.
     * @param parent Parent directory, usually this is the root directory of the project
     * @return Src-Directory
     */
    public ProjectDirectory create(CreationRequest request, ProjectDirectory parent) {
        ProjectDirectory srcDir = addDirectory("src", Optional.of(parent));
        addTestDir(request, srcDir);
        addMainDir(request, srcDir);
        return srcDir;
    }

    private ProjectDirectory addTestDir(CreationRequest request, ProjectDirectory srcDir) {
        ProjectDirectory testDir = addDirectory("test", Optional.of(srcDir));
        ProjectDirectory javaPackageDir = addDirectory("java", Optional.of(testDir));
        ProjectDirectory groupPackageDir = addGroupPackage(request, javaPackageDir);
        try {
            mainFileGenerator.createTestFile(request, groupPackageDir);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return testDir;
    }

    private ProjectDirectory addMainDir(CreationRequest request, ProjectDirectory srcDir) {
        ProjectDirectory mainDir = addDirectory("main", Optional.of(srcDir));
        ProjectDirectory resourcesDir = addDirectory("resources", Optional.of(mainDir));
        try {
            addFile(this.resourceFileHelper.getFile("application.properties"), resourcesDir);
        } catch (FileNotFoundException ex) {
            ex.printStackTrace();
        }
        ProjectDirectory javaPackageDir = addDirectory("java", Optional.of(mainDir));
        ProjectDirectory groupPackageDir = addGroupPackage(request, javaPackageDir);
        try {
            mainFileGenerator.create(request, groupPackageDir);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return mainDir;
    }

    private ProjectDirectory addGroupPackage(CreationRequest request, ProjectDirectory javaPackageDir) {
        String[] groupParts = request.getProject().getGroup().split("\\.");
        ProjectDirectory currentChildDir = javaPackageDir;
        for (String groupPart : groupParts) {
            currentChildDir = addDirectory(groupPart, Optional.of(currentChildDir));
        }
        return addDirectory(request.getProject().getArtifact(), Optional.of(currentChildDir));
    }

}
