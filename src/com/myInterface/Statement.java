package com.myInterface;

import java.io.Closeable;
import java.sql.SQLException;

public interface Statement extends Closeable{

	ResultSet executeQuery(String string) throws SQLException;
	
}
