package com.user;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Base64;
import java.util.LinkedList;
import java.util.List;
import java.util.UUID;

import org.json.JSONException;
import org.json.JSONObject;

import com.Product.Brand;
import com.Product.Category;
import com.customer.Customer;
import com.login.BCrypt;
import com.login.connection;

public class User {
	
    private int user_id;
    private Profile profile;
    private int role_id;
    private String session_id;
    private List<Category> listOfCategory;
    
	public User(int user_id, Profile profile, int role_id) {
		boolean nameOnly = true;
		this.user_id = user_id;
		this.profile = profile;
		this.role_id = role_id;
		try {
			this.retriveCategory(nameOnly);
		} catch (Exception e) {
			
		}
	}
	
	public User(int user_id) {
		this.user_id = user_id;
	}
	
	public User() {
		try {
			
			boolean notNameOnlyWithImage = false;
			this.retriveCategory(notNameOnlyWithImage);
		} catch (Exception e) {
			System.out.println("something went wrong");
		}
	}
	
	public User(JSONObject json) {
		try {
			if(json != null) {
				int user_id = json.getInt("user_id");				
				int role_id = json.getInt("role_id");
				this.user_id = user_id;
				this.role_id = role_id;
			}
			else {
				this.user_id = 0;
				this.role_id = 0;
			}
			boolean notNameOnlyWithImage = false;
			this.retriveCategory(notNameOnlyWithImage);
		} catch (Exception e) {
			System.out.println("something went wrong");
		}
	}
	
	public User(int user_id, int role_id) {
		boolean nameOnly = true;
		this.user_id = user_id;
		this.role_id = role_id;
		try {
			this.retriveCategory(nameOnly);
		} catch (Exception e) {
			
		}
	}
	
	public User(String emailId, String phoneno, int role_id, String session_id) {
		this.role_id = role_id;
		this.session_id = session_id;
		this.profile = new Profile(phoneno, emailId);
	}
	
	public User(String emailId, String phoneno) {
		this.profile = new Profile(phoneno, emailId);
	}
	
	public int getUser_id() {
		return user_id;
	}
	public List<Category> getListOfCategory() {
		return listOfCategory;
	}
	public void setListOfCategory(List<Category> listOfCategory) {
		this.listOfCategory = listOfCategory;
	}
	public void setUser_id(int user_id) {
		this.user_id = user_id;
	}
	public Profile getProfile() {
		return profile;
	}
	public void setProfile(Profile profile) {
		this.profile = profile;
	}
	public int getRole_id() {
		return role_id;
	}
	public void setRole_id(int role_id) {
		this.role_id = role_id;
	}
	
	private List<Category> retriveCategory(boolean nameOnlyorImage) throws Exception{
		
		this.listOfCategory = new LinkedList<>();
		
		String fetchCategory = "select * from Category";
		try {
			
			ResultSet allCategory = connection.getInstance().statement().executeQuery(fetchCategory);
			
			List<Category> wholeCategory = new ArrayList<>();
			while(allCategory.next()) {
				
				System.out.println(allCategory.getString("category_name"));
				Category category = new Category (allCategory.getInt("category_id"), allCategory.getString("category_name"), nameOnlyorImage);
				this.listOfCategory.add(category);
				wholeCategory.add(category);
				
				
			}
			for(Category c: wholeCategory) {
				c.setListOfBrands(c.retriveBrandsbyNameOnlyorImage(nameOnlyorImage));;
			}
			
			
		} catch (SQLException e) {
			throw new Exception("something went wrong");
		}
		
		return this.listOfCategory;
	} 
	
	public User login(String emailId, String password) throws Exception {
		User user = null;
		String welcome = "wrong password";
		int decisionMakingNum = 0;
		try {
			ResultSet resultSet = connection.getInstance().statement().executeQuery("select p.email_id, u.password, u.user_id, u.role_id from user u join Profile p on p.profile_id = u.profile_id");
			while (resultSet.next()) {
            	if(resultSet.getString(1).equals(emailId)&& BCrypt.checkpw(password, resultSet.getString(2))) {
            		welcome = "welcome";
            		decisionMakingNum = 1;
        			System.out.println(welcome+ "  login successfully!!");
        			String sessionInsertQuerry = "INSERT INTO session (session_id, user_id, role_id) VALUES (?,?,?)";
        			PreparedStatement preparedStatement = connection.getInstance().connector().prepareStatement(sessionInsertQuerry); 
        			String sessionId = UUID.randomUUID().toString();
        			preparedStatement.setString(1, sessionId);
        			preparedStatement.setInt(2, resultSet.getInt(3));
        			preparedStatement.setInt(3, resultSet.getInt(4));
        			preparedStatement.executeUpdate();
        			
        			user = new User(emailId, this.profile.getPhoneNo(), resultSet.getInt(4), sessionId);
        			return user;
            	}
            }
			if(decisionMakingNum == 0) {
				System.out.println("wrong password");
				return user;
			}
		} catch (SQLException e) {
			throw new Exception("something went wrong");
		}
		return user;
	}
	
