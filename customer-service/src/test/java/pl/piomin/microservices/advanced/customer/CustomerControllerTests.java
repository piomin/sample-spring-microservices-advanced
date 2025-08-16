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
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.MongoDBContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import pl.piomin.microservices.advanced.customer.model.Customer;
import pl.piomin.microservices.advanced.customer.model.CustomerType;

import static io.specto.hoverfly.junit.core.SimulationSource.dsl;
import static io.specto.hoverfly.junit.dsl.HoverflyDsl.service;
import static io.specto.hoverfly.junit.dsl.ResponseCreators.success;
import static io.specto.hoverfly.junit.dsl.matchers.HoverflyMatchers.startsWith;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT,
        properties = {
                "spring.cloud.discovery.enabled=false"})
@Testcontainers
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@HoverflyCore(config = @HoverflyConfig(logLevel = LogLevel.DEBUG))
@ExtendWith(HoverflyExtension.class)
public class CustomerControllerTests {

    @Container
    static MongoDBContainer mongoDBContainer = new MongoDBContainer("mongo:8.0");

    @DynamicPropertySource
    static void registerMongoProperties(DynamicPropertyRegistry registry) {
        String uri = mongoDBContainer.getConnectionString() + "/test";
        registry.add("spring.data.mongodb.uri", () -> uri);
    }

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
        c = template.postForObject("/customers", c, Customer.class);
        assertNotNull(c, "Customer should not be null after being added");
        assertNotNull(c.getId(), "Customer ID should not be null after being added");
        id = c.getId();
    }

    @Test
    @Order(2)
    public void findCustomerWithAccounts(Hoverfly hoverfly) {
        hoverfly.simulate(
                dsl(service("http://account-service")
                        .get(startsWith("/accounts/customer"))
                        .willReturn(success("[{\"id\":\"1\",\"number\":\"1234567890\"}]", "application/json"))));

        Customer c = template.getForObject("/customers/pesel/{pesel}", Customer.class, "1234567890");
        assertNotNull(c, "Customer should not be null when fetched by PESEL");
        assertEquals("1234567890", c.getPesel(), "Customer PESEL should match");

        Customer cc = template.getForObject("/customers/{id}", Customer.class, c.getId());
        assertNotNull(cc, "Customer should not be null when fetched by ID");
        assertTrue(cc.getAccounts().size() > 0, "Customer should have associated accounts");
    }
}
