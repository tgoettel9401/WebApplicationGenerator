package org.dhbw.webapplicationgenerator.webclient.request;

import org.dhbw.webapplicationgenerator.generator.entity.DataType;
import org.dhbw.webapplicationgenerator.generator.entity.RelationType;
import org.dhbw.webapplicationgenerator.webclient.exception.ValidationException;
import org.dhbw.webapplicationgenerator.webclient.exception.WagException;
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
        validateReferenceAndTableAttributes(request);
        validateUniquenessOfEntities(request);
        validateRelationNames(request);
        validateDockerRequest(request);
    }

    /**
     * Validates that all ManyToMany relations have JoinTables set (which are identical on both sides of the relation)
     *
     * @param request CreationRequest
     * @throws ValidationException Exception providing the validation error
     */
    public void validateManyToManyRelations(CreationRequest request) throws ValidationException {
        for (RequestEntity entity : request.getEntities()) {
            for (EntityRelation relation : entity.getRelations()) {
                if (relation.getRelationType().equals(RelationType.MANY_TO_MANY)) {
                    if (relation.getJoinTable() == null) {
                        throw new ValidationException("JoinTable of relation " + relation.getName() + " in entity " + entity.getName() + " is not set even though it is a ManyToMany Relation");
                    }
                    EntityRelation relationOnTheOtherSide = request.getEntities().stream()
                            // We filter for the entity on the other side of the relation
                            .filter(e -> e.getName().equals(relation.getEntityName()))
                            .findFirst().orElseThrow(() -> new WagException("Entity with name " + relation.getEntityName() + " not found"))
                            .getRelations().stream()
                            // We filter for the relation that points to the current entity
                            .filter(r -> r.getEntityName().equals(entity.getName()))
                            .findFirst().orElseThrow(() -> new WagException("Relation with name " + relation.getName() + " not found"));
                    if (!relation.getJoinTable().equals(relationOnTheOtherSide.getJoinTable())) {
                        throw new ValidationException("JoinTable of relation " + relation.getName() + " is different in the associated entities");
                    }
                }
                if (relation.getRelationType().equals(RelationType.ONE_TO_ONE)) {
                    EntityRelation relationOnTheOtherSide = request.getEntities().stream()
                            // We filter for the entity on the other side of the relation
                            .filter(e -> e.getName().equals(relation.getEntityName()))
                            .findFirst().orElseThrow(() -> new WagException("Entity with name " + relation.getEntityName() + " not found"))
                            .getRelations().stream()
                            // We filter for the relation that points to the current entity
                            .filter(r -> r.getEntityName().equals(entity.getName()))
                            .findFirst().orElseThrow(() -> new WagException("Relation with name " + relation.getName() + " not found"));
                    if (relation.isOwning() && relationOnTheOtherSide.isOwning() || !relation.isOwning() && !relationOnTheOtherSide.isOwning()) {
                        throw new ValidationException("OneToOne-Relation " + relation.getName() + " does not have exactly one owning side");
                    }
                }
            }
        }
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

                if (relation.getEntityCardinality() == null) {
                    throw new ValidationException("Value of Entity-Cardinality of Relation " + relation + " of entity " + entity + " is not set");
                }

                if (relation.getRelationCardinality() == null) {
                    throw new ValidationException("Value of Relation-Cardinality of Relation " + relation + " of entity " + entity + " is not set");
                }

                if (relation.getEntityName() == null) {
                    throw new ValidationException("Entity of Relation " + relation + " of entity " + entity + " is not set");
                }
            }

        }

    }

    /**
     * Validates that all entities are unique.
     * @param request CreationRequest
     * @throws ValidationException Exception providing the validation error
     */
    private void validateUniquenessOfEntities(CreationRequest request) throws ValidationException {
        Set<String> entityNames = new HashSet<>();
        for (RequestEntity entity : request.getEntities()) {
            if (entityNames.contains(entity.getName())) {
                throw new ValidationException("Entity with name " + entity.getName() + " is not unique");
            } else {
                entityNames.add(entity.getName());
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
     * Validates all entities have exactly one ReferenceAttribute and at least one TableAttribute
     *
     * @param request CreationRequest
     */
    private void validateReferenceAndTableAttributes(CreationRequest request) throws ValidationException {
        for(RequestEntity entity: request.getEntities()) {
            if (entity.getAttributes().stream().filter(EntityAttribute::isReferenceAttribute).count() != 1) {
                throw new ValidationException("For entity " + entity.getName() + " there must be exactly one ReferenceAttribute");
            }
            if (entity.getAttributes().stream().noneMatch(EntityAttribute::isTableAttribute)) {
                throw new ValidationException("For entity " + entity.getName() + " no TableAttribute has been specified");
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
                if (!entityNames.contains(relation.getEntityName())) {
                    throw new ValidationException("The relation " + relation.getName() + " contains the referenced entity " + relation.getEntityName() + ", however this is not set as an entity in the request.");
                }
            }
        }

    }

    /**
     * Validates the docker request
     * - imageName must be lowercase
     * @param request CreationRequest
     */
    private void validateDockerRequest(CreationRequest request) throws ValidationException {
        for (char letter : request.getDocker().getImageName().toCharArray()) {
            if (Character.isUpperCase(letter)) {
                throw new ValidationException("The provided imageName of docker-data has an uppercase letter but must " +
                        "only contain lowercase letters!");
            }
        }
    }
}
