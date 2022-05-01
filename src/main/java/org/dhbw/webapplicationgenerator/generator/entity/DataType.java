package org.dhbw.webapplicationgenerator.generator.entity;

import lombok.Getter;

@Getter
public enum DataType {
    LOCAL_DATE("LocalDate", "java.time.LocalDate"),
    UNKNOWN("", "");

    private String name;
    private String packageToImport;

    DataType (String name, String packageToImport) {
        this.name = name;
        this.packageToImport = packageToImport;
    }

    static DataType fromName(String name) {
        for (DataType type : values()) {
            if (type.name.equals(name)) {
                return type;
            }
        }
        return UNKNOWN;
    }

}
