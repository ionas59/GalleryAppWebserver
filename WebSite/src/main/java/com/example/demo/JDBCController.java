package com.example.demo;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.stereotype.Repository;

import com.example.demo.DTOs.Tags;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import javax.swing.tree.RowMapper;

// @Repository cuz it interacts with the db
@Repository
public class JDBCController  {

	private static final Logger log = LoggerFactory.getLogger(JDBCController.class);
	
	@Autowired
	JdbcTemplate jdbcTemplate;

	public void createTag(String name) {
		String sql = "INSERT INTO tags(name) VALUES(?)";
		
		try {
			jdbcTemplate.update(sql,name);
		
		} catch (DataAccessException e) {
			System.out.println(e);
		}
	}
	
	public void importIntoImageTable(String name, String path, String md5) {
		String sql3 = "INSERT INTO images(name,md5,path) VALUES(?,?,?)";
		jdbcTemplate.update(sql3,name,md5,path);
	}
	
	public void importIntoImageTagsTable(int imageId, int tagId) {
		String sql3 = "INSERT INTO image_tags(imageId,tagId) VALUES(?,?)";
		if(imageId > 0 && tagId > 0) {
			jdbcTemplate.update(sql3,imageId,tagId);
		} 
		
	}
	
	public void importIntoImageDB(String name, String path, String md5, String tag) {
		String sql = "SELECT imageId FROM images WHERE md5 =?";
		String sql2 = "SELECT tagId FROM tags WHERE name =?";
		
		try {
			
			importIntoImageTable(name,path,md5);
			
			//queryForObject return only a single row
			int imageId = jdbcTemplate.queryForObject(sql, (rs, rowNum) -> rs.getInt("imageId"), md5);
			int tagId = jdbcTemplate.queryForObject(sql2, (rs, rowNum) -> rs.getInt("tagId"), tag);
			System.out.println("ImageId:" + imageId);
			System.out.println("TagId:" + tagId);
			importIntoImageTagsTable(imageId, tagId);
			
		} catch (DataAccessException e) {
			System.out.println(e);
		}
		
		
	    		
	}
	
	public List<String> getAllMD5() {
		List<String> allMD5;
		String sql = "SELECT md5 FROM images";
		allMD5 = jdbcTemplate.query(sql, (rs, rowNum) -> rs.getString(1));
		return allMD5;
	}

	public List<DTOs.ImageTags> getImageTags() {
		List<DTOs.ImageTags> allImageTags;
		String sql = "SELECT images.md5, tags.name AS tagName FROM images \r\n"
				+ "INNER JOIN image_tags ON images.imageId = image_tags.imageId\r\n"
				+ "INNER JOIN tags ON tags.tagId = image_tags.tagId;";
		allImageTags = jdbcTemplate.query(sql, (rs, rowNum) -> new DTOs.ImageTags(rs.getString(1),rs.getString(2)));
		for(DTOs.ImageTags d : allImageTags)  {
			System.out.println();
			System.out.println(d.md5);
			System.out.println(d.tagName);
		}
		return allImageTags;
	}

	public List<Tags> getTags() {
		List<DTOs.Tags> allTags;
		String sql = "SELECT tagId, name FROM tags";
				
		allTags = jdbcTemplate.query(sql, (rs, rowNum) -> new DTOs.Tags(rs.getInt(1),rs.getString(2)));
		for(DTOs.Tags d : allTags)  {
			System.out.println();
			System.out.println(d.tagId);
			System.out.println(d.name);
		}
		return allTags;
	}
	
	
	
}

