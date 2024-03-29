package org.dhbw.webapplicationgenerator.generator.baseproject;

import lombok.AllArgsConstructor;
import org.dhbw.webapplicationgenerator.generator.baseproject.spring.MainFileGenerator;
import org.dhbw.webapplicationgenerator.generator.baseproject.spring.MainTestFileGenerator;
import org.dhbw.webapplicationgenerator.generator.util.FileFolderGenerator;
import org.dhbw.webapplicationgenerator.model.request.ProjectRequest;
import org.dhbw.webapplicationgenerator.model.request.backend.SpringBootData;
import org.dhbw.webapplicationgenerator.model.response.ProjectDirectory;
import org.dhbw.webapplicationgenerator.util.ResourceFileHelper;
import org.springframework.stereotype.Service;

import java.io.FileNotFoundException;
import java.util.Optional;

@Service
@AllArgsConstructor
public class SourceFolderGenerator extends FileFolderGenerator {

    private final MainFileGenerator mainFileGenerator;
    private final MainTestFileGenerator mainTestFileGenerator;
    private final ResourceFileHelper resourceFileHelper;

    /**
     * Creates the Src-Directory including test and main folder. The relevant base files like Main-Class are also contained.
     * @param parent Parent directory, usually this is the root directory of the project
     * @return Src-Directory
     */
    public ProjectDirectory create(ProjectRequest request, ProjectDirectory parent) {
        ProjectDirectory srcDir = addDirectory("src", Optional.of(parent));
        addTestDir(request, srcDir);
        addMainDir(request, srcDir);
        return srcDir;
    }

    private ProjectDirectory addTestDir(ProjectRequest request, ProjectDirectory srcDir) {
        ProjectDirectory testDir = addDirectory("test", Optional.of(srcDir));
        ProjectDirectory javaPackageDir = addDirectory("java", Optional.of(testDir));
        ProjectDirectory groupPackageDir = addGroupPackage(request, javaPackageDir);
        mainTestFileGenerator.create(request, groupPackageDir);
        return testDir;
    }

    private ProjectDirectory addMainDir(ProjectRequest request, ProjectDirectory srcDir) {
        ProjectDirectory mainDir = addDirectory("main", Optional.of(srcDir));
        ProjectDirectory resourcesDir = addDirectory("resources", Optional.of(mainDir));
        try {
            addFile(this.resourceFileHelper.getFile("application.properties"), resourcesDir);
        } catch (FileNotFoundException ex) {
            ex.printStackTrace();
        }
        ProjectDirectory javaPackageDir = addDirectory("java", Optional.of(mainDir));
        ProjectDirectory groupPackageDir = addGroupPackage(request, javaPackageDir);
        mainFileGenerator.create(request, groupPackageDir);
        return mainDir;
    }

    private ProjectDirectory addGroupPackage(ProjectRequest request, ProjectDirectory javaPackageDir) {
        SpringBootData data = (SpringBootData) request.getBackend().getData();
        String[] groupParts = data.getGroup().split("\\.");
        ProjectDirectory currentChildDir = javaPackageDir;
        for (String groupPart : groupParts) {
            currentChildDir = addDirectory(groupPart, Optional.of(currentChildDir));
        }
        return addDirectory(data.getArtifact(), Optional.of(currentChildDir));
    }

}
