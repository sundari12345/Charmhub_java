package com.customer;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.LinkedHashMap;
import java.util.Map;
import com.Product.Product;
import com.login.connection;

public class Cart {
	
    private Map<Integer, Product>  cartList;
    
    Cart(int customer_id){
    	this.cartList = retriveCartProduct(customer_id);
    }

    public Map<Integer, Product> getCartList() {
		return cartList;
	}
    
	public void setCartList(Map<Integer, Product> cartList) {
		this.cartList = cartList;
	}
	
	private Map<Integer, Product> retriveCartProduct(int customer_id){
    	 this.cartList = new LinkedHashMap<>();
    	 String fetchCart = "select * from Cart c join Customer cus on cus.customer_id = c.customer_id join Product p on p.product_id = c.product_id where cus.customer_id = " + customer_id;
     	 int quantity = 0;
    	 try {
 			ResultSet cartDetails = connection.getInstance().statement().executeQuery(fetchCart);
 			while (cartDetails.next()) {
 				Product product = new Product(cartDetails.getInt("product_id"), cartDetails.getString("product_name"), cartDetails.getInt("price"), cartDetails.getInt("ratting"), cartDetails.getString("product_image_file_path"), cartDetails.getString("discription"), cartDetails.getInt("brand_id"), cartDetails.getInt("category_id"));
 				quantity = cartDetails.getInt("quantity");
 				this.cartList.put(quantity, product);
             }
 		 } catch (SQLException e) {
 			e.printStackTrace();
 		 }
    	 
    	 return cartList;
    }
}
