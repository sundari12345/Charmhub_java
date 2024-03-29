package com.login;

import java.io.IOException;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import com.user.User;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

public class CreateAccount extends HttpServlet {
	private static final long serialVersionUID = 1L;
	
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		response.setHeader("Access-Control-Allow-Origin", "*");
        response.setHeader("Access-Control-Allow-Methods", "POST"); // You can specify other methods if needed
        response.setHeader("Access-Control-Allow-Headers", "Content-Type");
        String emailId = request.getParameter("emailId");
        System.out.println(emailId+": email");
		String password = request.getParameter("password");
		String phoneno = request.getParameter("phoneno");
		String welcome = "wrong password";
		int decisionMakingNum = 0;
		try {
			User user = new User(emailId, phoneno);
			decisionMakingNum = user.checkAccountExist(user);
			System.out.println("Check Account exist");
			if(decisionMakingNum == 0) {
				System.out.println("No Account exist");
				int done = user.SingUp(emailId, password, phoneno);
				if(done == 1) {
					welcome = "welcome";
				}
				else {
					welcome = "something went wrong";
				}
			}
		} catch (SQLException e) {
			e.printStackTrace();
			response.getWriter().write("wrong query");
		} catch (Exception e) {
			e.printStackTrace();
			response.getWriter().write(decisionMakingNum);
		}
		response.getWriter().write(welcome);
		
	}
	
	

}
