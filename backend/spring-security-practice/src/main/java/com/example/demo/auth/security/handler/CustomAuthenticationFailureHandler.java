package com.example.demo.auth.security.handler;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.example.demo.logic.exception.ErrorInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.http.MediaType;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationFailureHandler;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

@Component
public class CustomAuthenticationFailureHandler extends SimpleUrlAuthenticationFailureHandler {

	private final MessageSource messageSource;
	private final ObjectMapper objectMapper;

	@Autowired
	public CustomAuthenticationFailureHandler(MessageSource messageSource, MappingJackson2HttpMessageConverter messageConverter) {
		this.messageSource = messageSource;
		this.objectMapper = messageConverter.getObjectMapper();
	}

	@Override
	public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response, AuthenticationException exception) throws IOException, ServletException {
		response.setStatus(HttpServletResponse.SC_FORBIDDEN);
		response.setContentType(MediaType.APPLICATION_JSON_VALUE);
		response.setCharacterEncoding("UTF-8");

		ErrorInfo errorInfo = new ErrorInfo(messageSource.getMessage("security.auth-fail", null, request.getLocale()));

		PrintWriter pw = response.getWriter();
		pw.print(objectMapper.writeValueAsString(errorInfo));
		pw.flush();
		pw.close();
	}
}
