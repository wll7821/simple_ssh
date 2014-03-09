package com.shy2850.action;

import java.io.FileInputStream;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;
import java.util.Set;
import java.util.Map.Entry;

import com.createJavaFile.myutil.PropertyReader;
import com.createJavaFile.myutil.Util;


public class ActionManager {
	
	/**跳转置对象的Mapping*/
	static private Properties propForwards;
	/**应用程序管理的所有被映射跳转对象的单实例Mapping*/
	static private Map<String, ActionForward> allForwards;
	
	  static{
		  propForwards=new Properties();
		  String forwardProp = PropertyReader.get(Util.FORWARD_CONF); 
		  if(null != forwardProp){
	    	try{
	    		InputStream in = new FileInputStream(Util.contextPath+forwardProp);
	    		propForwards.load(in);
	    		in.close();
	    	}catch(Exception e){System.out.println("配置文件："+forwardProp+"不存在！");}
		  }
	  }
	  
	  static{
		  allForwards = new HashMap<String, ActionForward>();
		  Set<Entry<Object, Object>> set = propForwards.entrySet();
		  for (Entry<Object, Object> entry : set) {
			  String name = entry.getKey().toString();
			  ActionForward  forward = new ActionForward(entry.getValue().toString());
			  forward.setRedirect("true".equalsIgnoreCase(propForwards.getProperty(name+".isRedirect")));
			  allForwards.put(name, forward);
		}
	  }
	  
	  /**应用程序管理的跳转对象通过字符串获得
	   * <br>
	   * name+".isRedirect" = true;表示跳转方式为重定向
	   * </br>
	   * */
	  public static ActionForward getForward(String key){
		 return allForwards.get(key); 
	  }
	
}
