package com.Product;

import java.io.BufferedReader;
import java.io.IOException;

import org.json.JSONException;
import org.json.JSONObject;

import com.Manager.Manager;
import com.common.Session;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;


public class Add_Category extends HttpServlet {
	private static final long serialVersionUID = 1L;

	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		response.getWriter().append("Served at: ").append(request.getContextPath());

	}


	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		response.setHeader("Access-Control-Allow-Origin", "*");
        response.setHeader("Access-Control-Allow-Methods", "POST"); // You can specify other methods if needed
        response.setHeader("Access-Control-Allow-Headers", "Content-Type");
		JSONObject json = Session.getSession(request.getCookies());
		String result = "50";
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
				Category cate = new Category(categoryName);
				Manager manager = new Manager(user_id, role_id);
				result = manager.addCategory(cate);
				response.getWriter().write(result);
				return;
			} catch (JSONException e) {
				result = "json error";
				response.getWriter().write(result);
			} catch (Exception e) {
				response.getWriter().write(result);
			}
	   } 
		response.getWriter().write(result);
		
	}

}
