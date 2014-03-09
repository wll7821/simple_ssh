package com.shy2850.injection;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.Set;
import java.util.Map.Entry;

import com.createJavaFile.myutil.Util;
import com.shy2850.filter.ApplicationContext;

/**
 * 含set方法的field字段注入实现类
 * @author shy2850
 * @version 1.0
 */
public class SetMethodInjection extends ApplicationContext{
	
	/**实现含set方法的field注入*/
	public static void inject(){
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
	
	
}