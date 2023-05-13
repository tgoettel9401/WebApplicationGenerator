package org.dhbw.webapplicationgenerator.model.request.datamodel;

import lombok.Getter;

@Getter
public enum RelationType {
    ONE_TO_ONE(false, false), ONE_TO_MANY(true, false),
    MANY_TO_ONE(false, true), MANY_TO_MANY(true, true);

    /**
     * Gives true if it is a ONE_TO_MANY or MANY_TO_MANY relation and false for ONE_TO_ONE or MANY_TO_ONE.
     */
    private final boolean isToMany;
    /**
     * Gives true if it is a MANY_TO_ONE or MANY_TO_MANY relation and false for ONE_TO_ONE or ONE_TO_MANY.
     */
    private final boolean isFromMany;

    RelationType(boolean isToMany, boolean isFromMany) {
        this.isToMany = isToMany;
        this.isFromMany = isFromMany;
    }

}
