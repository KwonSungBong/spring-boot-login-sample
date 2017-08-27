package com.example.demo.auth.security.filter;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.example.demo.auth.security.helpers.HttpServletRequestHelper;
import com.example.demo.dto.UserDto;
import org.springframework.security.authentication.AuthenticationServiceException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.security.web.util.matcher.RequestMatcher;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class CustomUsernamePasswordAuthenticationFilter extends UsernamePasswordAuthenticationFilter {


	private final ObjectMapper objectMapper;
	private final RequestMatcher requestMatcher;

	public CustomUsernamePasswordAuthenticationFilter(String defaultFilterProcessesUrl, ObjectMapper objectMapper) {
		super();
		this.objectMapper = objectMapper;
		setFilterProcessesUrl(defaultFilterProcessesUrl);
		this.requestMatcher = new AntPathRequestMatcher(defaultFilterProcessesUrl);
	}

	@Override
	public void doFilter(ServletRequest req, ServletResponse res, FilterChain chain) throws IOException, ServletException {
		HttpServletRequest request = (HttpServletRequest) req;

		if(requestMatcher.matches(request)){
			//			final UserDto.Login user = objectMapper.readValue(req.getInputStream(), UserDto.Login.class);
			UserDto.Login user = new UserDto.Login();
			user.setEmail(req.getParameter("email"));
			user.setPassword(req.getParameter("password"));

			HttpServletRequestHelper requestWrapper = new HttpServletRequestHelper(request);

			requestWrapper.setParameter("email", user.getEmail());
			requestWrapper.setParameter("password", user.getPassword());

			if(user.isRememberMe()){
				requestWrapper.setParameter("remember-me", "yes");
			}

			super.doFilter(requestWrapper, res, chain);
		}else{
			super.doFilter(req, res,chain);
		}
	}

	@Override
	public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws AuthenticationException {
		if (!request.getMethod().equals("POST")){
			throw new AuthenticationServiceException(
				"Authentication method not supported: " + request.getMethod());
		}

		UsernamePasswordAuthenticationToken authRequest = new UsernamePasswordAuthenticationToken(request.getParameter("email"), request.getParameter("password"));
		setDetails(request, authRequest);

		return this.getAuthenticationManager().authenticate(authRequest);
	}
}
