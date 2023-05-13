package org.dhbw.webapplicationgenerator.webclient.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.ToString;
import org.dhbw.webapplicationgenerator.model.request.datamodel.RelationType;
import org.dhbw.webapplicationgenerator.generator.util.Utils;
import org.dhbw.webapplicationgenerator.webclient.exception.WagException;

import java.util.Locale;

@Data
public class EntityRelation {
    private String name;

    /**
     * Cardinality of the relation on the entity (this side)
     */
    private Cardinality entityCardinality;

    /**
     * Cardinality of the relation on the relation-entity (other side)
     */
    private Cardinality relationCardinality;

    @JsonProperty("entity") private String entityName;
    private String joinTable;
    private boolean owning = false;

    /**
     * Entity-Object on the relation-side
     */
    @ToString.Exclude
    private RequestEntity entityObject;

    public RelationType getRelationType() {
        if (entityCardinality.equals(Cardinality.ONE) && relationCardinality.equals(Cardinality.ONE)) {
            return RelationType.ONE_TO_ONE;
        }
        if (entityCardinality.equals(Cardinality.ONE) && relationCardinality.equals(Cardinality.MANY)) {
            return RelationType.ONE_TO_MANY;
        }
        if (entityCardinality.equals(Cardinality.MANY) && relationCardinality.equals(Cardinality.ONE)) {
            return RelationType.MANY_TO_ONE;
        }
        if (entityCardinality.equals(Cardinality.MANY) && relationCardinality.equals(Cardinality.MANY)) {
            return RelationType.MANY_TO_MANY;
        }
        throw new WagException("The combination of entityCardinality and relationCardinality is not known: " + entityCardinality + ":" + relationCardinality);
    }

    /**
     * Returns the name of the relation with all letters lowercase.
     * @return Name of the relation
     */
    public String getName() {
        return name.toLowerCase(Locale.ROOT);
    }

    /**
     * Returns the name of the relation with all letters lowercase in plural.
     * @return Name of the relation in plural
     */
    public String getNamePlural() {
        return name.toLowerCase(Locale.ROOT);
    }

    /**
     * Returns the name of the relation with the first letter uppercase.
     * @return Title of the attribute
     */
    public String getTitle() {
        return Utils.capitalize(name);
    }

    public boolean isOwning() {
        return owning || !getRelationType().equals(RelationType.ONE_TO_ONE);
    }

    /**
     * Always returns the entity's name with all letters lowercase.
     * @return entityName
     */
    public String getEntityName() {
        return entityName.toLowerCase(Locale.ROOT);
    }

    public String getEntityClassName() {
        return Utils.capitalize(entityName);
    }

    /**
     * Calculates the ClassName of the repository associated with the related entity. This is always the entity's
     * classname appended by the String "Repository".
     * @return ClassName of entity's Repository
     */
    public String getRepositoryClassName() {
        return Utils.capitalize(entityName) + "Repository";
    }

    /**
     * Calculates the VariableName of the repository associated with the related entity. This is always the entity's
     * name appended by the String "Repository".
     * @return ClassName of entity's Repository
     */
    public String getRepositoryVariableName() {
        return getEntityName() + "Repository";
    }

    public String getEntityNamePlural() {
        return Utils.plural(entityName);
    }

    public String getEntityClassNamePlural() {
        return Utils.capitalize(Utils.plural(entityName));
    }

}
