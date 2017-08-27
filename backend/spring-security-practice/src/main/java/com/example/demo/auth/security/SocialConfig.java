package com.example.demo.auth.security;

import com.example.demo.auth.security.service.CustomSocialAndUserDetailsService;
import com.example.demo.auth.security.service.impl.CustomSocialUsersConnectionRepository;
import com.example.demo.auth.social.kakao.connect.KakaoConnectionFactory;
import com.example.demo.auth.social.naver.connect.NaverConnectionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.social.UserIdSource;
import org.springframework.social.config.annotation.ConnectionFactoryConfigurer;
import org.springframework.social.config.annotation.EnableSocial;
import org.springframework.social.config.annotation.SocialConfigurer;
import org.springframework.social.connect.ConnectionFactoryLocator;
import org.springframework.social.connect.ConnectionSignUp;
import org.springframework.social.connect.UsersConnectionRepository;
import org.springframework.social.connect.web.ProviderSignInUtils;
import org.springframework.social.facebook.connect.FacebookConnectionFactory;
import org.springframework.social.security.AuthenticationNameUserIdSource;

import javax.annotation.Resource;

@Configuration
@EnableSocial
public class SocialConfig implements SocialConfigurer {

	@SuppressWarnings("SpringJavaAutowiringInspection")
	@Resource(name = "customSocialAndUserDetailService")
	private CustomSocialAndUserDetailsService customSocialAndUserDetailsService;

	@SuppressWarnings("SpringJavaAutowiringInspection")
	@Autowired
	private ConnectionSignUp autoSignUpHandler;

	@Value("${spring.social.kakao.app-id}")
	private String kakaoAppId;

	@Value("${spring.social.naver.app-id}")
	private String naverAppId;

	@Value("${spring.social.naver.app-secret}")
	private String naverAppSecret;

	@Value("${spring.social.facebook.app-id}")
	private String facebookAppId;

	@Value("${spring.social.facebook.app-secret}")
	private String facebookSecret;

	@Bean(name = "kakaoConnectionFactory")
	public KakaoConnectionFactory kakaoConnectionFactory(){
		return new KakaoConnectionFactory(kakaoAppId);
	}

	@Bean(name = "naverConnectionFactory")
	public NaverConnectionFactory naverConnectionFactory() { return new NaverConnectionFactory(naverAppId, naverAppSecret); }

	@Bean(name = "facebookConnectionFactory")
	public FacebookConnectionFactory facebookConnectionFactory() { return new FacebookConnectionFactory(facebookAppId, facebookSecret); }

	@Override
	public void addConnectionFactories(ConnectionFactoryConfigurer connectionFactoryConfigurer, Environment environment) {
		connectionFactoryConfigurer.addConnectionFactory(
			kakaoConnectionFactory()
		);
		connectionFactoryConfigurer.addConnectionFactory(
			naverConnectionFactory()
		);
	}

	@Override
	public UserIdSource getUserIdSource() {
		return new AuthenticationNameUserIdSource();
	}

	@Override
	public UsersConnectionRepository getUsersConnectionRepository(ConnectionFactoryLocator connectionFactoryLocator) {
		CustomSocialUsersConnectionRepository usersConnectionRepository =
			new CustomSocialUsersConnectionRepository(customSocialAndUserDetailsService, connectionFactoryLocator);
		usersConnectionRepository.setConnectionSignUp(autoSignUpHandler);
		return usersConnectionRepository;
	}

	@Bean
	public ProviderSignInUtils providerSignInUtils(ConnectionFactoryLocator connectionFactoryLocator, UsersConnectionRepository usersConnectionRepository) {
		return new ProviderSignInUtils(connectionFactoryLocator, usersConnectionRepository);
	}

}
