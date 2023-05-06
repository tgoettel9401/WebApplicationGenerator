package org.dhbw.webapplicationgenerator.generator.security;

import lombok.AllArgsConstructor;
import org.dhbw.webapplicationgenerator.model.response.Project;
import org.dhbw.webapplicationgenerator.generator.baseproject.FileFolderGenerator;
import org.dhbw.webapplicationgenerator.model.response.ProjectDirectory;
import org.dhbw.webapplicationgenerator.util.ResourceFileHelper;
import org.springframework.stereotype.Service;

import java.io.IOException;

@Service
@AllArgsConstructor
public class SecurityPagesGenerator extends FileFolderGenerator {

    private static final String TMP_PATH = ".tmp/";
    private static final String HTML_FILE_ENDING = ".html";

    private final ResourceFileHelper resourceFileHelper;

    public Project create(Project project) {

        ProjectDirectory templatesDir = getTemplatesDirectory(project);

        try {
            addFile(resourceFileHelper.getFile("login.html"), templatesDir);
            addFile(resourceFileHelper.getFile("register.html"), templatesDir);
            addFile(resourceFileHelper.getFile("users.html"), templatesDir);
        } catch (IOException ex) {
            ex.printStackTrace();
        }
        return project;
    }
}
