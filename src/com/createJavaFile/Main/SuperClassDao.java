package com.createJavaFile.Main;

import java.io.PrintStream;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.createJavaFile.createModel.ParseResultSetable;
import com.createJavaFile.myutil.PropertyReader;
import com.createJavaFile.myutil.Util;

public class SuperClassDao {

	/**所得实体对象列表是否已经逆序*/
	public boolean sortChanged;
	/**所得实体对象列表是否需要更新*/
	public boolean needUpdate;
	/**是否打印SQL语句：从配置文件中获得*/
	public static final boolean show_sql = "true".equalsIgnoreCase(PropertyReader.get(Util.SHOW_SQL));
	
	/**DBManager工具(由本类私有的静态成员)*/
	private static DBManager manager = new DBManager();
	
	/**DBManager工具(由本类的对象或子类对象使用)*/
	protected DBManager dbmanager = manager;
	protected static PrintStream out = DBManager.getOut();
	
	public void setDbmanager(DBManager dbmanager) {
		this.dbmanager = dbmanager;
	}
	public DBManager getDbmanager() {
		return dbmanager;
	}
	/**提交查询*/
	public void commit() throws SQLException{
		int n = dbmanager.commit();
		if(n>0)needUpdate = true;
	}
	
	/**系统通用的实体保存方法*/
	public static void persist(ParseResultSetable bean){
		StringBuffer sb = new StringBuffer("insert into "+Util.lowerFirst(bean.getClass().getSimpleName())+" values("); 
		String[] columns =  bean.getMemberList();
		for (int i = 0; i < columns.length; i++) {
			sb.append("?");
			if(i != columns.length-1)sb.append(",");
			else sb.append(")");
		}
		String sql = sb.toString();
		Object[] values = new Object[columns.length];
		for (int i = 0; i < values.length; i++) {
			values[i] = bean.get(i);
		}
		try {
			if(!manager.isAutoCommit()){
				out.println("当前连接不是自动提交连接！");
				throw new SQLException("当前连接不是自动提交连接！");
			}
			manager.executeUpdate(sql,show_sql,values);
		} catch (SQLException e) {
			out.println(e);
			out.println(bean+"保存失败！");
		}
	}
	
	/**系统通用的实体删除方法*/
	public static void delete(ParseResultSetable bean){
		StringBuffer sb = new StringBuffer("delete from "+Util.lowerFirst(bean.getClass().getSimpleName())+" where 1=1 "); 
		String[] columns =  bean.getMemberList();
		Object[] values = new Object[columns.length];
		for (int i = 0; i < values.length; i++) {
			sb.append("and "+columns[i]+" = ? ");
			values[i] = bean.get(i);
		}
		String sql = sb.toString();
		try {
			if(!manager.isAutoCommit()){
				out.println("当前连接不是自动提交连接！");
				throw new SQLException("当前连接不是自动提交连接！");
			}
			manager.executeUpdate(sql,show_sql,values);
		} catch (SQLException e) {
			out.println(e);
			out.println(bean+"删除失败！");
		}
	}
	
	/**系统通用的实体查找方法*/
	public static List<?> getBeansByModel(ParseResultSetable bean){
		StringBuffer sb = new StringBuffer("select * from "+Util.lowerFirst(bean.getClass().getSimpleName())+" where 1=1 "); 
		String[] columns =  bean.getMemberList();
		List<Object> values = new ArrayList<Object>();
		for (int i = 0; i < columns.length; i++) {
			if(null != bean.get(i)){
				sb.append("and "+columns[i]+" = ? ");
				values.add(bean.get(i));
			}
		}
		String sql = sb.toString();
		List<?> resultList = null;
		try {
			resultList = manager.executeQuery(sql, show_sql, bean, values.toArray());
		} catch (SQLException e) {
			out.println(e);
			out.println(bean+"查找异常！");
		}
		return resultList;
	}
	
		public static List<?> getAllBeansOf(ParseResultSetable bean){
			String sql = "select * from "+Util.lowerFirst(bean.getClass().getSimpleName());
			try {
				return manager.executeQuery(sql, show_sql, bean);
			} catch (SQLException e) {
				out.println(e);
				out.println(bean+"查找异常！");
				return null;
			}
		}
		/**系统通用的实体查找方法*/
		public static List<?> findBeansByModel(ParseResultSetable bean){
			StringBuffer sb = new StringBuffer("select * from "+Util.lowerFirst(bean.getClass().getSimpleName())+" where 1=1 "); 
			String[] columns =  bean.getMemberList();
			for (int i = 0; i < columns.length; i++) {
				if(null != bean.get(i)){
					sb.append("and "+columns[i]+" like '%"+bean.get(i)+"%' ");
				}
			}
			String sql = sb.toString();
			List<?> resultList = null;
			try {
				resultList = manager.executeQuery(sql, show_sql, bean);
			} catch (SQLException e) {
				out.println(e);
				out.println(bean+"查找异常！");
			}
			return resultList;
		}
}
