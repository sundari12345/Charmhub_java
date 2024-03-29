package com.user;

import jakarta.servlet.http.HttpServlet;
import java.io.IOException;
import org.json.JSONException;
import org.json.JSONObject;
import com.common.Session;

import jakarta.servlet.Servlet;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;


public class productView extends HttpServlet implements Servlet {
	private static final long serialVersionUID = 1L;
    
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		response.setHeader("Access-Control-Allow-Origin", "*");
        response.setHeader("Access-Control-Allow-Methods", "POST"); // You can specify other methods if needed
        response.setHeader("Access-Control-Allow-Headers", "Content-Type");
		String brand_name = request.getParameter("brand_name");
		String showAllBrands = "All";
		
		if(brand_name.equals(showAllBrands)) {
			User user = new User();
			String brandsToView = user.viewBrand();
			response.getWriter().write(brandsToView);
			return ;
		}
		else {
			System.out.println(brand_name);
			JSONObject json = Session.getSession(request.getCookies());
	        String success = "50";
	        
			User user = new User(json);
			String productDetails;
			try {
				productDetails = user.showProductThisBrand(brand_name);
				response.getWriter().write(productDetails);
				return ;
			} catch (Exception e) {
				response.getWriter().write("No product!!");
			}
		}
		
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		doGet(request, response);
	}

}
