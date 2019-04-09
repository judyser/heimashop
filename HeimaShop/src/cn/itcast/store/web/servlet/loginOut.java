package cn.itcast.store.web.servlet;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import cn.itcast.store.domain.User;
import cn.itcast.store.utils.CookieUtil;

/**
 * Servlet implementation class loginOut
 */
@WebServlet("/loginOut")
public class loginOut extends HttpServlet {

	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		User user = (User) request.getSession().getAttribute("loginUser");
	
		
		Cookie[] cookies = request.getCookies();
		for(Cookie c :cookies){
			if(c.getName().equals("autoLoinCookie"))
				c.setMaxAge(0);
			
		}
		request.getSession().removeAttribute("loginUser");
		request.getSession().invalidate();
		System.out.println("+++++++++++++++++");
		
		response.sendRedirect(request.getContextPath());
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		doGet(request, response);
	}

}
