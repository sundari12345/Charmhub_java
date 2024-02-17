package com.customer;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.LinkedList;
import com.Product.Product;
import com.login.connection;

public class WishList {
	
   private LinkedList<Product> WishListProducts;
   
   WishList(int customer_id){
	   this.WishListProducts = retriveWishListProduct(customer_id);
   }
   
   private LinkedList<Product> retriveWishListProduct(int customer_id){
	   this.WishListProducts = new LinkedList<>();
	   
	   String fetchWishList = "select * from WishList w join Customer cus on cus.customer_id = w.customer_id join Product p on p.product_id = w.product_id where cus.customer_id = " + customer_id;
  	   try {
			ResultSet wishListDetails = connection.getInstance().statement().executeQuery(fetchWishList);
			while (wishListDetails.next()) {
				Product product = new Product(wishListDetails.getInt("product_id"), wishListDetails.getString("product_name"), wishListDetails.getInt("price"), wishListDetails.getInt("ratting"), wishListDetails.getString("product_image_file_path"), wishListDetails.getString("discription"), wishListDetails.getInt("brand_id"), wishListDetails.getInt("category_id"));
				this.WishListProducts.add(product);
           }
		 } catch (SQLException e) {
			e.printStackTrace();
		 }
	   
	   return WishListProducts;
   }

	public LinkedList<Product> getWishListProducts() {
		return WishListProducts;
	}
	
	public void setWishListProducts(LinkedList<Product> wishListProducts) {
		WishListProducts = wishListProducts;
	}
   
   
}
