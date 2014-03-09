package com.createJavaFile.connectionSource;

import java.io.File;
import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.createJavaFile.createModel.Model;
import com.createJavaFile.createModel.ModelDao;
import com.createJavaFile.myutil.PropertyReader;
import com.createJavaFile.myutil.Util;
import com.shy2850.filter.ApplicationContext;

/**
 * 设置自动完成当前数据库中的所有表项自动生成映射java类。
 */
public class DBAutoRun {
	/**Derby数据库的JDBC连接规范*/
	public static final SQLDialog DERBY = new SQLDialog("DERBY","org.apache.derby.jdbc.ClientDriver", "jdbc:derby://<hostName>:<port>/<databaseName>",1527);
	/**MySql数据库的JDBC连接规范*/
	public static final SQLDialog MYSQL = new SQLDialog("MYSQL","com.mysql.jdbc.Driver","jdbc:mysql://<hostName>:<port>/<databaseName>",3306);
	/**SqlServer数据库的JDBC连接规范*/
	public static final SQLDialog SQLSERVER = new SQLDialog("SQLSERVER","com.microsoft.sqlserver.jdbc.SQLServerDriver","jdbc:sqlserver://<hostName>:<port>;DatabaseName=<databaseName>",1433);
	/**DB2数据库的JDBC连接规范*/
	public static final SQLDialog DB2 = new SQLDialog("DB2","com.ibm.db2.jcc.DB2Driver","jdbc:db2://<hostName>:<port>/<databaseName>",5000);
	/**Oracle数据库的JDBC连接规范*/
	public static final SQLDialog ORACLE = new SQLDialog("ORACLE","oracle.jdbc.driver.OracleDriver","jdbc:oracle:thin:@<hostName>:<port>:<databaseNamename>",1521);
	/**Windows-Access数据库的JDBC连接规范*/
	public static final SQLDialog ACCESS = new SQLDialog("ACCESS","sun.jdbc.odbc.JdbcOdbcDriver","jdbc:odbc:<databaseName>",0);
	/**PostgreSql数据库的JDBC连接规范*/
	public static final SQLDialog POSTGRE = new SQLDialog("POSTGRE","org.postgresql.Driver","jdbc:postgresql://<hostName>:<port>/<databaseName>",5432);
	/**SybaseSql数据库的JDBC连接规范*/
	public static final SQLDialog SYBASE = new SQLDialog("SYBASE","com.sybase.jdbc3.jdbc.SybDriver","jdbc:sybase:Tds:<hostName>:<port>/<databaseName>",2638);
	
	
	private Connection conn = new ConnectionProvider().getSQLConnection();
	/**实体类文件保存地址*/
	private String poURL = "com.bean.po";
	/**DAO类文件保存地址*/
	private String daoURL= "com.bean.dao";
	
	/**数据库所有要生成类文件的关联表项名*/
	private List<String> tables = new ArrayList<String>();
	/**数据库所有要生成类文件的关联表项的主键名称*/
	private List<String> pks = new ArrayList<String>();
	{init();}
	
