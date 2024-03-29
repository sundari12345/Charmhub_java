package com.Manager;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import com.Product.Brand;
import com.Product.Category;
import com.Product.Product;
import com.login.connection;
import com.user.Profile;
import com.user.User;

import jakarta.servlet.annotation.WebServlet;

public class Manager extends User{
	private int managerId;

	public Manager(int user_id, Profile profile, int role_id, int managerId) {
		super(user_id, profile, role_id);
		this.managerId = managerId;
	}
	
	public Manager(int user_id, int role_id) {
		super(user_id, role_id);
	}

	public int getManagerId() {
		return managerId;
	}

	public void setManagerId(int managerId) {
		this.managerId = managerId;
	}

	
	public int addProductTODb(Product product, String brand_name, String category_name) {
		
		String productName = product.getProductName();
		int price = product.getPrice();
		String imageFilePath = product.getImageFilePath();
		String discription = product.getDiscription();
	    int ratting = product.getRatting();
		
		int rowsAffected = 0;
		int noBrand = 0;
		int noCategory = -1;
		int categoryId = category_name_to_id(category_name);
		
		try {
			if(categoryId != noCategory) {
				int brandId = brand_name_to_id(brand_name, categoryId);
				System.out.println(brandId+": "+categoryId);
				if(brandId != noBrand) {
					String addProductQuerry = "INSERT INTO Product(product_name, price, ratting, product_image_file_path, discription, brand_id, category_id) VALUES (?,?,?,?,?,?,?)";
					PreparedStatement preparedStatement = connection.getInstance().connector().prepareStatement(addProductQuerry);
			    	
			    	preparedStatement.setString(1, productName);
			    	preparedStatement.setInt(2, price);
			    	preparedStatement.setInt(3, ratting);
			    	preparedStatement.setString(4, imageFilePath);
			    	preparedStatement.setString(5, discription);
			    	preparedStatement.setInt(6, brandId);
			    	preparedStatement.setInt(7, categoryId);
			    	
			    	rowsAffected = preparedStatement.executeUpdate();
			    	preparedStatement = connection.getInstance().connector().prepareStatement("SELECT LAST_INSERT_ID()");
			    	ResultSet resultSet = preparedStatement.executeQuery();
		            if (resultSet.next()) {
		            	rowsAffected = resultSet.getInt(1);
		                System.out.println("Last inserted ID: " + rowsAffected);
		            } else {
		                System.out.println("No auto-incremented ID generated.");
		            }
			    	
				}else {
					return noBrand;
				}
			}
			else {
				return noCategory;
			}
	    	
		}catch (SQLException e) {
			return rowsAffected;
		}
		return rowsAffected;
	}
	
	public int brand_name_to_id(String brand_name, int category_id) {
		int brand_id = 0;
		System.out.println(brand_name+":  "+category_id);
		String fetchQuerry = "select * from Brand";
		try {
			ResultSet result = connection.getInstance().statement().executeQuery(fetchQuerry);
			while(result.next()) {
				if(result.getInt("category_id") == category_id) {
					if(result.getString("brand_name").equalsIgnoreCase(brand_name)) {
						brand_id =  result.getInt("brand_id");
						return brand_id;
					};
				}
			}
		} catch (SQLException e) {
			return brand_id;
		}
		
		return brand_id;
	}
	
	public int category_name_to_id(String category_name) {
		int category_id = -1;
		String fetchQuerry = "select * from Category";
		System.out.println(category_name);
		try {
			ResultSet result = connection.getInstance().statement().executeQuery(fetchQuerry);
			while(result.next()) {
				if(result.getString("category_name").equalsIgnoreCase(category_name)) {
					category_id =  result.getInt("category_id");
					return category_id;
				};
			}
		} catch (SQLException e) {
			return category_id;
		}
		
		return category_id;
	}
	
