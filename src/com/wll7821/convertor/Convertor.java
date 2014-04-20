package com.wll7821.convertor;


/**<pre>
 * 转换器类
 * 用户自定义的转换器类需要通过ConvertorUtils.regist注册生效
 * 当前type为基本类型包装类或者String/Date时会覆盖原有的类型转换器
 * 每个转换器子类对象都必须通过此类的构造方法或者type的set方法设置类型
 * </pre>
 * */
public abstract class Convertor{
	
	/**当前转换器的主类型*/
	public abstract Class<?> getType();

	public Convertor() {
	}
	
	/**类型转换方法*/
	public abstract Object parse(String convertorString);
	
}
