package com.myInterface;

import java.io.Closeable;
import java.math.BigInteger;
import java.sql.Blob;
import java.sql.Clob;
import java.sql.SQLException;
import java.util.Date;

public interface ResultSet extends Closeable{

	boolean next() throws SQLException;

	ResultSetMetaData getMetaData() throws SQLException;

	Object getObject(int i) throws SQLException;

	Integer getInt(String column) throws SQLException;

	String getString(String column) throws SQLException;
	
	Double getDouble(String column) throws SQLException;
	
	Float getFloat(String column) throws SQLException;
	
	Boolean getBoolean(String column) throws SQLException;
	
	BigInteger getBigInteger(String column) throws SQLException;
	
	Long getLong(String column) throws SQLException;
	
	Blob getBlob(String column) throws SQLException;
	
	Clob getClob(String column) throws SQLException;
	
	Date getDate(String column) throws SQLException;

	Date getTimestamp(String column)throws SQLException;
	
}
