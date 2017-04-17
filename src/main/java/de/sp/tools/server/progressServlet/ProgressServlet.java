package de.sp.tools.server.progressServlet;

import java.io.IOException;
import java.util.HashMap;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.gson.Gson;

import de.sp.tools.server.BaseServlet;
import de.sp.tools.string.SessionIdentifierGenerator;

public class ProgressServlet extends BaseServlet {

	private static HashMap<String, ProgressResponseData> progressResponseMap = new HashMap<>();
	
	private static int progressCounter = 0;
	
	@Override
	protected void doPost(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {

//		Logger logger = LoggerFactory.getLogger(ProgressServlet.class);

		Gson gson = new Gson();
		
		ProgressRequestData progressRequestData = gson.fromJson(getPostData(request), ProgressRequestData.class);

		ProgressResponseData progressResponseData = progressResponseMap.get(progressRequestData.getProgressCode());
		
		if(progressResponseData == null){
			progressResponseData = new ProgressResponseData(0, 0, 0, "Waiting to start...", false, "");
		}
		
		if(progressResponseData.isCompleted()){
			progressResponseMap.remove(progressRequestData.getProgressCode());
		}
		
		
		
		response.setContentType("text/json");
		response.setStatus(HttpServletResponse.SC_OK);

		String out = gson.toJson(progressResponseData);

		response.getWriter().println(out);

	}

	@Override
	public void init(ServletConfig config) throws ServletException {

		super.init(config);
	}

	public static String publishProgress(int min, int max, int now,
			String text, boolean completed, Object result, String progressCode) {

		progressCounter++;
		
		if(progressCounter > 100){
			purge();
			progressCounter = 0;
		}
		
		if (progressCode == null) {
			progressCode = SessionIdentifierGenerator.nextSessionId();
		}

		ProgressResponseData prd = new ProgressResponseData(min, max, now,
				text, completed, result);
		progressResponseMap.put(progressCode, prd);

		return progressCode;
	}

	private static void purge() {
		
		for(String code: progressResponseMap.keySet()){
			
			ProgressResponseData prd = progressResponseMap.get(code);
			
			if(prd.isOutdated()){
				progressResponseMap.remove(code);
			}
		}
		
		
	}

}
