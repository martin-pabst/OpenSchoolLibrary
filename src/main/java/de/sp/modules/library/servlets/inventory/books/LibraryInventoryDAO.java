package de.sp.modules.library.servlets.inventory.books;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.sql2o.Connection;

import de.sp.database.statements.StatementStore;

public class LibraryInventoryDAO {
	public static List<LibraryInventoryRecord> getInventoryList(Long school_id, Integer limit, Integer offset, Connection con){
		
		String sql = StatementStore.getStatement("book.inventoryList");
		
		List<LibraryInventoryRecord> records = con.createQuery(sql)
				.addParameter("school_id", school_id)
				.executeAndFetch(LibraryInventoryRecord.class);
	
		List<LibraryInventoryRecord> consolidatedList = new ArrayList<>();
		HashMap<Long, LibraryInventoryRecord> map = new HashMap<>();
		
		for(LibraryInventoryRecord lir: records){
			if(!map.containsKey(lir.getId())){
				lir.addBookFormExtended(lir);
				lir.initSubjectData();
				map.put(lir.getId(), lir);
				consolidatedList.add(lir);
			} else {
				LibraryInventoryRecord old = map.get(lir.getId());
				old.addBookFormExtended(lir);
			}
		}
		
		return consolidatedList;
		
	}
}
