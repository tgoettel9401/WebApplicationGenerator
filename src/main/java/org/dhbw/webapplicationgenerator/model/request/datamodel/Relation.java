package org.dhbw.webapplicationgenerator.model.request.datamodel;

import lombok.Data;
import org.dhbw.webapplicationgenerator.webclient.request.Cardinality;

@Data
public class Relation {
    private String name;
    private String attributeName1;
    private String attributeName2;
    private String joinTable;

    private String entityName1;
    private String entityName2;
    private String owningSide; // Maps to owningEntity

    private Entity entity1;
    private Entity entity2;
    private Entity owningEntity;

    private Cardinality cardinality1;
    private Cardinality cardinality2;
}
