package org.dhbw.webapplicationgenerator.model.response;

public enum StructureLevel {
    FILE(false),
    DIRECTORY(true);

    StructureLevel(boolean isDirectory) {
        this.isDirectory = isDirectory;
    }

    private boolean isDirectory;

    public boolean isDirectory() {
        return this.isDirectory;
    }

}
