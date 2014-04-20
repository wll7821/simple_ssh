package com.wll7821.convertor;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.createJavaFile.Main.DBManager;

/**系统转换器DateConvertor*/
class DateConvertor extends Convertor {
	
	/**系统中已经提供的Date字符串转换格式 */
	public static final String[] DATEFORMATs = {
		"yyyy-MM-dd hh:mm:ss",
		"yyyy-MM-dd",
		"yyyy年MM月dd日 hh时mm分ss秒",
		"yyyy年MM月dd日",
		"MM/dd/yyyy"
		};
	
	private final static List<String> formats = new ArrayList<String>();
	static{
		for (int i = 0; i < DATEFORMATs.length; i++) {
			formats.add(DATEFORMATs[i]);
		}
	}
	
	/**
	 * 按指定位置添加format字符串
	 * @param index		添加format的位置，null时添加到结尾
	 * @param strings	要添加的字符串
	 */
	static final void addDateformats(Integer index, String...strings){
		for (int i = 0; i < strings.length; i++) {
			if(null == index)formats.add(strings[i]);
			else{
				formats.add(index, strings[i]);
			}
		}
	}
	
	public DateConvertor() {
		super();
	}
	
	public Object parse(String convertorString) {
		if(null == convertorString || "null".equals(convertorString))return null;
		Date date = null;
		for (int i = 0; i < formats.size(); i++) {
			date = parse(formats.get(i), convertorString);
			if(null != date)return date;
		}
		DBManager.getOut().println("String:"+convertorString+" 无法转换到java.util.Date");
		return null;
	}

	public Class<?> getType() {
		return Date.class;
	}
	
	/**
	 * 按照指定字符串强制转换string到java.util.Date
	 * @param format			指定字符串
	 * @param convertorString	要强制转换的string对象
	 * @return
	 */
	private Date parse(String format,String convertorString){
		try {
			return new SimpleDateFormat(format).parse(convertorString);
		} catch (ParseException e) {
			return null;
		}
	}
}
