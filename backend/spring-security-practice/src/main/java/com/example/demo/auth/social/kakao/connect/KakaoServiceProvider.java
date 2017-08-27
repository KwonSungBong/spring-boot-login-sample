package com.example.demo.auth.social.kakao.connect;


import com.example.demo.auth.social.kakao.api.Kakao;
import com.example.demo.auth.social.kakao.api.impl.KakaoTemplate;
import org.springframework.social.oauth2.AbstractOAuth2ServiceProvider;

public class KakaoServiceProvider extends AbstractOAuth2ServiceProvider<Kakao> {
	public KakaoServiceProvider(String clientId) {
		super(new KakaoOAuth2Template(clientId));
	}
	
	@SuppressWarnings("deprecation")
	public Kakao getApi(String accessToken) {
		return new KakaoTemplate(accessToken);
	}
}
