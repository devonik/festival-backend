package nik.dev.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.authentication.dao.AbstractUserDetailsAuthenticationProvider;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import nik.dev.model.Authentication.Admin;

public class JWTAuthenticationProvider extends AbstractUserDetailsAuthenticationProvider{
	@Autowired
	private JWTValidator validator;
	
	@Override
	public boolean supports(Class<?> aClass) {
		return (JWTAuthenticationToken.class.isAssignableFrom(aClass));
	}

	@Override
	protected void additionalAuthenticationChecks(UserDetails arg0, UsernamePasswordAuthenticationToken arg1)
			throws AuthenticationException {
		
	}

	@Override
	protected UserDetails retrieveUser(String username, UsernamePasswordAuthenticationToken passwordToken)
			throws AuthenticationException {
		
		JWTAuthenticationToken jwtAuthenticationToken = (JWTAuthenticationToken) passwordToken;
		
		String token = jwtAuthenticationToken.getToken();
		
		Admin admin = validator.validateAdmin(token);
		
		if (admin == null) {
			throw new RuntimeException("JWT Token is incorrect");
		}
		
		return new JWTUserDetails(admin.getEmail(), token);
	}
}
