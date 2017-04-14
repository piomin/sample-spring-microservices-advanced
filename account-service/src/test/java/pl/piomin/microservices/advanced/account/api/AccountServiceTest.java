package pl.piomin.microservices.advanced.account.api;

import java.util.Arrays;
import java.util.logging.Logger;

import org.junit.Assert;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.MethodSorters;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.test.context.junit4.SpringRunner;

import pl.piomin.microservices.advanced.account.model.Account;
import pl.piomin.microservices.advanced.account.repository.AccountRepository;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class AccountServiceTest {

	protected Logger logger = Logger.getLogger(AccountServiceTest.class.getName());
	
	@Autowired
	AccountRepository repository;
	
	@Autowired
	TestRestTemplate template;
	
	@Test
	public void test1AddAccount() {
		Account a = new Account();
		a.setNumber("12345678909");
		a.setBalance(1232);
		a.setCustomerId("234353464576586464");
		Account r = template.postForObject("/accounts", a, Account.class);
		logger.info("Add " + r);
		Account rr = repository.findOne(r.getId());
		Assert.assertEquals(r, rr);
	}
	
	@Test
	public void test2FindAccounts() {
		Account[] accounts = template.getForObject("/accounts/customer/{customerId}", Account[].class, "234353464576586464");
		logger.info("Find: " + Arrays.asList(accounts));
	}
	
}
