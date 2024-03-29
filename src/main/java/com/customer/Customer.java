package com.customer;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.LinkedList;
import java.util.List;
import java.util.Map.Entry;

import org.json.JSONException;
import org.json.JSONObject;

import com.Product.Product;
import com.Product.Review;
import com.login.connection;
import com.user.Address;
import com.user.Profile;
import com.user.User;

public class Customer extends User{
	
	private int customer_id;
	private Address address;
	private Cart cart;
	private WishList wishList;
	private List<Review> customerReviewAboutProducts;
	
	public List<Review> getCustomerReviewAboutProducts() {
		return customerReviewAboutProducts;
	}

	public void setCustomerReviewAboutProducts(List<Review> customerReviewAboutProducts) {
		this.customerReviewAboutProducts = customerReviewAboutProducts;
	}

	public Customer(int user_id, Profile profile, int role_id, int customer_id, Address address) {
		super(user_id, profile, role_id);
		this.customer_id = customer_id;
		this.address = address;
		this.cart = new Cart(customer_id);
		this.wishList = new WishList(customer_id);
		this.customerReviewAboutProducts = this.retriveReviews(customer_id);
	}
	
	public Customer(int user_id, int role_id) {
		super(user_id, role_id);
		this.customer_id = retriveCostumerId(user_id);	
	}
	
	public int retriveCostumerId(int user_id) {
		int customerId = 0;
		String fetchCostumerId = "SELECT customer_id from Customer where user_id = ?";
		PreparedStatement preparedStatement;
		try {
			preparedStatement = connection.getInstance().connector().prepareStatement(fetchCostumerId);
			preparedStatement.setInt(1, user_id);
			ResultSet customer_details = preparedStatement.executeQuery();
			if(customer_details.next()) {
				System.out.println(customerId);
				customerId = customer_details.getInt(1);
			}
		} catch (SQLException e) {
			System.out.println("error customer Id");
//			e.printStackTrace();
		}
		return customerId;
	}
	
	

	public Cart getCart() {
		return cart;
	}

	public void setCart(Cart cart) {
		this.cart = cart;
	}

	public WishList getWishList() {
		return wishList;
	}

	public void setWishList(WishList wishList) {
		this.wishList = wishList;
	}

	public int getCustomer_id() {
		return customer_id;
	}

	public void setCustomer_id(int customer_id) {
		this.customer_id = customer_id;
	}

	public Address getAddress() {
		return address;
	}

	public void setAddress(Address address) {
		this.address = address;
	}
	
	public List<Review> retriveReviews(int customer_id) {
		this.customerReviewAboutProducts = new LinkedList<>();
		String fetchReviews = "select * from Review r join Customer cus on cus.customer_id = r.customer_id join Product p on p.product_id = r.product_id where cus.customer_id = " + customer_id;
	  	   try {
				ResultSet reviewsDetails = connection.getInstance().statement().executeQuery(fetchReviews);
				while (reviewsDetails.next()) {
					Product product = new Product(reviewsDetails.getInt("product_id"), reviewsDetails.getString("product_name"), reviewsDetails.getInt("price"), reviewsDetails.getInt("ratting"), reviewsDetails.getString("product_image_file_path"), reviewsDetails.getString("discription"), reviewsDetails.getInt("brand_id"), reviewsDetails.getInt("category_id"));
					this.customerReviewAboutProducts.add(new Review(reviewsDetails.getInt("review_id"), reviewsDetails.getString("review"), reviewsDetails.getInt("ratting"), product));
	           }
			 } catch (SQLException e) {
				e.printStackTrace();
			 }
		return customerReviewAboutProducts;
	}
	
	public boolean addProductToCart(Product product, Integer quantity) throws Exception {
		int rowsAffected = 0;
		
		for(Entry<Product, Integer> entry : this.getCart().getCartList().entrySet()) {
			if(entry.getKey().getProductId() == product.getProductId()) {
				int oldQuantity = entry.getValue();
				this.getCart().getCartList().remove(product, oldQuantity);
				this.getCart().getCartList().put(product, quantity+oldQuantity);
				String updateCartProduct = "UPDATE Cart SET  quantity = ? WHERE product_id = ?"; 
				try {
					PreparedStatement preparedStatement = connection.getInstance().connector().prepareStatement(updateCartProduct);
				    preparedStatement.setInt(1, quantity+oldQuantity);
				    preparedStatement.setInt(2, product.getProductId());
				    rowsAffected = preparedStatement.executeUpdate();
				} catch (SQLException e) {
					e.printStackTrace();
					throw new Exception("something went wrong");
				}
				return rowsAffected > 0;
			}
		}
		
		cart.getCartList().put(product, quantity);
		String addProductQuerry = "INSERT INTO Cart (quantity, product_id, customer_id) VALUES (?,?,?)";
		try {
			PreparedStatement preparedStatement = connection.getInstance().connector().prepareStatement(addProductQuerry);
			preparedStatement.setInt(1, quantity);
			preparedStatement.setInt(2, product.getProductId());
	    	preparedStatement.setInt(3, this.getCustomer_id());
	    	rowsAffected = preparedStatement.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
			throw new Exception("something went wrong");
		}
    	
		return rowsAffected > 0;
	}
	
