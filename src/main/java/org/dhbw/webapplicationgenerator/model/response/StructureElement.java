package org.dhbw.webapplicationgenerator.model.response;

import java.util.List;

public interface StructureElement {
    String getTitle();
    void setTitle(String title);
    boolean hasChildren();
    List<StructureElement> getChildren();
    List<ProjectDirectory> getDirectoryChildren();
    void addChild(StructureElement child);
    StructureLevel getLevel();

    String getPath();

}
