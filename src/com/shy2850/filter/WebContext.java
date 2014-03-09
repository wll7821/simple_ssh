 package com.shy2850.filter;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

/**存取应用程序在本地的请求request对象以及相关对象*/
public class WebContext {
	
	private static ThreadLocal<HttpServletRequest> requestHolder = new ThreadLocal<HttpServletRequest>();
	private static ThreadLocal<HttpServletResponse> responseHolder = new ThreadLocal<HttpServletResponse>(); 
	
	public static void setRequest(HttpServletRequest request) {
		requestHolder.set(request);	
	}
	
	public static void setResponse(HttpServletResponse response) {
		responseHolder.set(response);
	}
	
	public static HttpServletResponse getResponse() {
		return responseHolder.get();
	}
	
	public static HttpServletRequest getRequest() {
		return requestHolder.get();
	}
	public static HttpSession getSession() {
		return requestHolder.get().getSession();
	}
	public static ServletContext getApplication() {
		return requestHolder.get().getSession().getServletContext();
	}
}
