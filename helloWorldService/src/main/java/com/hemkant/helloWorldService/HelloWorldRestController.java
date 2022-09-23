package com.hemkant.helloWorldService;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HelloWorldRestController {

	@GetMapping(value ="/hello")
	public String Hello() {
		return "Hello From Controller";
		
	}
}
