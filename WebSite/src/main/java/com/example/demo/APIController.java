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

@RestController
public class APIController {
	
	// relative 
    private static final String UPLOAD_DIR = "uploads"; 
      
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
    
    @PostMapping(value = "/uploadImageUris", consumes = "multipart/form-data")
    public ResponseEntity<String> uploadImages(
    		@RequestParam("images") List<MultipartFile> images,
    		@RequestParam("md5") List<String> md5 )throws NoSuchAlgorithmException {
        try {
            // create path where images will be saved
            File uploadDir = new File(System.getProperty("user.dir") + File.separator + UPLOAD_DIR);
            
            
            if (!uploadDir.exists()) {
                boolean created = uploadDir.mkdirs();
                if (!created) {
                    return ResponseEntity.internalServerError().body("Failed to create upload directory.");
                }
            }
            
            for (int i = 0; i< images.size(); i++) {
            	// replace 
            	String currentMD5 = md5.get(i).replace("\"", "");
                File file = new File(uploadDir, images.get(i).getOriginalFilename());
                images.get(i).transferTo(file);
                System.out.println("Received file: " + file.getAbsolutePath());
                
                jdbccontroller.importIntoImageDB(file.getName(), file.getAbsolutePath(), md5.get(i).replace("\"", "") );
                }

            return ResponseEntity.ok("Upload successful");
        } catch (IOException e) {
            e.printStackTrace();
            return ResponseEntity.internalServerError().body("Upload failed: " + e.getMessage());
        }
    }
}
	