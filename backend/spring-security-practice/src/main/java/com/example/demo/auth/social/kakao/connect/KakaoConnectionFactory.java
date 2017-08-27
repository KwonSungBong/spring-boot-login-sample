package com.example.demo.auth.social.kakao.connect;

import com.example.demo.auth.social.kakao.api.Kakao;
import org.springframework.social.connect.Connection;
import org.springframework.social.connect.support.OAuth2ConnectionFactory;
import org.springframework.social.oauth2.AccessGrant;

public class KakaoConnectionFactory extends OAuth2ConnectionFactory<Kakao> {
	public KakaoConnectionFactory(String clientId) {
		super("kakao", new KakaoServiceProvider(clientId), new KakaoAdapter());
	}

	@Override
	public Connection<Kakao> createConnection(AccessGrant accessGrant) {
		if(accessGrant.getRefreshToken() == null || accessGrant.getExpireTime() == null){
			Connection<Kakao> connection = super.createConnection(accessGrant);
			AccessGrant ag = new AccessGrant(accessGrant.getAccessToken(), accessGrant.getScope(), accessGrant.getRefreshToken(), connection.getApi().userOperation().accessTokenInfo().getExpiresInMillis());
			return super.createConnection(ag);
		}

		return super.createConnection(accessGrant);
	}
}
