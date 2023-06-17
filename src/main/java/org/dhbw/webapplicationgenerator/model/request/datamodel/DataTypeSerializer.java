package org.dhbw.webapplicationgenerator.model.request.datamodel;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;

import java.io.IOException;

public class DataTypeSerializer extends JsonDeserializer<DataType> {

    protected DataTypeSerializer() {
        super();
    }

    @Override
    public DataType deserialize(JsonParser jp, DeserializationContext ctxt) throws IOException {
        String dataTypeString = jp.readValueAs(String.class);
        return DataType.fromName(dataTypeString);
    }
}
