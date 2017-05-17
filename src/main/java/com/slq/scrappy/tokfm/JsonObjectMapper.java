package com.slq.scrappy.tokfm;

import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.IOException;

public final class JsonObjectMapper {

    private ObjectMapper objectMapper = new ObjectMapper();

    private JsonObjectMapper() {
    }

    public static JsonObjectMapper createJsonObjectMapper() {
        return new JsonObjectMapper();
    }

    public String writeValueAsString(Object value) {
        try {
            return objectMapper.writeValueAsString(value);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public <T> T readValue(String message, Class<T> resultClass) {
        try {
            return objectMapper.readValue(message, resultClass);
        } catch (IOException e) {
            throw new RuntimeException(message, e);
        }
    }
}
