package com.example.demo.auth.security;


import com.example.demo.auth.security.filter.CustomSocialAuthenticationFilter;
import com.example.demo.auth.security.filter.CustomUsernamePasswordAuthenticationFilter;
import com.example.demo.auth.security.service.impl.CustomAuthProvider;
import com.example.demo.auth.social.kakao.connect.KakaoConnectionFactory;
import com.example.demo.auth.social.naver.connect.NaverConnectionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.security.SecurityProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.ObjectPostProcessor;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.security.web.authentication.RememberMeServices;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.authentication.logout.LogoutSuccessHandler;
import org.springframework.security.web.authentication.preauth.AbstractPreAuthenticatedProcessingFilter;
import org.springframework.session.security.web.authentication.SpringSessionRememberMeServices;
import org.springframework.social.connect.UsersConnectionRepository;
import org.springframework.social.facebook.connect.FacebookConnectionFactory;
import org.springframework.social.security.SocialAuthenticationFilter;
import org.springframework.social.security.SpringSocialConfigurer;

import javax.annotation.Resource;

@Configuration
@EnableWebSecurity
@Order(SecurityProperties.ACCESS_OVERRIDE_ORDER)
public class SecurityConfig extends WebSecurityConfigurerAdapter {

	@Value("${remember-me.token-validity-seconds}")
	private int TOKEN_VALIDITY_SECONDS;

	@SuppressWarnings("SpringJavaAutowiringInspection")
	@Autowired
	private AccessDeniedHandler accessDeniedHandler;

	@SuppressWarnings("SpringJavaAutowiringInspection")
	@Autowired
	private AuthenticationFailureHandler authenticationFailureHandler;

	@SuppressWarnings("SpringJavaAutowiringInspection")
	@Autowired
	private AuthenticationSuccessHandler authenticationSuccessHandler;

	@SuppressWarnings("SpringJavaAutowiringInspection")
	@Autowired
	private LogoutSuccessHandler logoutSuccessHandler;

	@SuppressWarnings("SpringJavaAutowiringInspection")
	@Autowired
	private MappingJackson2HttpMessageConverter messageConverter;

	@SuppressWarnings("SpringJavaAutowiringInspection")
	@Resource(name = "customAuthProvider")
	private CustomAuthProvider authProvider;

	@SuppressWarnings("SpringJavaAutowiringInspection")
	@Autowired
	private UsersConnectionRepository usersConnectionRepository;

	@SuppressWarnings("SpringJavaAutowiringInspection")
	@Autowired
	private KakaoConnectionFactory kakaoConnectionFactory;

	@SuppressWarnings("SpringJavaAutowiringInspection")
	@Autowired
	private NaverConnectionFactory naverConnectionFactory;

	@SuppressWarnings("SpringJavaAutowiringInspection")
	@Autowired
	private FacebookConnectionFactory facebookConnectionFactory;

	@Bean
	@Override
	public AuthenticationManager authenticationManagerBean() throws Exception {
		return super.authenticationManagerBean();
	}

	@Bean
	public RememberMeServices rememberMeServices() {
		SpringSessionRememberMeServices rememberMeServices = new SpringSessionRememberMeServices();
		return rememberMeServices;
	}

	@Autowired
	public void configureGlobal(AuthenticationManagerBuilder auth) throws Exception {
		auth
			.authenticationProvider(authProvider);
	}

	@Override
	protected void configure(HttpSecurity http) throws Exception {
		final UsernamePasswordAuthenticationFilter userPasswordAuthFilter
			= new CustomUsernamePasswordAuthenticationFilter("/login", messageConverter.getObjectMapper());

		userPasswordAuthFilter.setUsernameParameter("email");
		userPasswordAuthFilter.setPasswordParameter("password");
		userPasswordAuthFilter.setAuthenticationSuccessHandler(authenticationSuccessHandler);
		userPasswordAuthFilter.setAuthenticationFailureHandler(authenticationFailureHandler);
		userPasswordAuthFilter.setRememberMeServices(rememberMeServices());
		userPasswordAuthFilter.setAuthenticationManager(authenticationManager());

		final SpringSocialConfigurer socialConfigurer = new SpringSocialConfigurer();
		socialConfigurer.addObjectPostProcessor(new ObjectPostProcessor<SocialAuthenticationFilter>() {
			@Override
			public <O extends SocialAuthenticationFilter> O postProcess(O socialAuthenticationFilter) {
				socialAuthenticationFilter.setAuthenticationSuccessHandler(authenticationSuccessHandler);
				//socialAuthenticationFilter.setAuthenticationFailureHandler(authenticationFailureHandler);
				socialAuthenticationFilter.setRememberMeServices(rememberMeServices());
				return socialAuthenticationFilter;
			}
		});

		final CustomSocialAuthenticationFilter customSocialAuthenticationFilter
			= new CustomSocialAuthenticationFilter("/social/**", messageConverter.getObjectMapper(), authProvider.getUserDetailsService(), usersConnectionRepository, kakaoConnectionFactory, naverConnectionFactory, facebookConnectionFactory);
		customSocialAuthenticationFilter.setAuthenticationSuccessHandler(authenticationSuccessHandler);
		customSocialAuthenticationFilter.setAuthenticationFailureHandler(authenticationFailureHandler);
		customSocialAuthenticationFilter.setRememberMeServices(rememberMeServices());

		http
			.csrf().disable()
			.authorizeRequests()
			.antMatchers("/test", "/login", "/social/**", "/connect/**", "/signin").permitAll()
			.anyRequest().authenticated()
			.and()
			.logout().logoutSuccessHandler(logoutSuccessHandler)
			.and()
			.exceptionHandling().accessDeniedHandler(accessDeniedHandler).accessDeniedPage(null)
			.and()
			.rememberMe().rememberMeServices(rememberMeServices()).tokenValiditySeconds(TOKEN_VALIDITY_SECONDS)
			.and()
			.sessionManagement()
			.sessionCreationPolicy(SessionCreationPolicy.IF_REQUIRED)
			.and()
			.apply(socialConfigurer)
			.and()
			.addFilterAt(userPasswordAuthFilter, UsernamePasswordAuthenticationFilter.class)
			.addFilterBefore(customSocialAuthenticationFilter, AbstractPreAuthenticatedProcessingFilter.class);
	}

}
