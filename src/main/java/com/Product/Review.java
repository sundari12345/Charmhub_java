package com.Product;

import com.customer.Customer;

public class Review {
	
   private int reviewId;
   private String review;
   private int ratting;
   private Product reviewedProduct;
   
   
   public Review(int reviewId, String review, int ratting, Product reviewedProduct) {
		this.reviewId = reviewId;
		this.review = review;
		this.reviewedProduct = reviewedProduct;
		this.ratting = ratting;
	}

	public int getRatting() {
		return ratting;
	}

	public void setRatting(int ratting) {
		this.ratting = ratting;
	}

	public int getReviewId() {
		return reviewId;
	}
	
	public void setReviewId(int reviewId) {
		this.reviewId = reviewId;
	}
	
	public String getReview() {
		return review;
	}
	
	public void setReview(String review) {
		this.review = review;
	}
	
	public Product getReviewedProduct() {
		return reviewedProduct;
	}
	
	public void setReviewedProduct(Product reviewedProduct) {
		this.reviewedProduct = reviewedProduct;
	}	
		
   
}
