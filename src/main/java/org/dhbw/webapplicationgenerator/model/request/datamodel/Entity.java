package org.dhbw.webapplicationgenerator.model.request.datamodel;

import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class Entity {
    private String name;
    private String title;
    private String tableName;
    private List<Attribute> attributes = new ArrayList<>();
}
