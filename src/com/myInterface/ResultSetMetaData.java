package com.myInterface;

import java.sql.SQLException;

public interface ResultSetMetaData{

	int getColumnCount() throws SQLException;

	String getColumnName(int i) throws SQLException;

	boolean isAutoIncrement(int i) throws SQLException;

	String getColumnClassName(int i) throws SQLException;

}
