package com.createJavaFile.Main;

import java.io.FileNotFoundException;
import java.io.PrintStream;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import static com.createJavaFile.myutil.Util.close;

import com.createJavaFile.connectionSource.ConnectionPool;
import com.createJavaFile.createModel.ParseResultSetable;
import com.createJavaFile.myutil.PropertyReader;
import com.myInterface.Connection;
import com.myInterface.PreparedStatement;
import com.myInterface.ResultSet;
import com.myInterface.ResultSetMetaData;
import com.shy2850.misc.LogOutputStream;

/**
 * <pre>
 * SQL语句的执行控制类
 * 支持原生态标准SQL语句
 * 在系统中一般可通过SuperClassDao(或者其子类)来获得
 * 
 * <pre>
 * */
public class DBManager {

	/** JDBC连接 */
	private Connection conn;
	/** 默认的是自动提交 */
	private boolean isHandleCommit;
	/** SQL语句执行的影响行数 */
	private int rows;

	/** 从ConnectionPool中获得连接池 */
	private static ConnectionPool connectionPool = ConnectionPool.connectionPoolImpl;

	/** 打印日志的PrintStream out */
	private static PrintStream out = getLogPrinter();

	/** 设置打印SQL语句的PrintStream out */
	public static void setOut(PrintStream out) {
		DBManager.out = out;
	}

	private static PrintStream getLogPrinter() {
		String logPrinter = PropertyReader.get("log");
		if(null != logPrinter){
			LogOutputStream.setFileName(logPrinter);
			try {
				return new LogOutputStream();
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			}
		}
		return System.out;
		
	}

	public static PrintStream getOut() {
		return out;
	}

	public boolean isAutoCommit() {
		try {
			isHandleCommit = !getConn().getAutoCommit();
		} catch (SQLException e) {
			out.println(e);
		}
		return !isHandleCommit;
	}

	public void setHandleCommit(boolean isHandleCommit) {
		try {
			getConn().setAutoCommit(!isHandleCommit);
		} catch (SQLException e) {
			out.println(e);
		}
		this.isHandleCommit = isHandleCommit;
	}

	
	DBManager() {
		getConn();
	}

	public Connection getConn() {
		Connection con = conn;
		try {
			if (null != con && !con.isClosed())
				return con;
			else {
				con = connectionPool.getConnection();
				conn = con;
			}
		} catch (SQLException e) {
			out.println("连接获取失败！" + e);
		}
		return con;
	}

	/**
	 * 执行更新语句
	 * 
	 * @param sql
	 *            原生态SQL
	 * @param printSQL
	 *            是否打印SQL语句
	 * @param objects
	 *            占位参数数组
	 * @return SQL语句执行的影响行数
	 * @throws SQLException
	 */
	public int executeUpdate(String sql, boolean printSQL, Object... objects)
			throws SQLException {
		Connection con = getConn();
		con.setBeingUsed(true);
		PreparedStatement pstmt = con.prepareStatement(sql);
		for (int i = 0; i < objects.length; i++) {
			pstmt.setObject((i + 1), objects[i]);
		}// 给 sql 语句的参数赋值

		if (printSQL)printSQL(pstmt);
		
		int n = pstmt.executeUpdate();
		close(pstmt);
		con.setBeingUsed(false);
		if (!isHandleCommit) {
			connectionPool.releaseConnection(conn);
			rows += n;
			return rows;
		} else {
			rows += n;
			return 0;
		}
	}// executeUpdate()

	/**
	 * 执行查询语句，中间设置con.setReadOnly(true)以提高效率，离开时重置
	 * 
	 * @param sql
	 *            原生态SQL
	 * @param printSQL
	 *            是否打印SQL语句
	 * @param obj
	 *            占位参数数组
	 * @return 执行SQL语句的结果集
	 * @throws SQLException
	 */
	public List<Object> executeQuery(String sql, boolean printSQL,
			ParseResultSetable pr, Object... obj) throws SQLException {
		List<Object> list = new ArrayList<Object>();
		Connection con = getConn();
		con.setBeingUsed(true);
		con.setReadOnly(true);
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		try {
			pstmt = con.prepareStatement(sql);
			if (null != obj)
				for (int i = 0; i < obj.length; i++) {
					pstmt.setObject((i + 1), obj[i]);
				}// 给 sql 语句的参数赋值
			
			if (printSQL)printSQL(pstmt);
			
			rs = pstmt.executeQuery();
			while (rs.next()) {
				if (null == pr) {
					ResultSetMetaData rsmd = rs.getMetaData();
					Object[] objs = new Object[rsmd.getColumnCount()];
					for (int i = 0; i < objs.length; i++) {
						objs[i] = rs.getObject(i + 1);
					}
					list.add(objs);
				} else {
					Object ent = pr.parseOf(rs);
					list.add(ent);
				}
			}// while
			close(rs);
			close(pstmt);
		} catch (SQLException e) {
			out.println(e);
		} finally {
			con.setReadOnly(false);
			con.setBeingUsed(false);
			connectionPool.releaseConnection(conn);
		}
		return list;
	}// executeQuery()

	/** 查询提交 */
	public int commit() {
		try {
			getConn().commit();
		} catch (SQLException e) {
			try {
				conn.rollback();
			} catch (SQLException e1) {
				throw new RuntimeException("回滚异常！", e1);
			}
			return 0;
		} finally {
			connectionPool.releaseConnection(conn);
			rows = 0;
		}
		return rows;

	}
	
	/**打印预处理的SQL语句*/
	private void printSQL(PreparedStatement pstmt){
		String sqlString = pstmt.toString();
		int index =sqlString.lastIndexOf("PreparedStatement");
		sqlString = index > -1 ? sqlString.substring(index + 25) : sqlString;
		out.println("SQL: "+ sqlString);
	}
	
	

}// class
