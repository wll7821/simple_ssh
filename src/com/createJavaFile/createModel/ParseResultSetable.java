package com.createJavaFile.createModel;

import java.sql.SQLException;

import com.myInterface.ResultSet;

/**系统中的数据库表映射实体类都必须实现该接口的*/
public interface ParseResultSetable {

	/**结果集转换成相应的实体对象*/
	Object parseOf(ResultSet rs) throws SQLException;
	/**返回数据库表中的字段列表*/
	String[] getMemberList();
	/**与字段列表对应序列的属性的返回值*/
	Object get(int i);
	/**数据库表主键下标，-1表示没有独立的主键*/
	int PrimaryKey();
}
