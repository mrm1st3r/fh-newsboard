package de.fh_bielefeld.newsboard;

import de.fh_bielefeld.newsboard.dao.*;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class NewsboardApplication {

	@Bean("prototype")
	public AuthenticationTokenDao authenticationTokenDao() {
		return new AuthenticationTokenDaoImpl();
	}

	@Bean("prototype")
	public ClassificationDao classificationDao() {
		return new ClassificationDaoImpl();
	}

	@Bean("prototype")
	public DocumentDao documentDao() {
		return new DocumentDaoImpl();
	}

	@Bean("prototype")
	public ExternDocumentDao externDocumentDao() {
		return new ExternDocumentDaoImpl();
	}

	@Bean("prototype")
	public ExternModuleDao externModuleDao() {
		return new ExternModuleDaoImpl();
	}

	@Bean("prototype")
	public SentenceDao sentenceDao() {
		return new SentenceDaoImpl();
	}

	public static void main(String[] args) {
		SpringApplication.run(NewsboardApplication.class, args);
	}
}
