package de.jonashackt.restexamples;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.PropertySource;

@SpringBootApplication
@PropertySource("classpath:git.properties")
public class RestexamplesApplication {

    public static void main(String[] args) {
        SpringApplication.run(RestexamplesApplication.class, args);
    }
}