	public int checkAccountExist(User user) throws Exception {
		int decisionMakingNum = 0;
		ResultSet resultSet;
		try {
			resultSet = connection.getInstance().statement().executeQuery("select p.email_id, u.password from user u join Profile p on p.profile_id = u.profile_id");
			while (resultSet.next()) {
	        	if(resultSet.getString(1).equals(user.getProfile().getEmailId())) {
	        		decisionMakingNum = 1;
	        		break;
	        	}
	        }
		} catch (SQLException e) {
			throw new Exception("something went wrong");
		}
	    return decisionMakingNum;
	}
	
	
	 public int SingUp(String emailId, String password, String phoneno) throws Exception {
		
		String hashedPassword = BCrypt.hashpw(password, BCrypt.gensalt());
		
		String emailInsertQuery = "INSERT INTO Profile (email_id, phoneno) VALUES (?, ?)";
		int profile_id = 0;
		
		String passwordInsertQuery = "INSERT INTO user (password, profile_id, role_id) VALUES (?, ?, ?)";
		int rowsAffected1 = 0;
		int rowsAffected2 = 0;
		int customerRole_id = 4;
		int rowsAffected3 = 0;
		int done = 0;
		
		try {
			PreparedStatement preparedStatement = connection.getInstance().connector().prepareStatement(emailInsertQuery);
		    preparedStatement.setString(1, emailId);
		    preparedStatement.setString(2, phoneno);
	        rowsAffected1 = preparedStatement.executeUpdate();
	        
	        String getProfileIdQuerry = "select profile_id from Profile where email_id like "+"'"+emailId+"'";
			ResultSet resultSet = connection.getInstance().statement().executeQuery(getProfileIdQuerry);
			
			if (resultSet.next()) {
				profile_id = resultSet.getInt(1);
				preparedStatement = connection.getInstance().connector().prepareStatement(emailInsertQuery);
			    preparedStatement.setString(1, emailId);
			}
			
	        preparedStatement = connection.getInstance().connector().prepareStatement(passwordInsertQuery, Statement.RETURN_GENERATED_KEYS);
	        preparedStatement.setString(1, hashedPassword);
	        preparedStatement.setInt(2, profile_id);
	        preparedStatement.setInt(3, customerRole_id);
	        rowsAffected2 = preparedStatement.executeUpdate();
	        
	        ResultSet generatedKeys = preparedStatement.getGeneratedKeys();
	        int customer_id = -1; 
	        if (generatedKeys.next()) {
	            customer_id = generatedKeys.getInt(1);
	            System.out.println(customer_id);
	            String customerInsertQuery = "INSERT INTO Customer (user_id) VALUES (?)";
	        	preparedStatement = connection.getInstance().connector().prepareStatement(customerInsertQuery);
	        	preparedStatement.setInt(1, customer_id);
	        	rowsAffected3 = preparedStatement.executeUpdate();
	        	System.out.println(rowsAffected3+"id");
	        }
	        
	        
	        
	        if (rowsAffected1 > 0 && rowsAffected2 > 0) {
	        	if(rowsAffected3 > 0) {
	        		done = 1;
		            return done;
	        	}     
	        } 
		}catch (SQLException e) {
			e.printStackTrace();
			throw new Exception("something went wrong");
        }

        return done;
	}

	public String getSession_id() {
		return session_id;
	}

	public void setSession_id(String session_id) {
		this.session_id = session_id;
	}
	
