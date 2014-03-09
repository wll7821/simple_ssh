package com.createJavaFile.Main;

import java.io.IOException;
import java.sql.SQLException;

import com.myInterface.ResultSet;
import com.myInterface.Statement;

public class StatementImpl implements Statement {
	
	private java.sql.Statement statement;
	
	public java.sql.Statement getStmt() {
		return statement;
	}
	
	public StatementImpl(java.sql.Statement statement) {
		this.statement = statement;
	}

	public ResultSet executeQuery(String sql) throws SQLException {
		return new ResultSetImpl(statement.executeQuery(sql));
	}

	public void close() throws IOException {
		try {
			statement.close();
		} catch (SQLException e) {
			throw new IOException(e);
		}
	}

}
