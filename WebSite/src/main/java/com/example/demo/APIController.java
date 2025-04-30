package com.example.demo;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.ResponseEntity;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import ch.qos.logback.core.net.SyslogOutputStream;
import ch.qos.logback.core.recovery.ResilientSyslogOutputStream;

import java.io.File;
import java.io.IOException;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

@RestController
public class APIController {
	
	// check for duplicate fileNames and return a unique one
	private String returnValidFileName(String filename) {
		String validFileName = filename;
		
		File file = new File(uploadDir.getAbsolutePath() + File.separatorChar + validFileName);
		boolean valid = file.exists();
		// file = C:\Users\...\GalleryAppWebserver\WebSite\...IMGNAME.png
		
		if (valid) {
			String [] splitFileName = validFileName.split("\\.");
			String typeEnding = "." + splitFileName[1];
			validFileName = splitFileName[0];
			Random r = new Random();
			int fileNameAddition = r.nextInt(10);
			validFileName = validFileName + String.valueOf(fileNameAddition) + typeEnding;
			return returnValidFileName(validFileName);
		} 
		
		return validFileName;
	}
	
	// relative 
    private static final String UPLOAD_DIR = "uploads"; 
    // create path where images will be saved
    private final File uploadDir = new File(System.getProperty("user.dir") + File.separator + UPLOAD_DIR);
     
    
    @Autowired
	JDBCController jdbccontroller;
    
    @Autowired
    Md5HashGenerator md5hashgenerator = new Md5HashGenerator();
    
    @GetMapping("/getAllImageMD5")
    public List<String> getAllImageMD5() {
    	
    	List<String> allMD5 = jdbccontroller.getAllMD5();
    	for(String s : allMD5) {
    		System.out.println(s);
    	}
    	return allMD5;
    	
    }
    
    @GetMapping("/getImageTags")
    public List<DTOs.ImageTags> getImageTags() {
    	
    	List<DTOs.ImageTags> allImageTags = jdbccontroller.getImageTags();
    	
    	return allImageTags;
    	
    }
    
    @GetMapping("/getTags")
    public List<DTOs.Tags> getTags() {
    	
    	List<DTOs.Tags> allTags = jdbccontroller.getTags();
    	
    	return allTags;
    	
    }
      
    @PostMapping(value = "/uploadImageUris", consumes = "multipart/form-data")
    public ResponseEntity<String> uploadImages(
    		@RequestParam("images") List<MultipartFile> images,
    		@RequestParam("md5") List<String> md5,
    		@RequestParam("selectedTags") List<String> selectedTags )throws NoSuchAlgorithmException {
        try {
            
            if (!uploadDir.exists()) {
                boolean created = uploadDir.mkdirs();
                if (!created) {
                    return ResponseEntity.internalServerError().body("Failed to create upload directory.");
                }
            }
            
            for (int i = 0; i< images.size(); i++) {
            	
            	 
            	String currentMD5 = md5.get(i);
            	String newImageName = returnValidFileName(images.get(i).getOriginalFilename());
            	
                File file = new File(uploadDir, newImageName);
                images.get(i).transferTo(file);
                System.out.println("Received file: " + file.getAbsolutePath());
                System.out.println("SelectedTags: " + selectedTags.get(i));
                jdbccontroller.importIntoImageDB(file.getName(), file.getAbsolutePath(), md5.get(i).replace("\"", ""),selectedTags.get(i).replace("\"", ""));
                }

            return ResponseEntity.ok("Upload successful");
        } catch (IOException e) {
            e.printStackTrace();
            return ResponseEntity.internalServerError().body("Upload failed: " + e.getMessage());
        }
    }
}
	