package reports;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonSyntaxException;
import de.sp.modules.library.servlets.borrow.borrowedbooks.addstudent.LibraryAddStundentRequest;
import de.sp.modules.library.servlets.reports.model.ReportManager;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ReportManagerTest {
	
	@Test
	public void test() {
		Logger logger = LoggerFactory.getLogger(ReportManagerTest.class);

		System.out.println(ReportManager.getInstance().toJSon());

		Gson gson = new GsonBuilder().setDateFormat("dd.MM.yyyy").create();

		LibraryAddStundentRequest re =
				null;
		try {
			re = gson.fromJson("{\"cmd\":\"save-record\",\"recid\":0,\"school_id\":1,\"school_term_id\":1,\n" +
                    "\"record\":{\n" +
                    "\"date_of_birth\":\"12.04.2017\",\n" +
                    "\"firstname\":\"Theodor\",\n" +
                    "\"surname\":\"Test\",\n" +
                    "\"before_surname\":\"von\",\n" +
                    "\"after_surname\":\"auf der Heid\",\n" +
                    "\"sex\":{\"id\":1,\"text\":\"m\",\"hidden\":false},\n" +
                    "\"classname\":{\"id\":1,\"text\":\"8a\",\"hidden\":false},\n" +
                    "\"curriculum\":{\"id\":20,\"text\":\"GY_NTG_8\",\"hidden\":false},\n" +
                    "\"language_1\":{\"id\":131,\"text\":\"E\",\"hidden\":false},\n" +
                    "\"from_form_1\":{\"id\":3,\"text\":\"5\",\"hidden\":false}}}", LibraryAddStundentRequest.class);
		} catch (JsonSyntaxException e) {
			System.out.println(e);
			e.printStackTrace();
		}

		System.out.println(re);

	}
}
