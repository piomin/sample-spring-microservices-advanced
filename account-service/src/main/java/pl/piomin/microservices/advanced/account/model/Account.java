package pl.piomin.microservices.advanced.account.model;

import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "account")
public class Account {

	private Integer id;
	private String number;
	private int balance;

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getNumber() {
		return number;
	}

	public void setNumber(String number) {
		this.number = number;
	}

	public int getBalance() {
		return balance;
	}

	public void setBalance(int balance) {
		this.balance = balance;
	}

}