	/**
	 * 初始化当前数据库连接，并且完成读取所有数据库表以及主键
	 * */
	private void init(){
		DatabaseMetaData mtdt = null;
		ResultSet rs = null;
		ResultSet rs2 = null;
		try {
			mtdt = conn.getMetaData();
			String[] types = {"TABLE"};
			rs = mtdt.getTables(null, null, null, types);
			while(rs.next()){
				String s = rs.getString("TABLE_NAME");
				tables.add(s);
				rs2 = mtdt.getPrimaryKeys(null, null, s);
				while(rs2.next()){
					String pk = rs2.getString(4);
					pks.add(null==pk?"shy2850":pk);
				}
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}finally{
			try {
				rs.close();
				rs2.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
			
		}
	}

	/**
	 * 设置自动完成当前数据库中的所有表项自动生成映射java类。
	 */
	public DBAutoRun() {
		
	}
	/**
	 * 设置自动完成当前数据库中的所有表项自动生成映射java类。
	 * @param poURL		实体类的java文件生成地址
	 * @param daoURL	DAO类的java文件生成地址
	 */
	public DBAutoRun(String poURL,String daoURL) {
		this.poURL = poURL;
		this.daoURL = daoURL;
	}
	
	/**配置文件生成以后用来自动运行生成java文件*/
	public void autoRun() {
		if(!new File(Util.contextPath+Util.DB_CONFIG).isFile())throw new RuntimeException("配置文件不存在！");
		for (int i = 0; i < tables.size(); i++) {
			Model model = new Model(tables.get(i), poURL, pks.get(i));
			model.saveModel();
			ModelDao dao = new ModelDao(model, daoURL);
			dao.saveModelDao();
		}
	}
	 
	/**
	 * 按照表名移除不需要生成映射类的数据库表
	 * @param tableNames 数据库表名。 
	 */
	public void remove(String...tableNames){
		for (int i = 0; i < tableNames.length; i++) {
			String name = tableNames[i];
			for (int j = 0; j < tables.size(); j++) {
				if(name.equals(tables.get(j))){
					tables.remove(j);
					pks.remove(j);
				}
			}
		}
	}
	
	/**
	 * 
	 * @param JDBC_DRIVER
	 *            JDBC驱动类名 如: com.mysql.jdbc.Driver
	 * @param DB_URL
	 *            数据库连接URL 如:jdbc:mysql://localhost:3306/u-disk
	 * @param DB_USER
	 *            数据库用户名
	 * @param DB_PASSWORD
	 *            数据库对应密码
	 */
	private static void setConfig(String JDBC_DRIVER, String DB_URL, String DB_USER,
			String DB_PASSWORD) {
		File file;
		file = new File(Util.contextPath+Util.DB_CONFIG);
		try {
			if (!file.exists()) {
				new File(Util.contextPath+"conf").mkdirs();
				file.createNewFile();
				PropertyReader.addProperties(Util.DB_USER, DB_USER);
				PropertyReader.addProperties(Util.DB_PASSWORD, DB_PASSWORD);
				PropertyReader.addProperties(Util.DB_URL, DB_URL);
				PropertyReader.addProperties(Util.JDBC_DRIVER, JDBC_DRIVER);
				PropertyReader.addProperties(Util.SHOW_SQL, "FALSE");
				PropertyReader.addProperties(Util.BEAN_CONF,
						"conf/ApplicationBeans.properties");
				PropertyReader.addProperties(Util.FORWARD_CONF,
						"conf/ApplicationForwards.properties");
				PropertyReader
						.addProperties(Util.CONNECTION_POOL,
								"com.createJavaFile.connectionSource.ConnectionPoolImpl");
				PropertyReader
						.addProperties(ConnectionPool.CONNECTION_PROVIDER,
								"com.createJavaFile.connectionSource.ConnectionProvider");
				PropertyReader.addProperties(
						ApplicationContext.ANNOTATION_INJECT, "false");
				PropertyReader.addProperties(
						ApplicationContext.SET_METHOD_INJECT, "true");
				PropertyReader.addProperties(Util.DIVID_CONFIG, "$");
				PropertyReader.addProperties(Util.PERSIST_CONF, "conf/PersistObj.properties");
				new File(Util.contextPath+"conf/ApplicationForwards.properties").createNewFile();
				System.out.println("配置文件已经生成");
			}
		} catch (Exception e) {
			System.out.println(e);
		}
	}
	/**
	 * 框架的起始点：生成基础配置文件
	 * @param dialog		采用的数据库种类所对应的连接规范
	 * @param hostName		数据库所在IP
	 * @param databaseName	将要操作的数据库名称
	 * @param user			数据库用户名
	 * @param password		数据库密码
	 */
	public static void setConfig(SQLDialog dialog, String hostName, String databaseName, String user, String password){
		setConfig(dialog.driver, dialog.getUrl(hostName, databaseName), user, password);
	}
	
	/**指定的数据库连接JDBC格式*/
	public static class SQLDialog{
		private String name;
		private String driver;
		private String urlPattern;
		
		/**
		 * @param name		当前规范的名称
		 * @param driver	该数据库的JDBC驱动类路径名称
		 *@param urlPattern	当前数据库格式中的URL配置规范
		 * @param port		数据库服务的端口号
		 */
		private SQLDialog(String name, String driver,String urlPattern,int port) {
			this.name = name;
			this.driver = driver;
			this.urlPattern = urlPattern.replace("<port>", port+"");
		}
		
		/**
		 * 获取符合当前规范的URL
		 * @param hostName		数据库所在IP
		 * @param databaseName	数据库名称
		 * @return
		 */
		public String getUrl(String hostName,String databaseName) {
			if(null == hostName)hostName = "localhost";
			if(null == databaseName)databaseName = "";
			return urlPattern.replace("<hostName>", hostName).replace("<databaseName>", databaseName);
		}

		public String toString() {
			return "SQLDialog:" + name + " \ndriver : " + driver
					+ "\nurlPattern : " + urlPattern;
		}

		public void setDriver(String driver) {
			this.driver = driver;
		}
		public void setName(String name) {
			this.name = name;
		}
		
	}
	
}
