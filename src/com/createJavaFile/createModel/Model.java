package com.createJavaFile.createModel;

import java.io.IOException;
import java.sql.SQLException;
import java.util.LinkedList;

import com.createJavaFile.connectionSource.ConnectionPool;
import com.createJavaFile.myutil.Util;
import com.myInterface.Connection;
import com.myInterface.ResultSet;
import com.myInterface.ResultSetMetaData;
import com.myInterface.Statement;
import com.shy2850.filter.ApplicationContext;

/**</pre>
 * 通过模块化的拼接字符串
 * 创建java文件
 * 实现数据库表与java实体对象的映射关系
 * </pre>
 * */
public class Model{
	
	/**表中所有列项的包装类Member类型实例所组成的链表*/
	private LinkedList<Member> members = new LinkedList<Member>();
	
	public LinkedList<Member> getMembers() {
		return members;
	}
	
	
	/**定义表中字段是否还有Date类型的数据*/
	boolean hasDate;
	/**定义表中字段是否还有Blob类型的数据*/
	boolean hasBlob;
	/**数据库表名*/
	String table;
	/**数据库表名大写*/
	String Table;
	/**生成的java文件的保存地址*/
	String url;
	/**主键所在的索引下标*/
	int pkIndex = -1;
	/**主键字段的名称*/
	private String pk;
	public void setPk(String pk) {
		this.pk = pk;
	}
	public String getPk() {
		return pk;
	}
	
