package com.slq.scrappy.tokfm;


import com.google.gson.*;

import java.lang.reflect.Type;

public class RecordsDeserializer implements JsonDeserializer<Podcasts> {

    @Override
    public Podcasts deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
        JsonArray podcasts = (JsonArray) json.getAsJsonObject().get("records");
        return null;
    }
}