	public String editProduct(String productName, int price, String imageFilePath, String discription, int brand_id, int category_id, int product_id) {
		int rowsAffected = 0;
		String success = "wrong";
		try {
			String editProductQuerry = "UPDATE Product SET  product_name = ?, price = ?, product_image_file_path = ?, discription WHERE brand_id = ? AND category_id = ? And product_id = ?";
			PreparedStatement preparedStatement = connection.getInstance().connector().prepareStatement(editProductQuerry);
	    	preparedStatement.setString(1, productName);
	    	preparedStatement.setInt(2, price);
	    	preparedStatement.setString(3, imageFilePath);
	    	preparedStatement.setString(4, discription);
	    	preparedStatement.setInt(5, brand_id);
	    	preparedStatement.setInt(6, category_id);
	    	preparedStatement.setInt(7, product_id);
	    	rowsAffected = preparedStatement.executeUpdate();
	    	if(rowsAffected > 0) {
	    		this.upateProductToBrandList(productName, price, imageFilePath, discription, brand_id, category_id, product_id);
	    		success = "success"; 
	    		return success;
	    	}
	    }catch (SQLException e) {
			return "wrong query";
		}
		
		return success;
	}
	
	private String upateProductToBrandList(String productName, int price, String imageFilePath, String discription, int brand_id, int category_id, int product_id) {
		for(Category category: this.getListOfCategory()) {
			if(category.getCategoryId() == category_id) {
				for(Brand brand: category.getListOfBrands()) {
					if(brand.getBrandId() == brand_id) {
						for(Product product: brand.getListOfBrandProduct()) {
							if(product.getProductId() == product_id) {
								product.setProductName(productName);
								product.setPrice(price);
								product.setDiscription(discription);
								product.setImageFilePath(imageFilePath);
								return "success";
							}
							else {
								return "no product found";
							}
						}
					}else {
						return "no brand found";
					}
				}
			}else {
				return "no category found";
			}
		}
		return "success";
	}
	
	private int category_name_to_idfromList(String category_name) {
		int category_id = -1;
		
		for(Category category: this.getListOfCategory()) {
			if(category.getCategoryName().equalsIgnoreCase(category_name)) {
				category_id = category.getCategoryId();
				return category_id;
			}
		}
		
		return category_id;
	}
	
	public int brand_name_to_idfromList(String brand_name, int category_id) {
		int brand_id = -1;
		
		for(Category category: this.getListOfCategory()) {
			if(category.getCategoryId() == category_id) {
				for(Brand brand: category.getListOfBrands()) {
					if(brand.getBrandName().equalsIgnoreCase(brand_name)) {
						brand_id = brand.getBrandId();
					}
					return brand_id;
				}
			}
		}
		return brand_id;
	}
	
	public String addProductBrandList(String product_name, int brand_id, int category_id) {
		String fectchProduct = "select * from Product p join Category c on c.category_id = p.category_id join Brand b on b.brand_id = p.brand_id where p.brand_id = "+brand_id+ "AND p.category_id = " +category_id+ "AND  p.product_name like "+"'"+product_name+"'";
    	try {
			ResultSet productDetails = connection.getInstance().statement().executeQuery(fectchProduct);
			while (productDetails.next()) {
				Product product = new Product(productDetails.getInt("product_id"), productDetails.getString("product_name"), productDetails.getInt("price"), productDetails.getInt("ratting"), productDetails.getString("product_image_file_path"), productDetails.getString("discription"), productDetails.getInt("brand_id"), productDetails.getInt("category_id"));
				for(Category category: this.getListOfCategory()) {
					if(category.getCategoryId() == category_id) {
						for(Brand brand: category.getListOfBrands()) {
							if(brand.getBrandId() == brand_id) {
								brand_id = brand.getBrandId();
								brand.ListOfBrandProduct.add(product);
							}
							
						}
					}
				}

            }
		} catch (SQLException e) {
			return "wrong query";
		}
    	return "success";
	}
	
	public boolean removeProduct(int product_id, int brand_id, int category_id) throws Exception{
		Product product = product_id_to_product(product_id, brand_id, category_id);
		int rowsAffected = 0;
		String deleteProduct = "DELETE FROM Product WHERE product_id = "+ product.getProductId();
		try {
			PreparedStatement preparedStatement = connection.getInstance().connector().prepareStatement(deleteProduct);
	    	rowsAffected = preparedStatement.executeUpdate();
	    	if(rowsAffected > 0) {
	    		for(Category category: this.getListOfCategory()) {
					if(category.getCategoryId() == product.getCategoryId()) {
						for(Brand brand: category.getListOfBrands()) {
							if(brand.getBrandId() == product.getBrandId()) {
								brand.ListOfBrandProduct.remove(product);
								return true;
							}
							
						}
					}
				}
	    	}
		} catch (SQLException e) {
			throw new Exception("something went wrong");
		}
		return rowsAffected > 0;
		
	}
	
