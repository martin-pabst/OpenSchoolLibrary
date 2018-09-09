package de.sp.main.services.settings;

import org.junit.Assert;
import org.junit.Test;

import java.util.HashMap;

public class SettingsManagerTest {
    @Test
    public void serializeUserSettings() throws Exception {

        SettingsManager sm = SettingsManager.getInstance();
        MockupModule mm = new MockupModule();

        sm.registerModule(mm);

        TestSettings ts = new TestSettings(true, "TestString", 1.23);

        HashMap<String, BaseSettings> userSettings = new HashMap<>();

        userSettings.put(mm.getIdentifier(), ts);

        String json = sm.serializeUserSettings(userSettings);
        
        HashMap<String, BaseSettings> newUserSettings = sm.deserializeUserSettings(json);

        TestSettings newTs = (TestSettings)newUserSettings.get(mm.getIdentifier());

        Assert.assertEquals(newTs.getDoubleValue(), 1.23, 0.0001);
        Assert.assertEquals(newTs.getStringValue(), "TestString");
        Assert.assertEquals(newTs.isBoolValue(), true);

        String newJson = "{\"mockupModule\":{\"boolValue\":true,\"stringValue\":\"TestString\",\"doubleValue\":1.23,\"additionalValue\":1.23}}";

        HashMap<String, BaseSettings> new2UserSettings = sm.deserializeUserSettings(json);

        TestSettings new2Ts = (TestSettings)new2UserSettings.get(mm.getIdentifier());

        Assert.assertEquals(new2Ts.getDoubleValue(), 1.23, 0.0001);
        Assert.assertEquals(new2Ts.getStringValue(), "TestString");
        Assert.assertEquals(new2Ts.isBoolValue(), true);
        
    }

}