package org.dhbw.webapplicationgenerator.webclient.request;

import lombok.Data;

@Data
public class EntityRelation {
    private String name;
    private Cardinality cardinalityMin;
    private Cardinality cardinalityMax;
    private String entity;
}
