package com.Product;

import jakarta.servlet.http.HttpServlet;

import java.io.BufferedReader;
import java.io.IOException;

import org.json.JSONException;
import org.json.JSONObject;

import com.Manager.Manager;
import com.common.Session;

import jakarta.servlet.Servlet;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;


public class Add_product extends HttpServlet implements Servlet {
	private static final long serialVersionUID = 1L;
       
    
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		try {
			
			JSONObject json = Session.getSession(request.getCookies());
			if(json != null) {
				String category_name = request.getParameter("category_name");
				JSONObject json2 = new JSONObject();
				System.out.println(category_name);
				String listOfBrand = "";
				Category category = new Category(category_name, true);
				for(Brand b: category.getListOfBrands()){
					listOfBrand += b.getBrandName()+"~";
				}
				
				System.out.println(listOfBrand);
				json2.put("listOfBrand", listOfBrand);
				System.out.println("json entry potuten");
				response.getWriter().write(json2.toString());
				return ;
			}
		}catch (JSONException e) {
			e.printStackTrace();
			System.out.println("something went wrong");
		}              

		response.getWriter().write("");
	}

	
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		System.out.println("is in correct class");
		JSONObject json = Session.getSession(request.getCookies());
		if(json != null) {
			try {
				int user_id = json.getInt("user_id");
				int role_id = json.getInt("role_id");
				System.out.println("product class:");
				BufferedReader jsonString = request.getReader();
				StringBuilder addressRetrive = new StringBuilder();
				String line;
		        while((line = jsonString.readLine()) != null) {
		        	addressRetrive.append(line);
		        }
		        JSONObject addressContent = new JSONObject(addressRetrive.toString());
				System.out.println("profile class: user_id: " + user_id);
				String category_name = addressContent.getString("category_name");
				String brand_name = addressContent.getString("brand_name");
				String product_name = addressContent.getString("product_name");
				int product_price = Integer.parseInt(addressContent.getString("product_price")) ;
				String product_image = addressContent.getString("product_image");
				String discription = addressContent.getString("discription");
				System.out.println(category_name+": "+brand_name+": no brand");
				Manager manager = new Manager(user_id, role_id);
				Product product = new Product(product_name, product_price, product_image, discription);
				String result = manager.addProductTODb(product, brand_name, category_name)+"";
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
