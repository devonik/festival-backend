package nik.dev.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import nik.dev.security.JWTAuthenticationEntryPoint;
import nik.dev.security.JWTAuthenticationTokenFilter;
import nik.dev.security.JWTSuccessHandler;


@EnableGlobalMethodSecurity(prePostEnabled = true)
@EnableWebSecurity
@Configuration

public class WebSecurityConfig extends WebSecurityConfigurerAdapter{
	@Autowired
    private JWTAuthenticationEntryPoint entryPoint;

    @Bean
    public JWTAuthenticationTokenFilter authenticationTokenFilter() throws Exception {

        JWTAuthenticationTokenFilter filter = new JWTAuthenticationTokenFilter();
        filter.setAuthenticationManager(authenticationManager());
        filter.setAuthenticationSuccessHandler(new JWTSuccessHandler());
        return filter;
    }

    @Override
    public void configure(HttpSecurity http) throws Exception {
        http.csrf().disable().authorizeRequests().antMatchers("**/**").authenticated()
                .and().exceptionHandling().authenticationEntryPoint(entryPoint)
                .and().sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS);
        http.addFilterBefore(authenticationTokenFilter(), UsernamePasswordAuthenticationFilter.class);
        http.headers().cacheControl();
    }
}
