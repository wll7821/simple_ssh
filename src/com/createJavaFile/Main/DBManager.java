package com.createJavaFile.Main;

import java.io.PrintStream;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import static com.createJavaFile.myutil.Util.close;

import com.createJavaFile.connectionSource.ConnectionProvider;
import com.createJavaFile.createModel.ParseResultSetable;

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

	/** 从ConnectionPool中获得连接池 */
	private static ConnectionProvider connectionProvider = new ConnectionProvider(); 
			
	/** 打印日志的PrintStream out */
	private static PrintStream out = System.out;

	/** 设置打印SQL语句的PrintStream out */
	public static void setOut(PrintStream out) {
		DBManager.out = out;
	}

	public static PrintStream getOut() {
		return out;
	}

	
	public DBManager() {
		getConn();
	}

	public Connection getConn() {
		Connection con = conn;
		try {
			if (null == con || con.isClosed()){
				con = connectionProvider.getConnection();
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
		PreparedStatement pstmt = con.prepareStatement(sql);
		for (int i = 0; i < objects.length; i++) {
			pstmt.setObject((i + 1), objects[i]);
		}// 给 sql 语句的参数赋值

		if (printSQL)printSQL(pstmt);
		
		int n = pstmt.executeUpdate();
		close(pstmt);
		
		return n;
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
		}
		return list;
	}// executeQuery()

	
	/**打印预处理的SQL语句*/
	private void printSQL(PreparedStatement pstmt){
		String sqlString = pstmt.toString();
		out.println("SQL: "+ sqlString);
	}
	
	

}
