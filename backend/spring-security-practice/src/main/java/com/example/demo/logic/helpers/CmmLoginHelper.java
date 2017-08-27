package com.example.demo.logic.helpers;

import com.example.demo.domain.User;
import com.example.demo.dto.UserDto;
import com.example.demo.logic.security.CustomUserDetails;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

@Component
public class CmmLoginHelper {
	private static Environment environment;

	private static ModelMapper modelMapper;


	@Autowired
	public CmmLoginHelper(Environment environment, ModelMapper modelMapper) {
		CmmLoginHelper.environment = environment;
		CmmLoginHelper.modelMapper = modelMapper;
	}

	public static UserDto.Session getUser(){
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

		if(authentication != null){
			CustomUserDetails cud = (CustomUserDetails) authentication.getPrincipal();
			User user = (User) cud.getUser();
			return modelMapper.map(user, UserDto.Session.class);
		}
		return null;
	}
}
