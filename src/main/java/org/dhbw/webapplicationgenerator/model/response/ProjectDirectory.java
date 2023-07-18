package org.dhbw.webapplicationgenerator.model.response;

import lombok.Data;
import org.dhbw.webapplicationgenerator.webclient.exception.WagException;

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

    /**
     * Returns a subfolder of the directory if it exists.
     * Otherwise throws an Exception.
     * @param folderName: Folder name to find as subfolder of this directory.
     * @return ProjectDirectory: ProjectDirectory
     */
    public ProjectDirectory findChildDirectory(String folderName) {
        return this.getChildren().stream()
                .filter(child -> child.getTitle().equals(folderName))
                .map(ProjectDirectory.class::cast)
                .findFirst()
                .orElseThrow(() -> new WagException("Finding folder " + folderName + " in path " + this.getPath() + " failed. The file does not exist."));
    }

    public StructureLevel getLevel() {
        return StructureLevel.DIRECTORY;
    }

}
