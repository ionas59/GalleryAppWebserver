package com.example.demo;

public class DTOs {
	
    public static class ImageTags {
        public String md5;
        public String tagName;

        public ImageTags(String md5, String tagName) {
            this.md5 = md5;
            this.tagName = tagName;
        }

    }
    
    public static class Tags {
        public int tagId;
        public String name;

        public Tags(int id, String tagName) {
            this.tagId = id;
            this.name = tagName;
        }

    }
    
}
