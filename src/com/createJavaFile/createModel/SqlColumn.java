package com.createJavaFile.createModel;

/**<pre>
 * 定义数据库表单列包装类
 * 用于查询的
 * exist表示查询的是存在事件还是不存在事件，默认为存在事件
 * </pre>
 * */
public class SqlColumn {
	
	private String columnName;
	private Object columnValue;
	private boolean exist;
	
	public boolean isExist() {
		return !exist;
	}
	public void setExist(boolean exist) {
		this.exist = !exist;
	}
	/**
	 * @param columnName  列名
	 * @param columnValue 列值
	 */
	public SqlColumn(String columnName, Object columnValue) {
		this.columnName = columnName;
		this.columnValue = columnValue;
	}
	/**
	 * @param columnValue 列值
	 */
	public SqlColumn(String columnValue) {
		this.columnValue = columnValue;
	}

	public String getColumnName() {
		return columnName;
	}

	public void setColumnName(String columnName) {
		this.columnName = columnName;
	}

	public Object getColumnValue() {
		return columnValue;
	}

	public void setColumnValue(Object columnValue) {
		this.columnValue = columnValue;
	}
	
	

}
