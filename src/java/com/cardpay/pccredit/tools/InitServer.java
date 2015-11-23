package com.cardpay.pccredit.tools;

import javax.servlet.ServletConfig;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;

/**
 * @author chenzhifang
 *
 * 2014-12-18下午5:52:39
 */
public class InitServer extends HttpServlet{
	private static final long serialVersionUID = 1L;
	
	private static String rootPath = null;
	
	/**
     * 初始化, 完成系统访问根目录和服务名的设置
     * @param config <tt>ServletConfig</tt>
     * @throws <tt>ServletException</tt>
     */
    public void init(ServletConfig config) throws ServletException {
        super.init(config);
        ServletContext context = config.getServletContext();
        rootPath = context.getRealPath("/");
   }
    
    public static String getRootPath(){
    	return rootPath;
    }
    
    public static String getClassPath(){
    	return rootPath + "WEB-INF/classes/";
    }
}
