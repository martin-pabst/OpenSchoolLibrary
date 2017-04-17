package de.sp.modules.library.servlets.reports.model;

/**
 * Created by Martin on 17.04.2017.
 */
public enum DataType {

    schueler(1), lehrer(2), klassen(3), buecher(4), sonstige(5);

    private int id;

    DataType(int id) {
        this.id = id;
    }

    public static DataType findById(long id) {

        for (DataType dataType : DataType.values()) {
            if (dataType.id == id) {
                return dataType;
            }
        }

        return null;

    }

    public int getId() {
        return id;
    }
}
