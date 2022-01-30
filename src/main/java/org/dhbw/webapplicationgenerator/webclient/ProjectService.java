package org.dhbw.webapplicationgenerator.webclient;

import lombok.AllArgsConstructor;
import org.dhbw.webapplicationgenerator.generator.Project;
import org.dhbw.webapplicationgenerator.generator.ProjectGenerator;
import org.dhbw.webapplicationgenerator.util.FileCleaner;
import org.dhbw.webapplicationgenerator.util.ZipHelper;
import org.springframework.stereotype.Service;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

@Service
@AllArgsConstructor
public class ProjectService {

    private final ProjectGenerator projectGenerator;
    private final ZipHelper zipHelper;
    private final FileCleaner fileCleaner;

    /**
     * Generates the Project based on the provided request
     * @param request Request for creating the project
     * @return Project based on the provided request
     * @throws IOException this may happen while zipping the project
     */
    public ByteArrayOutputStream generate(ProjectRequest request) throws IOException {
        Project project = projectGenerator.generate(request);
        ByteArrayOutputStream zipStream = zipHelper.zip(project);
        fileCleaner.performCleanup();
        return zipStream;
    }

}
