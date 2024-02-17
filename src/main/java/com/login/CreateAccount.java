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
/**
 * Servlet implementation class CreateAccount
 */
public class CreateAccount extends HttpServlet {
	private static final long serialVersionUID = 1L;


    public CreateAccount() {
        // TODO Auto-generated constructor stub
    }

	
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		String emailId = request.getParameter("emailId");
		String password = request.getParameter("password");
		String phoneno = request.getParameter("phoneno");
		String welcome = "wrong password";
		System.out.println("emailId = "+emailId+" password = "+password);
		int decisionMakingNum = 0;
		try {
			ResultSet resultSet = connection.getInstance().statement().executeQuery("select p.email_id, u.password from user u join Profile p on p.profile_id = u.profile_id");
			while (resultSet.next()) {
            	if(resultSet.getString(1).equals(emailId)&&BCrypt.checkpw(password, resultSet.getString(2))) {
            		System.out.println("emailId = "+emailId+" password = "+password);
            		decisionMakingNum = 1;
        			System.out.println(welcome);
        			response.getWriter().write(welcome);
            		break;
            	}
            }
			if(decisionMakingNum == 0) {
				System.out.println("emailId = "+emailId+" password = "+password);
				System.out.println(welcome);
				User user = new User (emailId, phoneno);
				int done = user.insertUserDetails(emailId, password, phoneno);
				if(done == 1) {
					welcome = "welcome";
				}
				else {
					welcome = "something went wrong";
				}
    			response.getWriter().write(welcome);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		
	}
	
	

}
