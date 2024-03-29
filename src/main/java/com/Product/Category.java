package com.Product;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.LinkedList;
import java.util.List;

import com.login.connection;

public class Category {
	
	private int categoryId;
	private String categoryName;
	private List<Brand> ListOfBrands;
	
	public Category(int categoryId, String categoryName) {
		this.categoryId = categoryId;
		this.categoryName = categoryName;
		ListOfBrands = this.retriveBrands();
	}
	
	public Category(String categoryName, boolean nameOnly) {
			this.categoryName = categoryName;
			this.categoryId = this.retriveCategoryId(categoryName);
			ListOfBrands = this.retriveBrandsbyNameOnlyorImage(nameOnly);
	}
	
	public Category(String categoryName) {
		this.categoryName = categoryName;
	}
	
	public Category(int categoryId, String categoryName, boolean check) {
		this.categoryId = categoryId;
		this.categoryName = categoryName;
	}
	
	public int getCategoryId() {
		return categoryId;
	}
	public void setCategoryId(int categoryId) {
		this.categoryId = categoryId;
	}
	public String getCategoryName() {
		return categoryName;
	}
	public void setCategoryName(String categoryName) {
		this.categoryName = categoryName;
	}
	public List<Brand> getListOfBrands() {
		return ListOfBrands;
	}
	public void setListOfBrands(List<Brand> listOfBrands) {
		ListOfBrands = listOfBrands;
	}
	
	private List<Brand> retriveBrands(){
		this.ListOfBrands = new LinkedList<>();
		String fetchBrands = "select * from Brand b join Category c on c.category_id = b.category_id where b.category_id = " + categoryId;
    	try {
			ResultSet BrandDetails = connection.getInstance().statement().executeQuery(fetchBrands);
			while (BrandDetails.next()) {
				Brand brand = new Brand(BrandDetails.getInt("brand_id"), BrandDetails.getString("brand_name"), BrandDetails.getString("brand_image"));
				this.ListOfBrands.add(brand);
            }
		} catch (SQLException e) {
			return ListOfBrands;
		}
		return ListOfBrands;
	}
	
	
	
	public List<Brand> retriveBrandsbyNameOnlyorImage(boolean nameOrImage){
		this.ListOfBrands = new LinkedList<>();
		String fetchBrands = "select * from Brand b join Category c on c.category_id = b.category_id where b.category_id = " + this.categoryId;
    	try {
			ResultSet BrandDetails = connection.getInstance().statement().executeQuery(fetchBrands);
			while (BrandDetails.next()) {
				if(nameOrImage) {
					Brand brand = new Brand(BrandDetails.getString("brand_name"));
					this.ListOfBrands.add(brand);
				}
				else {
					Brand brand = new Brand(BrandDetails.getString("brand_name"), BrandDetails.getString("brand_image"));
					this.ListOfBrands.add(brand);
				}
            }
		} catch (SQLException e) {
			return ListOfBrands;
		}
		return ListOfBrands;
	}
	
	
	
	private int  retriveCategoryId(String category_name){
		this.ListOfBrands = new LinkedList<>();
		String fetchCategory = "select category_id from Category where category_name like " + "'"+category_name+"'";
    	try {
			ResultSet Categoryid = connection.getInstance().statement().executeQuery(fetchCategory);
			if (Categoryid.next()) {
				this.categoryId = Categoryid.getInt(1);
				System.out.println(categoryId);
            }
		} catch (SQLException e) {
			//e.printStackTrace();
			return this.categoryId;
			
		}
		return this.categoryId;
	}

}
