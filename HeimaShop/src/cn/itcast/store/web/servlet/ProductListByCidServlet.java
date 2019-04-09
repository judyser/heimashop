package cn.itcast.store.web.servlet;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import cn.itcast.store.domain.PageBean;
import cn.itcast.store.domain.Product;
import cn.itcast.store.service.ProductService;

/**
 * Servlet implementation class ProductListByCidServlet
 */
@WebServlet("/productListByCid")
public class ProductListByCidServlet extends HttpServlet {

	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String cid = request.getParameter("cid");
		
		String currentPageStr = request.getParameter("currentPage");
		if(currentPageStr ==null){
			currentPageStr="1";
		}
		int currentPage =Integer.parseInt(currentPageStr);
		int currentCount = 12;
		
		ProductService service = new ProductService();
		PageBean pageBean = service.findProductByCid(cid,currentPage,currentCount);
		
		request.setAttribute("pageBean", pageBean);
		request.setAttribute("cid", cid);
		
		//����һ����¼��ʷ��Ʒ��Ϣ�ļ���
		List<Product> historyProductList = new ArrayList<Product>();
		
		//��ÿͻ���Я�����ֽ�pids��cookie
		Cookie[] cookies = request.getCookies();
		if(cookies!=null){
			for(Cookie cookie:cookies){
				if("pids".equals(cookie.getName())){
					String pids = cookie.getValue();//3-2-1
					String[] split = pids.split("-");
					for(String pid : split){
						Product pro = service.findProductByPid(pid);
						historyProductList.add(pro);
					}
				}
			}
		}
		
		//����ʷ��¼�ļ��Ϸŵ�����
		request.setAttribute("historyProductList", historyProductList);
		
		
		request.getRequestDispatcher("/product_list.jsp").forward(request, response);
		
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		doGet(request, response);
	}

}
