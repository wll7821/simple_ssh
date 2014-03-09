package com.createJavaFile.Main;

import java.io.IOException;
import java.sql.SQLException;

import com.myInterface.CallableStatement;
import com.myInterface.ResultSet;

public class CallableStatementImpl implements CallableStatement {
	
	private java.sql.CallableStatement callableStatement;
	
	public CallableStatementImpl(java.sql.CallableStatement callableStatement) {
		this.callableStatement = callableStatement;
	}

	public void setObject(String parameterName, Object x) throws SQLException {
		callableStatement.setObject(parameterName, x);
	}

	public ResultSet executeQuery() throws SQLException {
		return new ResultSetImpl(callableStatement.executeQuery());
	}

	public int executeUpdate() throws SQLException {
		return callableStatement.executeUpdate();
	}

	public void setObject(int i, Object object) throws SQLException {
		callableStatement.setObject(i, object);
	}

	public void close() throws IOException {
		try {
			callableStatement.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	public ResultSet executeQuery(String sql) throws SQLException {
		return new ResultSetImpl(callableStatement.executeQuery(sql));
	}

}
