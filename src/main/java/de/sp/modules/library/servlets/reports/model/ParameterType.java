package de.sp.modules.library.servlets.reports.model;

/**
 * Created by Martin on 21.04.2017.
 */
public enum ParameterType {

    typeBoolean("boolean"), typeInteger("int"), typeString("String");

    private String name;

    ParameterType(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }
}
