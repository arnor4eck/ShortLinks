package com.arnor4eck.ShortLinks.entity.user.role;

import com.fasterxml.jackson.core.JacksonException;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;

import java.io.IOException;

public class RoleDeserializer extends JsonDeserializer<String> {
    @Override
    public String deserialize(JsonParser jsonParser,
                            DeserializationContext deserializationContext) throws IOException, JacksonException {
        String roleString = jsonParser.getText();

        return roleString == null ? null : roleString.trim().toUpperCase();
    }
}
