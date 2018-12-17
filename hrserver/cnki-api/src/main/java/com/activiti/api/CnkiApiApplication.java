package com.activiti.api;

import org.activiti.spring.boot.SecurityAutoConfiguration;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication(exclude = SecurityAutoConfiguration.class)
@ComponentScan("com.activiti.api.domain")
public class CnkiApiApplication {

	public static void main(String[] args) {
		SpringApplication.run(CnkiApiApplication.class, args);
	}

}

