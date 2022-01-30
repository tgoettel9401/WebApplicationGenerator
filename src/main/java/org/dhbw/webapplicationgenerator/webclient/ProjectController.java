package org.dhbw.webapplicationgenerator.webclient;

import lombok.AllArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

@RestController
@AllArgsConstructor
public class ProjectController {

    private final Logger logger = LoggerFactory.getLogger(ProjectController.class);

    private final ProjectService projectService;

    @GetMapping(value = "generate", produces = "application/zip")
    public byte[] generateBaseProject() throws IOException {
        logger.info("Received generation request for base project");
        ProjectRequest request = new ProjectRequest();
        request.setTitle("Spring-Base-Project");
        request.setGroup("org.example");
        request.setDescription("This was generated automatically");
        request.setArtifact("demo");
        request.setHavingWeb(true);
        request.setHavingJpa(true);
        return projectService.generate(request).toByteArray();
    }

    @PostMapping("generate")
    public byte[] generateByRequest(@RequestBody() ProjectRequest request) throws IOException {
        logger.info("Received generation request for project with title {}", request.getTitle());
        ByteArrayOutputStream stream = projectService.generate(request);
        return stream.toByteArray();
    }

}
