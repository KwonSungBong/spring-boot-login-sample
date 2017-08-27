package com.example.demo.auth.social.naver.connect;

import com.example.demo.auth.social.naver.api.Naver;
import com.example.demo.auth.social.naver.api.NaverOAuth2ApiBinding;
import org.springframework.social.oauth2.AbstractOAuth2ServiceProvider;

public final class NaverServiceProvider extends AbstractOAuth2ServiceProvider<Naver> {
	public NaverServiceProvider(final String clientId, final String clientSecret) {
		super(new NaverOAuth2Template(clientId, clientSecret));
	}

	public Naver getApi(final String accessToken) {
		return new NaverOAuth2ApiBinding(accessToken);
	}
}
