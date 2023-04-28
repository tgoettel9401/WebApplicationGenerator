package org.dhbw.webapplicationgenerator.generator.entity;

import lombok.Getter;
import org.dhbw.webapplicationgenerator.webclient.exception.WagException;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Getter
public enum DataType {
    LOCAL_DATE("LocalDate", Arrays.asList("java.time.LocalDate", "org.springframework.format.annotation.DateTimeFormat"), "date"),
    STRING("String", new ArrayList<>(), "text"),
    INTEGER("Integer", new ArrayList<>(), "number");

    private String name;
    private List<String> packageToImport;
    private String inputType;

    DataType (String name, List<String> packageToImport, String inputType) {
        this.name = name;
        this.packageToImport = packageToImport;
        this.inputType = inputType;
    }

    public static DataType fromName(String name) throws WagException {
        for (DataType type : values()) {
            if (type.name.equals(name)) {
                return type;
            }
        }
        throw new WagException("Data Type " + name + " is not supported");
    }

}
