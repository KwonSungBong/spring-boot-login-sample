package com.example.demo.auth.security.service;

import com.example.demo.domain.User;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.social.connect.ConnectionKey;
import org.springframework.social.security.SocialUserDetailsService;

public interface CustomSocialAndUserDetailsService extends SocialUserDetailsService, UserDetailsService {
	User findByUserId(String userId);

	User loadUserByConnectionKey(ConnectionKey key);

	void updateUserSocial(User user);
}
