package com.createJavaFile.connectionSource;

import java.sql.SQLException;

import com.createJavaFile.Main.ConnectionImpl;

/**通过继承com.createJavaFile.Main.ConnectionImpl实现关闭已经建立的连接*/
class ConnectionClosed extends ConnectionImpl {

	private static final long serialVersionUID = -4856393312002742870L;

	/**关闭已经建立的连接*/
	public ConnectionClosed(ConnectionImpl conn) throws SQLException{
		super(conn);
		this.conn.close();
	}
	
	
	
}