	public String viewBrand() {
		JSONObject allBrands = new JSONObject();
		var listOfBrandName = "";
		try {
			for(Category category: this.listOfCategory) {
				for(Brand brand: category.getListOfBrands()) {
					String [] list = brand.getBrandImage().split("\\.");	
					allBrands.put(brand.getBrandName(), changeStringFileToBase64(brand.getBrandImage())+"~"+list[1]);
					listOfBrandName += brand.getBrandName()+"~";	
				} 
			}
			allBrands.put("allBrand", listOfBrandName);
		}
		catch (JSONException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return allBrands.toString();
	}
	
	public String changeStringFileToBase64(String filePath) throws Exception {
	   
	    
	    StringBuilder allBase64String = new StringBuilder();

	        String path = "/home/sundari-zstk338/Documents/Charmhub/"+filePath;
	        File file = new File(path);
	        FileInputStream fis;
	        String base64String = "";
	        try {
	            fis = new FileInputStream(file);
	            byte[] bufferarray = new byte[(int) file.length()];
	            fis.read(bufferarray);
	            fis.close();    
	            // Encode byte array to base64 string
	            base64String = Base64.getEncoder().encodeToString(bufferarray);
	            allBase64String.append(base64String);
	            
	             // Add a separator between base64 strings
	        } catch (FileNotFoundException e) {
	            throw new Exception("File is not found: " + path);
	        } catch (IOException e) {
	            throw new Exception("Something went wrong while reading file: " + path);
	        }

	    return allBase64String.toString();
	}
	
	
	
	public String showProductThisBrand(String brand_name) throws Exception {
		
		JSONObject json = new JSONObject();
		String getProduct = "select * from Brand where brand_name like ?";
		PreparedStatement preparedstatement;
		String categoryList = "";
		try {
			preparedstatement = connection.getInstance().connector().prepareStatement(getProduct);
			preparedstatement.setString(1, brand_name);
			ResultSet getBrandDetails = preparedstatement.executeQuery();
			
			while(getBrandDetails.next()) {
				
				categoryList += categoryName(getBrandDetails.getInt("category_id"))+"~";
				String productList = "";
				JSONObject product_in_category = new JSONObject();
				String fetachProduct = "select product_id from Product where brand_id = ?";
				PreparedStatement preparedstatement2 = connection.getInstance().connector().prepareStatement(fetachProduct);
				preparedstatement2.setInt(1, getBrandDetails.getInt("brand_id"));
				ResultSet allProduct = preparedstatement2.executeQuery(); 
				int i = 0;
				while(allProduct.next()) {
					int product_id = allProduct.getInt("product_id");
					productList += product_id+"~";
					product_in_category.put(product_id+"", showProduct(product_id));
				}
				
				product_in_category.put("allProduct", productList);
				json.put(categoryName(getBrandDetails.getInt("category_id")), product_in_category);
				
			}
			json.put("allCategory", categoryList);
			
			json.put("role", this.getRole_id());
			json.put("user", this.getUser_id());
			int customerRole = 4;
			if(this.getRole_id() == customerRole) {
				Customer c = new Customer(this.getUser_id(), this.getRole_id());
				json.put("wishListProducts", c.WishListProduct());
			}
			else {
				json.put("wishListProducts", "~");
			}
			
		}catch (SQLException e) {
			throw new Exception("something went wrong");
        } catch (JSONException e) {
        	throw new Exception("something went wrong");
		}
		
		return json.toString();
	}
	
	public String categoryName(int category_id) throws Exception {
		String category_name = "";
		String fetchCategory = "select category_name from Category where category_id = ?";
		PreparedStatement preparedstatement;
		try {
			System.out.println(category_id);
			preparedstatement = connection.getInstance().connector().prepareStatement(fetchCategory);
			preparedstatement.setInt(1, category_id);
			ResultSet getCategory_name = preparedstatement.executeQuery();
			if(getCategory_name.next()) {
				category_name = getCategory_name.getString("category_name");
			}
			
		}catch (SQLException e) {
			throw new Exception("something went wrong");
        } 
		return category_name;
	}
	
	public String showProduct(int product_id) throws Exception {
		
		JSONObject json = new JSONObject();
		String getProduct = "select * from Product p join Brand b on b.brand_id = p.brand_id join Category c on c.category_id = p.category_id where p.product_id = ? ";
		PreparedStatement preparedstatement;
		try {
			preparedstatement = connection.getInstance().connector().prepareStatement(getProduct);
			preparedstatement.setInt(1, product_id);
			ResultSet productDetails = preparedstatement.executeQuery();
			if(productDetails.next()) {

				json.put("product_id", productDetails.getInt("product_id"));
				json.put("product_name", productDetails.getString("product_name"));
				json.put("product_price", productDetails.getInt("price"));
				json.put("product_ratting", productDetails.getInt("ratting"));
				json.put("product_image", changeStringFileToBase64(productDetails.getString("product_image_file_path"))+"~"+productDetails.getString("product_image_file_path").split("\\.")[1]);
				json.put("product_discription", productDetails.getString("discription"));
				json.put("brand_name", productDetails.getString("brand_name"));
				json.put("category_name", productDetails.getString("category_name"));
				
			}
			
		}catch (SQLException e) {
			throw new Exception("something went wrong");
        } catch (JSONException e) {
        	throw new Exception("something went wrong");
		}
		
		return json.toString();
		
	}
	
	public String jsonToString(JSONObject json) {
		return json.toString();
	}
	
	
	
    
}
