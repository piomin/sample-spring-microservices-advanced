package pl.piomin.microservices.advanced.customer.api;

import static io.specto.hoverfly.junit.core.SimulationSource.dsl;
import static io.specto.hoverfly.junit.dsl.HoverflyDsl.service;
import static io.specto.hoverfly.junit.dsl.ResponseCreators.success;

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
import org.springframework.test.context.junit4.SpringRunner;

import io.specto.hoverfly.junit.rule.HoverflyRule;
import pl.piomin.microservices.advanced.customer.model.Customer;
import pl.piomin.microservices.advanced.customer.model.CustomerType;

import static io.specto.hoverfly.junit.core.SimulationSource.*;
import static io.specto.hoverfly.junit.dsl.HoverflyDsl.*;
import static io.specto.hoverfly.junit.dsl.matchers.HoverflyMatchers.*;
import static io.specto.hoverfly.junit.dsl.ResponseCreators.*;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = WebEnvironment.DEFINED_PORT)
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class CustomerControllerTest {

	@Autowired
	TestRestTemplate template;
	
    @ClassRule
    public static HoverflyRule hoverflyRule = HoverflyRule.inSimulationMode(dsl(
        service("account-service")
            .get(startsWith("/accounts/customer/"))
            .willReturn(success("[{\"id\":\"1\",\"number\":\"1234567890\"}]", "application/json"))
    )).printSimulationData();
    
	@Test
	public void addCustomerTest() {
		Customer c = new Customer("1234567890", "Jan Testowy", CustomerType.INDIVIDUAL);
		c = template.postForObject("/customers", c, Customer.class);
	}
	
	@Test
	public void findCustomerWithAccounts() {
		Customer c = template.getForObject("/customers/pesel/{pesel}", Customer.class, "1234567890");
		Customer cc = template.getForObject("/customers/{id}", Customer.class, c.getId());
		Assert.assertTrue(cc.getAccounts().size() > 0);
	}
}
