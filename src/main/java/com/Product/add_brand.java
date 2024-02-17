package com.Product;


import java.io.BufferedReader;

import java.io.IOException;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import org.json.JSONException;
import org.json.JSONObject;
import com.Manager.Manager;
import com.common.Session;
import com.login.connection;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;


public class add_brand extends HttpServlet {
	private static final long serialVersionUID = 1L;

    
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		try {
			
			JSONObject json = Session.getSession(request.getCookies());
			if(json != null) {
				int user_id = json.getInt("user_id");
				int role_id = json.getInt("role_id");
				com.user.User user = new com.user.User(user_id, role_id);
				
				JSONObject json2 = new JSONObject();
				String listOfCategory = "";
				System.out.println(user.getListOfCategory());
				
				for(Category c: user.getListOfCategory()){
					listOfCategory += c.getCategoryName()+"~";
				}
				System.out.println(listOfCategory);
				json2.put("listOfCategory", listOfCategory);
				System.out.println("json entry potuten");
				response.getWriter().write(json2.toString());
				return ;
			}
		}catch (JSONException e) {
			e.printStackTrace();
			System.out.println("something went wrong");
		}              

	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		JSONObject json = Session.getSession(request.getCookies());
		if(json != null) {
			try {
				int user_id = json.getInt("user_id");
				int role_id = json.getInt("role_id");
				System.out.println("brand class:");
				BufferedReader jsonString = request.getReader();
				StringBuilder addressRetrive = new StringBuilder();
				String line;
		        while((line = jsonString.readLine()) != null) {
		        	addressRetrive.append(line);
		        }
		        JSONObject addressContent = new JSONObject(addressRetrive.toString());
				System.out.println("profile class: user_id: " + user_id);
				String categoryName = addressContent.getString("category_name");
				String brandName = addressContent.getString("brand_name");
				String brandimage = addressContent.getString("brand_image");
				Brand newBrand = new Brand(brandName, brandimage);
				Manager manager = new Manager(user_id, role_id);
				String result = manager.addBrand(newBrand, categoryName);
				response.getWriter().write(result);
				return ;
				
			} catch (JSONException e) {
				e.printStackTrace();
			} catch (Exception e) {
				e.printStackTrace();
			}
			
		}
		String result = "50";
		response.getWriter().write(result);
	}

}
