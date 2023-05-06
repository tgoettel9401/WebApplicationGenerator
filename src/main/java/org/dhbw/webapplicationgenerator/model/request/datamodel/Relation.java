package org.dhbw.webapplicationgenerator.model.request.datamodel;

import lombok.Data;

@Data
public class Relation {
    private String name;
    private String attributeName1;
    private String attributeName2;
    private String joinTable;

    private String entityName1;
    private String entityName2;

    private Entity entity1;
    private Entity entity2;
    private Entity owningEntity;

    private String cardinality1;
    private String cardinality2;
}
