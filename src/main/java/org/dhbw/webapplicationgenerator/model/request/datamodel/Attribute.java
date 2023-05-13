package org.dhbw.webapplicationgenerator.model.request.datamodel;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import lombok.Data;
import org.dhbw.webapplicationgenerator.generator.util.Utils;

import java.util.Locale;

@Data
public class Attribute {
    private String name;
    private String columnName;
    private String title;
    @JsonDeserialize(using = DataTypeSerializer.class)
    private DataType dataType;
    private boolean referenceAttribute;
    private boolean tableAttribute;

    /**
     * Returns the name of the attribute with all letters lowercase.
     * @return Name of the attribute
     */
    public String getName() {
        return name.toLowerCase(Locale.ROOT);
    }

    /**
     * Returns the name of the attribute with the first letter uppercase.
     * @return Title of the attribute
     */
    public String getTitle() {
        return Utils.capitalize(name);
    }

}
