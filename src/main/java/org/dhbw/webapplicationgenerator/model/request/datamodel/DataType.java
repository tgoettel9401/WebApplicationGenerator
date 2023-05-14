package org.dhbw.webapplicationgenerator.model.request.datamodel;

import lombok.Getter;
import org.dhbw.webapplicationgenerator.webclient.exception.WagException;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Getter
public enum DataType {
    LOCAL_DATE("LocalDate", Arrays.asList("java.time.LocalDate", "org.springframework.format.annotation.DateTimeFormat"),
            "date", "DatePicker", "com.vaadin.flow.component.datepicker.DatePicker",
            "LocalDate.now()"),
    STRING("String", new ArrayList<>(), "text", "TextField",
            "com.vaadin.flow.component.textfield.TextField", "\"\""),
    INTEGER("Integer", new ArrayList<>(), "number", "NumberField",
            "com.vaadin.flow.component.textfield.NumberField", "0");

    private final String name;
    private final List<String> packageToImport;
    private final String htmlInputType;
    private final String vaadinFieldType;
    private final String vaadinPackageImport;
    private final String defaultValue;

    DataType (String name, List<String> packageToImport, String inputType, String vaadinFieldType,
              String vaadinPackageImport, String defaultValue) {
        this.name = name;
        this.packageToImport = packageToImport;
        this.htmlInputType = inputType;
        this.vaadinFieldType = vaadinFieldType;
        this.vaadinPackageImport = vaadinPackageImport;
        this.defaultValue = defaultValue;
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
