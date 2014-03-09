package com.createJavaFile.Main;

import java.sql.SQLException;

import com.myInterface.ResultSetMetaData;

public class ResultSetMetaDataImpl implements ResultSetMetaData {
	
	private java.sql.ResultSetMetaData  rsmd;
	
	public java.sql.ResultSetMetaData getRsmd() {
		return rsmd;
	}
	
	public ResultSetMetaDataImpl(java.sql.ResultSetMetaData  rsmd) {
		this.rsmd = rsmd;
	}

	public String getColumnClassName(int i) throws SQLException {
		return rsmd.getColumnClassName(i);
	}

	public int getColumnCount() throws SQLException {
		return rsmd.getColumnCount();
	}

	public String getColumnName(int i) throws SQLException {
		return rsmd.getColumnName(i);
	}

	public boolean isAutoIncrement(int i) throws SQLException {
		return rsmd.isAutoIncrement(i);
	}

}
