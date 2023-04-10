package org.dhbw.webapplicationgenerator.webclient.request;

import lombok.Data;
import org.dhbw.webapplicationgenerator.generator.util.Utils;
import org.dhbw.webapplicationgenerator.webclient.exception.WagException;

import java.util.HashSet;
import java.util.Locale;
import java.util.Set;

@Data
public class RequestEntity {
    private String name; // Name of the entities, referenced also in other entities
    private String title; // Title that is used in the frontend
    private String tableName; // TableName for the Database
    private Set<EntityAttribute> attributes = new HashSet<>();
    private Set<EntityRelation> relations = new HashSet<>();

    /**
     * Always returns the name of the entity with all letters lowercase
     * @return name of the entity
     */
    public String getName() {
        return name.toLowerCase(Locale.ROOT);
    }

    /**
     * Either returns the title specified in the request or alternatively returns the name with the first letter uppercase
     * @return title of the entity
     */
    public String getTitle() {
        return title != null && !title.isEmpty() ? title : Utils.capitalize(name);
    }

    /**
     * Gets the classname of the entity (name of the entity with the first letter in uppercase)
     * @return classname of the entity
     */
    public String getClassName() {
        return Utils.capitalize(name);
    }

    /**
     * Gets the variable name of the entity's repository (first letter uppercase)
     * @return class name of Repository
     */
    public String getRepositoryClassName() {
        return this.getTitle() + "Repository";
    }

    /**
     * Gets the variable name of the entity's repository (first letter lowercase)
     * @return variable name of Repository
     */
    public String getRepositoryVariableName() {
        return this.getName() + "Repository";
    }

    /**
     * Gets the variable name of the entity's repository (first letter uppercase)
     * @return class name of Repository
     */
    public String getControllerClassName() {
        return this.getTitle() + "Controller";
    }

    public EntityAttribute getReferenceAttribute() {
        return attributes.stream().filter(EntityAttribute::isReferenceAttribute)
                .findFirst()
                .orElseThrow(() -> new WagException("No Reference-Attribute was found for entity " + getClass().getName()));
    }

}
