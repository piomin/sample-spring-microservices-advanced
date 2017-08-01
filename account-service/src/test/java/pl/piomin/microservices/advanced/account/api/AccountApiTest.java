package pl.piomin.microservices.advanced.account.api;

import static io.specto.hoverfly.junit.core.SimulationSource.dsl;
import static io.specto.hoverfly.junit.dsl.HoverflyDsl.service;
import static io.specto.hoverfly.junit.dsl.ResponseCreators.success;

import java.util.Random;

import org.junit.Assert;
import org.junit.ClassRule;
import org.junit.Test;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.ResponseEntity;

import io.specto.hoverfly.junit.rule.HoverflyRule;
import pl.piomin.microservices.advanced.account.model.Account;


import static io.specto.hoverfly.junit.core.SimulationSource.*;
import static io.specto.hoverfly.junit.dsl.HoverflyDsl.*;
import static io.specto.hoverfly.junit.dsl.matchers.HoverflyMatchers.*;
import static io.specto.hoverfly.junit.dsl.ResponseCreators.*;

public class AccountApiTest {

	TestRestTemplate template = new TestRestTemplate();

	@ClassRule
	public static HoverflyRule hoverflyRule = HoverflyRule.inSimulationMode(dsl(service("http://account-service")
			.post("/accounts").anyBody().willReturn(success("{\"id\":\"1\"}", "application/json"))
			.get(startsWith("/accounts/")).willReturn(success("{\"id\":\"{{Request.Path.[1]}}\",\"number\":\"123456789\"}", "application/json"))));

	@Test
	public void addAccountTest() {
		Account a = new Account("1234567890", 1000, "1");
		ResponseEntity<Account> r = template.postForEntity("http://account-service/accounts", a, Account.class);
		System.out.println(r.getBody().getId());
	}
	
	@Test
	public void findAccountByIdTest() {
		Account a = template.getForObject("http://account-service/accounts/{id}", Account.class, new Random().nextInt(10));
		Assert.assertNotNull(a.getId());
	}

}
