package com.example.demo.auth.security.service.impl;

import com.example.demo.auth.security.service.CustomSocialAndUserDetailsService;
import com.example.demo.auth.security.service.UserRepository;
import com.example.demo.domain.User;
import com.example.demo.domain.code.SocialProvider;
import com.example.demo.logic.security.CustomUserDetails;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.social.connect.ConnectionKey;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service("customSocialAndUserDetailService")
public class CustomSocialAndUserDetailsServiceImpl implements CustomSocialAndUserDetailsService {

	@Resource(name = "authUserRepository")
	private UserRepository userRepository;

	@Transactional(readOnly = true)
	@Override
	public CustomUserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		User user = userRepository.findOne(UUID.fromString(username));

		if(user == null){
			throw new UsernameNotFoundException("사용자가 존재하지 않습니다.");
		}

		List<GrantedAuthority> authorities = new ArrayList<>();
		user.getUserRoleList().forEach(
				role -> authorities.add(new SimpleGrantedAuthority(role.getRole()))
		);

		return new CustomUserDetails(
				user.getUniqueId().toString(),
				user.getUniqueId().toString(),
				user.isEnabled(),
				user.isEnabled(),
				user.isEnabled(),
				user.isEnabled(),
				authorities,
				user
		);
	}

	@Override
	public CustomUserDetails loadUserByUserId(String userId) throws UsernameNotFoundException {
		return loadUserByUsername(userId);
	}

	@Transactional(readOnly = true)
	@Override
	public User findByUserId(String userId) {
		return userRepository.findOne(UUID.fromString(userId));
	}

	@Transactional(readOnly = true)
	@Override
	public User loadUserByConnectionKey(ConnectionKey connectionKey) {
		User user = userRepository.findByUserSocial_ProviderIdAndUserSocial_ProviderUserId(SocialProvider.valueOf(connectionKey.getProviderId()), connectionKey.getProviderUserId());
		return checkUser(user);
	}

	@Transactional
	@Override
	public void updateUserSocial(User user) {
		userRepository.saveAndFlush(user);
	}

	private User checkUser(User user){
		if(user == null)
			throw new UsernameNotFoundException("");
		return user;
	}
}
