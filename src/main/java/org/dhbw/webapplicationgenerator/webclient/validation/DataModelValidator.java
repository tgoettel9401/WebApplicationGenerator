package org.dhbw.webapplicationgenerator.webclient.validation;

import org.dhbw.webapplicationgenerator.model.request.ProjectRequest;
import org.dhbw.webapplicationgenerator.model.request.datamodel.Attribute;
import org.dhbw.webapplicationgenerator.model.request.datamodel.DataModel;
import org.dhbw.webapplicationgenerator.model.request.datamodel.Entity;
import org.dhbw.webapplicationgenerator.model.request.datamodel.Relation;
import org.dhbw.webapplicationgenerator.webclient.exception.ValidationException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class DataModelValidator implements RequestValidator {

    /**
     * Validates the dataModel request
     * - all required fields are present.
     */
    public void validate(ProjectRequest request) throws ValidationException {
        validateRequiredFields(request);
        validateEntitiesAreUnique(request);
        validateReferenceAndTableAttributes(request);
        validateRelationEntitiesExist(request);
    }

    /**
     * Validates that all required fields are present in the request. Also checks if any if the name fields contains spaces.
     *
     * @param request CreationRequest
     */
    private void validateRequiredFields(ProjectRequest request) throws ValidationException {
        DataModel dataModel = request.getDataModel();
        if (dataModel == null) {
            throw new ValidationException("DataModel must not be empty");
        }
        validateEntities(dataModel);
        validateRelations(dataModel);
    }


    private void validateEntities(DataModel dataModel) throws ValidationException {
        List<Entity> entities = dataModel.getEntities();
        validateEntitiesNotEmpty(entities);
        for (Entity entity : entities) {
            validateEntity(entity);
        }
    }

    private void validateEntity(Entity entity) throws ValidationException {
        if (entity.getName() == null) {
            throw new ValidationException("Name of entity " + entity.getName() + " is not set");
        }

        if (entity.getName().contains(" ")) {
            throw new ValidationException("Name of entity " + entity.getName() + " contains spaces - this is not allowed");
        }

        for (Attribute attribute : entity.getAttributes()) {
            validateAttribute(attribute, entity);
        }
    }

    private void validateAttribute(Attribute attribute, Entity entity) throws ValidationException {
        if (attribute.getName() == null) {
            throw new ValidationException("Name of Attribute " + attribute + " of entity " + entity.getName() + " is not set");
        }

        if (attribute.getName().contains(" ")) {
            throw new ValidationException("Name of Attribute " + attribute.getName() + " of entity " + entity.getName() + " contains spaces - this is not allowed");
        }

        if (attribute.getDataType() == null) {
            throw new ValidationException("DataType of Attribute " + attribute.getName() + " of entity " + entity.getName() + " is not set");
        }
    }

    private void validateEntitiesNotEmpty(List<Entity> entities) throws ValidationException {
        if (entities.isEmpty()) {
            throw new ValidationException("At least one entity must be supplied");
        }
    }

    private void validateRelations(DataModel dataModel) throws ValidationException {
        List<Relation> relations = dataModel.getRelations();
        for (Relation relation : relations) {
            if (relation.getEntity1() == null) {
                throw new ValidationException("Entity1 has not been supplied or has not been found in entities");
            }
            if (relation.getEntity2() == null) {
                throw new ValidationException("Entity2 has not been supplied or has not been found in entities");
            }
            if (relation.getCardinality1() == null) {
                throw new ValidationException("Cardinality of Entity1 has not been supplied or has not been found in entities");
            }
            if (relation.getCardinality2() == null) {
                throw new ValidationException("Cardinality of Entity2 has not been supplied or has not been found in entities");
            }
        }
    }

    /**
     * Validates that all entities are unique.
     * @param request CreationRequest
     * @throws ValidationException Exception providing the validation error
     */
    private void validateEntitiesAreUnique(ProjectRequest request) throws ValidationException {
        List<Entity> allEntities = request.getDataModel().getEntities();
        List<String> currentEntityNames = new ArrayList<>();
        for (Entity entity : allEntities) {
            if (currentEntityNames.stream().anyMatch(name -> name.equals(entity.getName()))) {
                throw new ValidationException("Entity with name " + entity.getName() + " is not unique");
            } else {
                currentEntityNames.add(entity.getName());
            }
        }
    }

    /**
     * Validates all entities have exactly one ReferenceAttribute and at least one TableAttribute
     *
     * @param request CreationRequest
     */
    private void validateReferenceAndTableAttributes(ProjectRequest request) throws ValidationException {
        List<Entity> entities = request.getDataModel().getEntities();
        for (Entity entity: entities) {
            if (entity.getAttributes().stream().filter(Attribute::isReferenceAttribute).count() != 1) {
                throw new ValidationException("For entity " + entity.getName() + " there must be exactly one ReferenceAttribute");
            }
            if (entity.getAttributes().stream().noneMatch(Attribute::isTableAttribute)) {
                throw new ValidationException("For entity " + entity.getName() + " no TableAttribute has been specified");
            }
        }
    }

    /**
     * Validates that referenced relations all exist in the request
     *
     * @param request CreationRequest
     */
    private void validateRelationEntitiesExist(ProjectRequest request) throws ValidationException {

        // TODO: Verify if this is really needed due to automatic mapping to entities in DataModelTransformer?!

        List<Entity> entities = request.getDataModel().getEntities();
        List<Relation> relations = request.getDataModel().getRelations();
        for (Relation relation : relations) {
            if (!entities.contains(relation.getEntity1())) {
                throw new ValidationException("The relation " + relation.getName() + " contains the entity1 " +
                        relation.getEntity1().getName() + " that does not exist in the entities");
            }
            if (!entities.contains(relation.getEntity2())) {
                throw new ValidationException("The relation " + relation.getName() + " contains the entity2 " +
                        relation.getEntity2().getName() + " that does not exist in the entities");
            }
        }

    }

}

