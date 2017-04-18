package pl.piomin.microservices.advanced.account.api;

import org.junit.Before;

import com.jayway.restassured.module.mockmvc.RestAssuredMockMvc;

public class MvcTest {

	@Before
	public void setup() {
		RestAssuredMockMvc.standaloneSetup(new AccountController());
	}

}
