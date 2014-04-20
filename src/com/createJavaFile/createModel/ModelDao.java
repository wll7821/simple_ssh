package com.createJavaFile.createModel;

import java.io.IOException;
import java.util.LinkedList;

import com.createJavaFile.myutil.Util;
import com.wll7821.filter.ApplicationContext;

/**<pre>
 * 通过模块化的拼接字符串
 * 创建java文件
 * 实现数据库表对应类实体的增删改查等方法
 * </pre>
 * */
public class ModelDao {
	
	/**表中所有列项的包装类Member类型实例所组成的链表*/
	LinkedList<Member> members;
	
	/**数据库表名*/
	String table;
	/**数据库表名大写*/
	String Table;
	/**生成的java文件的保存地址*/
	String modelUrl;
	/**对应的实体类文件的保存地址*/
	String url;
	/**主键字段的名称*/
	private String pk;
	public void setPk(String pk) {
		this.pk = pk;
	}
	public String getPk() {
		return pk;
	}
	/**
	 * @param model  Model类生成的有效实例
	 * @param url    java文件的保存地址:默认在src下,如(com.java.util)
	 */
	public ModelDao(Model model,String url) {
		this.table = model.table;
		this.url = url;
		this.modelUrl = model.url;
		this.members = model.getMembers();
		this.pk = model.getPk();
		Table = Util.upperFirst(table);
	}
	/**
	 * @param model Model类生成的有效实例
	 * @param url   java文件的保存地址:默认在src下,如(com.java.util)
	 * @param pk	主键名称
	 */
	public ModelDao(Model model,String url,String pk) {
		this.table = model.table;
		this.url = url;
		this.modelUrl = model.url;
		this.members = model.getMembers();
		this.pk = pk;
		Table = Util.upperFirst(table);
	}
	

	/**导入包模块的写入*/
	String include(){
		StringBuffer sb = new StringBuffer();
		sb.append("package "+url+";\n");
		sb.append("\nimport java.sql.SQLException;\n");
		sb.append("import java.util.ArrayList;\n");
//		sb.append("import java.util.Collections;\n");
		sb.append("import java.util.List;\n");
//		sb.append("\nimport com.createJavaFile.Main.SuperClassDao;\n");
		sb.append("\nimport com.createJavaFile.Main.DBManager;\n");
		sb.append("import com.createJavaFile.createModel.ParseResultSetable;\n");
		sb.append("import com.createJavaFile.createModel.SqlColumn;\n");
		sb.append("import com.createJavaFile.myutil.PropertyReader;\n");
		sb.append("import com.createJavaFile.myutil.Util;\n");
		sb.append("import "+modelUrl+"."+Table+";\n");
		return sb.toString();
	}  //包与引用类的导入 
	
	/**将当前对象列表逆序*/
	String reverse(){
		StringBuffer sb = new StringBuffer();
		sb.append("\n\tpublic List<"+Table+"> reverse() throws SQLException{//反序");
		sb.append("\n\t\tif(needUpdate||null=="+table+"List)find"+Table+"s();");
		sb.append("\n\t\tCollections.reverse("+table+"List);");
		sb.append("\n\t\tsortChanged = !sortChanged;");
		sb.append("\n\t\treturn "+table+"List;");
		sb.append("\n\t}//reverse()\n");
		return sb.toString();
	}
	/**获得实体对象列表*/
	String tableList(){
		StringBuffer sb = new StringBuffer();
		sb.append("\n\tprivate List<"+Table+"> "+table+"List;");
		sb.append("\n\tpublic List<"+Table+"> get"+Table+"List(){");
		sb.append("\n\t\tif(needUpdate||null=="+table+"List)try {find"+Table+"s();}catch (SQLException e) {throw new RuntimeException(\"数据库异常\",e);}");
		sb.append("\n\t\treturn "+table+"List;");
		sb.append("\n\t}//get"+Table+"List()\n");
		return sb.toString();
	}
	
	/**得到当前对象列表的长度*/
	String getCount(){
		StringBuffer sb = new StringBuffer();
		sb.append("\n\tpublic int getCount(){//获得总长");
		sb.append("\n\t\tif(null=="+table+"List)");
		sb.append("\n\t\t\ttry {return find"+Table+"s().size();");
		sb.append("\n\t\t\t} catch (SQLException e) {");
		sb.append("\n\t\t\te.printStackTrace();return 0;}");
		sb.append("\n\t\treturn "+table+"List.size();");
		sb.append("\n\t}//getCount()\n");
		return sb.toString();
	}
	
