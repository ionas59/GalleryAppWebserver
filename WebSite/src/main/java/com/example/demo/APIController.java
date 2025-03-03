package com.example.demo;


import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.List;

@RestController
public class APIController {

	// relative 
    private static final String UPLOAD_DIR = "uploads"; 

    @PostMapping(value = "/uploadImageUris", consumes = "multipart/form-data")
    public ResponseEntity<String> uploadImages(@RequestParam("images") List<MultipartFile> images) {
        try {
            // create path where images will be saved
            File uploadDir = new File(System.getProperty("user.dir") + File.separator + UPLOAD_DIR);
            
            
            if (!uploadDir.exists()) {
                boolean created = uploadDir.mkdirs();
                if (!created) {
                    return ResponseEntity.internalServerError().body("Failed to create upload directory.");
                }
            }

            
            for (MultipartFile image : images) {
                File file = new File(uploadDir, image.getOriginalFilename());
                image.transferTo(file);
                System.out.println("Received file: " + file.getAbsolutePath());
            }

            return ResponseEntity.ok("Upload successful");
        } catch (IOException e) {
            e.printStackTrace();
            return ResponseEntity.internalServerError().body("Upload failed: " + e.getMessage());
        }
    }
}
