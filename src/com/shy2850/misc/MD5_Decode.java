package com.shy2850.misc; 

import com.createJavaFile.myutil.Util;

public class MD5_Decode {

	private char begin;
	private char end;
	
	private int length = 6;
	
	public void setBegin(char begin) {
		this.begin = begin;
	}
	public void setEnd(char end) {
		this.end = end;
	}
	
	public void setLength(int length) {
		this.length = length;
	}
	
	public MD5_Decode(char begin, char end) {
		this.begin = begin;
		this.end = end;
	}
	
	public MD5_Decode() {
	}

	public String decode(String code){
		String s = new String();
		char[] cs = new char[length];
		for (int i = 0; i < cs.length; i++) {
			cs[i] = begin;
		}
		while(null != (s = nextString(cs))){
			if(Util.md5(s,"UTF-8",16==code.length()).equals(code))return s;
		}
		return null;
	}
	
	private String nextString(char[] stringChar){
		char lastChar = stringChar[length-1];
		if(lastChar < end){
			stringChar[length-1] = ++lastChar;
			return new String(stringChar);
		}
		
		int index = length - 1;
		while(index >= 0 && stringChar[index] == end){stringChar[index--] = begin;}
		if(index >= 0){
			stringChar[index] += 1; 
			return new String(stringChar);
		}
		return null;
	}
	
	public static void main(String[] args) {
		String s = "825393b4ebd4c269";
//		System.out.println(Util.md5("111985426", "UTF-8", true));
		MD5_Decode decode = new MD5_Decode('1','9');
		for (int i = 1; i < 10; i++) {
			decode.setLength(i);
			System.out.println("length="+i+" : "+s);
			System.out.println(decode.decode(s));
		}
		
	}
	
}
