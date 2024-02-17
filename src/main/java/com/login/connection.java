package com.login;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.Statement;

public class connection {

	private static connection con;
	private  Connection connector ;
	private  Statement statement;
	
	private connection() {
		
	}
	
	public static connection getInstance() {

		if (con == null) {
			con =  new connection();
		}
		
		
        return con;
    }
	
	public Statement statement() {
		if(this.statement == null) {
			try {
				this.statement = connector().createStatement();
	        } catch (Exception e) {
	            System.out.println(e);
	        }
		}
		return this.statement;
		
	}
	
	public Connection connector() {
		if(this.connector == null) {
			try {
	            Class.forName("com.mysql.cj.jdbc.Driver");
	            this.connector = DriverManager.getConnection(
	                    "jdbc:mysql://localhost:3306/charmhub", "sundari-zstk338", "PGRM=TGeB6VLICG"
	            );
	        } catch (Exception e) {
	            System.out.println(e);
	        }
		}
		return this.connector;
		
	}
	
}
