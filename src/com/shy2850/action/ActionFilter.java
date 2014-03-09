package com.shy2850.action;

import java.io.File;
import java.io.IOException;
import java.io.PrintStream;
import java.lang.reflect.Method;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.createJavaFile.Main.DBManager;
import com.createJavaFile.myutil.PropertyReader;
import com.createJavaFile.myutil.Util;
import com.shy2850.convertor.ConvertorUtils;
import com.shy2850.filter.ApplicationContext;
import com.shy2850.filter.WebContext;

/**
 * MVC框架的转发控制中心
 * 
 * <pre>
 * 	该Filter类通过截取请求字符串，
 * 	分别从系统中获得对应的请求对象或者是对象方法；
 * 	然后通过执行方法结果(String)通过配置文件寻找对应的请求路径
 * </pre>
 * */
public class ActionFilter implements Filter{
	private HttpServletRequest request;
	private HttpServletResponse response;
	private static String divid = "$";

	/** 页面请求以及分发的主要流程 */
	private void intercept(HttpServletRequest request,
			HttpServletResponse response, String urlEndding) throws ServletException, IOException {
		PrintStream out = DBManager.getOut();
		/** 将当前请求request保存至静态全局类WebContext中 */
		WebContext.setRequest(request);
		WebContext.setResponse(response);
		try {
			
			/** 截获请求中的 类名actionName 和 方法名methodName */
			String actionName = urlEndding.substring(1,urlEndding.indexOf(divid));
			String methodName = urlEndding.substring(urlEndding.indexOf(divid) + 1);
			/** 如果请求方法名为do，自动转换为execute */
			if ("do".equals(methodName) || "".equals(methodName))
				methodName = "execute";

			String forwardUrl = null;
			ActionForward next = null;

			/** 获得请求的action类的系统单实例 */
			Object action = ApplicationContext.getBean(actionName); // 反射类

			/** 获取失败时：抛出提示 */
			if (null == action)
				out.println("Action类" + actionName + "未找到！");

			try {
				/** 使用适配器，为进入指定action类做准备 */
				ConvertorUtils.convertToAction(request, action);// 适配器的使用
			} catch (Exception e2) {
				out.println("适配器转换异常！");
				throw new Exception(e2);
			}

			Method method = null;
			try {
				/** 获取指定action中的方法 */
				method = action.getClass().getMethod(methodName);
			} catch (Exception e1) {
				out.println(action.getClass() + "中没有方法："
						+ methodName);
			}

			try {
				/** 执行指定action中的方法 */
				forwardUrl = (String) method.invoke(action);
			} catch (Exception e1) {
				out.println(actionName+"."+methodName+"方法执行异常！");
				throw new RuntimeException(e1);
			}

			/** 构建跳转请求包装对象 */
			next = ActionManager.getForward(forwardUrl);

			if (null == next.getUrl()){
				out.println("配置文件中没有" + forwardUrl+ "的forward配置！");
				throw new Exception("配置文件中没有" + forwardUrl
						+ "的forward配置！");
			}
						
			try {
				if (next.isRedirect()) {
					response.sendRedirect(request.getContextPath()
							+ next.getUrl());
				} else {
					request.getRequestDispatcher(next.getUrl()).forward(
							request, response);
				}
			} catch (Exception e) {
				throw new RuntimeException(e.getMessage(), e.getCause());
			}

		} catch (Exception ex) {
			out.println(ex.getMessage() + "\n" + ex.getCause());
		}

	}//intercept

	public void destroy() {
		
	}

	public void doFilter(ServletRequest req, ServletResponse resp,
			FilterChain chain) throws IOException, ServletException {
		request = (HttpServletRequest) req;
		response= (HttpServletResponse) resp;
		String url = request.getRequestURI();
		String urlEndding = url.substring(url.lastIndexOf('/'));
		
		if(urlEndding.contains(divid)){
			intercept(request, response, urlEndding);
		}
		else{
			chain.doFilter(req, resp);
		}
		
	}

	public void init(FilterConfig config) throws ServletException {
		
		if(!new File(Util.DB_CONFIG).isFile()){
			Util.contextPath = config.getServletContext().getRealPath("/");
		}
		else{
			shy2850();
		}
		System.out.println("contextPath:"+Util.contextPath);
		try {
			Class.forName("com.shy2850.filter.ApplicationContext");
		} catch (ClassNotFoundException e) {
			System.out.println(e);
		}
		
		String s = PropertyReader.get(Util.DIVID_CONFIG);
		if(null != s && s.length() > 0){
			divid = s;
			System.out.println("分隔符配置:"+divid);
		}
	}
	

	/**框架作者标识②*/
	public static void shy2850(){
		System.err.println("@DEVELOPED BY SHY2850@");
	}

}// class

