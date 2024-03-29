package org.dhbw.webapplicationgenerator.webclient.request;

import lombok.Data;
import org.dhbw.webapplicationgenerator.model.request.datamodel.DataType;
import org.dhbw.webapplicationgenerator.generator.util.Utils;

import java.util.Locale;

@Data
public class EntityAttribute {
    private String name; // Name of the attribute
    private String title; // Title that is shown in the Frontend
    private String dataType;
    private String columnName;
    private boolean referenceAttribute = false;
    private boolean tableAttribute = false;

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

    /**
     * Returns the DataType (as object) of the attribute.
     * @return DataType as object
     */
    public DataType getDataTypeObject() {
        return DataType.fromName(dataType);
    }

}
