package de.fhbielefeld.newsboard;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@ComponentScan
public class NewsboardApplication {

	public NewsboardApplication() {
		// public default constructor needed for Spring Boot.
	}

	public static void main(String[] args) {
		SpringApplication.run(NewsboardApplication.class, args);
	}
}
