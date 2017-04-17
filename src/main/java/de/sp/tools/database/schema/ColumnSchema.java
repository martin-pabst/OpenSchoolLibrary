package de.sp.tools.database.schema;

import org.simpleframework.xml.Attribute;
import org.simpleframework.xml.Element;
import org.simpleframework.xml.Root;

@Root(name = "column")
public class ColumnSchema {

	@Attribute
	private String name;

	@Attribute
	private String type;

	@Attribute(required = false)
	private boolean autoincrement;

	@Attribute(required = false)
	private boolean primarykey;

	@Attribute(required = false)
	private boolean notnull;

	@Attribute(required = false)
	private int length;

	@Element(required = false)
	private ReferencesSchema references;

	@Attribute(required = false)
	private int digits;

	@Attribute(required = false)
	private int decimals;

	@Attribute(required = false)
	private Boolean index;

	public String getName() {
		return name;
	}

	public String getType() {
		return type;
	}

	public boolean isAutoincrement() {
		return autoincrement;
	}

	public boolean isPrimarykey() {
		return primarykey;
	}

	public int getLength() {
		return length;
	}

	public ReferencesSchema getReferences() {
		return references;
	}

	public int getDigits() {
		return digits;
	}

	public int getDecimals() {
		return decimals;
	}

	public boolean isNotnull() {
		return notnull;
	}

	public String toSQLStatement(DatabaseType databaseType) throws Exception {
		return name + " " + typeToSQL(databaseType);
	}

	private String typeToSQL(DatabaseType databaseType) throws Exception {

		String statement = "";

		if (databaseType == DatabaseType.postgres && autoincrement) {
			statement = "bigserial";
		} else {

			DDLTypeEnum typeEnum = getTypeEnum();

			if (typeEnum != null) {

				statement += typeEnum.getName(databaseType);

				switch (typeEnum) {
				case varchar:
					statement += "(" + length + ")";
					break;
				case decimal:
					statement += "(" + digits + "," + decimals + ")";
					break;
				default:
				}

			} else {
				statement += type;
			}

		}

		if (autoincrement && databaseType == DatabaseType.mysql) {
			statement += " auto_increment";
		}

		if (notnull && !primarykey) {
			statement += " not null";
		}

		return statement;
	}

	public DDLTypeEnum getTypeEnum() {
		return DDLTypeEnum.valueOf(type);
	}

	public String getJavaType() {

		DDLTypeEnum typeEnum = getTypeEnum();

		if (typeEnum != null) {
			return typeEnum.getJavaTypename();
		}

		return "";
	}

	public boolean hasIndex() {

		if (index == null) {
			return false;
		}

		return index;

	}

}
