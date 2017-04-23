package de.sp.asvsst.model.wertelisten;

import de.sp.database.daos.basic.SubjectDAO;
import de.sp.database.model.Subject;
import org.sql2o.Connection;

/**
 * Created by Martin on 23.04.2017.
 */
public enum ASVBesuchterReligionsunterricht {

    k("10", "rk", "römisch-katholisch", "0200100100"),
    ev("20", "ev", "evangelisch", "0200200100"),
    eth("80", "eth", "Ethik", "0200400100"),
    isr("50", "isr", "israelitisch", "0200300500"),
    orth("40", "orth", "orthodox", "0200300700");



    private String schluessel;
    private String kurzform;
    private String anzeigeform;
    private String asvFachschluessel; // zum besuchten Religionsunterricht gehörendes Fach

    private Long id; // Schlüssel der Tabelle subject

    ASVBesuchterReligionsunterricht(String schluessel, String kurzform, String anzeigeform, String fachschluessel) {
        this.schluessel = schluessel;
        this.kurzform = kurzform;
        this.anzeigeform = anzeigeform;
        this.asvFachschluessel = fachschluessel;
    }

    public static ASVBesuchterReligionsunterricht findBySchluessel(String schluessel){

        for (ASVBesuchterReligionsunterricht asvBesuchterReligionsunterricht : ASVBesuchterReligionsunterricht.values()) {
            if(asvBesuchterReligionsunterricht.schluessel.equals(schluessel)){
                return asvBesuchterReligionsunterricht;
            }
        }

        return null;

    }

    public String getSchluessel() {
        return schluessel;
    }

    public String getKurzform() {
        return kurzform;
    }

    public String getAnzeigeform() {
        return anzeigeform;
    }

    public String getAsvFachschluessel() {
        return asvFachschluessel;
    }

    public static void init(Connection con, Long school_id) {

        for (ASVBesuchterReligionsunterricht asvBesuchterReligionsunterricht : ASVBesuchterReligionsunterricht.values()) {
            Subject subject = SubjectDAO.findFirstBySchoolIDAndKey1(
                    school_id, asvBesuchterReligionsunterricht.asvFachschluessel, con);

            if(subject != null){
                asvBesuchterReligionsunterricht.id = subject.getId();
            }

        }

    }

    public Long getId() {
        return id;
    }
}
