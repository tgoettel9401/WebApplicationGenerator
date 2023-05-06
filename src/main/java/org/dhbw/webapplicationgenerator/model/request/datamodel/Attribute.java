package org.dhbw.webapplicationgenerator.model.request.datamodel;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import lombok.Data;

@Data
public class Attribute {
    private String name;
    private String columnName;
    private String title;
    @JsonDeserialize(using = DataTypeSerializer.class)
    private DataType dataType;
    private boolean referenceAttribute;
    private boolean tableAttribute;
}
