package org.dhbw.webapplicationgenerator.webclient;

import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.dhbw.webapplicationgenerator.model.request.ProjectRequest;
import org.dhbw.webapplicationgenerator.model.request.Strategy;
import org.dhbw.webapplicationgenerator.model.request.backend.Backend;
import org.dhbw.webapplicationgenerator.model.request.backend.DatabaseProduct;
import org.dhbw.webapplicationgenerator.model.request.backend.SpringBootData;
import org.dhbw.webapplicationgenerator.model.request.database.Database;
import org.dhbw.webapplicationgenerator.model.request.database.FlywayData;
import org.dhbw.webapplicationgenerator.model.request.datamodel.Entity;
import org.dhbw.webapplicationgenerator.model.request.datamodel.EntityRelation;
import org.dhbw.webapplicationgenerator.model.request.datamodel.Relation;
import org.dhbw.webapplicationgenerator.model.request.deployment.Deployment;
import org.dhbw.webapplicationgenerator.model.request.deployment.DockerData;
import org.dhbw.webapplicationgenerator.model.request.frontend.Frontend;
import org.dhbw.webapplicationgenerator.model.request.frontend.ThymeleafData;
import org.dhbw.webapplicationgenerator.model.request.frontend.VaadinData;
import org.dhbw.webapplicationgenerator.webclient.exception.WagException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
public class ProjectRequestTransformer {

    private final ObjectMapper mapper = new ObjectMapper();

    /**
     * Transforms the request details from the generic Request to the concrete Request,
     * e.g. DeploymentRequest<T> to DeploymentRequest<DockerData>
     *
     * @param request before transformation (will also be updated due to reference type!)
     * @return request after transformation
     */
    public ProjectRequest transform(ProjectRequest request) {
        if (request.isDeploymentEnabled()) {
            transformDeployment(request);
        }
        if (request.isBackendEnabled()) {
            transformBackend(request);
        }
        if (request.isFrontendEnabled()) {
            transformFrontend(request);
        }
        if (request.isDatabaseEnabled()) {
            transformDatabase(request);
        }
        transformDataModel(request);
        return request;
    }

    private void transformDeployment(ProjectRequest request) {
        Strategy strategy = request.getDeployment().getStrategy();
        if (Objects.requireNonNull(strategy) == Strategy.DOCKER) {
            JavaType type = new ObjectMapper().getTypeFactory().constructParametricType(Deployment.class, DockerData.class);
            Deployment<DockerData> docker = mapper.convertValue(request.getDeployment(), type);
            request.setDeployment(docker);
        } else {
            throw new WagException("Unknown Deployment-Strategy supplied");
        }
    }

    private void transformBackend(ProjectRequest request) {
        Strategy strategy = request.getBackend().getStrategy();
        if (Objects.requireNonNull(strategy) == Strategy.SPRING_BOOT) {
            JavaType type = new ObjectMapper().getTypeFactory().constructParametricType(Backend.class, SpringBootData.class);
            Backend<SpringBootData> springBoot = mapper.convertValue(request.getBackend(), type);
            if (springBoot.getData().isEmbeddedH2() && springBoot.getData().getDatabaseConnectionString().isEmpty()) {
                springBoot.getData().setDatabaseConnectionString("jdbc:h2:mem:testdb");
            }
            if (springBoot.getData().isEmbeddedH2()) {
                springBoot.getData().setDatabaseProduct(DatabaseProduct.H2);
            }
            request.setBackend(springBoot);
        } else {
            throw new WagException("Unknown Backend-Strategy supplied");
        }
    }

    private void transformFrontend(ProjectRequest request) {
        Strategy strategy = request.getFrontend().getStrategy();
        if (Objects.requireNonNull(strategy) == Strategy.THYMELEAF) {
            JavaType type = new ObjectMapper().getTypeFactory().constructParametricType(Frontend.class, ThymeleafData.class);
            Frontend<ThymeleafData> thymeleaf = mapper.convertValue(request.getFrontend(), type);
            request.setFrontend(thymeleaf);
        } else if (Objects.requireNonNull(strategy) == Strategy.VAADIN) {
            JavaType type = new ObjectMapper().getTypeFactory().constructParametricType(Frontend.class, VaadinData.class);
            Frontend<VaadinData> vaadin = mapper.convertValue(request.getFrontend(), type);
            request.setFrontend(vaadin);
        }
        else {
            throw new WagException("Unknown Frontend-Strategy supplied");
        }
    }

