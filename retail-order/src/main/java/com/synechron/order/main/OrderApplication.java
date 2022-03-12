package com.synechron.order.main;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

/**
 * 
 * @author darshan
 *
 */
@SpringBootApplication
@ComponentScan(basePackages = "com.synechron.*")
@EntityScan(basePackages = "com.synechron.*")
@EnableJpaRepositories(basePackages = "com.synechron.*")
public class OrderApplication {

	public static void main(String[] args) throws Exception {
		SpringApplication.run(OrderApplication.class, args);
	}

}
