package nik.dev.model.Authentication;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

public class UserJWTGenerator {
	private static final int EXPIRATION = 20;
	
	public String generate(LoginDto user) {

        Claims claims = Jwts.claims().setSubject(user.getUsername());
        claims.put("username", String.valueOf(user.getUsername()));

        return Jwts.builder()
                .setClaims(claims)
                .signWith(SignatureAlgorithm.HS512, "${jwt.secret}")
                .compact();

    }
	public Integer getExpiresMinute() {
		return EXPIRATION;
	}
}
