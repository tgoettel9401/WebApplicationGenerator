package org.dhbw.webapplicationgenerator.generator.model;

import lombok.Data;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

@Data
public class ProjectFile implements StructureElement {

    private String title;
    private StructureLevel level = StructureLevel.FILE;
    private File file;

    @Override
    public Boolean hasChildren() {
        return false;
    }

    @Override
    public List<StructureElement> getChildren() {
        return new ArrayList<>();
    }

    @Override
    public void addChild(StructureElement child) {
        // Nothing happens
    }

}
