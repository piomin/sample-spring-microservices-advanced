package pl.piomin.microservices.advanced.customer.api;

import org.junit.Before;
import org.junit.FixMethodOrder;
import org.junit.runner.RunWith;
import org.junit.runners.MethodSorters;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.cloud.contract.stubrunner.spring.AutoConfigureStubRunner;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.web.context.WebApplicationContext;

import com.jayway.restassured.module.mockmvc.RestAssuredMockMvc;

import pl.piomin.microservices.advanced.account.Application;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = {Application.class})
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
@AutoConfigureStubRunner(ids = {"pl.piomin:account-service:+:stubs:2222"}, workOffline = true)
public class ApiScenario1Base {

	@Autowired
	private WebApplicationContext context;
	
	@Before
	public void setup() {
		RestAssuredMockMvc.webAppContextSetup(context);
	}

}
