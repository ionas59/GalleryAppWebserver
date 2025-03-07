package com.example.demo;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

// @Repository cuz it interacts with the db
@Repository
public class JDBCController  {

	private static final Logger log = LoggerFactory.getLogger(JDBCController.class);
	
	@Autowired
	JdbcTemplate jdbcTemplate;

	public void importIntoImageDB(String name, String path, String md5) {
		jdbcTemplate.update("INSERT INTO images(name, path, md5) VALUES (?,?,?)",name,path, md5);
	}
	
}

