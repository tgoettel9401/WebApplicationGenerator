package org.dhbw.webapplicationgenerator.webclient.request;

import lombok.Data;
import lombok.ToString;
import org.dhbw.webapplicationgenerator.generator.entity.RelationType;
import org.dhbw.webapplicationgenerator.generator.util.Utils;
import org.dhbw.webapplicationgenerator.webclient.exception.WagException;

@Data
public class EntityRelation {
    private String name;
    private Cardinality cardinalityMin;
    private Cardinality cardinalityMax;
    private String entityName;
    private String joinTable;
    private boolean owning = false;

    @ToString.Exclude
    private RequestEntity entityObject;

    public RelationType getRelationType() {
        if (cardinalityMin.equals(Cardinality.ONE) && cardinalityMax.equals(Cardinality.ONE)) {
            return RelationType.ONE_TO_ONE;
        }
        if (cardinalityMin.equals(Cardinality.ONE) && cardinalityMax.equals(Cardinality.MANY)) {
            return RelationType.ONE_TO_MANY;
        }
        if (cardinalityMin.equals(Cardinality.MANY) && cardinalityMax.equals(Cardinality.ONE)) {
            return RelationType.MANY_TO_ONE;
        }
        if (cardinalityMin.equals(Cardinality.MANY) && cardinalityMax.equals(Cardinality.MANY)) {
            return RelationType.MANY_TO_MANY;
        }
        throw new WagException("The combination of cardinalityMin and cardinalityMax is not known: " + cardinalityMin + ":" + cardinalityMax);
    }

    public boolean isOwning() {
        return owning || !getRelationType().equals(RelationType.ONE_TO_ONE);
    }

    public String getEntityClassName() {
        return Utils.capitalize(entityName);
    }

    public String getEntityNamePlural() {
        return Utils.plural(entityName);
    }

    public String getEntityClassNamePlural() {
        return Utils.capitalize(Utils.plural(entityName));
    }

}
