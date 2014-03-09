package com.createJavaFile.myutil;

import java.util.*;
import java.io.*;

/**主配置文件src/db_conf.properties的读取专用类*/
public class PropertyReader {
	
	static private Properties ps;
	static private String fileName = Util.contextPath+Util.DB_CONFIG;
  
	/**更改默认的资源文件配置，并且更新资源文件*/
	public void setFileName(String fileName) {
		PropertyReader.fileName = fileName;
		init();
	}
	public static String getFileName() {
		return fileName;
	}
  
	static{
		init();
	}
	
	/**加载资源文件*/
	private static void init(){
		ps=new Properties();
		try{
			InputStream in = new FileInputStream(fileName);
			ps.load(in);
			in.close();
		}catch(Exception e){System.out.println("配置文件："+fileName+"不存在！");}
	}

	/**获得指定字符串配置*/
	public static String get(String key){
		return (String)ps.get(key);
	}
	/**向总配置文件中添加配置*/
	public static void addProperties(String key,String value){
		ps.put(key, value);
			try {
				OutputStream out = new FileOutputStream(fileName);
				ps.store(out, key);
				out.close();
			} catch (Exception e) {
				e.printStackTrace();
			}
	  	}
}