	public  boolean removeBrand(int brand_id, int category_id) throws Exception {
		Brand brand = brand_id_to_brand(brand_id, category_id);
		int rowsAffected = 0;
		String deletebrand = "DELETE FROM brand WHERE brand_id = "+ brand.getBrandId();
		try {
			PreparedStatement preparedStatement = connection.getInstance().connector().prepareStatement(deletebrand);
	    	rowsAffected = preparedStatement.executeUpdate();
	    	if(rowsAffected > 0) {
	    		for(Category category: this.getListOfCategory()) {
					
					for(Brand removeBrand: category.getListOfBrands()) {
						if(brand.getBrandId() == removeBrand.getBrandId()) {
							this.getListOfCategory().remove(brand);
						}
							return true;
					}
				}
			}
		} catch (SQLException e) {
			throw new Exception("something went wrong");
		}
		return rowsAffected > 0;
	}
	
	
	
	private Brand brand_id_to_brand(int brand_id, int category_id) {
		Brand brands = null;
		for(Category category: this.getListOfCategory()) {
			if(category.getCategoryId() == category_id) {
				for(Brand brand: category.getListOfBrands()) {
					if(brand.getBrandId() == brand_id) {
						brands = brand;
						return brand;
					}
					
				}
			}
		}
		return brands;
	}
	
	private Product product_id_to_product(int brand_id, int category_id, int product_id) {
		Product p = null;
		for(Category category: this.getListOfCategory()) {
			if(category.getCategoryId() == category_id) {
				for(Brand brand: category.getListOfBrands()) {
					if(brand.getBrandId() == brand_id) {
						for(Product product: brand.getListOfBrandProduct()) {
							if(product.getProductId() == product_id) {
								p = product;
								return p;
							}
						}
					}
				}
			}
		}
		return p;
	}
	
	public String addCategory(Category category) throws Exception {
		try {
			String category_name = category.getCategoryName();
			String addCategoryQuerry = "INSERT INTO Category(category_name) VALUES (?)";
			PreparedStatement preparedStatement = connection.getInstance().connector().prepareStatement(addCategoryQuerry);
	    	preparedStatement.setString(1, category_name);
	    	if(preparedStatement.executeUpdate() > 0) {
	    		String success = "100";
	    		return success;
	    	}
		}catch(SQLException e) {
			throw new Exception("Something went wrong");
		}
		String failure = "50";
		return failure;
	}
	
	public String  addBrand(Brand brand,String Category_name) throws Exception {
		String brand_name = brand.getBrandName(); 
		String brand_image = brand.getBrandImage(); 
		
		try {
			String fetchCateId = "select category_id from Category where category_name like ?";
			PreparedStatement preparedstatement;
			System.out.println(brand_name+": "+brand_image);
				preparedstatement = connection.getInstance().connector().prepareStatement(fetchCateId);
				preparedstatement.setString(1, Category_name);
				ResultSet cateId = preparedstatement.executeQuery();
				if(cateId.next()) {
					int category_id = cateId.getInt("category_id");
					String addCategoryQuerry = "INSERT INTO Brand(brand_name, category_id, brand_image) VALUES (?, ?, ?)";
					PreparedStatement preparedStatement2 = connection.getInstance().connector().prepareStatement(addCategoryQuerry);
			    	preparedStatement2.setString(1, brand_name);
			    	preparedStatement2.setInt(2, category_id);
			    	preparedStatement2.setString(3, brand_image);
			    	int  rowsAffected = preparedStatement2.executeUpdate(); 
			    	if(rowsAffected > 0) {
			    		String sucess = "100";
			    		PreparedStatement preparedstatement3 = connection.getInstance().connector().prepareStatement("SELECT LAST_INSERT_ID()");
				    	ResultSet resultSet = preparedstatement3.executeQuery();
			            if (resultSet.next()) {
			            	sucess = resultSet.getInt(1)+"";
			                System.out.println("Last inserted ID: " + sucess);
			            } else {
			                System.out.println("No auto-incremented ID generated.");
			            }
			    		return sucess;
			    	}
				}
			
		}catch(SQLException e) {
			throw new Exception("Something went wrong");
		}
		String failure = "300";
		return failure;
	}

	
} 
