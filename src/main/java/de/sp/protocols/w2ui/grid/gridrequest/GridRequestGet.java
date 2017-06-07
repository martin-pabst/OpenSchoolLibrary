package de.sp.protocols.w2ui.grid.gridrequest;

import com.google.gson.annotations.SerializedName;
import de.sp.tools.validation.Validation;

import java.util.ArrayList;
import java.util.List;

public class GridRequestGet {
	/*
	 * <code> "cmd": "get-records", "name": "myGrid", "limit": 50, "offset": 0,
	 * "selected": [1, 2], "search-logic": "AND", "search": [ { "field":
	 * "fname", "type": "text", "value": "vit", "operator": "is" }, { "field":
	 * "age", "type": "int", "value": [10, 20], "operator": "between" } ],
	 * "sort": { { "field": "fname", "direction": "ASC" }, { "field": "Lname",
	 * "direction": "DESC" } } </code>
	 */

	protected String cmd;

	protected String name;

	protected int limit;

	protected int offset;

	protected List<String> selected = new ArrayList<>();

	protected Long reference_id;

	protected String reference_type;

	@SerializedName("search-logic")
	protected String searchLogic;

	protected ArrayList<GridRequestSearchfield> search = new ArrayList<>();

	protected ArrayList<GridRequestSortfield> sort = new ArrayList<>();

	@Validation(notNull = true)
	protected Long school_id;

	protected Long school_term_id;

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
