package org.dhbw.webapplicationgenerator.webclient;

import lombok.AllArgsConstructor;
import org.dhbw.webapplicationgenerator.generator.GenerationRequest;
import org.dhbw.webapplicationgenerator.generator.ProjectGenerationService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.io.ByteArrayOutputStream;

@RestController
@AllArgsConstructor
public class ProjectionGenerationController {

    private final Logger logger = LoggerFactory.getLogger(ProjectionGenerationController.class);

    private final ProjectGenerationService projectGenerationService;

    @GetMapping(value = "generate", produces = "application/zip")
    public byte[] generateBaseProject() {
        logger.info("Received generation request for base project");
        GenerationRequest request = new GenerationRequest();
        request.setTitle("Spring-Base-Project");
        ByteArrayOutputStream stream = projectGenerationService.generate(request);
        return stream.toByteArray();
    }

    @PostMapping("generate")
    public byte[] generateByRequest(@RequestBody() GenerationRequest request) {
        logger.info("Received generation request for project with title {}", request.getTitle());
        ByteArrayOutputStream stream = projectGenerationService.generate(request);
        return stream.toByteArray();
    }

}
