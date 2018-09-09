package de.sp.main.services.settings;

import com.google.gson.*;
import de.sp.main.StartServer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.Map;

public class SettingsDeserializer implements JsonDeserializer<HashMap<String, BaseSettings>> {

    private Map<String, Class> settingsTypes;


    public SettingsDeserializer(Map<String, Class> settingsTypes) {
        this.settingsTypes = settingsTypes;
    }


    public HashMap<String, BaseSettings> deserialize(JsonElement json, Type typeOfT,
                                                     JsonDeserializationContext context) throws JsonParseException {

        HashMap<String, BaseSettings> map = new HashMap<>();

        JsonObject jo = json.getAsJsonObject();

        for (Map.Entry<String, JsonElement> entry : jo.entrySet()) {

            Class c = settingsTypes.get(entry.getKey());

            if (c == null) {
                Logger logger = LoggerFactory.getLogger(StartServer.class);
                logger.warn("Type to deserialize settings-section '" + entry.getKey() + "' not found", this.getClass());
            } else {
                map.put(entry.getKey(), context.deserialize(entry.getValue(), c));
            }

        }

        return map;

    }

}
