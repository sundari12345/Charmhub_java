package com.user;

import java.io.BufferedReader;

import java.io.IOException;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.json.JSONException;
import org.json.JSONObject;
import com.Product.Category;
import com.common.Session;
import com.login.connection;
import java.util.Date;


/**
 * Servlet implementation class ProfileServlet
 */
public class ProfileServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

    
    
	protected void doPost(HttpServletRequest request, HttpServletResponse response)  {
		response.setHeader("Access-Control-Allow-Origin", "*");
        response.setHeader("Access-Control-Allow-Methods", "POST"); // You can specify other methods if needed
        response.setHeader("Access-Control-Allow-Headers", "Content-Type");
		System.out.println("profile class:");
		try {
			System.out.println("profile class:");
			BufferedReader jsonString = request.getReader();
			StringBuilder profileRetrive = new StringBuilder();
			String line;
	        while((line = jsonString.readLine()) != null) {
	        	profileRetrive.append(line);
	        }
			SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
			System.out.println("profile class:"+profileRetrive);
			JSONObject profileContent = new JSONObject(profileRetrive.toString());
			
			int profileId = Integer.parseInt(profileContent.getString("profile_id"));
			System.out.println("profile class: profileId" + profileId);
			String firstname = profileContent.getString("firstname");
			String lastname = profileContent.getString("lastname");
			String phoneno = profileContent.getString("phoneno");
			String email = profileContent.getString("email");
			Date DOB = dateFormat.parse(profileContent.getString("DOB")); 
			Gender gender = Gender.valueOf(profileContent.getString("gender"));
			java.sql.Date sqlDate = new java.sql.Date(DOB.getTime());
			Profile userProfile = new Profile(profileId, firstname, lastname, phoneno, email, sqlDate, gender);
			JSONObject json = Session.getSession(request.getCookies());
			if(json != null) {
				int user_id = json.getInt("user_id");
				userProfile.editProfile(userProfile, user_id);
			}
			response.getWriter().write("Profile modified!!");
		} catch (JSONException e) {
			e.printStackTrace();
			System.out.println("something went wrong");
			
		} catch (ParseException e) {
			e.printStackTrace();
			System.out.println("something went wrong");
		}
		 catch (IOException e) {
			 e.printStackTrace();
			 System.out.println("something went wrong");
		}
		
	}
	
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException{
		
		JSONObject json = Session.getSession(request.getCookies());
		try {
			if(json != null) {
				int user_id = json.getInt("user_id");
				int role_id = json.getInt("role_id");
				Profile profile = new Profile();
				String result = profile.viewProfile(user_id, role_id);
				response.getWriter().write(result);
			} else {
	        	String empty  = "";
				response.getWriter().write(empty);
	        }
		}catch (IOException e) {
			String empty  = "";
			response.getWriter().write(empty);
		} catch (JSONException e) {
			String empty  = "json error";
			response.getWriter().write(empty);
		}

	}


}
