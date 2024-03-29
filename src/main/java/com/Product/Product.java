package com.Product;

import java.util.List;

public class Product {
	
	private int productId;
	private String productName;
	private int price;
	private int ratting;
	private String imageFilePath;
	private String discription;
	private int BrandId;
	private int CategoryId;
	
	
	
	public Product(int productId, String productName, int price, int ratting, String imageFilePath, String discription, int brandId, int categoryId) {
		this.productId = productId;
		this.productName = productName;
		this.price = price;
		this.ratting = ratting;
		this.imageFilePath = imageFilePath;
		this.discription = discription;
		BrandId = brandId;
		CategoryId = categoryId;
	}
	
	public Product(int productId) {
		this.productId = productId;
	}

	public Product(String productName, int price, String imageFilePath, String discription) {

		this.productName = productName;
		this.price = price;
		this.ratting = 0;
		this.imageFilePath = imageFilePath;
		this.discription = discription;
		
	}


	public int getBrandId() {
		return BrandId;
	}
	public void setBrandId(int brandId) {
		BrandId = brandId;
	}
	public int getCategoryId() {
		return CategoryId;
	}
	public void setCategoryId(int categoryId) {
		CategoryId = categoryId;
	}
	public int getProductId() {
		return productId;
	}
	public void setProductId(int productId) {
		this.productId = productId;
	}
	public String getProductName() {
		return productName;
	}
	public void setProductName(String productName) {
		this.productName = productName;
	}
	public int getPrice() {
		return price;
	}
	public void setPrice(int price) {
		this.price = price;
	}
	public int getRatting() {
		return ratting;
	}
	public void setRatting(int ratting) {
		this.ratting = ratting;
	}
	public String getImageFilePath() {
		return imageFilePath;
	}
	public void setImageFilePath(String imageFilePath) {
		this.imageFilePath = imageFilePath;
	}
	public String getDiscription() {
		return discription;
	}
	public void setDiscription(String discription) {
		this.discription = discription;
	}
	
}
