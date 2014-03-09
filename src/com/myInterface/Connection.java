package com.myInterface;

import java.io.Serializable;
import java.sql.Blob;
import java.sql.Clob;
import java.sql.NClob;
import java.sql.SQLException;
import java.sql.Savepoint;

/**本系统使用的包装型数据库连接*/
public interface Connection extends Serializable {
	/**当前Connection是否正在被使用*/
	boolean isBeingUsed();
	/**设置当前Connection为正在使用中*/
	void setBeingUsed(boolean beingUsed);
	/**设置当前Connection是否手动提交*/
	void setAutoCommit(boolean autoCommit) throws SQLException;
	/**设置当前Connection是否只读的*/
	void setReadOnly(boolean readOnly) throws SQLException;
	/**设置当前Connection的回滚保存点*/
	Savepoint setSavepoint(String name) throws SQLException;
	/**当前Connection回滚到指定的保存点*/
	void rollback(Savepoint savepoint) throws SQLException;
	/**当前Connection回滚*/
	void rollback() throws SQLException;
	/**释放当前Connection的回滚保存点*/
	void releaseSavepoint(Savepoint savepoint) throws SQLException;
	/**创建一个 PreparedStatement 对象来将参数化的 SQL 语句发送到数据库。*/
	PreparedStatement prepareStatement(String sql) throws SQLException;
	/**检索此 Connection 对象是否已经被关闭。*/
	boolean isClosed() throws SQLException;
	/**检索此 Connection 对象的当前自动提交模式。*/
	boolean getAutoCommit() throws SQLException;
	/***/
	NClob createNClob() throws SQLException;
	/***/
	Clob createClob() throws SQLException;
	/***/
	Blob createBlob() throws SQLException;
	/***/
	void commit() throws SQLException;
	/**创建一个 Statement 对象来将 SQL 语句发送到数据库。*/
	Statement createStatement() throws SQLException;
	/**调用存储过程*/
	public CallableStatement prepareCall(String sql) throws SQLException;
}
