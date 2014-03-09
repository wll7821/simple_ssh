package com.createJavaFile.connectionSource;


import com.createJavaFile.connectionSource.ConnectionPool;
import com.myInterface.Connection;

/**连接池的一个最简单的实现样例*/
public class ConnectionPoolImpl extends ConnectionPool {
	private int index;
	public static int MAXCONNECTIONS = 5;
	{
		for (int i = 0; i < MAXCONNECTIONS; i++) {
			connections.add(provider.getConnection());
		}
	}
	
	/** 从连接池中取出连接 */
	public Connection getConnection() {
		index ++;
		if(index >= connections.size())index=0;
		return connections.get(index);
	}

	/** 把连接释放 */
	public void releaseConnection(Connection con) {
		con = null;
	}

}
