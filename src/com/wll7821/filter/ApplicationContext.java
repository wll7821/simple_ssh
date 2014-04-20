package com.wll7821.filter;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;
import java.util.Set;
import java.util.Map.Entry;

import com.createJavaFile.myutil.PropertyReader;
import com.createJavaFile.myutil.Util;

/**应用程序全局管理类：
 * 主要用来读取配置文件获得对象，以及注入对象成员
 * */
public class ApplicationContext {

	/**类配置对象的Mapping*/
	static protected Properties propBeans;
	/**应用程序管理的所有被映射类对象的单实例Mapping*/
	static protected Map<String, Object> allBeans;
	  static{
		  propBeans=new Properties();
		  String beanProp = PropertyReader.get(Util.BEAN_CONF); 
		  if(null != beanProp){
	    	try{
	    		InputStream in = new FileInputStream(Util.contextPath+beanProp);
	    		propBeans.load(in);
	    		in.close();
	    	}catch(Exception e){System.out.println("配置文件："+beanProp+"不存在！");}
		  }
	  }
	  
	  static{
		  allBeans = new HashMap<String, Object>();
		  Set<Entry<Object, Object>> beans = propBeans.entrySet();
		  for (Entry<Object, Object> entry : beans) {
			String key = entry.getKey().toString();
			String bean = entry.getValue().toString();
			try {
				Object value = Class.forName(bean).newInstance();
				allBeans.put(key, value);
			} catch (Exception e) {
				System.err.println(key + "的配置类不存在或者"+bean+"构造方法不合法！");
			}
		}
	  }
	  
	  /**实现含set方法的field注入*/
	  private static void inject(){
		  System.out.println("SetMethodInjection.inject()");
		  Set<Entry<String, Object>> beans = allBeans.entrySet();
		  for (Entry<String, Object> entry : beans) {
			  Object o = entry.getValue();
			  Class<?> clazz = o.getClass();
			  Field[] fields = clazz.getDeclaredFields();
			  for (int i = 0; i < fields.length; i++) {
				  Field field = fields[i];
				  if(propBeans.containsKey(field.getName())){
					  Method setMethod = null;
					  try {
						  setMethod = clazz.getDeclaredMethod("set"+Util.upperFirst(field.getName()),field.getType());
						  setMethod.invoke(o, allBeans.get(field.getName()));
					  } catch (Exception e) {
						  System.out.println(e);
					  } 
				  }
			  }
		  }
	  }
	
	  
	  static{
		  inject();
	  }
	  
	  /**应用程序管理的单实例对象通过字符串获得*/
	  public static Object getBean(String key){
	     return allBeans.get(key);
	  }
	  
	  /**程序添加实现类对象的映射*/
	  public static void addProperties(String key,String value){
			propBeans.put(key, value);
			try {
				OutputStream out = new FileOutputStream(Util.contextPath+PropertyReader.get(Util.BEAN_CONF));
				propBeans.store(out, key);
				out.close();
			} catch (Exception e) {
				e.printStackTrace();
			}
		  }
}
