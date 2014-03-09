package com.shy2850.filter;

import java.text.SimpleDateFormat;
import java.util.Date;

/**实体BEAN中DATE类型字段的toString方式,*/
public class DateFormat {
	public static final String DATEFOMART="yyyy-MM-dd hh:mm:ss";
	
	static final SimpleDateFormat FORMAT = new SimpleDateFormat(DATEFOMART);
	
	public static String format(Date date){
		
		try {
			return FORMAT.format(date);
		} catch (Exception e) {
			return null;
		}
	}
	
}
