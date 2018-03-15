package nik.dev.security;

import org.springframework.stereotype.Component;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import nik.dev.model.Authentication.Admin;

@Component
public class JWTValidator {
	
	public Admin validateAdmin(String token) {
		
		Admin admin = new Admin();
		
		
		try {
			Claims payload = Jwts.parser().setSigningKey("${jwt.secret}").parseClaimsJws(token).getBody();
			admin.setEmail((String) payload.get("email"));
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return admin;
		
	}
}
