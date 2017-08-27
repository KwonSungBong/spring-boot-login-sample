package com.example.demo.web;

import com.example.demo.dto.UserDto;
import com.example.demo.logic.helpers.CmmLoginHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.social.connect.Connection;
import org.springframework.social.connect.UserProfile;
import org.springframework.social.connect.web.ProviderSignInUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.request.WebRequest;

import javax.servlet.http.HttpServletRequest;

@RestController
public class AuthController {


	@Autowired
	private ProviderSignInUtils providerSignInUtils;

	@GetMapping("/me")
	public UserDto.Session me(HttpServletRequest request){
		UserDto.Session session = CmmLoginHelper.getUser();
		session.setToken(request.getSession().getId());
		return session;
	}

	@RequestMapping(value = "/signup", method = RequestMethod.GET)
	public void socialSignUp(WebRequest request){
		Connection<?> connection = providerSignInUtils.getConnectionFromSession(request);
		UserProfile userProfile = connection.fetchUserProfile();
	}

//	@RequestMapping(value = "/test", method = RequestMethod.GET)
//	public String test(WebRequest request){
//		return "test";
//	}

	@RequestMapping(value = "/test", method = RequestMethod.POST)
	public String test(WebRequest request){
		return "test";
	}
}
