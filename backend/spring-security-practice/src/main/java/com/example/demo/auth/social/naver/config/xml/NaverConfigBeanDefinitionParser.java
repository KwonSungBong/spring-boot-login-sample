package com.example.demo.auth.social.naver.config.xml;

import com.example.demo.auth.social.naver.config.support.NaverApiHelper;
import com.example.demo.auth.social.naver.connect.NaverConnectionFactory;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.support.BeanDefinitionBuilder;
import org.springframework.social.config.xml.AbstractProviderConfigBeanDefinitionParser;
import org.springframework.social.security.provider.SocialAuthenticationService;

import java.util.Map;

public final class NaverConfigBeanDefinitionParser extends AbstractProviderConfigBeanDefinitionParser {
	public NaverConfigBeanDefinitionParser() {
		super(NaverConnectionFactory.class, NaverApiHelper.class);
	}

	@Override
	protected Class<? extends SocialAuthenticationService<?>> getAuthenticationServiceClass() {
		return NaverConnectionFactory.NaverAuthenticationService.class;
	}

	@Override
	protected BeanDefinition getConnectionFactoryBeanDefinition(String appId, String appSecret, Map<String, Object> allAttributes) {
		BeanDefinitionBuilder builder = BeanDefinitionBuilder
				.genericBeanDefinition(NaverConnectionFactory.class)
				.addConstructorArgValue(appId);
		return builder.getBeanDefinition();
	}
}
