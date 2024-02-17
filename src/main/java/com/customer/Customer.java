package com.customer;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.LinkedList;
import java.util.List;
import java.util.Map.Entry;

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
	
	private List<Review> retriveReviews(int customer_id) {
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
	
	private boolean addProductToCart(Product product, Integer quantity) throws Exception {
		int rowsAffected = 0;
		
		for(Entry<Integer, Product> entry : this.getCart().getCartList().entrySet()) {
			if(entry.getValue().getProductId() == product.getProductId()) {
				int oldQuantity = entry.getKey();
				this.getCart().getCartList().remove(oldQuantity, product);
				this.getCart().getCartList().put(quantity+oldQuantity, product);
				String updateCartProduct = "UPDATE Cart SET  quantity = ? WHERE product_id = ?"; 
				try {
					PreparedStatement preparedStatement = connection.getInstance().connector().prepareStatement(updateCartProduct);
				    preparedStatement.setInt(1, quantity+oldQuantity);
				    preparedStatement.setInt(2, product.getProductId());
				    rowsAffected = preparedStatement.executeUpdate();
				} catch (SQLException e) {
					throw new Exception("something went wrong");
				}
				return rowsAffected > 0;
			}
		}
		
		cart.getCartList().put(quantity, product);
		String addProductQuerry = "INSERT INTO Cart (quantity, product_id, cusotmer_id) VALUES (?,?,?)";
		try {
			PreparedStatement preparedStatement = connection.getInstance().connector().prepareStatement(addProductQuerry);
			preparedStatement.setInt(1, quantity);
			preparedStatement.setInt(2, product.getProductId());
	    	preparedStatement.setInt(3, this.getCustomer_id());
	    	rowsAffected = preparedStatement.executeUpdate();
		} catch (SQLException e) {
			throw new Exception("something went wrong");
		}
    	
		return rowsAffected > 0;
	}
	
	private boolean deleteProductFromCart(Product product, int quantity) throws Exception {
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
	
	private boolean addProductToWishList(Product product) throws Exception {
		
		int rowsAffected = 0;
		if(!this.getWishList().getWishListProducts().contains(product)) {
			String addProductQuerryTowishList = "INSERT INTO Cart (product_id, cusotmer_id) VALUES (?,?,?)";
			try {
				PreparedStatement preparedStatement = connection.getInstance().connector().prepareStatement(addProductQuerryTowishList);
				preparedStatement.setInt(2, product.getProductId());
		    	preparedStatement.setInt(3, this.getCustomer_id());
		    	rowsAffected = preparedStatement.executeUpdate();
		    	if(rowsAffected > 0) {
		    		this.getWishList().getWishListProducts().add(product);
		    	}
			} catch (SQLException e) {
				throw new Exception("something went wrong");
			}
	    	
		}
		return rowsAffected > 0;
	}
	
	private boolean deleteProductFromWishList(Product product) throws Exception {
		int rowsAffected = 0;
		String deleteCartProduct = "DELETE FROM Cart WHERE product_id = "+ product.getProductId();
		try {
			PreparedStatement preparedStatement = connection.getInstance().connector().prepareStatement(deleteCartProduct);
	    	rowsAffected = preparedStatement.executeUpdate();
	    	if(rowsAffected > 0) {
	    		this.getWishList().getWishListProducts().remove(product);
	    	}
		} catch (SQLException e) {
			throw new Exception("something went wrong");
		}
		return rowsAffected > 0;
	}
	
}
