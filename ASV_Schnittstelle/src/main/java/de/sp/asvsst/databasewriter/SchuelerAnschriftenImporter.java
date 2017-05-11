package de.sp.asvsst.databasewriter;

import java.util.ArrayList;
import java.util.List;

import org.sql2o.Connection;

import de.sp.asvsst.model.ASVAnschrift;
import de.sp.asvsst.model.ASVAnsprechpartner;
import de.sp.asvsst.model.ASVKommunikation;
import de.sp.asvsst.model.ASVPerson;
import de.sp.asvsst.model.ASVSchuelerAnschrift;
import de.sp.asvsst.model.ASVSchuelerin;
import de.sp.asvsst.model.asvwlstore.ASVWlStore;
import de.sp.asvsst.model.wertelisten.ASVKommunikationsTyp;
import de.sp.database.daos.basic.AddressDAO;
import de.sp.database.daos.basic.ContactDAO;
import de.sp.database.daos.basic.PersonDAO;
import de.sp.database.daos.basic.ValueDAO;
import de.sp.database.model.Address;
import de.sp.database.model.Person;
import de.sp.database.model.Student;
import de.sp.database.model.Value;
import de.sp.database.valuelists.ValueListType;
import de.sp.database.valuelists.VLPersonType;
import de.sp.database.valuelists.VLSex;

public class SchuelerAnschriftenImporter {

	public static void start(ASVToDatabaseWriter asvToDBWriter,
			Student student, Long school_id, ASVSchuelerin schuelerin,
			Connection con) throws Exception {

		PersonDAO.deletePersonsForStudent(student.getId(), con);
		ContactDAO.deleteByStudentId(student.getId(), con);

		List<Person> persons = new ArrayList<>();

		importSchuelerAnschriften(schuelerin.schueleranschriften,
				asvToDBWriter, student, schuelerin, school_id, con, persons);

		int i = 0;

		for (ASVAnsprechpartner asvAnsprechpartner : schuelerin.ansprechpartner) {
			importAnsprechpartner(asvAnsprechpartner, asvToDBWriter, student,
					schuelerin, school_id, con, i++);
		}

	}

	private static void importAnsprechpartner(
			ASVAnsprechpartner asvAnsprechpartner,
			ASVToDatabaseWriter asvToDBWriter, Student student,
			ASVSchuelerin schuelerin, Long school_id, Connection con, int order)
			throws Exception {

		ASVKommunikation asvKommunikation = asvAnsprechpartner.kommunikation;


		ASVWlStore wlStore = ASVWlStore.getInstance(); 
		
		Value contact_type = wlStore.findOrMakeValue("2009", asvKommunikation.typSchluessel, ValueListType.contact_type, school_id, con);
		
		ContactDAO.insert(asvKommunikation.nummer_adresse,
				asvAnsprechpartner.name, asvKommunikation.bemerkung, order,
				contact_type.getId(), null, student.getId(), null, con);

	}

	private static void importSchuelerAnschriften(
			List<ASVSchuelerAnschrift> asvSchuelerAnschriftList,
			ASVToDatabaseWriter asvToDBWriter, Student student,
			ASVSchuelerin schuelerin, Long school_id, Connection con,
			List<Person> persons) throws Exception {

		ASVSchuelerAnschrift anschriftMutter = null;
		ASVSchuelerAnschrift anschriftVater = null;
		ASVSchuelerAnschrift anschriftSchueler = null;
		ASVSchuelerAnschrift anschriftSonstiger = null;

		Address addressMutter = null;
		Address addressVater = null;
		Address addressSchueler = null;
		Address addressSonstiger = null;

		for (ASVSchuelerAnschrift asvSchuelerAnschrift : asvSchuelerAnschriftList) {

			ASVPerson asvPerson = asvSchuelerAnschrift.person;

			switch (asvSchuelerAnschrift.anschrift_wessen) {
			case 1:
				if (asvPerson != null) {
					asvPerson.isPrimaryLegalParent = true;
				}
			case 2:
				if (asvPerson != null) {
					asvPerson.isLegalParent = true;

					if (asvPerson.isMale()) {
						anschriftVater = asvSchuelerAnschrift;
					} else {
						anschriftMutter = asvSchuelerAnschrift;
					}
				}
				break;
			case 3:
				anschriftSchueler = asvSchuelerAnschrift;
				break;
			case 4:
				if (asvPerson != null) {
					anschriftSonstiger = asvSchuelerAnschrift;
				}
				break;
			}
		}

		boolean isGemeinsameAnschrift = false;
		ASVAnschrift gemeinsameAnschrift = null;

		// gemeinsame Anschrift?
		if (anschriftMutter != null && anschriftVater != null) {

			if (anschriftMutter.anschrift != null
					&& anschriftVater.anschrift != null) {
				if (anschriftMutter.anschrift.equals(anschriftVater.anschrift)) {
					isGemeinsameAnschrift = true;
					gemeinsameAnschrift = anschriftMutter.anschrift;
				}
			} else {
				isGemeinsameAnschrift = true;
				if (anschriftMutter.anschrift != null) {
					gemeinsameAnschrift = anschriftMutter.anschrift;
				} else if (anschriftVater.anschrift != null) {
					gemeinsameAnschrift = anschriftVater.anschrift;
				} else {
					isGemeinsameAnschrift = false;
				}

			}

		}

		if (isGemeinsameAnschrift) {

			String anschriftAnrede = anschriftMutter.person
					.getHerrnFrauWholeName() + "\n";
			anschriftAnrede += anschriftVater.person.getHerrnFrauWholeName();

			String anschriftSehrGeehrt = anschriftMutter.person
					.getBriefAnrede(true)
					+ ",\n"
					+ anschriftVater.person.getBriefAnrede(false) + ",";

			Address address = AddressDAO.insert(anschriftAnrede,
					gemeinsameAnschrift.strasse, gemeinsameAnschrift.nummer,
					gemeinsameAnschrift.postleitzahl,
					gemeinsameAnschrift.ortsbezeichnung,
					gemeinsameAnschrift.getStaatString(),
					gemeinsameAnschrift.postfach,
					gemeinsameAnschrift.postleitzahl_postfach,
					anschriftSehrGeehrt, con);

			addressMutter = address;
			addressVater = address;

		} else {
			addressMutter = makeAddress(anschriftMutter, student, con);
			addressVater = makeAddress(anschriftVater, student, con);
		}

		addressSonstiger = makeAddress(anschriftSonstiger, student, con);

		if (anschriftSchueler.anschrift != null && addressVater != null
				&& anschriftSchueler.anschrift.equals(anschriftVater.anschrift)) {
			addressSchueler = addressVater;
		}

		if (anschriftSchueler.anschrift != null && addressMutter != null
				&& anschriftSchueler.anschrift.equals(anschriftMutter.anschrift)) {
			addressSchueler = addressMutter;
		}

		if (addressSchueler == null) {
			addressSchueler = makeAddress(anschriftSchueler, student, con);
		}

		makePerson(anschriftMutter, student, VLPersonType.mother,
				addressMutter, school_id, con);
		makePerson(anschriftVater, student, VLPersonType.father, addressVater,
				school_id, con);
		makePerson(anschriftSonstiger, student, VLPersonType.other,
				addressSonstiger, school_id, con);

	}

