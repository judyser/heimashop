package cn.itcast.store.web.servlet;

import java.io.IOException;
import java.sql.SQLException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import cn.itcast.store.domain.User;
import cn.itcast.store.service.UserService;

@WebServlet("/UserServlet")
public class UserServlet extends HttpServlet {
	
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String username = request.getParameter("username");
		String password = request.getParameter("password");
		User user = new User();
		user.setUsername(username);
		user.setPassword(password);
		
		
		
		try {
			UserService service = new UserService();
			User userlogin =service.login(user);
			
			if(userlogin !=null){
				String aotoLogin = request.getParameter("autoLogin");
				System.out.println(aotoLogin);
				if("on".equals(aotoLogin)){
					Cookie autoLoginCookie = new Cookie("autoLoinCookie",user.getUsername()+"@"+user.getPassword());
					autoLoginCookie.setPath("/");
					autoLoginCookie.setMaxAge(60*60*24*7);
					response.addCookie(autoLoginCookie);
					
				}else{
					Cookie autoLoginCookie=new Cookie("autoLoginCookie","");
					autoLoginCookie.setPath("/");
					autoLoginCookie.setMaxAge(0);
					response.addCookie(autoLoginCookie);
				}
				String rememberme =request.getParameter("rememberme");
				if("1".equals(rememberme)){
					Cookie rememberCookie = new Cookie("rememberCookie",user.getUsername());
					rememberCookie.setPath("/");
					rememberCookie.setMaxAge(60*60*24*7);
					response.addCookie(rememberCookie);
				}else{
					Cookie rememberCookie=new Cookie("rememberCookie","");
					rememberCookie.setPath("/");
					rememberCookie.setMaxAge(0);
					response.addCookie(rememberCookie);
				}
				request.getSession().setAttribute("loginUser", userlogin);
				response.sendRedirect(request.getContextPath());
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		doGet(request, response);
	}
	
/*	public String login(HttpServletRequest request,HttpServletResponse response) throws ServletException, IOException, SQLException {
		User user = new User();
		String username = request.getParameter("username");
		String password = request.getParameter("password");
		
		user.setUsername(username);
		user.setPassword(password);
		UserService service = new UserService();
		User userlogin = service.login(user);
		System.out.println(userlogin.getName());
		
		if(userlogin !=null){
			request.getSession().setAttribute("loginUser", userlogin);
			response.sendRedirect(request.getContextPath()+"/");
			return null;
		}
		return "jsp/login.jsp";
	}*/


}
