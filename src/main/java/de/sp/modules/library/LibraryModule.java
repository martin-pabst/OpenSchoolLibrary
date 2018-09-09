package de.sp.modules.library;

import de.sp.database.model.User;
import de.sp.main.mainframe.menu.MenuItem;
import de.sp.main.mainframe.menu.MenuItemSide;
import de.sp.main.services.modules.Module;
import de.sp.main.services.settings.ModuleSettingsTypes;
import de.sp.main.services.templates.VelocityEngineFactory;
import de.sp.main.services.text.TS;
import de.sp.modules.library.reports.LibraryNavigatorDataServlet;
import de.sp.modules.library.reports.LibraryReportServlet;
import de.sp.modules.library.servlets.borrow.bookcopystatus.LibraryBookCopyStatusServlet;
import de.sp.modules.library.servlets.borrow.bookformstore.LibraryBookFormStoreServlet;
import de.sp.modules.library.servlets.borrow.borrowedbooks.LibraryBorrowedBooksListServlet;
import de.sp.modules.library.servlets.borrow.borrowedbooks.LibraryRegisterBorrowingServlet;
import de.sp.modules.library.servlets.borrow.borrowedbooks.addstudent.LibraryAddStudentServlet;
import de.sp.modules.library.servlets.borrow.borrowerlist.LibraryBorrowerListServlet;
import de.sp.modules.library.servlets.borrow.updateBorrowHoliday.UpdateBorrowHolidayServlet;
import de.sp.modules.library.servlets.inventory.books.LibraryBookFormServlet;
import de.sp.modules.library.servlets.inventory.books.LibraryInventoryBooksServlet;
import de.sp.modules.library.servlets.inventory.copies.LibraryInventoryCopiesServlet;
import de.sp.modules.library.servlets.returnbooks.returnerlist.ReturnerListServlet;
import de.sp.modules.library.servlets.returnbooks.scanbarcodeservlet.FeeServlet;
import de.sp.modules.library.servlets.returnbooks.scanbarcodeservlet.LibraryReturnBookServlet;
import de.sp.modules.library.servlets.settings.LibrarySettingsServlet;
import de.sp.modules.library.servlets.tools.LibraryToolsServlet;
import org.apache.velocity.Template;
import org.eclipse.jetty.servlet.ServletContextHandler;

public class LibraryModule extends Module {

	public static final String MODULE_IDENTIFIER = "library";

	public static final String PERMISSION_OPEN = "library.open";
	public static final String PERMISSION_INVENTORY_READ = "library.inventory.read";
	public static final String PERMISSION_INVENTORY_WRITE_BOOKS = "library.inventory.write.books";
	public static final String PERMISSION_INVENTORY_WRITE_COPIES = "library.inventory.write.copies";
	public static final String PERMISSION_BORROW = "library.borrow";
	public static final String PERMISSION_RETURN = "library.return";
	public static final String PERMISSION_EXAMINE = "library.examine";
	public static final String PERMISSION_REPORTS = "library.reports";
	public static final String PERMISSION_EDIT_STUDENTS = "library.editStudents";
	public static final String PERMISSION_SETTINGS = "library.settings";
	public static final String PERMISSION_ORDER_PAYMENTS = "library.orderPayments";
	public static final String PERMISSION_ACCEPT_PAYMENTS = "library.acceptPayments";


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
				PERMISSION_OPEN, PERMISSION_INVENTORY_READ, PERMISSION_INVENTORY_WRITE_BOOKS,
				PERMISSION_INVENTORY_WRITE_COPIES,
				PERMISSION_BORROW,
			PERMISSION_RETURN, PERMISSION_EXAMINE, PERMISSION_REPORTS,
			PERMISSION_SETTINGS, PERMISSION_EDIT_STUDENTS, PERMISSION_ORDER_PAYMENTS,
				PERMISSION_ACCEPT_PAYMENTS
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
				"fa-book", null, new String[] {PERMISSION_OPEN}, "libraryMenue",
				MenuItemSide.left, 1000);

		return new MenuItem[] { m };

	}

	
	@Override
	public String getMinimumPermission() {
		return PERMISSION_OPEN;
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
		context.addServlet(UpdateBorrowHolidayServlet.class, "/library/borrowedBooks/updateHoliday");
		context.addServlet(LibraryBookCopyStatusServlet.class, "/library/bookCopyStatus/*");
		context.addServlet(LibraryReturnBookServlet.class, "/library/returnBook");
		context.addServlet(FeeServlet.class, "/library/fee/*");
		context.addServlet(ReturnerListServlet.class, "/library/returnerList/*");
		context.addServlet(LibraryNavigatorDataServlet.class, "/library/reports/navigatordata");
		context.addServlet(LibraryReportServlet.class, "/library/reports/start");
		context.addServlet(LibraryAddStudentServlet.class, "/library/students/save");
		context.addServlet(LibrarySettingsServlet.class, "/library/settings/*");
		context.addServlet(LibraryToolsServlet.class, "/library/tools/*");
	}

	@Override
	public void getHtmlFragment(String fragmentId, TS ts, User user,
								Long school_id, StringBuilder sb) {
		
		switch (fragmentId) {
		case "startLibrary": 
			renderTemplate(template, ts, user, school_id, sb);
			break;

		default:
			break;
		}
		
		
	}

	@Override
	public String[] addFragmentIds() {
		return new String[] { "startLibrary" };
	}

	@Override
	public ModuleSettingsTypes getModuleSettingsTypes() {
		return null;
	}


}
