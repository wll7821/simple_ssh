package com.wll7821.convertor;

/**系统转换器StringConvertor*/
class StringConvertor extends Convertor {

	public StringConvertor() {
		super();
	}
	public Object parse(String convertorString) {
		return convertorString;
	}

	public Class<?> getType() {
		return String.class;
	}

}
