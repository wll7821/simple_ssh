package com.createJavaFile.connectionSource;

import java.io.PrintStream;
import java.sql.SQLException;

import com.createJavaFile.Main.ConnectionImpl;
import com.createJavaFile.myutil.PropertyReader;
import com.myInterface.Connection;
import com.myInterface.IConnectionProvider;

/** 系统独一无二的连接产生器 */
class ConnectionProvider implements IConnectionProvider{
	
	/**数据库数据源之--JDBC驱动路径*/
	private static String JDBC_DRIVER;
	/**数据库数据源之--数据库路径*/
	private static String DB_URL;
	/**数据库数据源之--数据库用户名*/
	private static String DB_USER;
	/**数据库数据源之--数据库密码*/
	private static String DB_PASSWORD;

	private PrintStream out = System.out;
		
	/**<pre>
	 * 通过db.conf中的配置字段
	 * JDBC_DRIVER|DB_URL|DB_USER|DB_PASSWORD
	 * 完成驱动的加载
	 * </pre>
	 * */
	ConnectionProvider() {
		JDBC_DRIVER = PropertyReader.get("JDBC_DRIVER");
		DB_URL = PropertyReader.get("DB_URL");
		DB_USER = PropertyReader.get("DB_USER");
		DB_PASSWORD = PropertyReader.get("DB_PASSWORD");
		try {
			Class.forName(JDBC_DRIVER);
		} catch (Exception e) {
			out.println("驱动文件路径有误！");
		}
	}

	/**通过当前(从配置文件中)获得的数据源产生连接*/
	public Connection getConnection() {
		return new ConnectionImpl(getSQLConnection());
	}
	
	java.sql.Connection getSQLConnection(){
		java.sql.Connection con = null;
		try {
			con = java.sql.DriverManager.getConnection(DB_URL, DB_USER,
					DB_PASSWORD);
			out.println("得到连接:Connection " + (ConnectionPool.connections.size()+1));
		} catch (SQLException e) {
			out.println("数据库连接建立异常！\n@shy2850@" + e.getMessage()
					+ e.getCause());
		}
		return con;
	}
	
}
