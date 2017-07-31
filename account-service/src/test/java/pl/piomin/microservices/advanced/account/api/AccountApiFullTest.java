package pl.piomin.microservices.advanced.account.api;

import static io.specto.hoverfly.junit.core.SimulationSource.dsl;
import static io.specto.hoverfly.junit.dsl.HoverflyDsl.service;
import static io.specto.hoverfly.junit.dsl.ResponseCreators.success;

import org.junit.ClassRule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.web.client.RestTemplate;

import io.specto.hoverfly.junit.rule.HoverflyRule;
import pl.piomin.microservices.advanced.account.Application;
import pl.piomin.microservices.advanced.account.model.Account;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = {Application.class}, webEnvironment = WebEnvironment.DEFINED_PORT)
public class AccountApiFullTest {

//	TestRestTemplate template = new TestRestTemplate();
	
	RestTemplate template = new RestTemplate();
	
    @ClassRule
    public static HoverflyRule hoverflyRule = HoverflyRule.inCaptureOrSimulationMode("account.json").printSimulationData();
    
    @Test
    public void addAccountTest() {    	
    	Account a = new Account("1234567890", 1000, "1");
    	ResponseEntity<Account> r = template.postForEntity("http://localhost:2222/accounts", a, Account.class);
    	System.out.println(r.getBody().getId());
    }
    
}
