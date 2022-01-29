package org.dhbw.webapplicationgenerator.generator.model;

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
