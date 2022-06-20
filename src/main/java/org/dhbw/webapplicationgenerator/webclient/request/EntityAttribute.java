package org.dhbw.webapplicationgenerator.webclient.request;

import lombok.Data;

@Data
public class EntityAttribute {
    private String name; // Name of the attribute
    private String title; // Title that is shown in the Frontend
    private String dataType;
    private String columnName;
    private boolean referenceAttribute = false;
    private boolean tableAttribute = false;
}
