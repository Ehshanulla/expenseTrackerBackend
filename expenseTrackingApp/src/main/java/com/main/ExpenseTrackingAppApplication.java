package com.main;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.persistence.autoconfigure.EntityScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.kafka.annotation.EnableKafka;

@EnableKafka
@SpringBootApplication(scanBasePackages = "com")
@EnableJpaRepositories(basePackages = "com.repositories")
@EntityScan(basePackages = "com.entities")
public class ExpenseTrackingAppApplication {

	public static void main(String[] args) {
        SpringApplication.run(ExpenseTrackingAppApplication.class, args);
	}

}
