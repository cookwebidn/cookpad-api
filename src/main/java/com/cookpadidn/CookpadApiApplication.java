package com.cookpadidn;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;

@SpringBootApplication(exclude = DataSourceAutoConfiguration.class)
public class CookpadApiApplication {

	public static void main(String[] args) {
		SpringApplication.run(CookpadApiApplication.class, args);
	}

}
