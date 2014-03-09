package com.shy2850.misc;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintStream;
import java.util.Date;

import com.createJavaFile.myutil.Util;
import com.shy2850.filter.DateFormat;

/**日志OutputStream设置*/
public class LogOutputStream extends PrintStream{
	
	/**默认的日志输出文件名*/
	private static String fileName = "conf/easyWebSqlBean.log";
	/**默认的日志输出文件*/
	private static File file = initLogFile(); 
	
	public LogOutputStream() throws FileNotFoundException{
		super(file);
	}

	/**初始化日志文件设置*/
	private static File initLogFile() {
		File f = new File(Util.contextPath + fileName);
		if(!f.exists()){
			try {
				f.createNewFile();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return f;
	}
	
	/**设置日志文件名*/
	public static void setFileName(String fileName) {
		if(null == fileName || fileName.length() == 0)return;
		LogOutputStream.fileName = fileName;
		file = initLogFile();
	}
	
	public void println(String x) {
		super.println(DateFormat.format(new Date())+"-----"+x);
	}
	
	public void println(Object x){
		super.println(DateFormat.format(new Date())+"-----"+x);
	}
	
}
