package com.customer;

import java.io.IOException;
import org.json.JSONException;
import org.json.JSONObject;
import com.Product.Product;
import com.common.Session;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;


public class wishListServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
   
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		response.setHeader("Access-Control-Allow-Origin", "*");
        response.setHeader("Access-Control-Allow-Methods", "POST"); 
        response.setHeader("Access-Control-Allow-Headers", "Content-Type");
        JSONObject json = Session.getSession(request.getCookies());
        String success = "50";
        try {
			if(json != null) {
				int user_id = json.getInt("user_id");				
				int role_id = json.getInt("role_id");
				int product_id = Integer.parseInt(request.getParameter("product_id"));
				String isInWishList = request.getParameter("isInWishList");
				String True = "true";
				System.out.println(isInWishList + "isitTrue");
				Product product = new Product(product_id);
				Customer customer = new Customer(user_id, role_id);
				if(!True.equals(isInWishList)) {
					if(customer.addProductToWishList(product)) {
						success = "100";
						response.getWriter().write(success);
						return ;
					}
				}
				else {
					if(customer.deleteProductFromWishList(product))
					    response.getWriter().write(success);
					return;
				}
			}
        } catch (JSONException e) {
        	System.out.println("2");
//			e.printStackTrace();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} 
		
	}
	
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		response.setHeader("Access-Control-Allow-Origin", "*");
        response.setHeader("Access-Control-Allow-Methods", "POST"); 
        response.setHeader("Access-Control-Allow-Headers", "Content-Type");
        JSONObject json = Session.getSession(request.getCookies());
        String success = "50";
        try {
			if(json != null) {
				int user_id = json.getInt("user_id");				
				int role_id = json.getInt("role_id");
				
				Customer customer = new Customer(user_id, role_id);
				String whisListDetails =  customer.showWishListProduct();
				response.getWriter().write(whisListDetails);
				return;
			}
        } catch (JSONException e) {
        	System.out.println("2");
//			e.printStackTrace();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} 
        
		return  ;
	}

}
