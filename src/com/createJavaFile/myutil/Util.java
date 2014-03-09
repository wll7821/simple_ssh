package com.createJavaFile.myutil;

import java.io.BufferedWriter;
import java.io.Closeable;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.security.MessageDigest;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

/**ORM框架的工具类*/
public class Util {
	
	/**导包字符串*/
	public static final String IMPORT_DATE = "import java.util.Date;\nimport com.shy2850.filter.DateFormat;";
	/**导包字符串*/
	public static final String IMPORT_BLOB = "import java.sql.Blob;";
	/**主配置文件名*/
	public static final String DB_CONFIG = "conf/db_conf.properties";
	/**JDBC驱动类名*/
	public static final String JDBC_DRIVER = "JDBC_DRIVER";
	/**数据库连接URL字段*/
	public static final String DB_URL = "DB_URL";
	/**数据库用户名字段*/
	public static final String DB_USER = "DB_USER";
	/**数据库对应密码字段*/
	public static final String DB_PASSWORD = "DB_PASSWORD";
	/**选用连接池配置字段*/
	public static final String CONNECTION_POOL = "CONNECTION_POOL";
	/**默认的打印SQL语句配置字段*/
	public static final String SHOW_SQL = "SHOW_SQL";
	/**跳转配置文件的指定配置字段*/
	public static final String FORWARD_CONF = "ApplicationForwards";
	/**应用程序类配置文件的指定配置字段*/
	public static final String BEAN_CONF = "ApplicationBeans";
	/**备份数据需要保存的地址配置*/
	public static final String PERSIST_CONF = "persist_conf";
	/**请求分发的类与方法之间的分隔符配置*/
	public static final String DIVID_CONFIG = "divid";
	/**默认的配置文件位置*/
	public static String contextPath = "WebRoot/";
	
	/**类型转换器*/
	public static String getType(String type) {
		if ("java.lang.Integer".equals(type))
			return "Integer";
		if ("java.lang.Double".equals(type)
				|| "java.math.BigDecimal".equals(type))
			return "Double";
		if ("java.lang.Float".equals(type))
			return "Float";
		if ("java.lang.String".equals(type))
			return "String";
		if ("java.lang.Boolean".equals(type))
			return "Boolean";
		if ("java.sql.Date".equals(type) || "java.sql.Timestamp".equals(type))
			return "Date";
		if ("java.math.BigInteger".equals(type))
			return "Long";
		if ("[B".equals(type))
			return "Blob";
		if ("[C".equals(type))
			return "Clob";
		return type;
	}

	/**此ORM框架的首字母大写公用方法*/
	public static String upperFirst(String s) {
		if (null == s)
			return null;
		String string = new String(s);
		if ("Integer".equals(string))
			return "Int";
		if (isUpperCase(string.charAt(0)))
			return string;
		else {
			return string.replaceFirst(string.charAt(0) + "", (char) (string
					.charAt(0) - 32)
					+ "");
		}
	}

	/**字符串首字母是否大写*/
	public static boolean isUpperCase(char first) {
		if (first >= 'A' && first <= 'Z')
			return true;
		return false;
	}

	/**首字母小写的公用方法*/
	public static String lowerFirst(String s) {
		if (null == s)
			return null;
		String string = new String(s);
		if ("Integer".equals(string))
			return "Int";
		if (isLowerCase(string.charAt(0)))
			return string;
		else {
			return string.replaceFirst(string.charAt(0) + "", (char) (string
					.charAt(0) + 32)
					+ "");
		}
	}

	/**字符串首字母是否小写*/
	public static boolean isLowerCase(char first) {
		if (first >= 'a' && first <= 'z')
			return true;
		return false;
	}
	
	/**
	 * 字符串写入文件的工具
	 * @param info   要写入的字符串
	 * @param url    要写入的文件夹(不存在时会自动创建)
	 * @param fileName  文件名
	 * @throws IOException
	 */
	public static void write(String info, String url, String fileName)
			throws IOException {
		FileWriter fw;
		File file = new File(url.replace(".", "/"));
		if (!file.exists())
			file.mkdirs();
		fw = new FileWriter(url.replace(".", "/") + "/" + fileName);
		BufferedWriter bw = new BufferedWriter(fw);
		bw.write(info);
		bw.flush();
		close(bw);
		close(fw);
	}

	/**<pre>
	 * 一个很通用的close方法
	 * 可以关闭sql包中的Connection、ResultSet、Statement
	 * 以及Closeable接口的对象
	 * </pre>
	 * */
	public static void close(Object o) {
		if (null == o)
			return;
		try {
			if (o instanceof Connection)
				((Connection) o).close();
			else if (o instanceof ResultSet)
				((ResultSet) o).close();
			else if (o instanceof Statement)
				((Statement) o).close();
			else if (o instanceof Closeable)
				((Closeable) o).close();
			else
				;
		} catch (SQLException e) {
			System.out.println("SQL关闭异常！");
		} catch (IOException e) {
			System.out.println("IO关闭异常！");
		} finally {
			o = null;
		}
	}
	
	/**对字符串进行规定编码类型的MD5转码*/
	public static String md5(String s){
		return md5(s, "UTF-8" , false);
	}
	
	/**对字符串进行规定编码类型的MD5转码*/
	public static String md5(String s,String Encode){
		return md5(s, Encode, false);
	}
	
	/**对字符串进行规定编码类型的MD5转码*/
	public static String md5(String s,String Encode,boolean b) {
		try {
			MessageDigest md5 = MessageDigest.getInstance("MD5");
			byte[] byteArray = s.getBytes(Encode);
			byte[] md5Bytes = md5.digest(byteArray);

			StringBuffer hexValue = new StringBuffer();

			for (int i = 0; i < md5Bytes.length; i++) {
				int val = ((int) md5Bytes[i]) & 0xff;
				if (val < 16)
					hexValue.append("0");
				hexValue.append(Integer.toHexString(val));
			}
			if(b)return hexValue.toString().substring(8, 24);
			return hexValue.toString();

		} catch (Exception e) {
			e.printStackTrace();
			return null;
		} 
	}

}
