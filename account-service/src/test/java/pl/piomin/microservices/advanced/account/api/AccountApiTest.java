package pl.piomin.microservices.advanced.account.api;

import org.junit.ClassRule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.jooq.JooqAutoConfiguration.DslContextConfiguration;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.web.client.RestTemplate;

import static io.specto.hoverfly.junit.core.SimulationSource.*;
import static io.specto.hoverfly.junit.dsl.HoverflyDsl.*;
import static io.specto.hoverfly.junit.dsl.matchers.HoverflyMatchers.*;
import static io.specto.hoverfly.junit.dsl.ResponseCreators.*;

import io.specto.hoverfly.junit.api.HoverflyClient;
import io.specto.hoverfly.junit.core.SimulationSource;
import io.specto.hoverfly.junit.dsl.ResponseBuilder;
import io.specto.hoverfly.junit.dsl.ResponseCreators;
import io.specto.hoverfly.junit.dsl.matchers.HoverflyMatchers;
import io.specto.hoverfly.junit.rule.HoverflyRule;
import pl.piomin.microservices.advanced.account.Application;
import pl.piomin.microservices.advanced.account.model.Account;

//@RunWith(SpringRunner.class)
//@SpringBootTest(classes = {Application.class})
public class AccountApiTest {

	
	TestRestTemplate template = new TestRestTemplate();
	
    @ClassRule
    public static HoverflyRule hoverflyRule = HoverflyRule.inSimulationMode(dsl(
        service("http://account-service")
            .post("/accounts")
            .anyBody()
            .willReturn(success("{\"id\":\"1\"}", "application/json"))
    ));
    
    
    @Test
    public void addAccountTest() {
    	
    	Account a = new Account("1234567890", 1000, "1");
    	ResponseEntity<Account> r = template.postForEntity("http://account-service/accounts", a, Account.class);
    	System.out.println(r.getBody().getId());
    }
}
