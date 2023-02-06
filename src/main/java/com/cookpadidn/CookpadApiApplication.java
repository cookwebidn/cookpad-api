package com.cookpadidn;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication()
@EnableJpaAuditing
@EnableJpaRepositories(basePackages = "com.cookpadidn.repository")
public class CookpadApiApplication {

	public static void main(String[] args) {
		SpringApplication.run(CookpadApiApplication.class, args);
	}

}
