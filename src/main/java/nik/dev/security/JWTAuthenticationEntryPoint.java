package nik.dev.security;

import java.io.IOException;


import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

@Component
public class JWTAuthenticationEntryPoint implements AuthenticationEntryPoint {

	@Override
    public void commence(HttpServletRequest req, HttpServletResponse res, AuthenticationException e)
           throws IOException, ServletException {

       res.sendError(HttpServletResponse.SC_UNAUTHORIZED, "UNAUTHORIZED");

   }

}
