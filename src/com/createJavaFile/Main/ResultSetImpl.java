package com.createJavaFile.Main;

import java.io.IOException;
import java.math.BigInteger;
import java.sql.Blob;
import java.sql.Clob;
import java.sql.SQLException;
import java.util.Date;

import com.myInterface.ResultSet;
import com.myInterface.ResultSetMetaData;

public class ResultSetImpl implements ResultSet {
	
	private java.sql.ResultSet resultSet;
	
	public java.sql.ResultSet getRs() {
		return resultSet;
	}
	
	public ResultSetImpl(java.sql.ResultSet resultSet) {
		this.resultSet = resultSet;
	}

	public ResultSetMetaData getMetaData() throws SQLException {
		return new ResultSetMetaDataImpl(resultSet.getMetaData());
	}

	public Object getObject(int i) throws SQLException {
		return resultSet.getObject(i);
	}

	public boolean next() throws SQLException {
		return resultSet.next();
	}

	public void close() throws IOException {
		try {
			resultSet.close();
		} catch (SQLException e) {
			throw new IOException(e);
		}
	}

	public BigInteger getBigInteger(String column) throws SQLException {
		return BigInteger.valueOf(resultSet.getLong(column));
	}

	public Blob getBlob(String column) throws SQLException {
		return resultSet.getBlob(column);
	}

	public Boolean getBoolean(String column) throws SQLException {
		return resultSet.getBoolean(column);
	}

	public Clob getClob(String column) throws SQLException {
		return resultSet.getClob(column);
	}

	public Double getDouble(String column) throws SQLException {
		return resultSet.getDouble(column);
	}

	public Float getFloat(String column) throws SQLException {
		return resultSet.getFloat(column);
	}

	public Integer getInt(String column) throws SQLException {
		return resultSet.getInt(column);
	}

	public Long getLong(String column) throws SQLException {
		return resultSet.getLong(column);
	}

	public String getString(String column) throws SQLException {
		return resultSet.getString(column);
	}

	public Date getDate(String column) throws SQLException {
		return resultSet.getDate(column);
	}

	public Date getTimestamp(String column) throws SQLException {
		return resultSet.getTimestamp(column);
	}

}
