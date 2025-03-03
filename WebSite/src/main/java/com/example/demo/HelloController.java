package com.example.demo;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HelloController {
	// index-Test
	@GetMapping("/index")
	public String greeting(@RequestParam(value = "name",defaultValue="no-name") String name) {
		return "<h1> Hello </h1> " + name;
	}
	
	
}
