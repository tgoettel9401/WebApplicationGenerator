package org.dhbw.webapplicationgenerator.generator.entity;

import lombok.Getter;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Getter
public enum DataType {
    LOCAL_DATE("LocalDate", Arrays.asList("java.time.LocalDate", "org.springframework.format.annotation.DateTimeFormat"), "date"),
    STRING("String", new ArrayList<>(), "text"),
    INTEGER("Integer", new ArrayList<>(), "number"),
    UNKNOWN("", new ArrayList<>(), "");

    private String name;
    private List<String> packageToImport;
    private String inputType;

    DataType (String name, List<String> packageToImport, String inputType) {
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
