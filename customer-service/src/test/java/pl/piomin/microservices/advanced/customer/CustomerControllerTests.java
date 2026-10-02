package pl.piomin.microservices.advanced.customer;

import io.specto.hoverfly.junit.core.Hoverfly;
import io.specto.hoverfly.junit.core.config.LogLevel;
import io.specto.hoverfly.junit5.HoverflyExtension;
import io.specto.hoverfly.junit5.api.HoverflyConfig;
import io.specto.hoverfly.junit5.api.HoverflyCore;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.resttestclient.TestRestTemplate;
import org.springframework.boot.resttestclient.autoconfigure.AutoConfigureTestRestTemplate;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.testcontainers.service.connection.ServiceConnection;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.testcontainers.containers.MongoDBContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import pl.piomin.microservices.advanced.customer.model.Customer;
import pl.piomin.microservices.advanced.customer.model.CustomerType;

import java.util.List;

import static io.specto.hoverfly.junit.core.SimulationSource.dsl;
import static io.specto.hoverfly.junit.dsl.HoverflyDsl.service;
import static io.specto.hoverfly.junit.dsl.ResponseCreators.success;
import static io.specto.hoverfly.junit.dsl.matchers.HoverflyMatchers.startsWith;
import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assumptions.assumeTrue;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT,
        properties = {
                "spring.cloud.discovery.enabled=false",
                "eureka.client.enabled=false",
                "spring.cloud.openfeign.client.config.account-service.url=http://account-service"})
@Testcontainers
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@HoverflyCore(config = @HoverflyConfig(logLevel = LogLevel.DEBUG))
@ExtendWith(HoverflyExtension.class)
@AutoConfigureTestRestTemplate
public class CustomerControllerTests {

    @Container
    @ServiceConnection
    static MongoDBContainer mongoDBContainer = new MongoDBContainer("mongo:8.0");

    static String id;

    @Autowired
    TestRestTemplate template;

    @Test
    @Order(1)
    public void addCustomerTest() {
        Customer c = new Customer();
        c.setType(CustomerType.INDIVIDUAL);
        c.setPesel("1234567890");
        c.setName("Jan Testowy");
        Customer created = template.postForObject("/customers", c, Customer.class);
        assertNotNull(created);
        assertNotNull(created.getId());
        assertEquals("Jan Testowy", created.getName());
        assertEquals("1234567890", created.getPesel());
        assertEquals(CustomerType.INDIVIDUAL, created.getType());
        id = created.getId();
    }

    @Test
    @Order(2)
    public void findAllCustomersTest() {
        ResponseEntity<List> response = template.getForEntity("/customers", List.class);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertFalse(response.getBody().isEmpty());
    }

    @Test
    @Order(3)
    public void findCustomerByPeselTest() {
        Customer c = template.getForObject("/customers/pesel/{pesel}", Customer.class, "1234567890");
        assertNotNull(c);
        assertEquals("Jan Testowy", c.getName());
        assertEquals("1234567890", c.getPesel());
    }

    @Test
    @Order(4)
    public void findCustomerWithAccountsTest(Hoverfly hoverfly) {
        assumeTrue(id != null, "addCustomerTest must have succeeded for id to be available");
        hoverfly.simulate(
                dsl(service("http://account-service")
                        .get(startsWith("/accounts/customer"))
                        .willReturn(success("[{\"id\":\"1\",\"number\":\"1234567890\"}]", "application/json"))));

        Customer c = template.getForObject("/customers/pesel/{pesel}", Customer.class, "1234567890");
        assertNotNull(c);
        assertNotNull(c.getId());
        Customer cc = template.getForObject("/customers/{id}", Customer.class, c.getId());
        assertNotNull(cc);
        assertNotNull(cc.getAccounts());
        assertFalse(cc.getAccounts().isEmpty());
    }

    @Test
    @Order(5)
    public void addSecondCustomerTest() {
        Customer c = new Customer();
        c.setType(CustomerType.BUSINESS);
        c.setPesel("9876543210");
        c.setName("Firma Testowa");
        Customer created = template.postForObject("/customers", c, Customer.class);
        assertNotNull(created);
        assertNotNull(created.getId());
        assertEquals(CustomerType.BUSINESS, created.getType());
    }

}
