package pl.piomin.microservices.advanced.product.api;

import org.junit.Before;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.web.context.WebApplicationContext;

import com.jayway.restassured.module.mockmvc.RestAssuredMockMvc;

import pl.piomin.microservices.advanced.product.Application;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = {Application.class})
public class ApiScenario1Base {

	@Autowired
	private WebApplicationContext context;
	
	@Before
	public void setup() {
		RestAssuredMockMvc.webAppContextSetup(context);
	}

}
