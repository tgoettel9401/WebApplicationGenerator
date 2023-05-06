package org.dhbw.webapplicationgenerator.model.request.datamodel;

import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class DataModel {
    private List<Entity> entities = new ArrayList<>();
    private List<Relation> relations = new ArrayList<>();
}
