package cn.itcast.store.web.filter;

import java.io.IOException;
import java.sql.SQLException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import cn.itcast.store.domain.User;
import cn.itcast.store.service.UserService;
import cn.itcast.store.utils.CookieUtil;

/**
 * Servlet Filter implementation class LoginFilter
 */
@WebFilter("/*")
public class LoginFilter implements Filter {


	public void destroy() {
		// TODO Auto-generated method stub
	}

	
	public void doFilter(ServletRequest req, ServletResponse resp, FilterChain chain) throws IOException, ServletException {
		HttpServletRequest request = (HttpServletRequest) req;
		HttpServletResponse response = (HttpServletResponse) resp;
		
		String servletPath = request.getContextPath();
		String request_url = request.getRequestURI();
		//System.out.println(servletPath);
		//System.out.println(request_url);
		if(request_url.substring(servletPath.length()).equals("/login.jsp")||
				request_url.substring(servletPath.length()).equals("/categoryList")	){
			chain.doFilter(request, response);
			return ;
		}
		User userlogin = (User) request.getSession().getAttribute("loginUser");
		if(userlogin !=null){
			
			chain.doFilter(request, response);
			
			
			return ;
		}
		Cookie userCookie = CookieUtil.getCookie(request.getCookies(), "autoLoinCookie");
		if(userCookie ==null){
			chain.doFilter(request, response);
			return ;
		}
		String []u = userCookie.getValue().split("@");
		String username = u[0];
		String password = u[1];
		User user = new User();
		user.setUsername(username);
		user.setPassword(password);
		System.out.println(username +":"+password);
		
		
			try {
				UserService service = new UserService();
				userlogin =service.login(user);
				if(userlogin ==null){
					chain.doFilter(request, response);
					return ;
				}
				request.getSession().setAttribute("loginUser", userlogin);
				System.out.println("-----------");
				chain.doFilter(request, response);
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				System.out.println("µÇÂ¼Òì³££¬ºöÂÉ");
			}
			
		
	
	}

	/**
	 * @see Filter#init(FilterConfig)
	 */
	public void init(FilterConfig fConfig) throws ServletException {
		// TODO Auto-generated method stub
	}

}
