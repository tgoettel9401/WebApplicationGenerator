package org.dhbw.webapplicationgenerator.model.request.datamodel;

import lombok.Data;
import org.dhbw.webapplicationgenerator.generator.util.Utils;
import org.dhbw.webapplicationgenerator.webclient.exception.WagException;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

@Data
public class Entity {
    private String name;
    private String title;
    private String tableName;
    private List<Attribute> attributes = new ArrayList<>();
    private List<EntityRelation> relations = new ArrayList<>();

    /**
     * Always returns the name of the entity with all letters lowercase
     * @return name of the entity
     */
    public String getName() {
        return name.toLowerCase(Locale.ROOT);
    }

    /**
     * Always returns the name of the entity in plural with all letters lowercase
     * @return name of the entity in plural
     */
    public String getNamePlural() {
        return Utils.plural(getName());
    }

    /**
     * Either returns the title specified in the request or alternatively returns the name with the first letter uppercase
     * @return title of the entity
     */
    public String getTitle() {
        return title != null && !title.isEmpty() ? title : Utils.capitalize(name);
    }

    /**
     * Always returns the title of the entity in plural with the first letter uppercase.
     * @return title of the entity in plural
     */
    public String getTitlePlural() {
        return Utils.plural(getTitle());
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

    public Attribute getReferenceAttribute() {
        return attributes.stream().filter(Attribute::isReferenceAttribute)
                .findFirst()
                .orElseThrow(() -> new WagException("No Reference-Attribute was found for entity " + getClass().getName()));
    }

}
