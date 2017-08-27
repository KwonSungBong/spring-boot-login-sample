package com.example.demo.auth.security.handler;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.example.demo.dto.UserDto;
import com.example.demo.logic.helpers.CmmLoginHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

@Component
public class CustomAuthenticationSuccessHandler extends SimpleUrlAuthenticationSuccessHandler {

	private final ObjectMapper objectMapper;

	@Autowired
	public CustomAuthenticationSuccessHandler(MappingJackson2HttpMessageConverter messageConverter) {
		this.objectMapper = messageConverter.getObjectMapper();
	}

	@Override
	public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {
		response.setStatus(HttpServletResponse.SC_OK);
		response.setContentType(MediaType.APPLICATION_JSON_VALUE);
		response.setCharacterEncoding("UTF-8");

		System.out.println(request.getSession().getMaxInactiveInterval());

		UserDto.Session sessionUser = CmmLoginHelper.getUser();
		sessionUser.setToken(request.getSession().getId());

		PrintWriter pw = response.getWriter();
		pw.print(objectMapper.writeValueAsString(sessionUser));
		pw.flush();
		pw.close();
	}
}
