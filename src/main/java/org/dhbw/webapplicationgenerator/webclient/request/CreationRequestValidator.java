package org.dhbw.webapplicationgenerator.webclient.request;

import org.dhbw.webapplicationgenerator.generator.entity.DataType;
import org.dhbw.webapplicationgenerator.webclient.exception.ValidationException;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.Set;

@Service
public class CreationRequestValidator {

    /**
     * Performs necessary Validations for a Creation Request.
     *
     * @param request CreationRequest
     * @throws ValidationException Exception providing the validation error
     */
    public void validate(CreationRequest request) throws ValidationException {
        validateRequiredFields(request);
        validateDataTypes(request);
        validateRelationNames(request);
    }

    /**
     * Validates that all required fields are present in the request. Also checks if any if the name fields contains spaces.
     *
     * @param request CreationRequest
     */
    private void validateRequiredFields(CreationRequest request) throws ValidationException {

        if (request.getProject() == null) {
            throw new ValidationException("Project is missing");
        }

        if (request.getProject().getTitle() == null) {
            throw new ValidationException("Project-Title is missing");
        }

        if (request.getProject().getGroup() == null) {
            throw new ValidationException("Project-Group is missing");
        }

        if (request.getProject().getArtifact() == null) {
            throw new ValidationException("Project-Artifact is missing");
        }

        for (RequestEntity entity : request.getEntities()) {
            if (entity.getName() == null) {
                throw new ValidationException("Name of entity " + entity + " is not set");
            }

            if (entity.getName().contains(" ")) {
                throw new ValidationException("Name of entity " + entity + " contains spaces - this is not allowed");
            }

            for (EntityAttribute attribute : entity.getAttributes()) {
                if (attribute.getName() == null) {
                    throw new ValidationException("Name of Attribute " + attribute + " of entity " + entity + " is not set");
                }

                if (attribute.getName().contains(" ")) {
                    throw new ValidationException("Name of Attribute " + attribute + " of entity " + entity + " contains spaces - this is not allowed");
                }

                if (attribute.getDataType() == null) {
                    throw new ValidationException("DataType of Attribute " + attribute + " of entity " + entity + " is not set");
                }
            }

            for (EntityRelation relation : entity.getRelations()) {
                if (relation.getName() == null || relation.getName().isEmpty()) {
                    throw new ValidationException("Name of Relation " + relation + " of entity " + entity + " is not set");
                }

                if (relation.getName().contains(" ")) {
                    throw new ValidationException("Name of Relation " + relation + " of entity " + entity + " contains spaces - this is not allowed");
                }

                if (relation.getCardinalityMin() == null) {
                    throw new ValidationException("Value of CardinalityMin of Relation " + relation + " of entity " + entity + " is not set");
                }

                if (relation.getCardinalityMax() == null) {
                    throw new ValidationException("Value of CardinalityMax of Relation " + relation + " of entity " + entity + " is not set");
                }

                if (relation.getEntity() == null) {
                    throw new ValidationException("Entity of Relation " + relation + " of entity " + entity + " is not set");
                }
            }

        }

    }

    /**
     * Validates only allowed DataTypes are present in the request.
     *
     * @param request CreationRequest
     */
    private void validateDataTypes(CreationRequest request) throws ValidationException {
        for (RequestEntity entity : request.getEntities()) {
            for (EntityAttribute attribute : entity.getAttributes()) {
                try {
                    DataType.fromName(attribute.getDataType());
                } catch (Exception ex) {
                    throw new ValidationException("Converting " + attribute.getDataType() + " to DataType failed with Exception '" + ex.getMessage() + "'");
                }
            }
        }
    }

    /**
     * Validates that referenced relations all exist in the request
     *
     * @param request CreationRequest
     */
    private void validateRelationNames(CreationRequest request) throws ValidationException {

        Set<String> entityNames = new HashSet<>();
        for (RequestEntity entity : request.getEntities()) {
            entityNames.add(entity.getName());
        }

        for (RequestEntity entity : request.getEntities()) {
            for (EntityRelation relation : entity.getRelations()) {
                if (!entityNames.contains(relation.getEntity())) {
                    throw new ValidationException("The relation " + relation.getName() + " contains the referenced entity " + relation.getEntity() + ", however this is not set as an entity in the request.");
                }
            }
        }

    }

}
