package com.user;

import jakarta.servlet.http.HttpServlet;
import java.io.IOException;
import jakarta.servlet.Servlet;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;


public class productView extends HttpServlet implements Servlet {
	private static final long serialVersionUID = 1L;
    
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		
		if(request.equals("brand_name")) {
			User user = new User();
			String brandsToView = user.viewBrand();
			response.getWriter().write(brandsToView);
		}
		else {
			
		}
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		doGet(request, response);
	}

}
