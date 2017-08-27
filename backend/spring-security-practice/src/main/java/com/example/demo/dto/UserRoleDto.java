package com.example.demo.dto;

import lombok.Data;

public class UserRoleDto {

	@Data
	public static class Create {
		private String role;
	}

	@Data
	public static class Response {
		private String role;
	}
}
