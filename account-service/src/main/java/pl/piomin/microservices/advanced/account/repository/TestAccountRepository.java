package pl.piomin.microservices.advanced.account.repository;

import java.util.List;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Repository;

import pl.piomin.microservices.advanced.account.model.Account;

@Repository
@ConfigurationProperties(prefix = "test")
public class TestAccountRepository {

	private List<Account> accounts;

	public List<Account> getAccounts() {
		return accounts;
	}

	public void setAccounts(List<Account> accounts) {
		this.accounts = accounts;
	}
	
	public Account findByNumber(String number) {
		return accounts.stream().filter(it -> it.getNumber().equals(number)).findFirst().get();
	}
	
}
