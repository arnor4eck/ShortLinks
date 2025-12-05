package com.arnor4eck.ShortLinks.utils;

import com.arnor4eck.ShortLinks.entity.user.Role;
import com.fasterxml.jackson.core.JacksonException;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;

import java.io.IOException;

public class RoleDeserializer extends JsonDeserializer<Role> {
    @Override
    public Role deserialize(JsonParser jsonParser, DeserializationContext deserializationContext) throws IOException, JacksonException {
        String roleString = jsonParser.getText();

        if(roleString == null || roleString.trim().isEmpty())
            throw new IOException("Поле role не может быть пустым.");

        try{
            return Role.valueOf(roleString.trim().toUpperCase());
        } catch (IllegalArgumentException e) {
            throw new IOException("Некорректное значение поля role.");
        }
    }
}
