package de.sp.protocols.w2ui.grid.gridrequest;

import java.util.ArrayList;
import java.util.List;

import com.google.gson.annotations.SerializedName;

public class GridRequestGet {
	/*
	 * <code> "cmd": "get-records", "name": "myGrid", "limit": 50, "offset": 0,
	 * "selected": [1, 2], "search-logic": "AND", "search": [ { "field":
	 * "fname", "type": "text", "value": "vit", "operator": "is" }, { "field":
	 * "age", "type": "int", "value": [10, 20], "operator": "between" } ],
	 * "sort": { { "field": "fname", "direction": "ASC" }, { "field": "Lname",
	 * "direction": "DESC" } } </code>
	 */

	private String cmd;

	private String name;

	private int limit;

	private int offset;

	private List<String> selected = new ArrayList<>();

	private Long reference_id;

	private String reference_type;

	@SerializedName("search-logic")
	private String searchLogic;

	private ArrayList<GridRequestSearchfield> search = new ArrayList<>();

	private ArrayList<GridRequestSortfield> sort = new ArrayList<>();

	private Long school_id;

	private Long school_term_id;

	public GridRequestGet() {

	}

	public String getName() {
		return name;
	}

	public int getLimit() {
		return limit;
	}

	public int getOffset() {
		return offset;
	}

	public List<String> getSelected() {
		return selected;
	}

	public String getSearchLogic() {
		return searchLogic;
	}

	public ArrayList<GridRequestSearchfield> getSearch() {
		return search;
	}

	public ArrayList<GridRequestSortfield> getSort() {
		return sort;
	}

	public GridRequestCommand getGridRequestCommand() {
		return GridRequestCommand.findByCommandText(cmd);
	}

	public GridSearchLogic getGridSearchLogic() {
		return GridSearchLogic.valueOf(searchLogic);
	}

	public Long getReference_id() {
		return reference_id;
	}

	public String getReference_type() {
		return reference_type;
	}

	public String getCmd() {
		return cmd;
	}

	public Long getSchool_id() {
		return school_id;
	}

	public Long getSchool_term_id() {
		return school_term_id;
	}

}
