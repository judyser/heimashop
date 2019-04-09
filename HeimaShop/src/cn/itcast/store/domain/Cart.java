package cn.itcast.store.domain;

import java.util.HashMap;
import java.util.Map;

public class Cart {
	//该购物车中存储的n个购物项
		private Map<String,CartItem> cartItems = new HashMap<String,CartItem>();
		
		//商品的总计
		private double total;

		public Map<String, CartItem> getCartItems() {
			return cartItems;
		}

		public void setCartItems(Map<String, CartItem> cartItems) {
			this.cartItems = cartItems;
		}

		public double getTotal() {
			total =0;
			for (Map.Entry<String, CartItem> entry :cartItems.entrySet()){
				CartItem cartItem = entry.getValue();
				total+=cartItem.getSubtotal();
			}
			return total;
		}
		

		public void setTotal(double total) {
			this.total = total;
		}
		
		public void addCart(Product product,int count){
			if(product ==null){
				return ;
			}
			CartItem cartItem = cartItems.get(product.getPid());
			
			if(cartItem ==null){
				cartItem = new CartItem();
				cartItem.setProduct(product);
				cartItem.setBuyNum(count);
				
				cartItems.put(product.getPid(), cartItem);
			}else{
				cartItem.setBuyNum(cartItem.getBuyNum()+count);
				
			}
			total +=cartItem.getSubtotal();
			total += count *product.getShop_price();
		}
		
		public void removeCart(String id){
			CartItem cartItem =cartItems.remove(id);
			total -=cartItem.getSubtotal();
		}
		
		public void clearCart(){
			cartItems.clear();
			total =0;
		}
}
