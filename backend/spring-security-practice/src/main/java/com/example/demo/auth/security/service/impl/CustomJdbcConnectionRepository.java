package com.example.demo.auth.security.service.impl;

import com.example.demo.auth.security.service.CustomSocialAndUserDetailsService;
import org.springframework.social.connect.Connection;
import org.springframework.social.connect.ConnectionFactoryLocator;
import org.springframework.social.connect.ConnectionKey;
import org.springframework.social.connect.ConnectionRepository;
import org.springframework.util.MultiValueMap;

import java.util.List;

public class CustomJdbcConnectionRepository implements ConnectionRepository {

	private final String userId;
	private final ConnectionFactoryLocator connectionFactoryLocator;
	private final CustomSocialAndUserDetailsService userService;

	public CustomJdbcConnectionRepository(String userId, ConnectionFactoryLocator connectionFactoryLocator, CustomSocialAndUserDetailsService userService) {
		this.userId = userId;
		this.connectionFactoryLocator = connectionFactoryLocator;
		this.userService = userService;
	}

	@Override
	public MultiValueMap<String, Connection<?>> findAllConnections() {
		return null;
	}

	@Override
	public List<Connection<?>> findConnections(String providerId) {
		return null;
	}

	@Override
	public <A> List<Connection<A>> findConnections(Class<A> apiType) {
		return null;
	}

	@Override
	public MultiValueMap<String, Connection<?>> findConnectionsToUsers(MultiValueMap<String, String> providerUserIds) {
		return null;
	}

	@Override
	public Connection<?> getConnection(ConnectionKey connectionKey) {
		return null;
	}

	@Override
	public <A> Connection<A> getConnection(Class<A> apiType, String providerUserId) {
		return null;
	}

	@Override
	public <A> Connection<A> getPrimaryConnection(Class<A> apiType) {
		return null;
	}

	@Override
	public <A> Connection<A> findPrimaryConnection(Class<A> apiType) {
		return null;
	}

	@Override
	public void addConnection(Connection<?> connection) {

	}

	@Override
	public void updateConnection(Connection<?> connection) {

	}

	@Override
	public void removeConnections(String providerId) {

	}

	@Override
	public void removeConnection(ConnectionKey connectionKey) {

	}
}