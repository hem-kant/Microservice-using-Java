package com.hemkant.Accounts;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.ComponentScans;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

/**
 * @author Hem Kant
 *
 */

@SpringBootApplication
@RefreshScope
@ComponentScans({ @ComponentScan("com.hemkant.Accounts.controller")})
@EnableJpaRepositories("com.hemkant.Accounts.repository")
@EntityScan("com.hemkant.Accounts.model")
public class AccountsApplication {

	public static void main(String[] args) {
		SpringApplication.run(AccountsApplication.class, args);
	}

}