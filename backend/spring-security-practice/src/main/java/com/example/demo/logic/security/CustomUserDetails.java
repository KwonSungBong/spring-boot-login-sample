package com.example.demo.logic.security;


import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.social.security.SocialUserDetails;

import java.util.Collection;

public class CustomUserDetails extends User implements SocialUserDetails {

	public CustomUserDetails(String username, String password, Collection<? extends GrantedAuthority> authorities) {
		super(username, password, authorities);
	}

	public CustomUserDetails(String username, String password, boolean enabled,
												boolean accountNotExpired, boolean credentialsNonExpired, boolean accountNonLocked,
												Collection<? extends GrantedAuthority> authorities) {
		super(username, password, enabled, accountNotExpired, credentialsNonExpired, accountNonLocked, authorities);
	}

	public CustomUserDetails(String username, String password, boolean enabled,
                             boolean accountNonExpired, boolean credentialsNonExpired, boolean accountNonLocked,
                             Collection<? extends GrantedAuthority> authorities, Object user) {
		super(username, password, enabled, accountNonExpired, credentialsNonExpired, accountNonLocked, authorities);
		setUser(user);
	}

	private Object user;

	public Object getUser() {
		return user;
	}

	public void setUser(Object user) {
		this.user = user;
	}

	@Override
	public String getUserId() {
		return ((com.example.demo.domain.User) getUser()).getUniqueId().toString();
	}

}
