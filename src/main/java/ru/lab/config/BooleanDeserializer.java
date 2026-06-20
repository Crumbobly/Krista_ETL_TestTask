package ru.lab.config;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;

import java.io.IOException;

public class BooleanDeserializer extends JsonDeserializer<Boolean> {

    @Override
    public Boolean deserialize(JsonParser p, DeserializationContext ctxt) throws IOException {
        String value = p.getText();

        if ("1".equals(value)) {
            return Boolean.TRUE;
        } else if ("0".equals(value)) {
            return Boolean.FALSE;
        }

        return Boolean.parseBoolean(value);
    }
}
