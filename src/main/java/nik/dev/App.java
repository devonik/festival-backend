package nik.dev;

import java.text.SimpleDateFormat;
import java.util.Date;

import nik.dev.service.GoabaseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import nik.dev.service.TicketPhaseAnalyzer;

@SpringBootApplication
@EnableScheduling
public class App 
{
	private static final SimpleDateFormat dateFormat = new SimpleDateFormat("HH:mm:ss");
	@Autowired
	private TicketPhaseAnalyzer ticketPhaseAnalyzer;
    @Autowired
    private GoabaseService goabaseService;
    public static void main( String[] args )
    {
        SpringApplication.run(App.class, args);
    }
    @Bean
    public BCryptPasswordEncoder bCryptPasswordEncoder() {
        return new BCryptPasswordEncoder();
    }
    /*
     *  //0 0 23 ? * *
    //Starting Job on at 10 on every day
    @Scheduled(cron="0 0 10 ? * *")
    
    Every 5 Seconds
    @Scheduled(fixedRate = 5000)
    */

    //Starting Job on at 08 on every day
    @Scheduled(cron="0 0 08 ? * *")
    public void checkGoabaseParties() {
        System.out.println("The time is now:" + dateFormat.format(new Date()));
        goabaseService.checkGoabaseParties();
    }

    //Starting Job on at 10 on every day
    @Scheduled(cron="0 0 10 ? * *")
    public void startTicketPhaseAnalyzer() {
        System.out.println("The time is now:" + dateFormat.format(new Date()));
        ticketPhaseAnalyzer.start();
    }
}
