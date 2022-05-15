package org.dhbw.webapplicationgenerator.generator.entity;

import lombok.Getter;

@Getter
public enum DataType {
    LOCAL_DATE("LocalDate", "java.time.LocalDate", "date"),
    STRING("String", "", "text"),
    INTEGER("Integer", "", "number"),
    UNKNOWN("", "", "");

    private String name;
    private String packageToImport;
    private String inputType;

    DataType (String name, String packageToImport, String inputType) {
        this.name = name;
        this.packageToImport = packageToImport;
        this.inputType = inputType;
    }

    public static DataType fromName(String name) {
        for (DataType type : values()) {
            if (type.name.equals(name)) {
                return type;
            }
        }
        return UNKNOWN;
    }

}
