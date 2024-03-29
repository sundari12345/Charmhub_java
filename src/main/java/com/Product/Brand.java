package com.Product;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.LinkedList;
import java.util.List;

import com.Manager.Manager;
import com.login.connection;
import com.user.Profile;

public class Brand {
	private int brandId;
	private String brandName;
	private String brandImage;
	
	public String getBrandImage() {
		return brandImage;
	}

	public void setBrandImage(String brandImage) {
		this.brandImage = brandImage;
	}

	public List<Product> ListOfBrandProduct;
	
	
	
	public Brand(int brandId, String brandName, String brandImage) {
		this.brandId = brandId;
		this.brandName = brandName;
		this.brandImage = brandImage;
		this.ListOfBrandProduct = retriveProducts();
	}
	
	public Brand(String brandName) {
		this.brandName = brandName;
	}
	
	public Brand(String brandName, String brandImage) {
		this.brandName = brandName;
		this.brandImage = brandImage;
		
	}
	
	public Brand(String brandName, boolean check) {
		this.brandName = brandName;
		this.brandId =  this.retriveBrandId(brandName);
		
	}
	
	public Brand(int brandId, String brandName, String brandImage, boolean check) {
		this.brandId = brandId;
		this.brandName = brandName;
		this.brandImage = brandImage;
	}
	
	public int getBrandId() {
		return brandId;
	}
	public void setBrandId(int brandId) {
		this.brandId = brandId;
	}
	public String getBrandName() {
		return brandName;
	}
	public void setBrandName(String brandName) {
		this.brandName = brandName;
	}
	public List<Product> getListOfBrandProduct() {
		return ListOfBrandProduct;
	}
	public void setListOfBrandProduct(List<Product> listOfBrandProduct) {
		ListOfBrandProduct = listOfBrandProduct;
	}
	
	private List<Product> retriveProducts(){
		this.ListOfBrandProduct = new LinkedList<>();
		String fectchProduct = "select * from Product p join Category c on c.category_id = p.category_id join Brand b on b.brand_id = p.brand_id where p.brand_id = "+this.brandId;
    	try {
			ResultSet productDetails = connection.getInstance().statement().executeQuery(fectchProduct);
			while (productDetails.next()) {
				Product product = new Product(productDetails.getInt("product_id"), productDetails.getString("product_name"), productDetails.getInt("price"), productDetails.getInt("ratting"), productDetails.getString("product_image_file_path"), productDetails.getString("discription"), productDetails.getInt("brand_id"), productDetails.getInt("category_id"));
				this.ListOfBrandProduct.add(product);
            }
		} catch (SQLException e) {
			return ListOfBrandProduct;
		}
		
		return ListOfBrandProduct;
	}
	
	private int retriveBrandId(String brand_name){
		
		String fectchbrandId = "select brand_id from Brand where brand_name like "+"'"+brand_name+"'";
    	try {
			ResultSet brandId = connection.getInstance().statement().executeQuery(fectchbrandId);
			while (brandId.next()) {
				this.brandId = brandId.getInt(1);
            }
		} catch (SQLException e) {
			return this.brandId;
		}
		
		return this.brandId;
	}
}
