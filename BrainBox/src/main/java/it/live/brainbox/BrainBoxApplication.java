package it.live.brainbox;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class BrainBoxApplication {
    public static void main(String[] args) {
        SpringApplication.run(BrainBoxApplication.class, args);
    }
}
