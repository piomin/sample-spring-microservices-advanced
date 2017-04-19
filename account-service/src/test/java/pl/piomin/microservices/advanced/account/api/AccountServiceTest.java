package pl.piomin.microservices.advanced.account.api;

import java.util.Arrays;
import java.util.List;
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

//@RunWith(SpringRunner.class)
//@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class AccountServiceTest {

	private static final String TEST_CUSTOMER_ID = "234353464576586464";
	private static final String TEST_NUMBER = "12345678909";
	
	protected Logger logger = Logger.getLogger(AccountServiceTest.class.getName());
	
	@Autowired
	AccountRepository repository;
	@Autowired
	TestRestTemplate template;
	
//	@Test
	public void test1AddAccount() {
		Account a = new Account();
		a.setNumber(TEST_NUMBER);
		a.setBalance(1232);
		a.setCustomerId(TEST_CUSTOMER_ID);
		Account r = template.postForObject("/accounts", a, Account.class);
		logger.info("Add: " + r);
		Account rr = repository.findOne(r.getId());
		Assert.assertEquals(r, rr);
	}
	
//	@Test
	public void test2FindAccounts() {
		Account[] accounts = template.getForObject("/accounts/customer/{customerId}", Account[].class, TEST_CUSTOMER_ID);
		List<Account> l = Arrays.asList(accounts);
		logger.info("Find: " + l);
		Assert.assertEquals(1, l.size());
	}
	
//	@Test
	public void test3FindAndUpdateAccount() {
		Account a = template.getForObject("/accounts/{number}", Account.class, TEST_NUMBER);
		Assert.assertNotNull(a);
		a.setBalance(0);
		template.put("/accounts", a);
		logger.info("Updated: " + a);
		Account rr = repository.findOne(a.getId());
		Assert.assertEquals(0, rr.getBalance());
	}
	
}
