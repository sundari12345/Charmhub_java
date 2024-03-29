package com.Product;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import com.login.connection;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.MultipartConfig;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.Part;

@WebServlet("/commonImageUploader")
@MultipartConfig(
    fileSizeThreshold = 1024 * 1024, 
    maxFileSize = 5 * 1024 * 1024,   
    maxRequestSize = 10 * 1024 * 1024 
)
public class commonImageUploader extends HttpServlet {
    private static final long serialVersionUID = 1L;

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		response.setHeader("Access-Control-Allow-Origin", "*");
        response.setHeader("Access-Control-Allow-Methods", "POST"); // You can specify other methods if needed
        response.setHeader("Access-Control-Allow-Headers", "Content-Type");
		System.out.println("is method entered here");
        Part filePart = request.getPart("imageFile");
        String brand_id = request.getParameter("brand_id");
        System.out.println(brand_id);
        System.out.println(filePart.getName());
        String fileName = filePart.getSubmittedFileName();     
        String folderPath = "/home/sundari-zstk338/Documents/Charmhub/";
        System.out.println(folderPath+": "+fileName);
        File destinationFile = new File(folderPath, fileName);
        
        try {
        	InputStream input = filePart.getInputStream();
        	try (FileOutputStream output = new FileOutputStream(destinationFile)) {
				byte[] buffer = new byte[1024];
				System.out.println(buffer.toString());
				int bytesRead;
				while ((bytesRead = input.read(buffer)) != -1) {
					System.out.println(bytesRead);
				    output.write(buffer, 0, bytesRead);
				}
				System.out.println(buffer.toString());
				
		
			} 	
            System.out.println("Image saved to: " + destinationFile.getAbsolutePath());
            response.getWriter().println("Image saved to: " + destinationFile.getAbsolutePath());
        } catch (IOException e) {
            e.printStackTrace();
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            response.getWriter().println("Error saving image");
        }
    }
}


