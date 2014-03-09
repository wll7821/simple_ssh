package com.fileSystem;


import java.sql.Blob;
import java.sql.Clob;
import java.sql.NClob;
import java.sql.SQLException;
import java.sql.Savepoint;

import com.createJavaFile.myutil.PropertyReader;
import com.createJavaFile.myutil.Util;
import com.myInterface.CallableStatement;
import com.myInterface.Connection;
import com.myInterface.PreparedStatement;
import com.myInterface.Statement;

public class FConnectionImpl implements Connection{
	private static final long serialVersionUID = 15877392850L;
	
	String root = PropertyReader.get(Util.DB_URL);
	boolean beingUsed;
	boolean readOnly;
	boolean closed;
	
	public void commit() throws SQLException {
		// TODO Auto-generated method stub
		
	}
	public Blob createBlob() throws SQLException {
		// TODO Auto-generated method stub
		return null;
	}
	public Clob createClob() throws SQLException {
		// TODO Auto-generated method stub
		return null;
	}
	public NClob createNClob() throws SQLException {
		// TODO Auto-generated method stub
		return null;
	}
	public Statement createStatement() throws SQLException {
		return null;
	}
	public boolean getAutoCommit() throws SQLException {
		return true;
	}
	public boolean isBeingUsed() {
		return beingUsed;
	}
	public boolean isClosed() throws SQLException {
		// TODO Auto-generated method stub
		return false;
	}
	public PreparedStatement prepareStatement(String sql) throws SQLException {
		// TODO Auto-generated method stub
		return null;
	}
	public void releaseSavepoint(Savepoint savepoint) throws SQLException {
		// TODO Auto-generated method stub
		
	}
	public void rollback(Savepoint savepoint) throws SQLException {
		// TODO Auto-generated method stub
		
	}
	public void rollback() throws SQLException {
		// TODO Auto-generated method stub
		
	}
	public void setAutoCommit(boolean autoCommit) throws SQLException {
		throw new SQLException("此方法不适用于文件系统");
	}
	public void setBeingUsed(boolean beingUsed) {
		this.beingUsed = beingUsed;
	}
	public void setReadOnly(boolean readOnly) throws SQLException {
		// TODO Auto-generated method stub
		
	}
	public Savepoint setSavepoint(String name) throws SQLException {
		throw new SQLException("此方法不适用于文件系统");
	}
	public CallableStatement prepareCall(String sql) throws SQLException {
		// TODO Auto-generated method stub
		return null;
	}
	
}
