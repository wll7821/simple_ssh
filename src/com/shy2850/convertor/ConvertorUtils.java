package com.shy2850.convertor;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.Date;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import com.createJavaFile.Main.DBManager;
import com.createJavaFile.myutil.Util;
import com.shy2850.filter.ApplicationContext;

/**
 * @author shy2850
 *         <pre>
 *         转换器的控制中心 包括加载系统已经提供的转换器类型 接收用户自定义的转换器实例(已注册类型的)
 *         </pre>
 */
@SuppressWarnings("unchecked")
public class ConvertorUtils {
	
	/**空的Enumeration对象*/
	private static final Enumeration<String> EMPTY_ENUMERATIONS = new Enumeration<String>() {
		public String nextElement() {
			return null;
		}

		public boolean hasMoreElements() {
			return false;
		}
	}; 

	/** 系统中受管理的所有单实例映射对象 */
	public static Map<String, Object> beans = new HashMap<String, Object>();

	/** 系统已经注册的所有转换器 */
	public static Map<Class<?>, Convertor> convertors = new HashMap<Class<?>, Convertor>();
	static {
		convertors.put(String.class, new StringConvertor());
		convertors.put(Integer.class, new IntegerConvertor());
		convertors.put(Double.class, new DoubleConvertor());
		convertors.put(Boolean.class, new BooleanConvertor());
		convertors.put(Date.class, new DateConvertor());
		convertors.put(Float.class, new FloatConvertor());
		convertors.put(Long.class, new LongConvertor());
		convertors.put(int.class, new IntegerConvertor());
		convertors.put(boolean.class, new BooleanConvertor());
		convertors.put(double.class, new DoubleConvertor());
		convertors.put(float.class, new FloatConvertor());
		convertors.put(long.class, new LongConvertor());
	}

	/** 从request请求的参数中特征性地修改系统管理的单实例映射类 */
	private static void convertBeans(HttpServletRequest request, Object action)
			throws SecurityException, NoSuchFieldException {
		beans.clear();
		Enumeration<String> params = (null == request.getParameterNames()) ? EMPTY_ENUMERATIONS
				: request.getParameterNames();
		while (params.hasMoreElements()) {
			String param = params.nextElement();
			if (param.indexOf('.') != -1) {
				String value = request.getParameter(param);
				String bean = param.substring(0, param.indexOf('.'));
				if (!beans.containsKey(bean)) {
					Object o;
					try {
						o = ApplicationContext.getBean(bean);
						beans.put(bean, o);
					} catch (Exception e) {
						DBManager.getOut().println(bean + "在配置文件中未找到或类型有误!\n" + e);
					}
				}
				String fieldName = param.substring(param.indexOf('.') + 1);
				{
					Object o = beans.get(bean);
					Field field = null;
					field = o.getClass().getDeclaredField(fieldName);
					Method setMethod = null;
					try {
						setMethod = o.getClass().getMethod(
								"set" + Util.upperFirst(field.getName()),
								field.getType());
					} catch (Exception e1) {
						throw new RuntimeException(o.getClass() + "中没有" + field
								+ "的set方法");
					}
					try {
						setMethod.invoke(o, convertors.get(field.getType())
								.parse(value));
					} catch (Exception e) {
						throw new RuntimeException("没有" + field.getType()
								+ "的转换器！", e);
					}
				}
			}
		}

	}

	/** 转换方法实现指定类型的字符串到类型对象的转换 */
	public static Object parseString(Class<?> clazz, String param) {
		return convertors.get(clazz).parse(param);
	}

	/** 自定义转换器需要的静态注册方法 */
	public static void regist(Convertor convertor) {
		if (null == convertor || null == convertor.getType()) {
			System.out
					.println("注册器：" + convertor + "注册失败\n可能因为该对象不存在或者注册类型为空！");
		}
		convertors.put(convertor.getType(), convertor);
	}

	/** 请求分发到指定action类时所做的准备工作：主要将匹配(即同名)的action成员&请求参数同步 */
	public static void convertToAction(HttpServletRequest request, Object action)
			throws SecurityException, NoSuchFieldException {
		// System.out.println(action.getClass());
		Class<?> clazz = action.getClass();
		Field[] fields = clazz.getDeclaredFields();
		for (int i = 0; i < fields.length; i++) {
			Field field = fields[i];
			String value = request.getParameter(field.getName());
			if (null == value)
				continue;
			Method setMethod = null;
				try {
					setMethod = clazz.getMethod("set"
							+ Util.upperFirst(field.getName()), field.getType());
				} catch (NoSuchMethodException e1) {
					throw new RuntimeException(clazz + "中没有" + field + "的set方法");
				}
			try {
				setMethod.invoke(action, convertors.get(field.getType()).parse(
						value));
			} catch (Exception e) {
				throw new RuntimeException("没有" + field.getType() + "的转换器！");
			}
		}
		convertBeans(request, action);
	}

	/**
	 * 按指定位置添加format字符串
	 * @param index		添加format的位置，null时添加到结尾
	 * @param strings	要添加的字符串
	 */
	public static void addDateFomarts(Integer index, String...strings){
		DateConvertor.addDateformats(index, strings);
	}
	
	
}
