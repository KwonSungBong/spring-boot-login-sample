package com.example.demo.auth.security.filter;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.example.demo.auth.security.helpers.HttpServletRequestHelper;
import com.example.demo.auth.security.service.CustomSocialAndUserDetailsService;
import com.example.demo.auth.social.kakao.connect.KakaoConnectionFactory;
import com.example.demo.auth.social.naver.connect.NaverConnectionFactory;
import com.example.demo.domain.code.SocialProvider;
import com.example.demo.dto.UserDto;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationServiceException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AbstractAuthenticationProcessingFilter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.security.web.util.matcher.RequestMatcher;
import org.springframework.social.connect.Connection;
import org.springframework.social.connect.UsersConnectionRepository;
import org.springframework.social.facebook.connect.FacebookConnectionFactory;
import org.springframework.social.oauth2.AccessGrant;
import org.springframework.social.security.SocialAuthenticationProvider;
import org.springframework.social.security.SocialAuthenticationToken;
import org.springframework.util.StringUtils;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Slf4j
public class CustomSocialAuthenticationFilter extends AbstractAuthenticationProcessingFilter {
	private final String FILTER_PROCESS_URL;
	private final RequestMatcher requestMatcher;
	private final ObjectMapper objectMapper;
	private final CustomSocialAndUserDetailsService userDetailsService;
	private final UsersConnectionRepository usersConnectionRepository;

	private final KakaoConnectionFactory kakaoConnectionFactory;
	private final NaverConnectionFactory naverConnectionFactory;
	private final FacebookConnectionFactory facebookConnectionFactory;

	public CustomSocialAuthenticationFilter(String defaultFilterProcessesUrl, ObjectMapper objectMapper, CustomSocialAndUserDetailsService userDetailsService, UsersConnectionRepository usersConnectionRepository, KakaoConnectionFactory kakaoConnectionFactory, NaverConnectionFactory naverConnectionFactory, FacebookConnectionFactory facebookConnectionFactory) {
		super(defaultFilterProcessesUrl);
		this.FILTER_PROCESS_URL = defaultFilterProcessesUrl;
		this.requestMatcher = new AntPathRequestMatcher(defaultFilterProcessesUrl);
		this.objectMapper = objectMapper;
		this.userDetailsService = userDetailsService;
		this.usersConnectionRepository = usersConnectionRepository;
		this.kakaoConnectionFactory = kakaoConnectionFactory;
		this.naverConnectionFactory = naverConnectionFactory;
		this.facebookConnectionFactory = facebookConnectionFactory;
	}

	@Override
	public void doFilter(ServletRequest req, ServletResponse res, FilterChain chain) throws IOException, ServletException {
		HttpServletRequest request = (HttpServletRequest) req;

		if(requestMatcher.matches(request)){
			final UserDto.SocialLogin socialLogin = objectMapper.readValue(req.getInputStream(), UserDto.SocialLogin.class);
			log.debug("Social Param : {}", socialLogin);

			HttpServletRequestHelper requestWrapper = new HttpServletRequestHelper(request);

			String uri = request.getRequestURI();
			int pathParamIndex = uri.indexOf(';');

			if (pathParamIndex > 0) {
				// strip everything after the first semi-colon
				uri = uri.substring(0, pathParamIndex);
			}

			// uri must start with context path
			uri = uri.substring(request.getContextPath().length());

			String filterProcessUrl = FILTER_PROCESS_URL.replace("/**", "").replace("/*", "").replace("*", "");

			log.debug("filterProcessUrl : {}", filterProcessUrl);

			uri = uri.substring(filterProcessUrl.length());
			log.debug("uri : {}", uri);

			if(uri.startsWith("/")){
				requestWrapper.setParameter("providerId", uri.substring(1));
			}

			if(socialLogin.getExpiresIn() != 0){
				requestWrapper.setParameter("expiresIn", String.valueOf(socialLogin.getExpiresIn()));
			}

			requestWrapper.setParameter("accessToken", socialLogin.getAccessToken());
			requestWrapper.setParameter("refreshToken", socialLogin.getRefreshToken());

			if(socialLogin.isRememberMe()){
				requestWrapper.setParameter("remember-me", "yes");
			}
			super.doFilter(requestWrapper, res, chain);
		}else{
			super.doFilter(req, res, chain);
		}
	}

	@Override
	public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws AuthenticationException, IOException, ServletException {
		if (!request.getMethod().equals("POST")){
			throw new AuthenticationServiceException(
				"Authentication method not supported: " + request.getMethod());
		}

		Long expiresIn = StringUtils.isEmpty(request.getParameter("expiresIn")) ? null : Long.valueOf(request.getParameter("expiresIn"));

		AccessGrant accessGrant = new AccessGrant(request.getParameter("accessToken"), null, request.getParameter("refreshToken"), expiresIn);

		Connection connect;

		log.debug("System currentTimeMillis : {}", System.currentTimeMillis());
		log.debug("expiresIn : {}", expiresIn);

		String providerId = request.getParameter("providerId");
		switch (SocialProvider.valueOf(providerId)){
			case naver:
				connect = naverConnectionFactory.createConnection(accessGrant);
				break;
			case facebook:
				connect = facebookConnectionFactory.createConnection(accessGrant);
				break;
			case kakao:
			default:
				connect = kakaoConnectionFactory.createConnection(accessGrant);
		}

		SocialAuthenticationToken result = new SocialAuthenticationToken(connect, null);

		return new SocialAuthenticationProvider(usersConnectionRepository, userDetailsService).authenticate(result);
	}
}