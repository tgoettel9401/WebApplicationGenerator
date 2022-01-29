package org.dhbw.webapplicationgenerator.generator.model;

import java.util.List;

public interface StructureElement {
    String getTitle();
    void setTitle(String title);
    Boolean hasChildren();
    List<StructureElement> getChildren();
    void addChild(StructureElement child);
    StructureLevel getLevel();

    String getPath();

}