	private static Person makePerson(ASVSchuelerAnschrift sanschrift,
			Student student, VLPersonType typ, Address address, Long school_id,
			Connection con) throws Exception {

		if (sanschrift == null || sanschrift.person == null) {
			return null;
		}

		ASVPerson asvPerson = sanschrift.person;

		Person person = PersonDAO.insert(asvPerson.familienname,
				asvPerson.vornamen, asvPerson.namensbestandteil_vorangestellt,
				asvPerson.namensbestandteil_nachgestellt, asvPerson
						.getAkadGradString(false), student.getId(), new Long(
						typ.getKey()), address.getId(),
				asvPerson.isLegalParent, asvPerson.isPrimaryLegalParent, con);

		if (sanschrift.kommunikationsdaten != null) {
			for (ASVKommunikation asvKommunikation : sanschrift.kommunikationsdaten) {
				makeKommunikation(asvKommunikation, person, school_id, con);
			}
		}

		return person;

	}

	private static void makeKommunikation(ASVKommunikation asvKommunikation,
			Person person, Long school_id, Connection con) throws Exception {

		ASVKommunikationsTyp asvKTyp = ASVKommunikationsTyp
				.findBySchluessel(asvKommunikation.typSchluessel);

		Value contact_type = ValueDAO.findOrWrite(school_id,
				ValueListType.contact_type.getKey(), asvKTyp.getSchluessel(), con,
				asvKTyp.getName(), asvKTyp.getAbbreviation(), 1);

		ContactDAO.insert(asvKommunikation.nummer_adresse,
				person.getFirstname() + " " + person.getSurname(),
				asvKommunikation.bemerkung, 1, contact_type.getId(),
				person.getId(), null, null, con);

	}

	private static Address makeAddress(ASVSchuelerAnschrift sanschrift,
			Student student, Connection con) throws Exception {

		if (sanschrift == null || sanschrift.anschrift == null) {
			return null;
		}

		ASVAnschrift anschrift = sanschrift.anschrift;

		String leading_text = getStudentLeadingText(student);
		String textSehrGeehrt = getStudentSehrGeehrt(student);

		if (sanschrift.person != null) {
			leading_text = sanschrift.person.getHerrnFrauWholeName();
			textSehrGeehrt = sanschrift.person.getBriefAnrede(true) + ",";
		}

		Address address = AddressDAO.insert(leading_text, anschrift.strasse,
				anschrift.nummer, anschrift.postleitzahl,
				anschrift.ortsbezeichnung, anschrift.getStaatString(),
				anschrift.postfach, anschrift.postleitzahl_postfach,
				textSehrGeehrt, con);

		return address;

	}

	private static String getStudentSehrGeehrt(Student student) {

		String s = student.getSex() == VLSex.male ? "Sehr geehrter Herr "
				: "Sehr geehrte Frau ";

		if (student.getBefore_surname() != null && !student.getBefore_surname().isEmpty()) {
			s += student.getBefore_surname() + " ";
		}

		s += student.getSurname();

		if (student.getAfter_surname() != null && !student.getAfter_surname().isEmpty()) {
			s += " " + student.getAfter_surname();
		}

		return s + ",";

	}

	private static String getStudentLeadingText(Student student) {

		String s = student.getSex() == VLSex.male ? "Herrn " : "Frau ";

		s += student.getFirstname() + " ";

		if (student.getBefore_surname() != null && !student.getBefore_surname().isEmpty()) {
			s += student.getBefore_surname() + " ";
		}

		s += student.getSurname();

		if (student.getAfter_surname() != null && !student.getAfter_surname().isEmpty()) {
			s += " " + student.getAfter_surname();
		}

		return s;

	}

}
