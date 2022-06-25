package org.dhbw.webapplicationgenerator.webclient;

import lombok.AllArgsConstructor;
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
    private final CreationRequestValidator creationRequestValidator;

    @PostMapping("generate")
    public byte[] generateByRequest(@RequestBody() CreationRequest request) throws IOException, ValidationException {
        logger.info("Received generation request for project with title {}", request.getProject().getTitle());
        logger.info("Validating the request");
        creationRequestValidator.validate(request);
        addEntitiesToRelations(request);
        creationRequestValidator.validateManyToManyRelations(request);
        logger.info("Validation passed");
        ByteArrayOutputStream stream = projectService.generate(request);
        return stream.toByteArray();
    }

    public void addEntitiesToRelations(CreationRequest request) {
        Set<RequestEntity> entities = new HashSet<>(request.getEntities());
        for (RequestEntity entity : request.getEntities()) {
            for (EntityRelation relation : entity.getRelations()) {
                relation.setEntityObject(
                        entities.stream()
                                .filter(e -> e.getName().equals(relation.getEntity()))
                                .findFirst()
                                .orElseThrow(() -> new WagException("Relation-Entity " + relation.getEntity() + " is not found")
                                ));
            }
        }
    }

}
