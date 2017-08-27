package com.example.demo.auth.social.kakao.api.impl;

import com.example.demo.auth.social.kakao.api.KakaoTalkProfile;
import com.example.demo.auth.social.kakao.api.TalkOperation;
import org.springframework.web.client.RestTemplate;

public class TalkTemplate extends AbstractKakaoOperations implements TalkOperation {
	private final RestTemplate restTemplate;
	
	public TalkTemplate(RestTemplate restTemplate, boolean isAuthorized) {
		super(isAuthorized);
		this.restTemplate = restTemplate;
	}

	public KakaoTalkProfile getUserProfile() {
		requireAuthorization();
		return restTemplate.getForObject(buildApiUri("/v1/api/talk/profile"), KakaoTalkProfile.class);
	}
}
