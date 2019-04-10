package cn.itcast.store.web.servlet;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.google.gson.Gson;

import cn.itcast.store.domain.Cart;
import cn.itcast.store.domain.CartItem;
import cn.itcast.store.domain.Category;
import cn.itcast.store.domain.PageBean;
import cn.itcast.store.domain.Product;
import cn.itcast.store.service.ProductService;
import cn.itcast.store.utils.JedisPoolUtils;
import redis.clients.jedis.Jedis;


@WebServlet("/product")
public class ProductServlet extends BaseSerlvet {

	public void clearCart(HttpServletRequest request,HttpServletResponse response) throws IOException{
		HttpSession session = request.getSession();
		session.removeAttribute("cart");
		
		response.sendRedirect(request.getContextPath()+"/cart.jsp");
	}
	
	//ɾ����һ��Ʒ
	public void delProFromCart(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		//���Ҫɾ����item��pid
		String pid = request.getParameter("pid");
		//ɾ��session�еĹ��ﳵ�еĹ�������е�item
		HttpSession session = request.getSession();
		Cart cart = (Cart) session.getAttribute("cart");
		if(cart!=null){
			Map<String, CartItem> cartItems = cart.getCartItems();
			//��Ҫ�޸��ܼ�
			cart.setTotal(cart.getTotal()-cartItems.get(pid).getSubtotal());
			//ɾ��
			cartItems.remove(pid);
			cart.setCartItems(cartItems);

		}

		session.setAttribute("cart", cart);

		//��ת��cart.jsp
		response.sendRedirect(request.getContextPath()+"/cart.jsp");
	}
	
	public void addProductToCart(HttpServletRequest request,HttpServletResponse response) throws IOException{
		HttpSession session = request.getSession(); 
		ProductService service = new ProductService();
		
		String pid = request.getParameter("pid");
		int buyNum = Integer.parseInt(request.getParameter("buyNum"));
		
		Product product = service.findProductByPid(pid);
		double subtotal = product.getShop_price()*buyNum;
		
		CartItem item = new CartItem ();
		item.setBuyNum(buyNum);
		item.setProduct(product);
		item.setSubtotal(subtotal);
		
		Cart cart = (Cart) session.getAttribute("cart");
		if(cart == null){
			cart = new Cart();
		}
		Map<String ,CartItem> cartItems = cart.getCartItems();
		double newsubtotal = 0.0;
		if(cartItems.containsKey(pid)){
			CartItem cartItem = cartItems.get(pid);
			int oldBuyNum = cartItem.getBuyNum();
			oldBuyNum +=buyNum;
			cartItem.setBuyNum(oldBuyNum);
			cart.setCartItems(cartItems);
			
			double oldsubtotal = cartItem.getSubtotal();
			newsubtotal =buyNum*product.getShop_price();
			cartItem.setSubtotal(oldsubtotal+newsubtotal);
		}else{
			cart.getCartItems().put(product.getPid(), item);
			newsubtotal =buyNum*product.getShop_price();
		}
		double total = cart.getTotal()+newsubtotal;
		cart.setTotal(total);
		
		session.setAttribute("cart", cart);
		response.sendRedirect(request.getContextPath()+"/cart.jsp");
		
	}
	//��ʾ��Ʒ�����ĵĹ���
	public void categoryList(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		ProductService service = new ProductService();

		//�ȴӻ����в�ѯcategoryList �����ֱ��ʹ�� û���ڴ����ݿ��в�ѯ �浽������
		//1�����jedis���� ����redis���ݿ�
		Jedis jedis = JedisPoolUtils.getJedis();
		String categoryListJson = jedis.get("categoryListJson");
		//2���ж�categoryListJson�Ƿ�Ϊ��
		if(categoryListJson==null){
			System.out.println("����û������ ��ѯ���ݿ�");
			//׼����������
			List<Category> categoryList = service.findAllCategory();
			Gson gson = new Gson();
			categoryListJson = gson.toJson(categoryList);
			jedis.set("categoryListJson", categoryListJson);
		}

		response.setContentType("text/html;charset=UTF-8");
		response.getWriter().write(categoryListJson);
	}
	//��ʾ��ҳ�Ĺ���

	public void index(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		ProductService service = new ProductService();

		//׼��������Ʒ---List<Product>
		List<Product> hotProductList = service.findHotProdcutList();

		//׼��������Ʒ---List<Product>
		List<Product> newProductList = service.findNewProductList();

		//׼����������
		//List<Category> categoryList = service.findAllCategory();

		//request.setAttribute("categoryList", categoryList);
		request.setAttribute("hotProductList", hotProductList);
		request.setAttribute("newProductList", newProductList);

		request.getRequestDispatcher("/index.jsp").forward(request, response);

	}
	//��ʾ��Ʒ����ϸ��Ϣ����
	public void productInfo(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

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


		//��ÿͻ���Я��cookie---���������pids��cookie
		String pids = pid;
		Cookie[] cookies = request.getCookies();
		if(cookies!=null){
			for(Cookie cookie : cookies){
				if("pids".equals(cookie.getName())){
					pids = cookie.getValue();
					//1-3-2 ���η�����Ʒpid��8----->8-1-3-2
					//1-3-2 ���η�����Ʒpid��3----->3-1-2
					//1-3-2 ���η�����Ʒpid��2----->2-1-3
					//��pids���һ������
					String[] split = pids.split("-");//{3,1,2}
					List<String> asList = Arrays.asList(split);//[3,1,2]
					LinkedList<String> list = new LinkedList<String>(asList);//[3,1,2]
					//�жϼ������Ƿ���ڵ�ǰpid
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
	//������Ʒ���������Ʒ���б�
	public void productList(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

		//���cid
		String cid = request.getParameter("cid");

		String currentPageStr = request.getParameter("currentPage");
		if(currentPageStr==null) currentPageStr="1";
		int currentPage = Integer.parseInt(currentPageStr);
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
}
