package com.shy2850.convertor;

import com.createJavaFile.Main.DBManager;

/**系统转换器LongConvertor*/
class LongConvertor extends Convertor {
	
	public LongConvertor() {
		super();
	}
	
	public Object parse(String convertorString) {
		if(null == convertorString || "null".equals(convertorString))return null;
		try {
			return Long.parseLong(convertorString);
		} catch (NumberFormatException e) {
			DBManager.getOut().println("String:"+convertorString+" 无法转换到java.lang.Long");
			return null;
		}
	}

	public Class<?> getType() {
		return Long.class;
	}

}
