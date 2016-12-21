package de.fh_bielefeld.newsboard;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@ComponentScan
public class NewsboardApplication {

	public static void main(String[] args) {
		SpringApplication.run(NewsboardApplication.class, args);
	}
}
