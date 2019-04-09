package cn.itcast.store.dao;

import java.sql.SQLException;
import java.util.List;

import org.apache.commons.dbutils.QueryRunner;
import org.apache.commons.dbutils.handlers.BeanHandler;
import org.apache.commons.dbutils.handlers.BeanListHandler;
import org.apache.commons.dbutils.handlers.ScalarHandler;

import cn.itcast.store.domain.Category;
import cn.itcast.store.domain.PageBean;
import cn.itcast.store.domain.Product;
import cn.itcast.store.utils.DataSourceUtils;

public class ProductDao {

	public List<Product> findHotProdcutList() throws SQLException{
		QueryRunner runner = new QueryRunner(DataSourceUtils.getDataSource());
		String sql = "select * from product where is_hot=? limit ?,?";
		
		return  runner.query(sql, new BeanListHandler<Product>(Product.class),1,0,9);
		
	}
	public List<Product> findNewProductList()throws SQLException{
		QueryRunner runner = new QueryRunner(DataSourceUtils.getDataSource());
		String sql = "select * from product order by pdate desc limit ?,?";
		
		return  runner.query(sql, new BeanListHandler<Product>(Product.class),0,9);
		
	}
	
	public List<Category> findAllCategory() throws SQLException{
		QueryRunner runner = new QueryRunner(DataSourceUtils.getDataSource());
		String sql = "select * from category ";
		return runner.query(sql, new BeanListHandler<Category>(Category.class));
	}
	
	public int getCount(String cid) throws SQLException{
		QueryRunner runner = new QueryRunner(DataSourceUtils.getDataSource());
		String sql = "select count(*) from product where cid =?";
		
		 Long query = (Long)runner.query(sql, new ScalarHandler(),cid);
		 return query.intValue(); 
	}
	
	public List<Product> findProductByPage(String cid ,int index,int currentCount) throws SQLException{
		QueryRunner runner = new QueryRunner(DataSourceUtils.getDataSource());
		String sql = "select * from product where cid =? limit ?,?";
		
		return runner.query(sql, new BeanListHandler<Product>(Product.class),cid,index,currentCount);
		
	}
	public Product findProductByPid(String pid) throws SQLException {
		QueryRunner runner = new QueryRunner(DataSourceUtils.getDataSource());
		String sql = "select * from product where pid=?";
		return runner.query(sql, new BeanHandler<Product>(Product.class), pid);
	}
}
