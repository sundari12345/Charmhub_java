package com.user;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.UUID;

import org.json.JSONException;
import org.json.JSONObject;

import com.Product.Brand;
import com.Product.Category;
import com.login.BCrypt;
import com.login.connection;
import jakarta.servlet.http.Cookie;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

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
	
	public User() {
		try {
			boolean notNameOnlyWithImage = false;
			this.retriveCategory(notNameOnlyWithImage);
		} catch (Exception e) {
			
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
			e.printStackTrace();
			throw new Exception("something went wrong");
		}
		
		return this.listOfCategory;
	} 
	
	public User login(String emailId, String password) {
		User user = null;
		String welcome = "wrong password";
		int decisionMakingNum = 0;
		try {
			ResultSet resultSet = connection.getInstance().statement().executeQuery("select p.email_id, u.password, u.user_id, u.role_id from user u join Profile p on p.profile_id = u.profile_id");
			while (resultSet.next()) {
            	if(resultSet.getString(1).equals(emailId)&& BCrypt.checkpw(password, resultSet.getString(2))) {
            		welcome = "welcome";
            		decisionMakingNum = 1;
        			System.out.println(welcome);
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
			e.printStackTrace();
		}
		return user;
	}
	
	
	 public int insertUserDetails(String emailId, String password, String phoneno) throws SQLException {
		
		String hashedPassword = BCrypt.hashpw(password, BCrypt.gensalt());
		
		String emailInsertQuery = "INSERT INTO Profile (email_id, phoneno) VALUES (?)";
		int profile_id = 0;
		
		String passwordInsertQuery = "INSERT INTO user (password, profile_id, role_id) VALUES (?, ?, ?)";
		int rowsAffected1 = 0;
		int rowsAffected2 = 0;
		int customerRole_id = 4;
		int done = 0;
		try {
			PreparedStatement preparedStatement = connection.getInstance().connector().prepareStatement(emailInsertQuery);
		    preparedStatement.setString(1, emailId);
		    preparedStatement.setString(1, phoneno);
	        rowsAffected1 = preparedStatement.executeUpdate();
	        String getProfileIdQuerry = "select profile_id from Profile where email_id like "+"'"+emailId+"'";
			ResultSet resultSet = connection.getInstance().statement().executeQuery(getProfileIdQuerry);
			while (resultSet.next()) {
				profile_id = resultSet.getInt(1);
			}
	        preparedStatement = connection.getInstance().connector().prepareStatement(passwordInsertQuery);
	        preparedStatement.setString(1, hashedPassword);
	        preparedStatement.setInt(2, profile_id);
	        preparedStatement.setInt(3, customerRole_id);
	        rowsAffected2 = preparedStatement.executeUpdate();
	        if (rowsAffected1 > 0 && rowsAffected2 > 0) {
	            done = 1;
	            return done;
	        } 
		}catch (SQLException e) {
            e.printStackTrace();
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
					allBrands.put(brand.getBrandName(), brand.getBrandImage());
					listOfBrandName += brand.getBrandName()+"~";	
				} 
			}
			allBrands.put("allBrand", listOfBrandName);
		}
		catch (JSONException e) {
			e.printStackTrace();
		}
		
		return allBrands.toString();
		
		
	}
	
	
    
}
