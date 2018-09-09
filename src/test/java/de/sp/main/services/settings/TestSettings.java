package de.sp.main.services.settings;

public class TestSettings extends BaseSettings {

    private boolean boolValue = true;
    private String stringValue = "Test";
    private double doubleValue;

    public TestSettings(boolean boolValue, String stringValue, double doubleValue) {
        this.boolValue = boolValue;
        this.stringValue = stringValue;
        this.doubleValue = doubleValue;
    }

    public boolean isBoolValue() {
        return boolValue;
    }

    public String getStringValue() {
        return stringValue;
    }

    public double getDoubleValue() {
        return doubleValue;
    }
}
