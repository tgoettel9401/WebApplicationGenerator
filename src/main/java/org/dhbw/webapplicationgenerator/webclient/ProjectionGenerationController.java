package org.dhbw.webapplicationgenerator.webclient;

import lombok.AllArgsConstructor;
import org.apache.commons.io.IOUtils;
import org.dhbw.webapplicationgenerator.generator.GeneratedProject;
import org.dhbw.webapplicationgenerator.generator.GenerationRequest;
import org.dhbw.webapplicationgenerator.generator.ProjectGenerationService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.method.annotation.StreamingResponseBody;

import java.io.*;
import java.util.zip.ZipOutputStream;

@RestController
@AllArgsConstructor
public class ProjectionGenerationController {

    private final Logger logger = LoggerFactory.getLogger(ProjectionGenerationController.class);

    private final ProjectGenerationService projectGenerationService;

    @GetMapping(value = "generate", produces = "application/zip")
    public @ResponseBody byte[] generate() throws IOException {
        logger.info("Received generation request");
        GenerationRequest request = new GenerationRequest();
        request.setTitle("Spring-Base-Project");
        ZipOutputStream stream = projectGenerationService.generate(request);
        InputStream in = new FileInputStream("project.zip");

        return IOUtils.toByteArray(in);
    }

    @PostMapping("generate")
    public ZipOutputStream generate(@RequestBody() GenerationRequest request) {
        logger.info("Received generation request");
        return projectGenerationService.generate(request);
    }

}
