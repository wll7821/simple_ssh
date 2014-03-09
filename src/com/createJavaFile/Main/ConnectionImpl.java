package com.createJavaFile.Main;

import java.sql.Blob;
import java.sql.Clob;
import java.sql.NClob;
import java.sql.SQLException;
import java.sql.Savepoint;

import com.myInterface.CallableStatement;
import com.myInterface.Connection;
import com.myInterface.PreparedStatement;
import com.myInterface.Statement;

/**包装java.sql.Connection的接口实现类*/
public class ConnectionImpl implements Connection {

	private static final long serialVersionUID = 8933680610988795520L;
	protected java.sql.Connection conn; 
	private boolean beingUsed;
	
	public ConnectionImpl(java.sql.Connection conn) {
		this.conn = conn;
	}
	protected ConnectionImpl(ConnectionImpl conn) {
		this.conn = conn.conn;
	}

	public void commit() throws SQLException {
		conn.commit();
	}

	public Blob createBlob() throws SQLException {
		return conn.createBlob();
	}

	public Clob createClob() throws SQLException {
		return conn.createClob();
	}

	public NClob createNClob() throws SQLException {
		return conn.createNClob();
	}

	public boolean getAutoCommit() throws SQLException {
		return conn.getAutoCommit();
	}

	public boolean isClosed() throws SQLException {
		return conn.isClosed();
	}
	
	public PreparedStatement prepareStatement(String sql) throws SQLException {
		return new PreparedStatementImpl(conn.prepareStatement(sql));
	}


	public void releaseSavepoint(Savepoint savepoint) throws SQLException {
		conn.releaseSavepoint(savepoint);
	}

	public void rollback() throws SQLException {
		conn.rollback();
	}

	public void rollback(Savepoint savepoint) throws SQLException {
		conn.rollback(savepoint);
	}

	public void setAutoCommit(boolean autoCommit) throws SQLException {
		conn.setAutoCommit(autoCommit);
	}

	public void setReadOnly(boolean readOnly) throws SQLException {
		conn.setReadOnly(readOnly);
	}

	public Savepoint setSavepoint(String name) throws SQLException {
		return conn.setSavepoint(name);
	}

	public void setBeingUsed(boolean beingUsed) {
		this.beingUsed = beingUsed;
	}
	public boolean isBeingUsed() {
		return beingUsed;
	}
	public Statement createStatement() throws SQLException {
		return new StatementImpl(conn.createStatement());
	}
	public CallableStatement prepareCall(String sql) throws SQLException {
		return new CallableStatementImpl(conn.prepareCall(sql));
	}

}
