package com.user;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import org.json.JSONException;
import org.json.JSONObject;

import com.login.connection;

public class Address {

	 private String address;
	 private String landmark;
	 private int pincode;
	 private String locality;
	 private String state;
	 private String city;
	 private int user_id;
	 
	 public Address(String address, String landmark, int pincode, String locality, String state, String city, int user_id) {
		this.address = address;
		this.landmark = landmark;
		this.pincode = pincode;
		this.locality = locality;
		this.state = state;
		this.city = city;
		this.user_id = user_id;
	 }
	 
	 public Address(int user_id) {
		this.user_id = user_id;
	 }

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public String getLandmark() {
		return landmark;
	}

	public void setLandmark(String landmark) {
		this.landmark = landmark;
	}

	public int getPincode() {
		return pincode;
	}

	public void setPincode(int pincode) {
		this.pincode = pincode;
	}

	public String getLocality() {
		return locality;
	}

	public void setLocality(String locality) {
		this.locality = locality;
	}

	public String getState() {
		return state;
	}

	public void setState(String state) {
		this.state = state;
	}

	public String getCity() {
		return city;
	}

	public void setCity(String city) {
		this.city = city;
	}

	public int getUser_id() {
		return user_id;
	}

	public void setUser_id(int user_id) {
		this.user_id = user_id;
	}
	 
	public boolean editAddress(Address address){
		
		String updateAddress = "UPDATE Address SET address = ?, landmark = ?, pincode = ?, locality = ?, state = ?, city = ? WHERE user_id = ?"; 
		int rowsAffected = 0;
		try {
			PreparedStatement preparedStatement = connection.getInstance().connector().prepareStatement(updateAddress);
		    preparedStatement.setString(1, address.address);
		    preparedStatement.setString(2, address.landmark);
		    preparedStatement.setInt(3, address.pincode);
		    preparedStatement.setString(4, address.locality);
		    preparedStatement.setString(5, address.state);
		    preparedStatement.setString(6, address.city);
		    preparedStatement.setInt(7, address.user_id);
	        rowsAffected = preparedStatement.executeUpdate();  
		}catch (SQLException e) {
			e.printStackTrace();
        }
		
		if(rowsAffected > 0) {
			return true;
		}
		
		else if( rowsAffected == 0) {
			return insertAddress(address);
		}
		
		return false;
	} 
	
	
	public boolean insertAddress(Address address) {
		String inserAddress = "INSERT INTO Address(address, landmark, pincode, locality, state, city, user_id) VALUES (?,?,?,?,?,?,?)"; 
		int rowsAffected = 0;
		try {
			PreparedStatement preparedStatement = connection.getInstance().connector().prepareStatement(inserAddress);
		    preparedStatement.setString(1, address.address);
		    preparedStatement.setString(2, address.landmark);
		    preparedStatement.setInt(3, address.pincode);
		    preparedStatement.setString(4, address.locality);
		    preparedStatement.setString(5, address.state);
		    preparedStatement.setString(6, address.city);
		    preparedStatement.setInt(7, address.user_id);
	        rowsAffected = preparedStatement.executeUpdate();  
		}catch (SQLException e) {
			e.printStackTrace();
        }
		
		if(rowsAffected > 0) {
			return true;
		}
		else {
			return false;
		}
	}
	
	public String viewAddress(Address address) {
		try {
			String addressquery = "select * from Address where user_id = ?" ;
			System.out.println("3: "+ address.user_id);
			PreparedStatement preparedstatement3 = connection.getInstance().connector().prepareStatement(addressquery);
			preparedstatement3.setInt(1, user_id);
			ResultSet addressContent = preparedstatement3.executeQuery();
			if(addressContent.next()) {
				String add = addressContent.getString("address");
				String landmark = addressContent.getString("landmark");
				String pincode = addressContent.getInt("pincode")+"";
				String locality = addressContent.getString("locality");
				String state = addressContent.getString("state");
				String city = addressContent.getString("city");
				String userId = user_id+"";
				JSONObject json = new JSONObject();
				json.put("address", add);
				json.put("landmark", landmark);
				json.put("pincode", pincode);
				json.put("locality", locality);
				json.put("state", state);
				json.put("city", city);
				json.put("userId", userId);
				return json.toString();
			}
			else {
				return "no address";
			}
		}catch (SQLException | JSONException e) {
			e.printStackTrace();
			System.out.println("something went wrong");
		} 
		return "Something went wrong";
	}

}
