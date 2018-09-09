package de.sp.main.services.settings;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;
import de.sp.main.StartServer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.Map;

public class SettingsSerializer implements JsonSerializer<HashMap<String, BaseSettings>> {

    private Map<String, Class> settingsTypes;


    public SettingsSerializer(Map<String, Class> settingsTypes) {
        this.settingsTypes = settingsTypes;
    }

    @Override
    public JsonElement serialize(HashMap<String, BaseSettings> src, Type typeOfSrc,
                                 JsonSerializationContext context) {
        if (src == null)
            return null;
        else {
            JsonObject jo = new JsonObject();
            src.forEach((key, value) -> {

                Class c = settingsTypes.get(key);

                if(c == null){
                    Logger logger = LoggerFactory.getLogger(StartServer.class);
                    logger.warn("Type to serialize settings-section '" + key + "' not found", this.getClass());
                } else {
                    jo.add(key, context.serialize(value, c));
                }
            });

            return jo;
        }
    }
}