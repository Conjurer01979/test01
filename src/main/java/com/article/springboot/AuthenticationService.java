package com.article.springboot;

import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.AuthorityUtils;

import jakarta.servlet.http.HttpServletRequest;

public class AuthenticationService {

	public static final String AUTH_TOKEN_HEADER_NAME = "API-KEY";
	public static final String AUTH_USER = "user";
	public static final String AUTH_ADMIN = "admin";

	public static Authentication getAuthentication(HttpServletRequest request) {
		String apiKey = request.getHeader(AUTH_TOKEN_HEADER_NAME);
		if (apiKey == null || (!apiKey.equals(AUTH_USER) && !apiKey.equals(AUTH_ADMIN))) {
			throw new BadCredentialsException("Invalid API Key");
		}
		String requestedService = request.getServletPath();
		if (requestedService.equals("/articlecount") && !apiKey.equals(AUTH_ADMIN)) {
			throw new BadCredentialsException("Access restricted");
		}

		return new ApiKeyAuthentication(apiKey, AuthorityUtils.NO_AUTHORITIES);
	}
}
