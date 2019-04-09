package cn.itcast.store.web.servlet;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import cn.itcast.store.service.UserService;


@WebServlet("/checkUsername")
public class CheckUsernameServlet extends HttpServlet {
	
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String username = request.getParameter("username");
		
		UserService service = new UserService();
		boolean isExist = service.checkUsername(username);
		
		String json ="{\"isExist\":" +isExist+"}";
		System.out.println(json);
		response.getWriter().write(json);
	}

	
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		doGet(request, response);
	}

}
