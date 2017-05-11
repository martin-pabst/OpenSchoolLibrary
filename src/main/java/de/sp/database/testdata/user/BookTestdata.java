package de.sp.database.testdata.user;

import de.sp.database.connection.ConnectionPool;
import de.sp.database.daos.basic.BookDAO;
import de.sp.database.daos.basic.BookFormDAO;
import de.sp.database.daos.basic.SubjectDAO;
import de.sp.database.daos.basic.ValueDAO;
import de.sp.database.model.Book;
import de.sp.database.model.Subject;
import de.sp.database.model.Value;
import de.sp.database.valuelists.ValueListType;
import de.sp.tools.file.FileTool;
import de.sp.tools.string.FormTool;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.DataFormatter;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.sql2o.Connection;

import java.io.InputStream;
import java.util.List;

public class BookTestdata {

	private List<Value> jahrgangsstufen;

	private List<Value> ausbildungsrichtungen;

	private DataFormatter dataFormatter = new DataFormatter();


	public void test() throws Exception {

		Logger logger = LoggerFactory.getLogger(this.getClass());
		logger.info("Inserting CSG books");


		try (Connection con = ConnectionPool.open()) {


			Long school_id = SchoolTestdata.exampleSchool.getId();

			jahrgangsstufen = ValueDAO.findBySchoolAndValueStore(school_id, ValueListType.form.getKey(), con);

			ausbildungsrichtungen = ValueDAO.findBySchoolAndValueStore(school_id, ValueListType.curriculum.getKey(), con);

			String pathname = "/database/testdata/Buecher_CSG.xlsx";

			logger.info("Hole Daten der Scheiner-Bücher aus der Datei " + pathname);

			InputStream fis = FileTool.getInputStream(pathname);

			XSSFWorkbook wb = new XSSFWorkbook(fis);

			XSSFSheet sh = wb.getSheet("Tabelle1");

			int lastRowNum = sh.getLastRowNum();

			for(int i = 1; i <= lastRowNum; i++){

				Row row = sh.getRow(i);

				int c = 0;
				String jahrgangsstufe = getSafeCellvalue(row, c++);
				String fach = getSafeCellvalue(row, c++);
				String verlag = getSafeCellvalue(row, c++);
				String titel = getSafeCellvalue(row, c++);
				String kurztitel = getSafeCellvalue(row, c++);
				String author = getSafeCellvalue(row, c++);
				String vbn = getSafeCellvalue(row, c++);
				String auflage = getSafeCellvalue(row, c++);
				String preis = getSafeCellvalue(row, c++);
				String zulassungsnummer = getSafeCellvalue(row, c++);
				String ausbildungsrichtung = getSafeCellvalue(row, c++);
				String lernjahr = getSafeCellvalue(row, c++);

				Subject subject = SubjectDAO.findFirstBySchoolIDAndSubjectShortform(school_id, fach, con);

				Long subject_id = subject == null ? null : subject.getId();

				Long jahrgangsstufe_id = getJahrgangsstufe_id(jahrgangsstufe);

				Long ausbildungsrichtung_id = getAusbildungsrichtung_id(ausbildungsrichtung);

				Double preisDouble = null;

				if(preis != null){
					preisDouble = Double.parseDouble(preis.replace(',', '.'));
				}

				Integer sprachenJahr = FormTool.formToInteger(lernjahr);
				if(sprachenJahr == 0){
					sprachenJahr = null;
				}

				Book book = BookDAO.insert(school_id, titel,
						author, vbn, verlag,
						"Eintrag automatisch aus Excelliste generiert", zulassungsnummer,
						auflage,
						subject_id, preisDouble , con);

				BookFormDAO.insert(book.getId(), jahrgangsstufe_id, ausbildungsrichtung_id, sprachenJahr, con);

			}

			wb.close();


		}
	}

	private Long getAusbildungsrichtung_id(String ausbildungsrichtung) {

		if(ausbildungsrichtung == null){
			return null;
		}

		for(Value value: ausbildungsrichtungen){
			if(value.getAbbreviation().equalsIgnoreCase(ausbildungsrichtung)){
				return value.getId();
			}
		}

		return null;
	}

	private String getSafeCellvalue(Row row, int column) {

		Cell cell = row.getCell(column);
		if(cell == null){
			return "";
		} else {
			return dataFormatter.formatCellValue(cell);
		}

	}

	
	private Long getJahrgangsstufe_id(String jahrgangsstufe) {

		int jgstInt = FormTool.formToInteger(jahrgangsstufe);

		for(Value jgstValue: jahrgangsstufen){
			Integer j1 = FormTool.formToInteger(jgstValue.getAbbreviation());
			if(j1 != null && jgstInt == j1){
				return jgstValue.getId();
			}
		}

		return null;

	}

}
