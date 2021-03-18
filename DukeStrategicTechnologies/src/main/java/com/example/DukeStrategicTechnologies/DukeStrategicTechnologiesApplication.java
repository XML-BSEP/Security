package com.example.DukeStrategicTechnologies;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;

@SpringBootApplication(exclude={DataSourceAutoConfiguration.class})
public class DukeStrategicTechnologiesApplication {

	public static void main(String[] args) {
		SpringApplication.run(DukeStrategicTechnologiesApplication.class, args);
	}

}
