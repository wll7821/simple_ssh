package com.fileSystem;

import java.io.IOException;
import java.sql.SQLException;

import com.myInterface.PreparedStatement;
import com.myInterface.ResultSet;

public class FPreparedStatementImpl implements PreparedStatement {
	
	private String sql;
	
	public FPreparedStatementImpl(String sql) {
		this.sql = sql;
	}	

	public ResultSet executeQuery() throws SQLException {
		// TODO Auto-generated method stub
		return null;
	}

	public int executeUpdate() throws SQLException {
		// TODO Auto-generated method stub
		return 0;
	}

	public void setObject(int i, Object object) throws SQLException {
		// TODO Auto-generated method stub

	}

	public void close() throws IOException {
		
	}
	
	public String toString() {
		return "文件系统： - sql:"+sql.toString();
	}

	public ResultSet executeQuery(String string) throws SQLException {
		// TODO Auto-generated method stub
		return null;
	}

}
