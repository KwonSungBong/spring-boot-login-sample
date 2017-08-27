package com.example.demo.auth.security.service.impl;

import com.example.demo.auth.security.service.UserRepository;
import com.example.demo.domain.User;
import com.example.demo.domain.UserRole;
import com.example.demo.domain.UserSocial;
import com.example.demo.domain.code.SocialProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.social.connect.Connection;
import org.springframework.social.connect.ConnectionSignUp;
import org.springframework.social.connect.UserProfile;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

@Component
public class AutoSignUpHandler implements ConnectionSignUp {
	private final BCryptPasswordEncoder passwordEncoder;

	@Resource(name = "authUserRepository")
	private UserRepository userRepository;

	@Autowired
	public AutoSignUpHandler(BCryptPasswordEncoder passwordEncoder) {
		this.passwordEncoder = passwordEncoder;
	}

	@Transactional
	@Override
	public String execute(Connection<?> connection) {
		final User user = new User();
		final List<UserRole> userRoleList = new ArrayList<>();
		final UserRole userRole = new UserRole();
		final UserSocial userSocial = new UserSocial();

		UserProfile userProfile = connection.fetchUserProfile();

		user.setUserName(userProfile.getFirstName() + ' ' + (userProfile.getLastName() == null ? "" : userProfile.getLastName()));
		user.setEnabled(true);
		user.setPassword(passwordEncoder.encode("test@test@" + connection.getKey().getProviderId()));
		user.setEmail(connection.getKey().getProviderUserId() + "@" + connection.getKey().getProviderId() + ".tmp.com");

		userSocial.setEmail(userProfile.getEmail());
		userSocial.setDisplayName(connection.createData().getDisplayName());
		userSocial.setProviderId(SocialProvider.valueOf(connection.getKey().getProviderId()));
		userSocial.setProviderUserId(connection.getKey().getProviderUserId());
		userSocial.setAccessToken(connection.createData().getAccessToken());
		userSocial.setRefreshToken(connection.createData().getRefreshToken());
		userSocial.setImageUrl(connection.createData().getImageUrl());
		userSocial.setProfileUrl(connection.createData().getProfileUrl());
		userSocial.setSecret(connection.createData().getSecret());
		//userSocial.setUser(user);

		user.setUserSocial(userSocial);

		userRole.setRole("USER");
		userRole.setUser(user);
		userRoleList.add(userRole);

		user.setUserRoleList(userRoleList);

		userRepository.saveAndFlush(user);

		return user.getUniqueId().toString();
	}
}
