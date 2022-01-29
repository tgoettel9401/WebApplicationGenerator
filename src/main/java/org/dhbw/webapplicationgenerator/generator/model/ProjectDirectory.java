package org.dhbw.webapplicationgenerator.generator.model;

import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class ProjectDirectory implements StructureElement {

    private String title;
    private Boolean hasChildren;
    private String path;
    private List<StructureElement> children = new ArrayList<>();

    public Boolean hasChildren() {
        return this.hasChildren;
    }

    public void addChild(StructureElement child) {
        this.children.add(child);
    }

    public StructureLevel getLevel() {
        return StructureLevel.DIRECTORY;
    }

}
