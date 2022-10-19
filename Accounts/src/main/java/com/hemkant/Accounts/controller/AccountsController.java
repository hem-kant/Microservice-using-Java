package com.hemkant.Accounts.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RestController;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import com.hemkant.Accounts.config.AccountsServiceConfig;
import com.hemkant.Accounts.model.Accounts;
import com.hemkant.Accounts.model.Cards;
import com.hemkant.Accounts.model.Customer;
import com.hemkant.Accounts.model.CustomerDetails;
import com.hemkant.Accounts.model.Loans;
import com.hemkant.Accounts.model.Properties;
import com.hemkant.Accounts.repository.AccountsRepository;
import com.hemkant.Accounts.service.client.CardsFeignClient;
import com.hemkant.Accounts.service.client.LoansFeignClient;

import ch.qos.logback.classic.Logger;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import io.github.resilience4j.ratelimiter.annotation.RateLimiter;
import io.github.resilience4j.retry.annotation.Retry;
import io.micrometer.core.annotation.Timed;

import java.util.List;  
import org.slf4j.LoggerFactory;

/**
 * @author Hem Kant
 *
 */

@RestController
public class AccountsController {
	
	private static final Logger logger = (Logger) LoggerFactory.getLogger(AccountsController.class);
	
	@Autowired
	private AccountsRepository accountsRepository;
	
	@Autowired
	AccountsServiceConfig accountsConfig;
	
	@Autowired
	LoansFeignClient loansFeignClient;

	@Autowired
	CardsFeignClient cardsFeignClient;
	
	@PostMapping("/myAccount")
	@Timed(value = "getAccountDetails.time", description = "Time taken to return Account Details")
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
	
	@PostMapping("/myCustomerDetails")  
    @CircuitBreaker(name = "detailsForCustomerSupportApp",fallbackMethod ="myCustomerDetailsFallBack")
	@Retry(name = "retryForCustomerDetails", fallbackMethod = "myCustomerDetailsFallBack")
	public CustomerDetails myCustomerDetails(@RequestHeader("hemkant-correlation-id") String correlationid, @RequestBody Customer customer) {
		logger.info("myCustomerDetails() method started");
		Accounts accounts = accountsRepository.findByCustomerId(customer.getCustomerId());
		List<Loans> loans = loansFeignClient.getLoansDetails(correlationid,customer);
		//List<Cards> cards = cardsFeignClient.getCardDetails(customer);

		CustomerDetails customerDetails = new CustomerDetails();
		customerDetails.setAccounts(accounts);
		customerDetails.setLoans(loans);
		//customerDetails.setCards(cards);
		logger.info("myCustomerDetails() method ended");
		return customerDetails;

	}
	
	private CustomerDetails myCustomerDetailsFallBack(@RequestHeader("hemkant-correlation-id") String correlationid, Customer customer, Throwable t) {
		Accounts accounts = accountsRepository.findByCustomerId(customer.getCustomerId());
		List<Loans> loans = loansFeignClient.getLoansDetails(correlationid,customer);
		CustomerDetails customerDetails = new CustomerDetails();
		customerDetails.setAccounts(accounts);
		customerDetails.setLoans(loans);
		return customerDetails;

	}
	
	@GetMapping("/sayHello")
	@RateLimiter(name = "sayHello", fallbackMethod = "sayHelloFallback")
	public String sayHello() {
		return "Hello, Welcome to Bank!!";
	}
	private String sayHelloFallback(Throwable t) {
		return "Hi, Welcome to BankFallBack";
	}
}
