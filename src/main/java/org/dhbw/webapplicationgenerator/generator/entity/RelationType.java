package org.dhbw.webapplicationgenerator.generator.entity;

import lombok.Getter;

@Getter
public enum RelationType {
    ONE_TO_ONE(false), ONE_TO_MANY(true), MANY_TO_ONE(false), MANY_TO_MANY(true);

    private boolean isToMany;

    RelationType(boolean isToMany) {
        this.isToMany = isToMany;
    }

}
