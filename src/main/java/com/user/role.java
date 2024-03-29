package com.user;

import java.io.IOException;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import com.login.connection;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

/**
 * Servlet implementation class role
 */
public class role extends HttpServlet {
	private static final long serialVersionUID = 1L;

 
	
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		response.setHeader("Access-Control-Allow-Origin", "*");
        response.setHeader("Access-Control-Allow-Methods", "POST"); // You can specify other methods if needed
        response.setHeader("Access-Control-Allow-Headers", "Content-Type");
		String role_id = 4+"";
        Cookie[] cookies = request.getCookies();
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
            				
            				role_id = Integer.toString(sessionDetails.getInt("role_id"));
            				System.out.println("role_id: "+ role_id);
            				response.getWriter().write(role_id);
            				break;
            			}
            			
            		} catch (SQLException e) {
            			String role = "0";
            			response.getWriter().write(role);
					}
                }
            }
        }
        else {
        	String login = "1"; 
        	response.getWriter().write(login);
        }
        
		
	}


}