	/**
	 * @param table 数据库表名
	 * @param url   java文件的保存地址:只允许在src下,如(com.java.util)
	 */
	public Model(String table, String url, String pk) {
		this.table = table;
		this.Table = Util.upperFirst(table);
		this.url = url;
		this.pk = pk;
		Connection con = ConnectionPool.connectionPoolImpl.getConnection();
		try {
            Statement stmt = con.createStatement();				
			ResultSet rs = stmt.executeQuery("select * from "+table);
			ResultSetMetaData rsm = rs.getMetaData(); 
			for (int i = 0; i < rsm.getColumnCount(); i++) {
				String name = rsm.getColumnName(i+1);
				String type = Util.getType(rsm.getColumnClassName(i+1));
				boolean isAutoIncrement = rsm.isAutoIncrement(i+1);
				if("Date".equals(type))hasDate = true;
				if("Blob".equals(type))hasBlob = true;
				System.out.println(name+":"+type);
				members.add(new Member(name, type, isAutoIncrement));
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}	
	}
	
	/**循环声明private成员*/
	private String def(){   //声明private成员
		StringBuffer sb = new StringBuffer("");
		for (int i = 0; i < members.size(); i++) {
			sb.append(members.get(i).creatMem()+";");
		}
		return sb.toString();
	}
	
	/**循环生成各个成员的get-set方法*/
	private String getSet(){  //生成get-set方法
		StringBuffer sb = new StringBuffer("");
		for (int i = 0; i < members.size(); i++) {
			sb.append(members.get(i).creatSetFun());
			sb.append(members.get(i).creatGetFun());
		}
		return sb.toString();
	}
	/**<pre>
	 * 生成构造方法：
	 * 包括一个空的构造方法以及全参数的构造方法
	 * </pre>
	 * */
	private String conStructor(){
		StringBuffer sb = new StringBuffer("\n\tpublic "+Util.upperFirst(table)+"(){}\n\tpublic "+Util.upperFirst(table)+"(");
		for (int i = 0; i < members.size(); i++) {
			sb.append(members.get(i).getType()+" "+members.get(i).getName());
			if(i!=members.size()-1)sb.append(", ");
		}
		sb.append("){\n\t\tsuper();\n\t\t");
		for (int i = 0; i < members.size(); i++) {
			sb.append("this."+members.get(i).getName()+" = "+members.get(i).getName()+";\n\t\t");
		}
		sb.append("}");
		return sb.toString();
	}
	
	/**重写的equals方法*/
	private String Equals(Member member){
		StringBuffer sb = new StringBuffer();
		sb.append("\n\t@Override\n\tpublic boolean equals(Object o) {\n\t");
		sb.append("\treturn (o instanceof "+Table+")&&(("+Table+")o)."+member.get()+"=="+member.get()+";\n\t}");
		return sb.toString();
	}
	/**重写的toString方法*/
	private String ToString() {  //添加toString()方法
		StringBuffer sb = new StringBuffer("\n\t@Override\n\tpublic String toString(){\n");
		sb.append("\t\treturn \""+Util.upperFirst(table)+" [");
		for (int i = 0; i < members.size(); i++) {
			if(Util.getType(members.get(i).getType()).equals("Date")){
				sb.append(members.get(i).getName()+"=\" + DateFormat.format("+members.get(i).getName()+")+\"");
			}
			else{
				sb.append(members.get(i).getName()+"=\" +"+members.get(i).getName()+"+\"");
			}
			if(i!=members.size()-1)sb.append(",");else sb.append("]\";\n\t}");
		}
		return sb.toString();
	}
	/**实现ParseResultable接口方法parseOf的字符串拼接*/
	private String ParseOf(){ //实现ParseResultable接口的字符串拼接
		StringBuffer sb = new StringBuffer("\n\tpublic Object parseOf(ResultSet rs) throws SQLException{\n\t\tif(null==rs)return null;");
		for (int i = 0; i < members.size(); i++) {
		sb.append("\n\t\t"+members.get(i).getParseResultSet());
		}
		sb.append("\n\t\t"+Util.upperFirst(table)+" "+table+" = new "+Util.upperFirst(table)+"(");
		for (int i = 0; i < members.size(); i++) {
		sb.append(members.get(i).getName());
		if(i!=members.size()-1)sb.append(", ");else sb.append(");");
		}
		sb.append("\n\t\treturn "+table+";\n\t}");
		return sb.toString();
	}
	/**创建成员列表方法*/
	private String MemberList(){
		StringBuffer sb = new StringBuffer();
		sb.append("\n\tpublic static final String[] memberList = {");
		for (int i = 0; i < members.size(); i++) {
			sb.append("\""+members.get(i).getName()+"\"");
			if(i!=members.size()-1)sb.append(",");
			else sb.append("};\n");
		}
		sb.append("\tpublic String[] getMemberList(){");
		sb.append("\n\t\treturn memberList;\n\t}");
		return sb.toString();
	}
	
	
	
	/**创建按序号获得属性值的方法*/
	private String Get(){
		StringBuffer sb = new StringBuffer();
		sb.append("\n\tpublic Object get(int i){");
		sb.append("\n\t\tObject[] members = {");
		for (int i = 0; i < members.size(); i++) {
			sb.append(members.get(i).getName());
			if(i!=members.size()-1)sb.append(",");
			else sb.append("};\n");
		}
		sb.append("\t\tif(i<members.length)");
		sb.append("return members[i];\n\t\telse \n\t\t\treturn null;\n\t}");
		return sb.toString();
	}
	
	@Override
	public String toString() {
		StringBuffer allInfo = new StringBuffer("package "+url+";\n");
		if(hasDate)allInfo.append(Util.IMPORT_DATE+"\n");
		if(hasBlob)allInfo.append(Util.IMPORT_BLOB+"\n");
		allInfo.append("\nimport java.io.Serializable;\nimport com.myInterface.ResultSet;\nimport java.sql.SQLException;\n");
		allInfo.append("\nimport com.createJavaFile.createModel.ParseResultSetable;\n\n");
		allInfo.append("public class "+Util.upperFirst(table)+" implements ParseResultSetable,Serializable{\n\t");
		allInfo.append("\n\tprivate static final long serialVersionUID = 1L;\n\t");
		allInfo.append(def());
		allInfo.append(conStructor());
		allInfo.append(getSet());
		for (int i = 0; i < members.size(); i++) {
			if(members.get(i).getName().equals(pk)){
				pkIndex = i;
				allInfo.append(Equals(members.get(i)));
			}
		}
		allInfo.append(ToString());
		allInfo.append(ParseOf());
		allInfo.append(MemberList());
		allInfo.append(Get());
		allInfo.append("\n\tpublic final int primaryKey = "+pkIndex+";");
		allInfo.append("\n\tpublic int PrimaryKey(){return primaryKey;}");
		allInfo.append("\n}");
		return allInfo.toString();
	}
	
	/**将生成的字符串写入文件*/
	public void saveModel(){
		try {
			Util.write(toString(),"src."+url,Table+".java");
			System.out.println("实体类:"+Table+".java 已经存入 "+url);
			ApplicationContext.addProperties(table, url+"."+Table);
		} catch (IOException e) {
			e.printStackTrace();
			System.out.println("文件写入异常！");
		}
	}

	
	
}
