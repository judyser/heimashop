package cn.itcast.store.service;

import java.sql.SQLException;
import java.util.List;

import org.apache.commons.dbutils.QueryRunner;
import org.apache.commons.dbutils.handlers.BeanHandler;
import org.apache.commons.dbutils.handlers.BeanListHandler;
import org.apache.commons.dbutils.handlers.ScalarHandler;

import cn.itcast.store.dao.ProductDao;
import cn.itcast.store.domain.Category;
import cn.itcast.store.domain.PageBean;
import cn.itcast.store.domain.Product;
import cn.itcast.store.utils.DataSourceUtils;

public class ProductService {
	private  ProductDao dao  = new ProductDao();
	
	public List<Product> findHotProdcutList(){
		List<Product> productList = null;
		try {
			productList = dao.findHotProdcutList();
		} catch (SQLException e) {
			
			e.printStackTrace();
		}
		return productList;
		
		
	}
	public List<Product> findNewProductList(){
		List<Product> list = null;
		try {
			list =dao.findNewProductList();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return list;
	}
	
	public List<Category> findAllCategory() {
		List<Category> list = null;
		try {
			list = dao.findAllCategory();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return list;
	}
	
	public int getCount(String cid) {
		int count =0;
		try {
			dao.getCount(cid);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return count;
	}
	
	public PageBean<Product> findProductByCid(String cid ,int currentPage,int currentCount) {
		PageBean<Product> pageBean  = new PageBean<Product>();
		pageBean.setCurrentCount(currentCount);
		
		pageBean.setCurrentPage(currentPage);
		
		int totalCount = 0;
		try {
			totalCount = dao.getCount(cid);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		pageBean.setTotalCount(totalCount);
		int totalPage = (int) Math.ceil(1.0*totalCount/currentCount);
		
		int index = (currentPage-1)*currentCount;
		List<Product>list = null;
		try {
			list = dao.findProductByPage(cid, index, currentCount);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		pageBean.setList(list);
		return pageBean;
		
		
	}
	public Product findProductByPid(String pid)  {
		Product product = null;
		try {
			product = dao.findProductByPid(pid);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return product;
	}
}
