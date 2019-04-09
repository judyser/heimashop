package cn.itcast.store.web.servlet;

import java.io.IOException;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import cn.itcast.store.domain.Product;
import cn.itcast.store.service.ProductService;


@WebServlet("/productInfo")
public class ProductInfoServlet extends HttpServlet {
	
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		//��õ�ǰҳ
		String currentPage = request.getParameter("currentPage");
		//�����Ʒ���
		String cid = request.getParameter("cid");
		
		//���Ҫ��ѯ����Ʒ��pid
		String pid = request.getParameter("pid");
		
		ProductService service = new ProductService();
		Product product = service.findProductByPid(pid);
		
		request.setAttribute("product", product);
		request.setAttribute("currentPage", currentPage);
		request.setAttribute("cid", cid);
		
		String pids = pid;
		Cookie[] cookies = request.getCookies();
		
		if(cookies != null){
			for(Cookie cookie :cookies){
				if("pids".equals(cookie.getName())){
					pids = cookie.getValue();
					
					String []split = pids.split("-");
					List<String> asList = Arrays.asList(split);
					LinkedList<String> list = new LinkedList<String>(asList);
					if(list.contains(pid)){
						//������ǰ�鿴��Ʒ��pid
						list.remove(pid);
						list.addFirst(pid);
					}else{
						//��������ǰ�鿴��Ʒ��pid ֱ�ӽ���pid�ŵ�ͷ��
						list.addFirst(pid);
					}
					//��[3,1,2]ת��3-1-2�ַ���
					StringBuffer sb = new StringBuffer();
					for(int i=0;i<list.size()&&i<7;i++){
						sb.append(list.get(i));
						sb.append("-");//3-1-2-
					}
					//ȥ��3-1-2-���-
					pids = sb.substring(0, sb.length()-1);
				}
			}
		}
		Cookie cookie_pids = new Cookie("pids",pids);
		response.addCookie(cookie_pids);
		
		
		
		request.getRequestDispatcher("/product_info.jsp").forward(request, response);
		
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		doGet(request, response);
	}

}
