package com.createJavaFile.createModel;

import com.createJavaFile.myutil.Util;

/**<pre>
 * 用于ORM框架中逆向工程的列项类
 * 即：数据库表中的每列用包装对象的表示。
 * </pre>
 * */
class Member {
	
	/**表中的列名*/
	private String name;
	/**表中的列名(首字母大写后的)*/
	private String Name;
	/**该列在java中的数据类型*/
	private String type;
	/** 该列是否自动增长*/
	boolean isAutoIncrement;
	
	/**
	 * @param name   表中的列名
	 * @param type   该列在java中的数据类型
	 * @param isAutoIncrement   该列是否自动增长
	 * @param table  该列所属的表名
	 */
	public Member(String name, String type, boolean isAutoIncrement) {
		super();
		this.name = name;
		this.type = type;
		this.isAutoIncrement = isAutoIncrement;
		this.Name = Util.upperFirst(name);
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	
	/**创建形如：private Type name;字符串*/
	public String creatMem(){
		return "\n\tprivate "+this.type+" "+this.name;
	} 
	/**</pre>
	 * 创建形如：
	 * public void setName(Type name){
	 * 	this.name = name; 
	 * }
	 * 字符串
	 * </pre>
	 * */
	public String creatSetFun(){ 
			return "\n\t" +
					"public void set" +Name+
					"("+type+" "+name+")"+"{\n\t\tthis."+name+" = "+name+";\n\t}";
	}
	
	/**<pre>
	 * 创建形如：
	 * public Type getName(){
	 * 	return name;
	 * }
	 * 字符串
	 * </pre>
	 * */
	public String creatGetFun(){
		if("boolean".equals(type)) return "\n\tpublic boolean is"+Name+"(){\n\t\treturn "+name+";\n\t}";
		else 
			return "\n\tpublic "+type+" get"+Name+"(){\n\t\treturn "+name+";\n\t}";
	}
	
	/**创建形如：psetName(name)字符串*/
	public String set(){
		return "set"+Name+"("+name+")";
	}
	
	/**创建形如：getName()字符串*/
	public String get(){
		if("boolean".equals(type))return "is"+Name+"()";
		else
			return "get"+Name+"()";
	}
	
	/**<pre>
	 * 创建形如：
	 * Type name = rs.getType("name");
	 * 字符串
	 * </pre>
	 * */
	public String getParseResultSet(){
		return type+" "+name+" = rs.get"+Util.upperFirst(type)+"(\""+name+"\");";
	}
	
}
