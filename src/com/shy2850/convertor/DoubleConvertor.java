package com.shy2850.convertor;

import com.createJavaFile.Main.DBManager;

/**系统转换器DoubleConvertor*/
class DoubleConvertor extends Convertor {

	public DoubleConvertor() {
		super();
	}
	
	public Object parse(String convertorString) {
		if(null == convertorString || "null".equals(convertorString))return null;
		try {
			return Double.parseDouble(convertorString);
		} catch (NumberFormatException e) {
			DBManager.getOut().println("String:"+convertorString+" 无法转换到java.lang.Double");
			return null;
		}
	}

	public Class<?> getType() {
		return Double.class;
	}

}
