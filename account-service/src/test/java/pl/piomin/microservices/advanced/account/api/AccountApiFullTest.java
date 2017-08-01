package pl.piomin.microservices.advanced.account.api;

import java.util.logging.Logger;

import org.junit.Assert;
import org.junit.ClassRule;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.MethodSorters;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit4.SpringRunner;

import io.specto.hoverfly.junit.core.HoverflyConfig;
import io.specto.hoverfly.junit.rule.HoverflyRule;
import pl.piomin.microservices.advanced.account.Application;
import pl.piomin.microservices.advanced.account.model.Account;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = {Application.class}, webEnvironment = WebEnvironment.DEFINED_PORT)
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class AccountApiFullTest {

	protected Logger logger = Logger.getLogger(AccountApiFullTest.class.getName());
	
	@Autowired
	TestRestTemplate template;
	
    @ClassRule
    public static HoverflyRule hoverflyRule = HoverflyRule.inCaptureOrSimulationMode("account.json", HoverflyConfig.configs().proxyLocalHost()).printSimulationData();
    
    @Test
    public void addAccountTest() {    	
    	Account a = new Account("1234567890", 1000, "1");
    	ResponseEntity<Account> r = template.postForEntity("/accounts", a, Account.class);
    	Assert.assertNotNull(r.getBody().getId());
    	logger.info("New account: " + r.getBody().getId());
    }
    
    @Test
    public void findAccountByNumberTest() {
    	Account a = template.getForObject("/accounts/number/{number}", Account.class, "1234567890");
    	Assert.assertNotNull(a);
    	logger.info("Found account: " + a.getId());
    }
    
    @Test
    public void findAccountByCustomerTest() {
    	Account[] a = template.getForObject("/accounts/customer/{customer}", Account[].class, "1");
    	Assert.assertTrue(a.length > 0);
    	logger.info("Found accounts: " + a);
    }
    
}
