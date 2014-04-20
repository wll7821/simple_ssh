package com.createJavaFile.connectionSource;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

import com.createJavaFile.myutil.PropertyReader;

public class ConnectionProvider {
	/**数据库数据源之--JDBC驱动路径*/
	private static String JDBC_DRIVER;
	/**数据库数据源之--数据库路径*/
	private static String DB_URL;
	/**数据库数据源之--数据库用户名*/
	private static String DB_USER;
	/**数据库数据源之--数据库密码*/
	private static String DB_PASSWORD;

	/**<pre>
	 * 通过db.conf中的配置字段
	 * JDBC_DRIVER|DB_URL|DB_USER|DB_PASSWORD
	 * 完成驱动的加载
	 * </pre>
	 * */
	public ConnectionProvider() {
		JDBC_DRIVER = PropertyReader.get("JDBC_DRIVER");
		DB_URL = PropertyReader.get("DB_URL");
		DB_USER = PropertyReader.get("DB_USER");
		DB_PASSWORD = PropertyReader.get("DB_PASSWORD");
		try {
			Class.forName(JDBC_DRIVER);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public Connection getConnection(){
		Connection conn = null;
		try {
			conn = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return conn;
	}
	
	

}
