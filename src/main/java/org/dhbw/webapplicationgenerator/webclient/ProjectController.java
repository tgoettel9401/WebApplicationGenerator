package org.dhbw.webapplicationgenerator.webclient;

import lombok.AllArgsConstructor;
import org.dhbw.webapplicationgenerator.webclient.request.EntityAttribute;
import org.dhbw.webapplicationgenerator.webclient.request.ProjectRequest;
import org.dhbw.webapplicationgenerator.webclient.request.RequestEntity;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.GetMapping;
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
        request.setEntities(initializeEntities());
        return projectService.generate(request).toByteArray();
    }

    @PostMapping("generate")
    public byte[] generateByRequest(@RequestBody() ProjectRequest request) throws IOException {
        logger.info("Received generation request for project with title {}", request.getTitle());
        ByteArrayOutputStream stream = projectService.generate(request);
        return stream.toByteArray();
    }

    private Set<RequestEntity> initializeEntities() {
        Set<RequestEntity> requestEntities = new HashSet<>();

        // Student Entity
        RequestEntity studentEntity = new RequestEntity();
        studentEntity.setTitle("Student");
        studentEntity.setAttributes(initializeStudentAttributes());
        requestEntities.add(studentEntity);

        return requestEntities;
    }

    private Set<EntityAttribute> initializeStudentAttributes() {
        Set<EntityAttribute> attributes = new HashSet<>();
        attributes.add(createAttribute("First-Name", "String"));
        attributes.add(createAttribute("Last-Name", "String"));
        attributes.add(createAttribute("Birthday", "LocalDateTime"));
        return attributes;
    }

    private EntityAttribute createAttribute(String title, String dataType) {
        EntityAttribute attribute = new EntityAttribute();
        attribute.setTitle(title);
        attribute.setDataType(dataType);
        return attribute;
    }

}
