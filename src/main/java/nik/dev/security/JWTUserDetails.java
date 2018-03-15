package nik.dev.security;
import java.util.Collection;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

public class JWTUserDetails implements UserDetails {

	private static final long serialVersionUID = 1L;
	
	private long Id;
	private String email;
	private String token;
	
	public JWTUserDetails(String email, String token) {
		this.email= email;
		this.token = token;
	}

	@Override
	public String getPassword() {
		return null;
	}

	@Override
	public boolean isAccountNonExpired() {
		return true;
	}

	@Override
	public boolean isAccountNonLocked() {
		return true;
	}

	@Override
	public boolean isCredentialsNonExpired() {
		return true;
	}

	@Override
	public boolean isEnabled() {
		return true;
	}

	public long getId() {
		return Id;
	}

	public String getEmail() {
		return email;
	}

	public String getToken() {
		return token;
	}

	@Override
	public Collection<? extends GrantedAuthority> getAuthorities() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getUsername() {
		// TODO Auto-generated method stub
		return null;
	}
}
