package com.example.demo;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class DemoApplication {

	public static void main(String[] args) {
		SpringApplication.run(DemoApplication.class, args);
	}
}

//curl -H "Content-Type: application/json" -X POST -d {\"username\":\"lynas\",\"password\":\"123456\"} http://localhost:8888/auth
