package com.user;

import java.io.BufferedReader;
import java.io.IOException;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import org.json.JSONException;
import org.json.JSONObject;

import com.common.Session;
import com.login.connection;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;


public class AddressServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

   
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		response.setHeader("Access-Control-Allow-Origin", "*");
        response.setHeader("Access-Control-Allow-Methods", "POST"); // You can specify other methods if needed
        response.setHeader("Access-Control-Allow-Headers", "Content-Type");
		try {
			JSONObject json = Session.getSession(request.getCookies());
			if(json != null) {
				int user_id = json.getInt("user_id");
				int role_id = json.getInt("role_id");
				Address address = new Address(user_id);
				String result = address.viewAddress(address);
				response.getWriter().write(result);
			}
		}catch (JSONException e) {
			String result = "json error";
			response.getWriter().write(result);
			
		} 
	}


	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		response.setHeader("Access-Control-Allow-Origin", "*");
        response.setHeader("Access-Control-Allow-Methods", "POST"); // You can specify other methods if needed
        response.setHeader("Access-Control-Allow-Headers", "Content-Type");

        try {
			System.out.println("address class:");
			BufferedReader jsonString = request.getReader();
			StringBuilder addressRetrive = new StringBuilder();
			String line;
	        while((line = jsonString.readLine()) != null) {
	        	addressRetrive.append(line);
	        }
	        int user_id = 0;

    			JSONObject json = Session.getSession(request.getCookies());
    			if(json != null) {
    				user_id = json.getInt("user_id");
    				int role_id = json.getInt("role_id");
    			}

			
			
			JSONObject addressContent = new JSONObject(addressRetrive.toString());
			System.out.println("profile class: user_id: " + user_id);
			String address = addressContent.getString("address");
			String landmark = addressContent.getString("landmark");
			int pincode = Integer.parseInt(addressContent.getString("pincode"));
			String locality = addressContent.getString("locality");
			String state = addressContent.getString("state"); 
			String city = addressContent.getString("city");
			
			Address userAddress = new Address(address, landmark, pincode, locality, state, city, user_id);
			if(userAddress.editAddress(userAddress))
				response.getWriter().write("Address modified !!");
		} catch (JSONException e) {
			String result = "json error";
			response.getWriter().write(result);
		} 
		 catch (IOException e) {
			String result = "something went wrong";
			response.getWriter().write(result);
		}
	}

}
