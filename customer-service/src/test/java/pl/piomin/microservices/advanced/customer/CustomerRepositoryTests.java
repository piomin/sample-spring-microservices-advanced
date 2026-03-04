package pl.piomin.microservices.advanced.customer;

import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.data.mongodb.test.autoconfigure.DataMongoTest;
import org.springframework.boot.testcontainers.service.connection.ServiceConnection;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.MongoDBContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import pl.piomin.microservices.advanced.customer.model.Customer;
import pl.piomin.microservices.advanced.customer.model.CustomerType;
import pl.piomin.microservices.advanced.customer.repository.CustomerRepository;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

@DataMongoTest
@Testcontainers
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class CustomerRepositoryTests {

    @Container
    @ServiceConnection
    static MongoDBContainer mongoDBContainer = new MongoDBContainer("mongo:8.0");

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
        assertNotNull(c);
        assertNotNull(c.getId());
        id = c.getId();
    }

    @Test
    @Order(2)
    public void testFindCustomer() {
        Optional<Customer> optCus = repository.findById(id);
        assertTrue(optCus.isPresent());
    }

    @Test
    @Order(2)
    public void testFindCustomerByPesel() {
        Customer c = repository.findByPesel("1234567890");
        assertNotNull(c);
        assertNotNull(c.getId());
    }

}
