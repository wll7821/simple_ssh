package com.wll7821.action;

/**将一个字符串请求包装成带跳转方式的类型对象
 * <br>
 * 配置文件中name+".isRedirect" = true;表示跳转方式为重定向
 * </br>
 * */
public class ActionForward {
	
	/**<pre>
	 * 请求类型：
	 * true 重定向
	 * false 转发
	 * </pre>
	 * */
	private boolean redirect = false;	//true 为 新请求(重定向) false 为 转发
	/**跳转的URL*/
	private String url;				    //跳转的url
	
	/**
	 * @param url 	跳转的URL
	 */
	public ActionForward(String url) {
		this(url, false);
	}
	/**
	 * @param redirect 	请求类型： true 重定向; false 转发
	 * @param url		跳转的URL
	 */
	public ActionForward(String url,boolean redirect) {
		this.redirect = redirect;
		this.url = url;
	}
	public boolean isRedirect() {
		return redirect;
	}
	public void setRedirect(boolean type) {
		this.redirect = type;
	}
	public String getUrl() {
		return url;
	}
	public void setUrl(String url) {
		this.url = url;
	}
}
