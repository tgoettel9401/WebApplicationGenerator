package org.dhbw.webapplicationgenerator.webclient.request;

import lombok.Data;

import java.util.HashSet;
import java.util.Set;

@Data
public class RequestEntity {
    private String name; // Name of the entities, referenced also in other entities
    private String title; // Title that is used in the frontend
    private String tableName; // TableName for the Database
    private Set<EntityAttribute> attributes = new HashSet<>();
    private Set<EntityRelation> relations = new HashSet<>();
}