    private void transformDatabase(ProjectRequest request) {
        Strategy strategy = request.getDatabase().getStrategy();
        if (Objects.requireNonNull(strategy) == Strategy.FLYWAY) {
            JavaType type = new ObjectMapper().getTypeFactory().constructParametricType(Database.class, FlywayData.class);
            Frontend<FlywayData> flyway = mapper.convertValue(request.getFrontend(), type);
            request.setFrontend(flyway);
        } else {
            throw new WagException("Unknown Database-Strategy supplied");
        }
    }

    private void transformDataModel(ProjectRequest request) {
        List<Entity> entities = request.getDataModel().getEntities();
        List<Relation> relations = request.getDataModel().getRelations();
        for (Relation relation : relations) {

            // Add entity1
            String entityName1 = relation.getEntityName1();
            Entity entity1 = entities.stream().filter(entity -> entity.getName().equals(entityName1)).findFirst()
                    .orElseThrow(() -> new WagException("For relation " + relation.getName() +
                            " the first entity with name " + entityName1 + " has not been found in entities-list."));
            relation.setEntity1(entity1);

            // Add entity2
            String entityName2 = relation.getEntityName2();
            Entity entity2 = entities.stream().filter(entity -> entity.getName().equals(entityName2)).findFirst()
                    .orElseThrow(() -> new WagException("For relation " + relation.getName() +
                            " the second entity with name " + entityName2 + " has not been found in entities-list."));
            relation.setEntity2(entity2);

            // Set the owning-entity if it has been specified in the request (field owningSide). This field is optional
            // though and hence neither of following if statements may be reached.
            if (relation.getOwningSide() != null && relation.getOwningSide().equals(entityName1)) {
                relation.setOwningEntity(entity1);
            }
            if (relation.getOwningSide() != null && relation.getOwningSide().equals(entityName2)) {
                relation.setOwningEntity(entity2);
            }
        }
        for (Entity entity : entities) {
            addEntityRelationsToEntity(entity, relations);
        }
    }

    private void addEntityRelationsToEntity(Entity entity, List<Relation> relations) {

        // Extract relations for this entity as entity1 and then as entity2.
        List<Relation> entity1Relations = relations.stream()
                .filter(relation -> relation.getEntity1().equals(entity))
                .collect(Collectors.toList());
        List<Relation> entity2Relations = relations.stream()
                .filter(relation -> relation.getEntity2().equals(entity))
                .collect(Collectors.toList());

        for (Relation relation : entity1Relations) {
            EntityRelation entityRelation = new EntityRelation();
            entityRelation.setName(relation.getName());
            entityRelation.setEntityName(relation.getEntityName2());
            entityRelation.setEntityCardinality(relation.getCardinality1());
            entityRelation.setRelationCardinality(relation.getCardinality2());
            entityRelation.setJoinTable(relation.getJoinTable());
            boolean isOwning = relation.getOwningEntity() != null && relation.getOwningEntity().equals(entity);
            entityRelation.setOwning(isOwning);
            entityRelation.setEntityObject(relation.getEntity2());
            entity.getRelations().add(entityRelation);
        }

        for (Relation relation : entity2Relations) {
            // TODO: Use attribute-name as well!
            EntityRelation entityRelation = new EntityRelation();
            entityRelation.setName(relation.getName());
            entityRelation.setEntityName(relation.getEntityName1());
            entityRelation.setEntityCardinality(relation.getCardinality2());
            entityRelation.setRelationCardinality(relation.getCardinality1());
            entityRelation.setJoinTable(relation.getJoinTable());
            boolean isOwning = relation.getOwningEntity() != null && relation.getOwningEntity().equals(entity);
            entityRelation.setOwning(isOwning);
            entityRelation.setEntityObject(relation.getEntity1());
            entity.getRelations().add(entityRelation);
        }

    }

}
