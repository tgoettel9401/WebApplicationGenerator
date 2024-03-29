package org.dhbw.webapplicationgenerator.model.response;

import lombok.Data;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

@Data
public class ProjectFile implements StructureElement {

    private String title;
    private StructureLevel level = StructureLevel.FILE;
    private File file;
    private String path;

    @Override
    public boolean hasChildren() {
        return false;
    }

    @Override
    public List<StructureElement> getChildren() {
        return new ArrayList<>();
    }

    @Override
    public List<ProjectDirectory> getDirectoryChildren() {
        throw new UnsupportedOperationException("Used getDirectoryChildren() on a ProjectFile.");
    }

    @Override
    public void addChild(StructureElement child) {
        // Nothing happens
    }

}
