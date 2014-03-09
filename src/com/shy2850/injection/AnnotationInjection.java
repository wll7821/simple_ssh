package com.shy2850.injection;


import java.lang.reflect.Field;
import java.util.Set;
import java.util.Map.Entry;

import com.shy2850.filter.ApplicationContext;

/**
 * 注解Injection标注的field字段注入实现类
 * @author shy2850
 * @version 1.0
 */
public class AnnotationInjection extends ApplicationContext{
	
	/**实现注解标注的field注入*/
	public static void inject(){
		System.out.println("AnnotationInjection.inject()");
		  Set<Entry<String, Object>> beans = allBeans.entrySet();
		  for (Entry<String, Object> entry : beans) {
			Object o = entry.getValue();
			Class<?> clazz = o.getClass();
			Field[] fields = clazz.getDeclaredFields();
			for (int i = 0; i < fields.length; i++) {
				Field field = fields[i];
				if(null != field.getAnnotation(Injection.class)){
					try {
						System.out.println(field + " = " + allBeans.get(field.getName()));
						field.set(o, allBeans.get(field.getName()));
					} catch (Exception e) {
						throw new RuntimeException(e);
					}
				}
				}
			}
		}
	
	
}
