package org.dhbw.webapplicationgenerator.webclient.request;

import lombok.Data;
import org.dhbw.webapplicationgenerator.webclient.exception.WagException;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Data
public class RequestEntity {
    private String name; // Name of the entities, referenced also in other entities
    private String title; // Title that is used in the frontend
    private String tableName; // TableName for the Database
    private Set<EntityAttribute> attributes = new HashSet<>();
    private Set<EntityRelation> relations = new HashSet<>();

    public EntityAttribute getReferenceAttribute() {
        return attributes.stream().filter(EntityAttribute::isReferenceAttribute)
                .findFirst()
                .orElseThrow(() -> new WagException("No Reference-Attribute was found for entity " + getClass().getName()));
    }

    public List<EntityAttribute> getTableAttributes() {
        return attributes.stream().filter(EntityAttribute::isTableAttribute)
                .collect(Collectors.toList());
    }

}
