package org.dhbw.webapplicationgenerator.webclient;

import lombok.AllArgsConstructor;
import org.dhbw.webapplicationgenerator.model.request.ProjectRequest;
import org.dhbw.webapplicationgenerator.webclient.exception.ValidationException;
import org.dhbw.webapplicationgenerator.webclient.exception.WagException;
import org.dhbw.webapplicationgenerator.webclient.request.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.HashSet;
import java.util.Set;

@RestController
@AllArgsConstructor
public class ProjectController {

    private final Logger logger = LoggerFactory.getLogger(ProjectController.class);

    private final ProjectService projectService;
    private final ProjectRequestTransformer projectRequestTransformer;
    private final ProjectRequestValidator projectRequestValidator;

    @PostMapping("generateNew")
    public byte[] generateByRequestNew(@RequestBody() ProjectRequest request) throws ValidationException, IOException {
        logger.info("Received generation request for project with title {}", request.getTitle());
        logger.info("Validating the request");
        projectRequestTransformer.transform(request);
        projectRequestValidator.validate(request);
        logger.info("Validation passed");
        ByteArrayOutputStream stream = projectService.generate(request);
        return stream.toByteArray();
    }

}
