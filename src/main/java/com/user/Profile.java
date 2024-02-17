package com.user;

import java.io.IOException;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Date;

import org.json.JSONException;
import org.json.JSONObject;

import com.common.Session;
import com.login.connection;

import jakarta.servlet.http.Cookie;

public class Profile {
	private int profile_id;
    private String firstName;
    private String lastName;
    private String phoneNo;
    private String emailId;
    private Date DOB;
    private Gender gender;   
    
	public Profile(int profile_id, String firstName, String lastName, String phoneNo, String emailId, Date dOB, Object gender2) {
		this.profile_id = profile_id;
		this.firstName = firstName;
		this.lastName = lastName;
		this.phoneNo = phoneNo;
		this.emailId = emailId;
		this.DOB = dOB;
		this.gender = (Gender) gender2;
	}
	
	public Profile() {
		
	}
	
	public Profile(String phoneNo, String emailId) {
		this.phoneNo = phoneNo;
		this.emailId = emailId;
	}
	
	public int getProfile_id() {
		return profile_id;
	}

	public void setProfile_id(int profile_id) {
		this.profile_id = profile_id;
	}

	public String getFirstName() {
		return firstName;
	}
	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}
	public String getLastName() {
		return lastName;
	}
	public void setLastName(String lastName) {
		this.lastName = lastName;
	}
	public String getPhoneNo() {
		return phoneNo;
	}
	public void setPhoneNo(String phoneNo) {
		this.phoneNo = phoneNo;
	}
	public String getEmailId() {
		return emailId;
	}
	public void setEmailId(String emailId) {
		this.emailId = emailId;
	}
	public Date getDOB() {
		return DOB;
	}
	public void setDOB(Date dOB) {
		DOB = dOB;
	}
	public Gender getGender() {
		return gender;
	}
	public void setGender(Gender gender) {
		this.gender = gender;
	}
	
	public void editProfile(Profile profile, int user_id){
		
		this.firstName = profile.firstName;
		this.lastName = profile.lastName;
		this.phoneNo = profile.phoneNo;
		this.emailId = profile.emailId;
		this.DOB = profile.DOB;
		this.gender = profile.gender;
		
		String profileDetails = "select profile_id from user where user_id = ?" ;
		String updateAddress = "UPDATE Profile SET  firstName = ?, lastName = ?, phoneNo = ?, email_id = ?, DOB = ?,  gender = ? WHERE profile_id = ?"; 
		int rowsAffected = 0;
		
		try {
			int profile_id = 0;
			PreparedStatement preparedstatement2 = connection.getInstance().connector().prepareStatement(profileDetails);
			preparedstatement2.setInt(1, user_id);
			ResultSet profile_ids = preparedstatement2.executeQuery();
			if(profile_ids.next()) {
				profile_id = profile_ids.getInt("profile_id");
				this.profile_id = profile_id;
			}
			
			PreparedStatement preparedStatement = connection.getInstance().connector().prepareStatement(updateAddress);
		    preparedStatement.setString(1, profile.firstName);
		    preparedStatement.setString(2, profile.lastName);
		    preparedStatement.setString(3, profile.phoneNo);
		    preparedStatement.setString(4, profile.emailId);
		    preparedStatement.setDate(5, (java.sql.Date) profile.DOB);
		    System.out.println(profile.gender.name());
		    preparedStatement.setString(6, profile.gender.name());
		    preparedStatement.setInt(7, profile_id);
	        rowsAffected = preparedStatement.executeUpdate();  
		}catch (SQLException e) {
		    e.printStackTrace();
			return ;
        }
		
		if(rowsAffected == 0) {
			System.out.println("Something went wrong");
			return ;
		}
	} 
	
	public String viewProfile(int user_id, int role_id) {
		JSONObject json = new JSONObject();
		try {
			String profile = "select profile_id from user where user_id = ?" ;
			PreparedStatement preparedstatement2 = connection.getInstance().connector().prepareStatement(profile);
			preparedstatement2.setInt(1, user_id);
			ResultSet profile_ids = preparedstatement2.executeQuery();
			if(profile_ids.next()) {
				int profile_id = profile_ids.getInt("profile_id");
				String profilequery = "select * from Profile where profile_id = ?" ;
	    		PreparedStatement preparedstatement3 = connection.getInstance().connector().prepareStatement(profilequery);
	    		preparedstatement3.setInt(1, profile_id);
	    		ResultSet profileContent = preparedstatement3.executeQuery();
	    		if(profileContent.next()) {
	    			String firstname = profileContent.getString("firstname");
	    			String lastname = profileContent.getString("lastname");
	    			String phoneno = profileContent.getString("phoneno");
	    			String email_id = profileContent.getString("email_id");
	    			String DOB = profileContent.getDate("DOB").toString();
	    			String gender = profileContent.getObject("gender").toString();
	    			
	    			json.put("firstname", firstname);
	    			json.put("lastname", lastname);
	    			json.put("phoneno", phoneno);
	    			json.put("email_id", email_id);
	    			json.put("DOB", DOB);
	    			json.put("gender", gender);
	    			json.put("profile_id", profile_id);
	    			
	    		}
	    		else {
	    			System.out.println("something went wrong");
	    		}
			}
			else {
				System.out.println("something went wrong");
			}
		
	    }catch (SQLException e) {
			e.printStackTrace();
		} catch (JSONException e) {
		e.printStackTrace();
	    }
		return json.toString();
  }
    
    
}
