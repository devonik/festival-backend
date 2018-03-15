package nik.dev.security;
import java.io.IOException;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AbstractAuthenticationProcessingFilter;

public class JWTAuthenticationTokenFilter extends AbstractAuthenticationProcessingFilter {
	
	public JWTAuthenticationTokenFilter() {
		super("/rest/**");
	}

	@Override
	public Authentication attemptAuthentication(HttpServletRequest req, HttpServletResponse res)
			throws AuthenticationException, IOException, ServletException {
		
		String header = req.getHeader("Authorisation");
		
		if (header == null || !header.startsWith("Token ")) {
			throw new RuntimeException("JWT Token is missing");
		}
		
		String authenticationToken = header.substring(6);
		
		JWTAuthenticationToken token = new JWTAuthenticationToken(authenticationToken);
		
		return getAuthenticationManager().authenticate(token);
	}
	
	@Override
	protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response, FilterChain chain,
			Authentication authResult) throws IOException, ServletException {
		super.successfulAuthentication(request, response, chain, authResult);
		chain.doFilter(request, response);
	}

}