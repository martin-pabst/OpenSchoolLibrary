package de.sp.main.services.settings;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import de.sp.main.services.modules.Module;

import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.Map;

public class SettingsManager {

    private static SettingsManager instance = null;

    private Map<String, Class> userSettingsTypes = new HashMap<>();
    private Map<String, Class> schoolSettingsTypes = new HashMap<>();
    private Map<String, Class> systemSettingsTypes = new HashMap<>();

    public static SettingsManager getInstance(){

        if(instance == null){
            instance = new SettingsManager();
        }

        return instance;

    }

    public void registerModule(Module module){

        ModuleSettingsTypes mst = module.getModuleSettingsTypes();

        if(mst == null){
            return;
        }

        String identifier = module.getIdentifier();

        if(mst.getUserSettingsType() != null) {
            userSettingsTypes.put(identifier, mst.getUserSettingsType());
        }

        if(mst.getSchoolSettingsType() != null) {
            schoolSettingsTypes.put(identifier, mst.getSchoolSettingsType());
        }

        if(mst.getSystemSettingsType() != null) {
            systemSettingsTypes.put(identifier, mst.getSystemSettingsType());
        }

    }

    private String serializeSettings(HashMap<String, BaseSettings> settings, Map<String, Class> typeMap){

        Type type = new TypeToken<HashMap<String, BaseSettings>>(){}.getType();
        GsonBuilder gb = new GsonBuilder();

        gb.registerTypeAdapter(type, new SettingsDeserializer(typeMap));
        Gson gson = gb.create();

        return gson.toJson(settings);

    }

    public String serializeUserSettings(HashMap<String, BaseSettings> settings){
        return serializeSettings(settings, userSettingsTypes);
    }

    public String serializeSchoolSettings(HashMap<String, BaseSettings> settings){
        return serializeSettings(settings, schoolSettingsTypes);
    }

    public String serializeSystemSettings(HashMap<String, BaseSettings> settings){
        return serializeSettings(settings, systemSettingsTypes);
    }


    private HashMap<String, BaseSettings> deserializeSettings(String json, Map<String, Class> typeMap){

        Type type = new TypeToken<HashMap<String, BaseSettings>>(){}.getType();
        GsonBuilder gb = new GsonBuilder();

        gb.registerTypeAdapter(type, new SettingsDeserializer(typeMap));
        Gson gson = gb.create();

        return gson.fromJson(json, type);

    }

    public HashMap<String, BaseSettings> deserializeUserSettings(String json){
    
        return deserializeSettings(json, userSettingsTypes);

    }

    public HashMap<String, BaseSettings> deserializeSchoolSettings(String json){
    
        return deserializeSettings(json, userSettingsTypes);

    }

    public HashMap<String, BaseSettings> deserializeSystemSettings(String json){
    
        return deserializeSettings(json, userSettingsTypes);

    }


}
