package com.Admin;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;

import com.Manager.Manager;
import com.login.connection;
import com.user.Profile;

public class Admin {
    private String userName;
    private List<Manager> ListOfManager;
     
	public Admin(String userName) {
		this.userName = userName;
		ListOfManager = this.retriveManagerDetails();
	}
	
	public String getUserName() {
		return userName;
	}
	public void setUserName(String userName) {
		this.userName = userName;
	}
	public List<Manager> getListOfManager() {
		return ListOfManager;
	}
	public void setListOfManager(List<Manager> listOfManager) {
		ListOfManager = listOfManager;
	}
     
    private List<Manager> retriveManagerDetails(){
    	this.ListOfManager = new LinkedList<>();
    	String fetchManagers = "select * from Manager m join user u on u.user_id = m.user_id join  Profile p on p.profile_id = u.profile_id";
    	try {
			ResultSet managersDetails = connection.getInstance().statement().executeQuery(fetchManagers);
			while (managersDetails.next()) {
				Profile managerProfile = new Profile(managersDetails.getInt("profile_id"), managersDetails.getString("firstname"), managersDetails.getString("lastname"), managersDetails.getString("phoneno"), managersDetails.getString("email_id"), managersDetails.getDate("DOB"),  managersDetails.getObject("gender"));
				this.ListOfManager.add(new Manager(managersDetails.getInt("user_id"), managerProfile,  managersDetails.getInt("role_id"), managersDetails.getInt("manager_id")));
            }
		} catch (SQLException e) {
			return ListOfManager;
			
		}
    	return ListOfManager;
    } 
    
	public Manager addManager(String firstName, String lastName, String phoneNo, String emailId, Date dOB, Object gender2) {
    	int profile_id = 0;
    	int user_id = 0;
    	int manager_id = 0;
    	int role_id = 2;
    	Manager newManager = null;
    	
    	try {
    		String managerProfileAddQuerry = "INSERT INTO Profile (firstname, lastname, phoneno, email_id, DOB, gender) VALUES (?,?,?,?,?,?)";
        	PreparedStatement preparedStatement = connection.getInstance().connector().prepareStatement(managerProfileAddQuerry);
			preparedStatement.setString(1, firstName);
	    	preparedStatement.setString(2, lastName);
	    	preparedStatement.setString(3, phoneNo);
	    	preparedStatement.setString(4, emailId);
	    	preparedStatement.setDate(5, (java.sql.Date) dOB);
	    	preparedStatement.setObject(6, gender2);
	    	int rowsAffected = preparedStatement.executeUpdate();
	    	Profile newManagerProfile = new Profile(profile_id, firstName, lastName, phoneNo, emailId, dOB,  gender2);   
	    	String managerProfile_id = "select profile_id from Profile where email_id like "+ "'"+emailId+"'";
			ResultSet resultset = connection.getInstance().statement().executeQuery(managerProfile_id);
				if(resultset.next()) {
					profile_id = resultset.getInt(1);
					String userAddQuerry = "INSERT INTO Profile (profile_id, role_id) VALUES (?,?)";
			    	PreparedStatement preparedStatement2 = connection.getInstance().connector().prepareStatement(userAddQuerry);
			    	preparedStatement2.setInt(1, profile_id);
			    	preparedStatement2.setInt(2, role_id);
			    	int rowAffected2 = preparedStatement2.executeUpdate();
			    	String manageruser_id = "select user_id from user where profile_id = "+ profile_id;
			    	ResultSet resultset2 = connection.getInstance().statement().executeQuery(manageruser_id);
						if(resultset2.next()) {
							user_id = resultset.getInt(1);
							String managerAddQuerry = "INSERT INTO Manager (user_id) VALUES (?)";
					    	PreparedStatement preparedStatement3 = connection.getInstance().connector().prepareStatement(managerAddQuerry);
					    	preparedStatement3.setInt(1, user_id);
					    	int rowAffected1 = preparedStatement3.executeUpdate();				    	
					    	String manager_idQuerry = "select manager_id from Manager where user_id = "+ user_id;
							ResultSet resultset3 = connection.getInstance().statement().executeQuery(manager_idQuerry);
								if(resultset3.next()) {
									manager_id = resultset3.getInt(1);
								}	
						}
			    	
				}
				
			
    	 newManager = new Manager(user_id, newManagerProfile, manager_id, role_id);
    	} catch (SQLException e) {
    		return newManager;
    		
		}
    	
    	
    	return newManager;
    }
}

















