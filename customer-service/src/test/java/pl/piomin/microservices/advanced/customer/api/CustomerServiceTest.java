package pl.piomin.microservices.advanced.customer.api;

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
import org.springframework.cloud.contract.stubrunner.spring.AutoConfigureStubRunner;
import org.springframework.test.context.junit4.SpringRunner;

import pl.piomin.microservices.advanced.customer.model.Customer;
import pl.piomin.microservices.advanced.customer.model.CustomerType;
import pl.piomin.microservices.advanced.customer.repository.CustomerRepository;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
@AutoConfigureStubRunner(ids = {"pl.piomin:account-service:+:stubs:8080"}, workOffline = true)
public class CustomerServiceTest {

	private static final String TEST_PESEL = "12345678909";
	
	protected Logger logger = Logger.getLogger(CustomerServiceTest.class.getName());
	
	private String id;
	
	@Autowired
	CustomerRepository repository;
	@Autowired
	TestRestTemplate template;
	
	@Test
	public void test1AddCustomer() {
		Customer c = new Customer();
		c.setName("Jan Testowy");
		c.setPesel(TEST_PESEL);
		c.setType(CustomerType.INDIVIDUAL);
		Customer r = template.postForObject("/customers", c, Customer.class);
		logger.info("Add: " + r);
		id = r.getId();
		Customer rr = repository.findOne(r.getId());
		Assert.assertEquals(r, rr);
	}
	
	@Test
	public void test2FindCustomers() {
		Customer[] customers = template.getForObject("/customers", Customer[].class);
		List<Customer> l = Arrays.asList(customers);
		logger.info("Find: " + l);
		Assert.assertEquals(1, l.size());
	}
	
	@Test
	public void test3FindAndUpdateCustomer() {
		Customer c = template.getForObject("/customers/pesel/{pesel}", Customer.class, TEST_PESEL);
		Assert.assertNotNull(c);
		c.setType(CustomerType.BUSINESS);
		template.put("/customers", c);
		logger.info("Updated: " + c);
		Customer rr = repository.findOne(c.getId());
		Assert.assertEquals(CustomerType.BUSINESS, rr.getType());
	}
	
//	@Test
//	public void test4FindWithAccounts() {
//		Customer customer = template.getForObject("/customers/{id}", Customer.class, id);
//		Assert.assertNotNull(customer);
//	}
	
}
