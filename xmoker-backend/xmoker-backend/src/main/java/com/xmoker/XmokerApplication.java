package com.xmoker;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class XmokerApplication {
	public static void main(String[] args) {
		SpringApplication.run(XmokerApplication.class, args);
	}
}
