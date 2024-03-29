package com.customer;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.LinkedHashMap;
import java.util.Map;
import com.Product.Product;
import com.login.connection;

public class Cart {
	
    private Map<Product, Integer>  cartList;
    
    Cart(int customer_id){
    	System.out.println(retriveCartProduct(customer_id));
    	this.cartList = retriveCartProduct(customer_id);
    }

    public Map<Product, Integer> getCartList() {
		return cartList;
	}
    
	public void setCartList(Map<Product, Integer> cartList) {
		this.cartList = cartList;
	}
	
	private Map<Product, Integer> retriveCartProduct(int customer_id){
		System.out.println(customer_id+"why this happen");
		 Map<Product, Integer>  cartList = new LinkedHashMap<>();
    	 String fetchCart = "select * from Cart c join Customer cus on cus.customer_id = c.customer_id join Product p on p.product_id = c.product_id where cus.customer_id = ?";
     	 int quantity = 0;
     	 Product product = null; 
    	 try {
    		 
    		 PreparedStatement preparedStatement = connection.getInstance().connector().prepareStatement(fetchCart);
			 preparedStatement.setInt(1, customer_id);
		     ResultSet cartDetails =  preparedStatement.executeQuery();

 			 while (cartDetails.next()) {
 				product = new Product(cartDetails.getInt("product_id"), cartDetails.getString("product_name"), cartDetails.getInt("price"), cartDetails.getInt("ratting"), cartDetails.getString("product_image_file_path"), cartDetails.getString("discription"), cartDetails.getInt("brand_id"), cartDetails.getInt("category_id"));
 				quantity = cartDetails.getInt("quantity");
 				cartList.put(product, quantity);
             }
 		 } catch (SQLException e) {
 			e.printStackTrace();
 		 }
    	 
    	 return cartList;
    }
}
