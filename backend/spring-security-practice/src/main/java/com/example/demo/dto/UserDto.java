package com.example.demo.dto;

import lombok.Data;
import org.joda.time.DateTime;

import java.util.List;
import java.util.UUID;

public class UserDto {

	@Data
	public static class Create {
		private String email;
		private String password;
		private String userName;
		private List<UserRoleDto.Create> userRoleList;
	}

	@Data
	public static class Response {
		private UUID uniqueId;
		private String email;
		private String userName;
		private boolean enabled;
		private DateTime createdDate;
		private DateTime lastModifiedDate;
		private List<UserRoleDto.Response> userRoleList;
	}

	@Data
	public static class Delete {
		private UUID uniqueId;
	}

	@Data
	public static class Update {
		private UUID uniqueId;
		private String userName;
		private String password;
		private boolean enabled;
		private List<UserRoleDto.Create> userRoleList;
	}

	@Data
	public static class Search {
		private String email;
		private String userName;
		private String enabled;
	}

	@Data
	public static class LoginInfo {
		private String email;
		private String password;
	}

	@Data
	public static class UpdateDetailUser {
		private String email;
		private String userName;
		private List<UserRoleDto.Response> userRoleList;
	}

	@Data
	public static class Session {
		private UUID uniqueId;
		private String email;
		private String userName;
		private long expires;
		private List<UserRoleDto.Response> userRoleList;
		private String token;
	}

	@Data
	public static class Refer {
		private String email;
		private String userName;
		private String phone;
	}

	@Data
	public static class SocialLogin {
		private String accessToken;
		private String refreshToken;
		private long expiresIn = 0;
		private boolean rememberMe = false;
	}

	@Data
	public static class Login {
		private String email;
		private String password;
		private String userName;
		private boolean rememberMe = false;
	}
}