	/**
	 * pageSize 分页列表的页面大小
	 * pageNum	分页列表的当前页码
	 * 			返回分页所得的对象列表(截取的)
	 */
	String pageOf(){
		StringBuffer sb = new StringBuffer();
		sb.append("\n\tpublic List<"+Table+"> pageOf(int pageSize,int pageNum){//分页");
		sb.append("\n\t\tif(pageSize<=1||pageNum<1)return null;");
		sb.append("\n\t\tList<"+Table+"> list = new ArrayList<"+Table+">();");
		sb.append("\n\t\tint index = (pageNum-1)*pageSize;");
		sb.append("\n\t\tfor (int i = index; i < index+pageSize; i++) {");
		sb.append("\n\t\t\tif(i>=getCount())break;");
		sb.append("\n\t\t\tlist.add("+table+"List.get(i));");
		sb.append("\n\t\t}\n\t\treturn list;\n\t}//pageOf()\n");
		return sb.toString();
	}
	
	/** 模糊查询实现得到列表(内部调用版) */
	private String privateFind(){
		StringBuffer sb = new StringBuffer();
		sb.append("\n\tprivate List<"+Table+"> find"+Table+"(SqlColumn...sqlColumns)throws SQLException{");
		sb.append("\n\t\tParseResultSetable "+table+" = new "+Table+"();");
		sb.append("\n\t\tStringBuffer sql = new StringBuffer(\"select * from "+table+" where 1=1 \");");
		sb.append("\n\t\tfor (int i = 0; i < sqlColumns.length; i++) {");
		sb.append("\n\t\t\tSqlColumn s = sqlColumns[i];");
		sb.append("\n\t\tif(null != s.getColumnName()){");
		sb.append("\n\t\t\t\tif(null == s.getColumnValue())sql.append(\"and \"+s.getColumnName()+\" is null \");");
		sb.append("\n\t\t\t\telse if(\"\".equals(s.getColumnValue()))sql.append(\"and \"+s.getColumnName()+\" is not null \");");
		sb.append("\n\t\t\t\telse {");
		sb.append("\n\t\t\t\t\tif(s.isExist())sql.append(\"and \"+s.getColumnName()+\" like '%\"+s.getColumnValue()+\"%' \");");
		sb.append("\n\t\t\t\t\telse  sql.append(\"and \"+s.getColumnName()+\" != '%\"+s.getColumnValue()+\"%' \");");
		sb.append("\n\t\t\t\t}\n\t\t\t}");
		sb.append("\n\t\t\tif(i==sqlColumns.length-1&&null==s.getColumnName())sql.append(\"order by \"+s.getColumnValue());");
		sb.append("\n\t\t}\n\t\tList<Object> list = dbmanager.executeQuery(sql.toString(),");
		sb.append(" show_sql, ");
		sb.append(table+");");
		sb.append("\n\t\tList<"+Table+"> "+table+"List = new ArrayList<"+Table+">();");
		sb.append("\n\t\tfor(int i=0;i<list.size();i++){");
		sb.append("\n\t\t\t"+table+"List.add(("+Table+")list.get(i));");
		sb.append("\n\t\t\t}\n\t\treturn "+table+"List;\n\t}//find"+Table+"()\n");
		return sb.toString();
	}
	
