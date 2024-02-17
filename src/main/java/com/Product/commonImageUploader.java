package com.Product;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
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
        
		System.out.println("is method entered here");
        Part filePart = request.getPart("imageFile");
        System.out.println(filePart.getName());
        String fileName = filePart.getSubmittedFileName();     
        String folderPath = getServletContext().getRealPath("/Image");
        System.out.println(folderPath+": "+fileName);
        File destinationFile = new File(folderPath, fileName);

        try {
        	InputStream input = filePart.getInputStream();
        	try (FileOutputStream output = new FileOutputStream(destinationFile)) {
				byte[] buffer = new byte[1024];
				int bytesRead;
				while ((bytesRead = input.read(buffer)) != -1) {
				    output.write(buffer, 0, bytesRead);
				}
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
//public class commonImageUploader {
//	
//	public String saveImage(String image, String folderPath) {
//		System.out.println(image);
//		File imageFile = new File(image);
//        System.out.println(imageFile.getAbsolutePath());
////        if (!imageFile.exists() || !imageFile.isFile()) {
////            System.out.println("File does not exist or is not a file.");
////            return  "";
////        }
//
//        byte[] imageData = new byte[(int) imageFile.length()];
//        try (FileInputStream fis = new FileInputStream(imageFile)) {
//            fis.read(imageData);
//        } catch (FileNotFoundException e) {
//			e.printStackTrace();
//		} catch (IOException e) {
//			e.printStackTrace();
//		}
//        File destinationFile = new File(folderPath, imageFile.getName());
//
//        try (FileOutputStream fos = new FileOutputStream(destinationFile)) {
//            fos.write(imageData);
//        } catch (FileNotFoundException e) {
//			e.printStackTrace();
//		} catch (IOException e) {
//			e.printStackTrace();
//		}
//
//        System.out.println("Image saved to: " + destinationFile.getAbsolutePath());
//        return "sucess";
//	}
//}
