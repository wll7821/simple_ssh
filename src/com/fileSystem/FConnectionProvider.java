package com.fileSystem;

import com.createJavaFile.myutil.PropertyReader;
import com.myInterface.Connection;
import com.myInterface.IConnectionProvider;

public class FConnectionProvider implements IConnectionProvider {

	String URL = PropertyReader.get("DB_URL");
	
	public Connection getConnection() {
		return null;
	}

}
