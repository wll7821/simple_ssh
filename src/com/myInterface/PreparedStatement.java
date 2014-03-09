package com.myInterface;

import java.io.Closeable;
import java.sql.SQLException;

public interface PreparedStatement extends Closeable,Statement {

	void setObject(int i, Object object) throws SQLException;

	int executeUpdate() throws SQLException;

	ResultSet executeQuery() throws SQLException;

}
