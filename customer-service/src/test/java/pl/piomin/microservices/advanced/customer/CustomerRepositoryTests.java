package pl.piomin.microservices.advanced.customer;

import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.mongo.DataMongoTest;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.MongoDBContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import pl.piomin.microservices.advanced.customer.model.Customer;
import pl.piomin.microservices.advanced.customer.model.CustomerType;
import pl.piomin.microservices.advanced.customer.repository.CustomerRepository;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@DataMongoTest
@Testcontainers
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class CustomerRepositoryTests {

    @Container
    static MongoDBContainer mongoDBContainer = new MongoDBContainer("mongo:8.0");

    @DynamicPropertySource
    static void registerMongoProperties(DynamicPropertyRegistry registry) {
        String uri = mongoDBContainer.getConnectionString() + "/test";
        registry.add("spring.data.mongodb.uri", () -> uri);
    }

    static String id;

    @Autowired
    CustomerRepository repository;

    @Test
    @Order(1)
    public void testAddCustomer() {
        Customer c = new Customer();
        c.setName("Test1");
        c.setPesel("1234567890");
        c.setType(CustomerType.INDIVIDUAL);
        c = repository.save(c);
        assertNotNull(c, "Customer should not be null after saving");
        assertNotNull(c.getId(), "Customer ID should not be null after saving");
        id = c.getId();
    }

    @Test
    @Order(2)
    public void testFindCustomer() {
        Optional<Customer> optCus = repository.findById(id);
        assertTrue(optCus.isPresent(), "Customer should be found by ID");
        assertEquals("Test1", optCus.get().getName(), "Customer name should match");
    }

    @Test
    @Order(3)
    public void testFindCustomerByPesel() {
        Customer c = repository.findByPesel("1234567890");
        assertNotNull(c, "Customer should not be null when found by PESEL");
        assertEquals("1234567890", c.getPesel(), "Customer PESEL should match");
    }

    @Test
    @Order(4)
    public void testFindNonExistentCustomer() {
        Optional<Customer> optCus = repository.findById("non-existent-id");
        assertFalse(optCus.isPresent(), "Customer should not be found for a non-existent ID");
    }
}
