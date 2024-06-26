package com.login;

import java.io.IOException;

import java.sql.ResultSet;
import java.sql.SQLException;
import com.user.User;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;


public class Addservlet extends HttpServlet {
	
	private static final long serialVersionUID = 1L;

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		response.setHeader("Access-Control-Allow-Origin", "*");
        response.setHeader("Access-Control-Allow-Methods", "POST"); // You can specify other methods if needed
        response.setHeader("Access-Control-Allow-Headers", "Content-Type");
		// TODO Auto-generated method stub
		String emailId = request.getParameter("emailId");
		String password = request.getParameter("password");
		String phoneno = request.getParameter("phoneno");
		User user = new User(emailId, phoneno);
		try {
			user = user.login(emailId, password);
			String welcome = "wrong password";
			if(user != null) {
			   Cookie cookie = new Cookie("sessionId", user.getSession_id());
			   welcome = "welcome";
			   response.addCookie(cookie);
			   int manager = 2;
			   System.out.println("role: "+ user.getRole_id());
			   if(user.getRole_id() != manager) {
				   response.getWriter().write(welcome);
			       System.out.println("role any other: "+ user.getRole_id());
			   }
			   else if(user.getRole_id() == manager) {
				   response.getWriter().write(manager+"");
			   	   System.out.println("manager: "+ user.getRole_id());
			   }
				   
			}
			else {
				response.getWriter().write(welcome);
			}
		} catch (Exception e) {
			String welcome = "wrong password";
			response.getWriter().write(welcome);
		}
	}
	
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		System.out.println("came into address servlet");
		Cookie[] cookies = request.getCookies();
        if (cookies != null) {
        	System.out.println("1");
            for (Cookie cookie : cookies) {
                if (cookie.getName().equals("sessionId")) {
                	cookie.setMaxAge(0);
                    response.addCookie(cookie);
                    String logout = "0";
                    response.getWriter().write(logout);
                }else {
                	response.getWriter().write("no session id");
                }
            }
        }
        else {
        	response.getWriter().write("no cookie");
        }
    }

}
