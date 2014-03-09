package com.createJavaFile.connectionSource;

import java.io.PrintStream;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Iterator;

import com.createJavaFile.Main.ConnectionImpl;
import com.createJavaFile.Main.DBManager;
import com.createJavaFile.myutil.PropertyReader;
import com.myInterface.Connection;
import com.myInterface.IConnectionProvider;


/**<pre>
 * ConnectionPool:连接池基类
 * 系统中所实现的连接池必须是继承自此类的(含空构造方法的)非抽象类
 * 此类用于选取系统所采用的线程池类(通过总配置文件中的CONNECTION_POOL的配置值(类配置)实现)
 * 此类还用于提供分配系统所需连接
 * </pre>
 * */
public abstract class ConnectionPool{
	
	public static final String CONNECTION_PROVIDER = "CONNECTION_PROVIDER";
	
	/**connections：当前连接池中的所有已生成连接*/
	protected static final ArrayList<Connection> connections = new ArrayList<Connection>();

	/** 打印SQL语句的PrintStream out */
	private static PrintStream out = DBManager.getOut();
	
	/**所选的连接池，通过getConnectionPool()方法来实现*/
	public static ConnectionPool  connectionPoolImpl = getConnectionPool();
	
	/**引用一个连接产生器对象*/
	protected IConnectionProvider provider = getConnectionProvider();
	
	/** 从连接池中取出连接 */
	public abstract Connection getConnection(); 
    
	/** 把连接释放 */
	public abstract void releaseConnection(Connection con);
	
	/**<pre>
	 * 通过db.conf配置文件中CONNECTION_PROVIDER的配置值(类配置)获取
	 * 没有配置时将使用系统自带的ConnectionProvider类实现
	 * </pre>
	 * */
	private static IConnectionProvider getConnectionProvider(){
		String CONNECTION_PROVIDER = PropertyReader.get("CONNECTION_PROVIDER");
		if(null == CONNECTION_PROVIDER){
			CONNECTION_PROVIDER = "com.createJavaFile.connectionSource.ConnectionProvider";
		}
		IConnectionProvider  icp = null; 
		try {
			icp= (IConnectionProvider) Class.forName(CONNECTION_PROVIDER).newInstance();
		} catch (InstantiationException e) {
			out.print(e);
		} catch (IllegalAccessException e) {
			out.print(e);
		} catch (ClassNotFoundException e) {
			out.print(e);
		}
		return icp;
	}
	
	/**<pre>
	 * 通过db.conf配置文件中CONNECTION_POOL的配置值(类配置)获取
	 * 没有配置时将使用系统自带的ConnectionPoolImpl类实现
	 * </pre>
	 * */
    static ConnectionPool getConnectionPool(){
    	
		String CONNECTION_POOL = PropertyReader.get("CONNECTION_POOL");
		if(null == CONNECTION_POOL){
			out.print("连接池为空，使用默认连接池！");
			CONNECTION_POOL = "com.createJavaFile.connectionSource.ConnectionPoolImpl";
		}
		ConnectionPool connectionPool = null;
		try {
			connectionPool = (ConnectionPool) (Class.forName(CONNECTION_POOL).newInstance());
		} catch (InstantiationException e) {
			out.print(e);
		} catch (IllegalAccessException e) {
			out.print(e);
		} catch (ClassNotFoundException e) {
			out.print(e);
		}
		return connectionPool;
	}
    
    /**重写Object的finalize()方法，实现在销毁前关闭所有池中的连接*/
    protected void finalize() {
    	out.println("ConnectionPool.finalize()");
        close();
      }

    /** 关闭连接池*/	
    public void close() {
        Iterator<Connection> iter = connections.iterator();
        while ( iter.hasNext()) {
          try {
        	  Connection conn = iter.next();
              new ConnectionClosed((ConnectionImpl) conn);
              conn = null;
          }catch (SQLException e){out.print(e);}
        }
        connections.clear();		
      }
}