	/** 精确查询实现得到列表(内部调用版)*/
	private String privateGet(){
		StringBuffer sb = new StringBuffer();
		sb.append("\n\tprivate List<"+Table+"> get"+Table+"(SqlColumn...sqlColumns)throws SQLException{");
		sb.append("\n\t\tParseResultSetable "+table+" = new "+Table+"();");
		sb.append("\n\t\tStringBuffer sql = new StringBuffer(\"select * from "+table+" where 1=1 \");");
		sb.append("\n\t\tfor (int i = 0; i < sqlColumns.length; i++) {");
		sb.append("\n\t\t\tSqlColumn s = sqlColumns[i];");
		sb.append("\n\t\tif(null != s.getColumnName()){");
		sb.append("\n\t\t\t\tif(null == s.getColumnValue())sql.append(\"and \"+s.getColumnName()+\" is null \");");
//		sb.append("\n\t\t\t\telse if(\"\".equals(s.getColumnValue()))sql.append(\"and \"+s.getColumnName()+\" is not null \");");
		sb.append("\n\t\t\t\telse {");
		sb.append("\n\t\t\t\t\tif(s.isExist())sql.append(\"and \"+s.getColumnName()+\" = '\"+s.getColumnValue()+\"' \");");
		sb.append("\n\t\t\t\t\telse  sql.append(\"and \"+s.getColumnName()+\" != '\"+s.getColumnValue()+\"' \");");
		sb.append("\n\t\t\t\t}\n\t\t\t}");
		sb.append("\n\t\t\tif(i==sqlColumns.length-1&&null==s.getColumnName())sql.append(\"order by \"+s.getColumnValue());");
		sb.append("\n\t\t}\n\t\tList<Object> list = dbmanager.executeQuery(sql.toString(),");
		sb.append(" show_sql, ");
		sb.append(table+");");
		sb.append("\n\t\tList<"+Table+"> "+table+"List = new ArrayList<"+Table+">();");
		sb.append("\n\t\tfor(int i=0;i<list.size();i++){");
		sb.append("\n\t\t\t"+table+"List.add(("+Table+")list.get(i));");
		sb.append("\n\t\t\t}\n\t\treturn "+table+"List;\n\t}//get"+Table+"()\n");
		return sb.toString();
	}
	
	/** 模糊查询实现得到列表(外部调用版)*/
    String publicFind(){
		StringBuffer sb = new StringBuffer();
		sb.append("\n\t/** 模糊查询实现得到列表");
		sb.append("\n\t  * @param sqlColumns 传入查询的参数对象，最后一组参数若name属性为空，将value的toString作为order_by依据");
		sb.append("\n\t  * @return   返回一组对象列表");
		sb.append("\n\t  * @throws SQLException 可能抛出SQL异常");
		sb.append("\n\t  */");
		sb.append("\n\tpublic List<"+Table+"> find"+Table+"s(SqlColumn...sqlColumns)throws SQLException{");
		sb.append("\n\t\treturn find"+Table+"(sqlColumns);");
		sb.append("\n\t}\n");
		return sb.toString();
	}
    
    /** 精确查询实现得到列表(外部调用版) */
    String publicGet(){
		StringBuffer sb = new StringBuffer();
		sb.append("\n\t/** 精确查询实现得到列表");
		sb.append("\n\t  * @param sqlColumns 传入查询的参数对象，最后一组参数若name属性为空，将value的toString作为order_by依据");
		sb.append("\n\t  * @return   返回一组对象列表");
		sb.append("\n\t  * @throws SQLException 可能抛出SQL异常");
		sb.append("\n\t  */");
		sb.append("\n\tpublic List<"+Table+"> get"+Table+"s(SqlColumn...sqlColumns)throws SQLException{");
		sb.append("\n\t\treturn get"+Table+"(sqlColumns);");
		sb.append("\n\t}\n");
		return sb.toString();
	}
	
    /**主键查找*/
	String getByPK(Member m){
		StringBuffer sb = new StringBuffer();
		sb.append("\n\tpublic "+Table+" get"+Table+"ByPK("+m.getType()+" "+m.getName()+") throws SQLException{");
		sb.append("\n\t\tList<"+Table+"> list = get"+Table+"(new SqlColumn(\""+m.getName()+"\","+m.getName()+"));");
		sb.append("\n\t\tif(list.size() == 0)return null;");
		sb.append("\n\t\telse return list.get(0);\n\t}\n");
		return sb.toString();
	}
	
	String deleteByPK(Member m){
		return "\n" +
				"\tpublic int deleteByPK("+m.getType()+" "+m.getName()+") throws SQLException{" +
				"\n\t\tString sql = new String(\"delete from "+table+" where "+m.getName()+" = \" + "+m.getName()+");" +
				"\n\t\treturn dbmanager.executeUpdate(sql, show_sql);" +
				"\n\t}";
	}
	
