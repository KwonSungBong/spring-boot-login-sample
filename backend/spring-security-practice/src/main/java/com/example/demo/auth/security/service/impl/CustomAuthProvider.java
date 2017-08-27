package com.example.demo.auth.security.service.impl;

import com.example.demo.auth.security.service.CustomSocialAndUserDetailsService;
import com.example.demo.auth.security.service.UserRepository;
import com.example.demo.domain.User;
import com.example.demo.logic.security.CustomUserDetails;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Arrays;
import java.util.List;

@Slf4j
@Service("customAuthProvider")
public class CustomAuthProvider implements AuthenticationProvider {


	@Autowired
	private PasswordEncoder passwordEncoder;

	@Autowired
	private CustomSocialAndUserDetailsService userDetailsService;

	@Resource(name = "authUserRepository")
	private UserRepository userRepository;

	@Override
	public Authentication authenticate(Authentication authentication) throws AuthenticationException {
		String email = (String) authentication.getPrincipal();
		String password = (String) authentication.getCredentials();
		log.debug("사용자가 입력한 로그인 정보 입니다. {}", Arrays.asList(email, passwordEncoder.encode(password)));

		List<User> userList = userRepository.findByEmailAndEnabled(email, true);

		if(userList.size() == 0){
			throw new BadCredentialsException("사용자 정보가 없습니다.");
		}

		User user = userList.get(0);

//		현재 평문으로 저장되어 있음
		if(!passwordEncoder.matches(password, user.getPassword())){
			throw new BadCredentialsException("사용자 정보가 없습니다.");
		}

		log.debug("최초 권한 : {}", user.getUserRoleList().toString());
		CustomUserDetails cud = (CustomUserDetails) userDetailsService.loadUserByUserId(user.getUniqueId().toString());

		log.debug("이메일 주소 : {}", user.getEmail());
		log.debug("비밀번호 : {}", user.getPassword());
		log.debug("이름 : {}", user.getUserName());
		log.debug("권한 : {}", user.getUserRoleList().toString());

		UsernamePasswordAuthenticationToken result = new UsernamePasswordAuthenticationToken(cud, user.getUniqueId().toString(), cud.getAuthorities());

		result.setDetails(authentication.getDetails());

		return result;
	}

	@Override
	public boolean supports(Class<?> authentication) {
		return authentication.equals(UsernamePasswordAuthenticationToken.class);
	}

	public CustomSocialAndUserDetailsService getUserDetailsService(){
		return this.userDetailsService;
	}
}
