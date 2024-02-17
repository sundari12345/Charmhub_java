package com.common;

import java.io.IOException;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import org.json.JSONException;
import org.json.JSONObject;
import com.login.connection;
import jakarta.servlet.http.Cookie;

public class Session {
	
	public static JSONObject getSession(Cookie[] cookies) {
		JSONObject json = null;
		
	        if (cookies != null) {
	            for (Cookie cookie : cookies) {
	                if (cookie.getName().equals("sessionId")) {
	                    String sessionId = cookie.getValue();
	                    String fetchquerry = "select * from session where session_id like ?" ;
	                    
	            		try {
	            			PreparedStatement preparedstatement = connection.getInstance().connector().prepareStatement(fetchquerry);
	            			preparedstatement.setString(1, sessionId);
	            			ResultSet sessionDetails = preparedstatement.executeQuery();
	            			if(sessionDetails.next()) {
	            				int user_id = sessionDetails.getInt("user_id");
	            				int role_id = sessionDetails.getInt("role_id");
	                    		json = new JSONObject();
	                    		json.put("user_id", user_id);
	                    		json.put("role_id", role_id);
	                    		System.out.println(user_id);
	            			}	
	            		} catch (SQLException | JSONException e) {
	            			e.printStackTrace();
	            			System.out.println("something went wrong");
	            		}
	                }
	            }
		}
	    return json;
	}
}

