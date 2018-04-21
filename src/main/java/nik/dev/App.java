package nik.dev;

import java.util.TimeZone;

import javax.annotation.PostConstruct;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

@SpringBootApplication
public class App 
{
	@PostConstruct
	  void started() {
	    TimeZone.setDefault(TimeZone.getTimeZone("CEST"));
	}
    public static void main( String[] args )
    {
        SpringApplication.run(App.class, args);
    }
    @Bean
    public BCryptPasswordEncoder bCryptPasswordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
