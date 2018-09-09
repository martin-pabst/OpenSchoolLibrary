package de.sp.main.mainframe.menu;

import de.sp.database.model.User;
import de.sp.main.services.text.TS;

import java.util.ArrayList;

public class MenuItem implements Comparable<MenuItem> {

	private String caption = "";

	private String action = "";

	private String fontAwesomeName = null;

	private String iconhtml = null;

	private ArrayList<MenuItem> subItems = new ArrayList<>();

	private MenuItem parent = null;

	private String[] permissions = null;

	private MenuItemSide side = MenuItemSide.left;

	private Integer order = 100;

	private String id = "";

	private String insertBelowThisId = "";

	/**
	 * 
	 * Contructor
	 * 
	 * @param caption
	 * @param action
	 * @param glyphiconName
	 * @param iconhtml
	 * @param permissions
	 * @param id
	 * @param side
	 * @param order
	 */
	public MenuItem(String caption, String action, String glyphiconName,
			String iconhtml, String[] permissions, String id,
			MenuItemSide side, Integer order) {

		this(caption, action, glyphiconName, iconhtml, permissions, id, order);

		this.side = side;

	}

	/**
	 * 
	 * Constructor
	 * 
	 * @param caption
	 * @param action
	 * @param glyphiconName
	 * @param iconhtml
	 * @param permissions
	 * @param id
	 */
	public MenuItem(String caption, String action, String glyphiconName,
			String iconhtml, String[] permissions, String id, int order) {
		super();
		this.caption = caption;
		this.action = action;
		this.fontAwesomeName = glyphiconName;
		this.iconhtml = iconhtml;
		this.permissions = permissions;

		if (permissions == null) {
			permissions = new String[0];
		}

		if (id == null) {
			id = caption;
		}

		this.id = id;

		this.order = order;
	}

	public MenuItem addItem(MenuItem item) {

		int lastOrder = 0;

		if (subItems.size() > 0) {
			lastOrder = subItems.get(subItems.size() - 1).getOrder();
		}

		if (item.getOrder() == 0) {
			item.setOrder(lastOrder + 1);
		}

		subItems.add(item);
		item.parent = this;

		return this;

	}

	public String getHtml(User user, int level, TS ts, Long school_id) {

		StringBuilder sb = new StringBuilder();

		addHtml(sb, level, user, ts, school_id);

		return sb.toString();

	}

	public void addHtml(StringBuilder sb, int level, User user, TS ts,
			Long school_id) {

		if (permissions != null && permissions.length > 0) {

			boolean hasPermission = false;

			for (String p : permissions) {
				if (user.hasPermission(p, school_id)) {
					hasPermission = true;
					break;
				}
			}

			if (!hasPermission) {
				return;
			}

		}

		String indent = "";

		for (int i = 0; i < level; i++) {
			indent += "   ";
		}

		String liClassId = "";

		if (subItems.size() > 0) {
			liClassId = " class = \""
					+ (parent == null ? "dropdown" : "dropdown-submenu") + "\"";
		}

		if (!id.isEmpty()) {
			liClassId += " id = \"" + id + "\"";
		}

		sb.append(indent).append("<li ").append(liClassId).append(">\n");

		String indent1 = indent + "   ";

		sb.append(indent1).append("<a href=\"#").append(action).append("\"");

		if (subItems.size() > 0) {
			if (parent == null) {
				sb.append(" class=\"dropdown-toggle\" data-toggle=\"dropdown\" role=\"button\" aria-haspopup=\"true\" aria-expanded=\"false\"");
			} else {
				sb.append(" tabindex = \"-1\"");
			}
		}

		sb.append(">\n").append(indent1);

		if (fontAwesomeName != null) {
			sb.append("<i class=\"fa ").append(fontAwesomeName)
					.append(" fa-fw\"></i>"); // fa-2x statt fa-fw
		}

		if (iconhtml != null) {
			sb.append(iconhtml);
		}

		sb.append("<span class=\"menuitemtext\">");
		sb.append(ts.get(caption));
		sb.append("</span>");

		if (subItems.size() > 0 && parent == null) {
			sb.append("<span class=\"caret\"></span>"); // triangle-arrow
														// downwards
		}

		sb.append("</a>\n");

		if (subItems.size() > 0) {
			sb.append(indent1).append("<ul class=\"dropdown-menu\">\n");

			for (MenuItem me : subItems) {
				me.addHtml(sb, level + 1, user, ts, school_id);
			}

			sb.append(indent1).append("</ul>\n");

		}

		sb.append(indent).append("</li>\n");

	}

	public MenuItemSide getSide() {
		return side;
	}

	@Override
	public int compareTo(MenuItem other) {
		return order.compareTo(other.order);
	}

	public Integer getOrder() {
		return order;
	}

	public void setOrder(Integer order) {
		this.order = order;
	}

	public String getId() {
		return id;
	}

	public String getInsertBelowThisId() {
		return insertBelowThisId;
	}

	public void setInsertBelowThisId(String insertBelowThisId) {
		this.insertBelowThisId = insertBelowThisId;
	}

}
