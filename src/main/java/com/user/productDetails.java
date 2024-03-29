package com.user;

import java.io.IOException;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

/**
 * Servlet implementation class productDetails
 */
public class productDetails extends HttpServlet {
	private static final long serialVersionUID = 1L;

   
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		response.setHeader("Access-Control-Allow-Origin", "*");
        response.setHeader("Access-Control-Allow-Methods", "POST"); // You can specify other methods if needed
        response.setHeader("Access-Control-Allow-Headers", "Content-Type");
		int product_id = Integer.parseInt(request.getParameter("product_id")) ;
		
		
		User user = new User();
		String productDetails;
		try {
			productDetails = user.showProduct(product_id);
			response.getWriter().write(productDetails);
			return ;
		} catch (Exception e) {
			response.getWriter().write("No product!!");
		}
		
		response.getWriter().append("Served at: ").append(request.getContextPath());
	}

	

}
