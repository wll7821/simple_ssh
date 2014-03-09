package com.shy2850.misc;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import com.createJavaFile.Main.DBManager;
import com.createJavaFile.myutil.Util;

/**
 * 包含可能用到的javaBean相关工具
 */

public class BeanUtils {
	/**
	 * 实现从一个对象中获取有效数据copy到指定对象的相应属性</br>
	 * (要求：源对象的copy字段需要get方法；目标对象需要set方法)
	 * 
	 * @param target  	目标对象
	 * @param root		待copy的源对象
	 */
	public static void copy(Object target,Object root){
		Class<?> targetClass = target.getClass();
		Class<?> rootClass = root.getClass();
		
		Field[] fields = rootClass.getDeclaredFields();
		for (Field field : fields) {
			try {
				String fieldName = field.getName();
				boolean isBoolean = field.getType().getSimpleName().equals("Boolean")|| field.getType().getSimpleName().equals("boolean");
				String getMethodName = isBoolean ? "is"+Util.upperFirst(fieldName) : "get"+Util.upperFirst(fieldName);
				Method getMethod = rootClass.getMethod(getMethodName);
				Object value = getMethod.invoke(root);
				
//				Field targetField = targetClass.getDeclaredField(fieldName);
//				Object obj = targetField.get(target);
//				targetField.set(obj, value);
				
				String setMethodName = "set"+Util.upperFirst(fieldName);
				Method setMethod = targetClass.getMethod(setMethodName, value.getClass());
				setMethod.invoke(target, value);
				
			} catch (SecurityException e) {
				DBManager.getOut().println(e);
			} catch (IllegalArgumentException e) {
				DBManager.getOut().println(e); 
			} catch (IllegalAccessException e) {
				DBManager.getOut().println(e); 
			} catch (NoSuchMethodException e) {
//				DBManager.getOut().println(e); 
			} catch (InvocationTargetException e) {
				DBManager.getOut().println(e); 
			}
			
		}
		
	}

}
