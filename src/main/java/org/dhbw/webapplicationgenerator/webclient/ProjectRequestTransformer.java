package org.dhbw.webapplicationgenerator.webclient;

import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.dhbw.webapplicationgenerator.model.request.ProjectRequest;
import org.dhbw.webapplicationgenerator.model.request.Strategy;
import org.dhbw.webapplicationgenerator.model.request.backend.Backend;
import org.dhbw.webapplicationgenerator.model.request.backend.SpringBootData;
import org.dhbw.webapplicationgenerator.model.request.datamodel.Entity;
import org.dhbw.webapplicationgenerator.model.request.datamodel.Relation;
import org.dhbw.webapplicationgenerator.model.request.database.Database;
import org.dhbw.webapplicationgenerator.model.request.database.FlywayData;
import org.dhbw.webapplicationgenerator.model.request.deployment.Deployment;
import org.dhbw.webapplicationgenerator.model.request.deployment.DockerData;
import org.dhbw.webapplicationgenerator.model.request.frontend.Frontend;
import org.dhbw.webapplicationgenerator.model.request.frontend.ThymeleafData;
import org.dhbw.webapplicationgenerator.webclient.exception.WagException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;

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
        } else {
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
            String entityName1 = relation.getEntityName1();
            Entity entity1 = entities.stream().filter(entity -> entity.getName().equals(entityName1)).findFirst()
                    .orElseThrow(() -> new WagException("For relation " + relation.getName() +
                            " the first entity with name " + entityName1 + " has not been found in entities-list."));
            relation.setEntity1(entity1);
            String entityName2 = relation.getEntityName2();
            Entity entity2 = entities.stream().filter(entity -> entity.getName().equals(entityName2)).findFirst()
                    .orElseThrow(() -> new WagException("For relation " + relation.getName() +
                            " the second entity with name " + entityName2 + " has not been found in entities-list."));
            relation.setEntity2(entity2);
        }
    }

}
