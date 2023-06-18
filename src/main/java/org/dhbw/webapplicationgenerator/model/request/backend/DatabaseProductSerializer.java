package org.dhbw.webapplicationgenerator.model.request.backend;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;

import java.io.IOException;

public class DatabaseProductSerializer extends JsonDeserializer<DatabaseProduct> {

    protected DatabaseProductSerializer() {
        super();
    }

    @Override
    public DatabaseProduct deserialize(JsonParser jp, DeserializationContext ctxt) throws IOException {
        String databaseProductString = jp.readValueAs(String.class);
        return DatabaseProduct.fromKey(databaseProductString);
    }
}
