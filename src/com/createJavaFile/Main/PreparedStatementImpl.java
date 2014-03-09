package com.createJavaFile.Main;

import java.io.IOException;
import java.sql.SQLException;

import com.myInterface.PreparedStatement;
import com.myInterface.ResultSet;

public class PreparedStatementImpl implements PreparedStatement {
	
	private java.sql.PreparedStatement pstmt;
	
	public PreparedStatementImpl(java.sql.PreparedStatement pstmt) {
		this.pstmt = pstmt;
	}

	public ResultSet executeQuery() throws SQLException {
		return new ResultSetImpl(pstmt.executeQuery());
	}

	public int executeUpdate() throws SQLException {
		return pstmt.executeUpdate();
	}

	public void setObject(int i, Object object) throws SQLException {
		pstmt.setObject(i, object);
	}

	public void close() throws IOException {
		try {
			pstmt.close();
		} catch (SQLException e) {
			throw new IOException(e);
		}
	}
	
	public String toString() {
		return pstmt.toString();
	}

	public ResultSet executeQuery(String sql) throws SQLException {
		return new ResultSetImpl(pstmt.executeQuery(sql));
	}

}
