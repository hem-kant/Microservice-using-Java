package com.hemkant.Accounts.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import com.hemkant.Accounts.config.AccountsServiceConfig;
import com.hemkant.Accounts.model.Accounts;
import com.hemkant.Accounts.model.Customer;
import com.hemkant.Accounts.model.Properties;
import com.hemkant.Accounts.repository.AccountsRepository;
 
 

import org.springframework.beans.factory.annotation.Autowired;

/**
 * @author Hem Kant
 *
 */

@RestController
public class AccountsController {
	
	@Autowired
	private AccountsRepository accountsRepository;
	
	@Autowired
	AccountsServiceConfig accountsConfig;
	
	@PostMapping("/myAccount")
	public Accounts getAccountDetails(@RequestBody Customer customer) {
		
		Accounts accounts = accountsRepository.findByCustomerId(customer.getCustomerId());
		if (accounts != null) {
			return accounts;
		}
		else {
			
			return null;
		}
	}
	
	@GetMapping("/account/properties")
	public String getPropertyDetails() throws JsonProcessingException {
		 ObjectWriter ow = (ObjectWriter) new ObjectMapper().writer().withDefaultPrettyPrinter();
		 Properties properties = new Properties(accountsConfig.getMsg(),
				 accountsConfig.getBuildVersion(),accountsConfig.getMailDetails(),accountsConfig.getActiveBranches());
				 	String jsonStr = ow.writeValueAsString(properties);
				 	return jsonStr;
	}
}
