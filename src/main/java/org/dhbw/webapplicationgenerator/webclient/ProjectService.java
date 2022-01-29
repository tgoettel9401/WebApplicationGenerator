package org.dhbw.webapplicationgenerator.webclient;

import lombok.AllArgsConstructor;
import org.dhbw.webapplicationgenerator.generator.Project;
import org.dhbw.webapplicationgenerator.generator.ProjectGenerator;
import org.dhbw.webapplicationgenerator.util.ZipHelper;
import org.springframework.stereotype.Service;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

@Service
@AllArgsConstructor
public class ProjectService {

    private final ProjectGenerator projectGenerator;
    private final ZipHelper zipHelper;

    public ByteArrayOutputStream generate(ProjectRequest request) throws IOException {
        Project project = projectGenerator.generate(request);
        return zipHelper.zip(project);
    }

}