	/**保存实体模块的写入*/
	String saveModel(){
		StringBuffer sb = new StringBuffer();
		sb.append("\n\tpublic int save("+Table+" "+table+") throws SQLException{");
		sb.append("\n\t\treturn dbmanager.executeUpdate(\"insert into "+table+" values(");
		for (int i = 0; i < members.size(); i++) {
			sb.append("?");
			if(i!=members.size()-1)sb.append(",");
			else sb.append(")\",");
		}
		sb.append(" show_sql, ");
		for (int i = 0; i < members.size(); i++) {
			sb.append(table+"."+members.get(i).get());
			if(i!=members.size()-1)sb.append(",");
			else sb.append(");");
		}
		sb.append("\n\t}//save()\n");
		return sb.toString();
	}
	
	/**删除实体模块的写入*/
	String deleteModel(){
		StringBuffer sb = new StringBuffer();
		sb.append("\n\tpublic int delete(SqlColumn...sqlColumns)throws SQLException{");
		sb.append("\n\t\tStringBuffer sql = new StringBuffer(\"delete from "+table+" where 1=1 \");");
		sb.append("\n\t\tfor (int i = 0; i < sqlColumns.length; i++) {");
		sb.append("\n\t\t\tSqlColumn s = sqlColumns[i];");
		sb.append("\n\t\t\tif(null!=s.getColumnValue()&&!\"\".equals(s.getColumnValue())){" +
				"\n\t\t\t\tif(s.isExist())sql.append(\"and \"+s.getColumnName()+\" = '\"+s.getColumnValue()+\"' \");" +
				"\n\t\t\t\telse sql.append(\"and \"+s.getColumnName()+\" != '\"+s.getColumnValue()+\"' \");");
		sb.append("\n\t\t\t}\n\t\t}\n\t\treturn dbmanager.executeUpdate(sql.toString()");
		sb.append(", show_sql");
		sb.append(");");
		sb.append("\n\t}\n");
		return sb.toString();
	}
	
	/**更新实体模块的写入*/
	String updateModel(Member m){
		StringBuffer sb = new StringBuffer();
		sb.append("\n\tpublic int update("+Table+" "+table+") throws SQLException{");
		sb.append("\n\t\treturn dbmanager.executeUpdate(\"update "+table+" set ");
		for (int i = 0; i < members.size(); i++) {
			if(m!=members.get(i)){
				sb.append(members.get(i).getName()+"=?");
				if(i!=members.size()-1)sb.append(",");
			}
			if(i==members.size()-1) sb.append(" where "+m.getName()+"=?\",");
		}
		sb.append(" show_sql, ");
		for (int i = 0; i < members.size(); i++) {
			if(m!=members.get(i)){
				sb.append(table+"."+members.get(i).get()+",");
			}
			if(i==members.size()-1)sb.append(table+"."+m.get()+");");
		}
		sb.append("\n\t}//update()\n");
		return sb.toString();
	}
	
	@Override
	public String toString() {
		StringBuffer sb = new StringBuffer();
		sb.append(include());
		sb.append("\npublic class "+Table+"Dao {\n");
		sb.append("\n\tprivate DBManager dbmanager = new DBManager();"+
				"\n\tprivate Boolean show_sql = \"true\".equalsIgnoreCase(PropertyReader.get(Util.SHOW_SQL));\n");
//		sb.append(tableList());
//		sb.append(reverse());
//		sb.append(getCount());
//		sb.append(pageOf());
		sb.append(privateFind());
		sb.append(publicFind());
		sb.append(privateGet());
		sb.append(publicGet());
		sb.append(saveModel());
		sb.append(deleteModel());
		for (int i = 0; i < members.size(); i++) {
			Member m = members.get(i);
			if(m.isAutoIncrement && (null != (pk = m.getName())) || m.getName().equals(pk)){
				sb.append(getByPK(m));
				sb.append(deleteByPK(m));
				sb.append(updateModel(members.get(i)));
			}
		}
		sb.append("\n}//class "+Table+"Dao");
		return sb.toString();
	}
	
	/**将生成的字符串写入文件*/
	public void saveModelDao(){
		try {
			Util.write(toString(),"src."+url,Table+"Dao.java");
			System.out.println("DAO工具类:"+Table+"Dao.java 已经存入 "+url);
			ApplicationContext.addProperties(table+"Dao", url+"."+Table+"Dao");
		} catch (IOException e) {
			e.printStackTrace();
			System.out.println("文件写入异常！");
		}
	}
	
}
