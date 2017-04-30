package de.sp.modules.library;

import de.sp.database.model.User;
import de.sp.main.mainframe.menu.MenuItem;
import de.sp.main.mainframe.menu.MenuItemSide;
import de.sp.main.resources.modules.Module;
import de.sp.main.resources.templates.VelocityEngineFactory;
import de.sp.main.resources.text.TS;
import de.sp.modules.library.servlets.borrow.bookcopystatus.LibraryBookCopyStatusServlet;
import de.sp.modules.library.servlets.borrow.bookformstore.LibraryBookFormStoreServlet;
import de.sp.modules.library.servlets.borrow.borrowedbooks.addstudent.LibraryAddStudentServlet;
import de.sp.modules.library.servlets.borrow.borrowedbooks.LibraryBorrowedBooksListServlet;
import de.sp.modules.library.servlets.borrow.borrowedbooks.LibraryRegisterBorrowingServlet;
import de.sp.modules.library.servlets.borrow.borrowerlist.LibraryBorrowerListServlet;
import de.sp.modules.library.servlets.inventory.books.LibraryBookFormServlet;
import de.sp.modules.library.servlets.inventory.books.LibraryInventoryBooksServlet;
import de.sp.modules.library.servlets.inventory.copies.LibraryInventoryCopiesServlet;
import de.sp.modules.library.servlets.reports.LibraryNavigatorDataServlet;
import de.sp.modules.library.servlets.reports.LibraryReportServlet;
import de.sp.modules.library.servlets.returnbooks.returnerlist.ReturnerListServlet;
import de.sp.modules.library.servlets.returnbooks.scanbarcodeservlet.FeeServlet;
import de.sp.modules.library.servlets.returnbooks.scanbarcodeservlet.LibraryReturnBookServlet;
import de.sp.modules.library.servlets.settings.LibrarySettingsServlet;
import org.apache.velocity.Template;
import org.eclipse.jetty.servlet.ServletContextHandler;

public class LibraryModule extends Module {

	public static final String MODULE_IDENTIFIER = "library";

	public static final String PERMISSION_LIBRARY = "library";
	public static final String PERMISSION_INVENTORY = "library.inventory";
	public static final String PERMISSION_BORROW = "library.borrow";
	public static final String PERMISSION_RETURN = "library.return";
	public static final String PERMISSION_EXAMINE = "library.examine";
	public static final String PERMISSION_REPORTS = "library.reports";
	public static final String PERMISSION_EDIT_STUDENTS = "library.editStudents";
	public static final String PERMISSION_SETTINGS = "library.settings";


	private Template template;
	
	public LibraryModule() {
		setMayGetDeactivated(true);
		setInstalled(true);
		
		template = VelocityEngineFactory.getVelocityEngine().getTemplate(
				"templates/modules/library/library.vm", "utf-8");
		
	}

	@Override
	public String[] getPermissionNames() {
		
		return new String[]{
			PERMISSION_LIBRARY, PERMISSION_INVENTORY, PERMISSION_BORROW,
			PERMISSION_RETURN, PERMISSION_EXAMINE, PERMISSION_REPORTS,
			PERMISSION_SETTINGS, PERMISSION_EDIT_STUDENTS
		};
	
	}
	
	@Override
	public String getIdentifier() {
		return MODULE_IDENTIFIER;
	}

	@Override
	public String getName() {
		return "library.moduleName";
	}


	
	@Override
	public MenuItem[] getMenuItems() {

		MenuItem m = new MenuItem("library.menu.library", "startLibrary",
				"fa-book", null, new String[] { PERMISSION_LIBRARY }, "libraryMenue",
				MenuItemSide.left, 1000);

		return new MenuItem[] { m };

	}

	
	@Override
	public String getMinimumPermission() {
		return PERMISSION_LIBRARY;
	}
	
	@Override
	public void addServlets(ServletContextHandler context) {
		context.addServlet(LibraryInventoryBooksServlet.class, "/library/inventoryBooks/*");
		context.addServlet(LibraryBookFormServlet.class, "/library/inventoryBookForm/*");
		context.addServlet(LibraryInventoryCopiesServlet.class, "/library/inventoryCopies/*");
		context.addServlet(LibraryBorrowerListServlet.class, "/library/borrowerList/*");
		context.addServlet(LibraryBookFormStoreServlet.class, "/library/bookFormStore");
		context.addServlet(LibraryBorrowedBooksListServlet.class, "/library/borrowedBooks/*");
		context.addServlet(LibraryRegisterBorrowingServlet.class, "/library/borrowedBooks/save");
		context.addServlet(LibraryBookCopyStatusServlet.class, "/library/bookCopyStatus/*");
		context.addServlet(LibraryReturnBookServlet.class, "/library/returnBook");
		context.addServlet(FeeServlet.class, "/library/fee/*");
		context.addServlet(ReturnerListServlet.class, "/library/returnerList/*");
		context.addServlet(LibraryNavigatorDataServlet.class, "/library/reports/navigatordata");
		context.addServlet(LibraryReportServlet.class, "/library/reports/start");
		context.addServlet(LibraryAddStudentServlet.class, "/library/students/save");
		context.addServlet(LibrarySettingsServlet.class, "/library/settings/*");
	}

	@Override
	public void getHtmlFragment(String fragmentId, TS ts, User user,
			StringBuilder sb) {
		
		switch (fragmentId) {
		case "startLibrary": 
			renderTemplate(template, ts, user, sb);
			break;

		default:
			break;
		}
		
		
	}

	@Override
	public String[] addFragmentIds() {
		return new String[] { "startLibrary" };
	}


	
}
