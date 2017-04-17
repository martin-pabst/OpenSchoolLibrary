package de.sp.asvsst.servlet;

import com.google.gson.Gson;
import de.sp.asvsst.ASVSchnittstellePlugin;
import de.sp.asvsst.ASVXMLParser;
import de.sp.asvsst.databasewriter.ASVToDatabaseWriter;
import de.sp.asvsst.model.ASVExport;
import de.sp.database.model.User;
import de.sp.main.resources.text.TS;
import de.sp.protocols.w2ui.forms.ResponseStatusEnum;
import de.sp.protocols.w2ui.forms.upload.UploadRequest;
import de.sp.protocols.w2ui.forms.upload.UploadRequestFile;
import de.sp.protocols.w2ui.forms.upload.UploadResponseStatus;
import de.sp.tools.server.BaseServlet;
import de.sp.tools.server.progressServlet.ProgressServlet;
import org.apache.commons.codec.binary.Base64;
import org.slf4j.Logger;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class ASVImportServlet extends BaseServlet {

	@Override
	protected void doPostExtended(HttpServletRequest request,
			HttpServletResponse response, Logger logger, HttpSession session,
			User user, TS ts, String postData) throws ServletException,
			IOException {

		Gson gson = new Gson();

		UploadRequest r = gson.fromJson(postData.toString(),
				UploadRequest.class);

		UploadResponseStatus uploadResponse;

		try {
			user.checkPermission(ASVSchnittstellePlugin.PERMISSIONASVSST,
					r.getSchool_id());

			if (r.isSaveRecordCommand() && r.getRecord().hasFiles()) {

				UploadRequestFile urf = r.getRecord().getFile().get(0);

				byte[] contentBytes = Base64.decodeBase64(urf.getContent());
				String zipPassword = r.getRecord().getZippassword();

				r = null; // release memory!
				urf = null; // release memory!


				Files.createDirectories(Paths.get("temp"));
				final Path path = Paths.get("temp/export.zip");

				Files.write(path, contentBytes);
				contentBytes = null; // release memory!

				String progressCode = ProgressServlet.publishProgress(0, 0, 0,
						"Verarbeitung beginnt...", false, "", null);

				uploadResponse = new UploadResponseStatus(
						ResponseStatusEnum.success, null, progressCode);

				// TODO: Weiterarbeitung und Anzeige des Fortschritts mittels
				// des
				// ProgressCode

				ASVXMLParser importer = new ASVXMLParser();

				final ASVExport asvExport = importer.parseASVXMLData(
						path.toString(), zipPassword, progressCode);

				Files.delete(path);

				if (asvExport != null) {

					Thread t = new Thread(new Runnable() {

						@Override
						public void run() {

							ASVToDatabaseWriter asvToDatabaseWriter = new ASVToDatabaseWriter(
									progressCode);

							asvToDatabaseWriter.writeToDatabase(asvExport);

						}
					});

					t.start();

				}

			} else {

				uploadResponse = new UploadResponseStatus(
						ResponseStatusEnum.error, "No files present.", "");

			}
		} catch (Exception ex) {
			
			String message = "Error importing ASV-Data: " + ex.toString();
			
			logger.error(message);
			
			uploadResponse = new UploadResponseStatus(
					ResponseStatusEnum.error, message, "");
			
		}

		response.setContentType("text/json");
		response.setStatus(HttpServletResponse.SC_OK);

		String out = gson.toJson(uploadResponse);

		response.getWriter().println(out);

	}

}
