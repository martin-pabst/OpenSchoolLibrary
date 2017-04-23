package reports;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
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


	}
}
