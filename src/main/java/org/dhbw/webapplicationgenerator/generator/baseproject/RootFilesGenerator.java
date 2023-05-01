package org.dhbw.webapplicationgenerator.generator.baseproject;

import lombok.AllArgsConstructor;
import org.dhbw.webapplicationgenerator.generator.baseproject.pom.PomXmlGenerator;
import org.dhbw.webapplicationgenerator.generator.model.ProjectDirectory;
import org.dhbw.webapplicationgenerator.generator.model.ProjectFile;
import org.dhbw.webapplicationgenerator.util.ResourceFileHelper;
import org.dhbw.webapplicationgenerator.webclient.request.CreationRequest;
import org.springframework.stereotype.Service;

import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;

@Service
@AllArgsConstructor
public class RootFilesGenerator extends FileFolderGenerator {

    private final ResourceFileHelper resourceFileHelper;
    private final PomXmlGenerator pomXmlGenerator;
    private final ReadmeGenerator readmeGenerator;

    /**
     * Creates the Files that reside in the root directory
     * @param parent Parent directory, usually this is the root directory of the project
     * @return List of Files in the root directory
     */
    public List<ProjectFile> create(CreationRequest request, ProjectDirectory parent) {
        List<ProjectFile> files = new ArrayList<>();
        try {
            files.add(addFile(resourceFileHelper.getFile("mvnw"), parent));
            files.add(addFile(resourceFileHelper.getFile("mvnw.cmd"), parent));
            files.add(pomXmlGenerator.create(request, parent));
            files.add(addFile(".gitignore", resourceFileHelper.getFile("gitignore"), parent));
            files.add(readmeGenerator.create(request, parent));
        } catch (FileNotFoundException ex) {
            ex.printStackTrace();
        }
        return files;
    }

}
