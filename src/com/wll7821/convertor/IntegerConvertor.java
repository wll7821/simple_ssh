package com.wll7821.convertor;

import com.createJavaFile.Main.DBManager;

/**系统转换器IntegerConvertor*/
class IntegerConvertor extends Convertor {

	public IntegerConvertor() {
		super();
	}
	
	public Object parse(String convertorString) {
		if(null == convertorString || "null".equals(convertorString))return null;
		try {
			return Integer.parseInt(convertorString);
		} catch (NumberFormatException e) {
			DBManager.getOut().println("String:"+convertorString+" 无法转换到java.lang.Ingeter");
			return null;
		}
	}

	public Class<?> getType() {
		return Integer.class;
	}

}
