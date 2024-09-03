package com.coreyd97.stepper.step.serializer;

import com.coreyd97.stepper.variable.RegexVariable;
import com.coreyd97.stepper.variable.StepVariable;
import com.coreyd97.stepper.step.Step;
import com.google.gson.*;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.List;
import java.util.Vector;

public class StepSerializer implements JsonSerializer<Step>, JsonDeserializer<Step> {

    @Override
    public Step deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
        JsonObject jsonObject = json.getAsJsonObject();
        Step step = new Step();
        step.setTitle(jsonObject.has("title") ? jsonObject.get("title").getAsString() : "Unnamed Step");
        step.setHostname(jsonObject.get("host") != null ? jsonObject.get("host").getAsString() : "" );
        step.setPort(jsonObject.get("port") != null ? jsonObject.get("port").getAsInt() : 443 );
        step.setSSL(jsonObject.get("ssl") == null || jsonObject.get("ssl").getAsBoolean());
        step.setRequestBody(jsonObject.get("request") != null ? jsonObject.get("request").getAsString().getBytes() : "".getBytes());
        List<StepVariable> variables = context.deserialize(
                jsonObject.getAsJsonArray("variables"), new TypeToken<List<StepVariable>>(){}.getType());
        for (StepVariable variable : variables) {
            step.getVariableManager().addVariable(variable);
        }
        return step;
    }

    @Override
    public JsonElement serialize(Step src, Type typeOfSrc, JsonSerializationContext context) {
        JsonObject json = new JsonObject();
        json.addProperty("title", src.getTitle());
        json.addProperty("host", src.getHostname());
        json.addProperty("port", src.getPort());
        json.addProperty("ssl", src.isSSL());
        json.addProperty("request", new String(src.getRequest()));
        json.add("variables", context.serialize(src.getVariableManager().getVariables(), new TypeToken<List<StepVariable>>(){}.getType()));
        return json;
    }
}