	public boolean deleteProductFromCart(Product product, int quantity) throws Exception {
		int rowsAffected = 0;
		this.getCart().getCartList().remove(quantity, product);
		String deleteCartProduct = "DELETE FROM Cart WHERE product_id = "+ product.getProductId();
		try {
			PreparedStatement preparedStatement = connection.getInstance().connector().prepareStatement(deleteCartProduct);
	    	rowsAffected = preparedStatement.executeUpdate();
		} catch (SQLException e) {
			throw new Exception("something went wrong");
		}
		return rowsAffected > 0;
	}
	
	public boolean addProductToWishList(Product product) throws Exception {
		
		int rowsAffected = 0;

			String addProductQuerryTowishList = "INSERT INTO WishList (product_id, customer_id) VALUES (?,?)";
			try {
				PreparedStatement preparedStatement = connection.getInstance().connector().prepareStatement(addProductQuerryTowishList);
				preparedStatement.setInt(1, product.getProductId());
		    	preparedStatement.setInt(2, this.getCustomer_id());
		    	rowsAffected = preparedStatement.executeUpdate();
		    	
			} catch (SQLException e) {
				System.out.println("1");
//				e.printStackTrace();
				throw new Exception("something went wrong");
			}
		return rowsAffected > 0;
	}
	
	public String WishListProduct() throws Exception {
			
		
			String productIds = "";
				String addProductQuerryTowishList = "SELECT product_id from WishList where customer_id = ?";
				try {
					PreparedStatement preparedStatement = connection.getInstance().connector().prepareStatement(addProductQuerryTowishList);
					preparedStatement.setInt(1, this.customer_id);
			    	
			    	ResultSet rs =  preparedStatement.executeQuery();
			    	while(rs.next()) {
			    		productIds += rs.getInt(1)+"~";
			    	}
			    	System.out.println(productIds);
				} catch (SQLException e) {
					System.out.println("1");
					throw new Exception("something went wrong");
					
				}
			return productIds;
		}
	
	
	public String showWishListProduct() throws Exception {
		
		JSONObject json = new JSONObject();
		String wishList = this.WishListProduct();
		String [] WishListProduct = wishList.split("~");
		try {
			for(int i = 0; i < WishListProduct.length; i++) {
				int productId = Integer.parseInt(WishListProduct[i]);
				json.put(productId+"", showProduct(productId));
				System.out.println(productId); 
			}
			json.put("wishListIds", wishList);
			
		}catch (SQLException e) {
			throw new Exception("something went wrong");
        } 
		return json.toString();
		
	}
	
	public String showCartProduct() throws Exception {
		
		JSONObject json = new JSONObject();
		String productQuantity = "";
		System.out.println(this.getCart().getCartList().toString());
		for(Entry<Product, Integer> entry : this.getCart().getCartList().entrySet()) {	
			json.put(entry.getKey().getProductId()+"", showProduct(entry.getKey().getProductId()));
			productQuantity += entry.getKey().getProductId()+","+entry.getValue()+"~";	
			System.out.println(productQuantity);
		}
		json.put("productQuantity", productQuantity);
		return json.toString();
		
	}
	
	public boolean deleteProductFromWishList(Product product) throws Exception {
		int rowsAffected = 0;
		String deleteCartProduct = "DELETE FROM WishList WHERE product_id = "+ product.getProductId();
		try {
			PreparedStatement preparedStatement = connection.getInstance().connector().prepareStatement(deleteCartProduct);
	    	rowsAffected = preparedStatement.executeUpdate();
	    	
		} catch (SQLException e) {
			System.out.println("4");
			throw new Exception("something went wrong");
		}
		return rowsAffected > 0;
	}
	
}
