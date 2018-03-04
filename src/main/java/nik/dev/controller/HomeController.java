package nik.dev.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HomeController {
	
	@RequestMapping("/api/v1/")
	public String home() {
		return "festivalticker, BACKEND!!!";
	}
}
