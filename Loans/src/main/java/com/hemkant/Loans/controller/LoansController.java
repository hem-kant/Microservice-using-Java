package com.hemkant.Loans.controller;

import java.util.List;

import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;

import com.hemkant.Loans.config.LoansServiceConfig;
import com.hemkant.Loans.model.Customer;
import com.hemkant.Loans.model.Loans;
import com.hemkant.Loans.model.Properties;
import com.hemkant.Loans.repository.LoansRepository;

import ch.qos.logback.classic.Logger; 

@RestController
public class LoansController {

	private static final Logger logger = (Logger) LoggerFactory.getLogger(LoansController.class);
	
	@Autowired
	private LoansRepository loansRepository;
	
	@Autowired
	LoansServiceConfig loansConfig;
	
	@PostMapping("/myLoans")
	public List<Loans> getLoansDetails(@RequestHeader("hemkant-correlation-id") String correlationid,@RequestBody Customer customer) {
		System.out.println("Invocking Loans MS");
		logger.info("getLoansDetails() method started");
		List<Loans> loans = loansRepository.findByCustomerIdOrderByStartDtDesc(customer.getCustomerId());
		logger.info("getLoansDetails() method started");
		if (loans != null) {
			return loans;
		} else {
			return null;
		}

	}
	
	@GetMapping("/loans/properties")
	public String getPropertyDetails() throws JsonProcessingException {
		ObjectWriter ow = new ObjectMapper().writer().withDefaultPrettyPrinter();
		Properties properties = new Properties(loansConfig.getMsg(), loansConfig.getBuildVersion(),
				loansConfig.getMailDetails(), loansConfig.getActiveBranches());
		String jsonStr = ow.writeValueAsString(properties);
		return jsonStr;
	}
}
