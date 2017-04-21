package reports;

import com.google.gson.Gson;
import de.sp.modules.library.servlets.reports.ExecuteReportRequest;
import de.sp.modules.library.servlets.reports.model.ReportManager;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ReportManagerTest {
	
	@Test
	public void test() {
		Logger logger = LoggerFactory.getLogger(ReportManagerTest.class);

		System.out.println(ReportManager.getInstance().toJSon());

		Gson gson = new Gson();
		ExecuteReportRequest erq = gson.fromJson("{\"selectedRows\":[1,2],\"reportId\":0,\"contentType\":\"html\",\"dataType\":1,\"school_id\":1,\"school_term_id\":1,\"parameterValues\":[\"true\"]}", ExecuteReportRequest.class);

		System.out.println(erq.getParameterValues().size());
	}
}
