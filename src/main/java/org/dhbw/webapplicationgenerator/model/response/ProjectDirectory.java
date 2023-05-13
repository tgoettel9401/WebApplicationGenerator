package org.dhbw.webapplicationgenerator.model.response;

import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class ProjectDirectory implements StructureElement {

    private String title;
    private String path;
    private List<StructureElement> children = new ArrayList<>();

    @Override
    public List<ProjectDirectory> getDirectoryChildren() {
        List<ProjectDirectory> directories = new ArrayList<>();
        for (StructureElement child : children) {
            if (child.hasChildren()) {
                directories.add((ProjectDirectory) child);
            }
        }
        return directories;
    }

    public boolean hasChildren() {
        return !this.children.isEmpty();
    }

    public void addChild(StructureElement child) {
        this.children.add(child);
    }

    public StructureLevel getLevel() {
        return StructureLevel.DIRECTORY;
    }

}
