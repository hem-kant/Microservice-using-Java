package com.hemkant.Accounts.model;

import java.util.List;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * @author Hem Kant
 *
 */
@Getter
@Setter
@ToString
public class CustomerDetails {
	
	private Accounts accounts;
	private List<Loans> loans;
	public Accounts getAccounts() {
		return accounts;
	}
	public void setAccounts(Accounts accounts) {
		this.accounts = accounts;
	}
	public List<Loans> getLoans() {
		return loans;
	}
	public void setLoans(List<Loans> loans) {
		this.loans = loans;
	}
	public List<Cards> getCards() {
		return cards;
	}
	public void setCards(List<Cards> cards) {
		this.cards = cards;
	}
	private List<Cards> cards;
	
	

}