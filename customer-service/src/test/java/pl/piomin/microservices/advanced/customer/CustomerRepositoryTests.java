package pl.piomin.microservices.advanced.customer;

import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.data.mongodb.test.autoconfigure.DataMongoTest;
import org.springframework.boot.testcontainers.service.connection.ServiceConnection;
import org.testcontainers.containers.MongoDBContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import pl.piomin.microservices.advanced.customer.model.Customer;
import pl.piomin.microservices.advanced.customer.model.CustomerType;
import pl.piomin.microservices.advanced.customer.repository.CustomerRepository;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assumptions.assumeTrue;

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
        assertEquals("Test1", c.getName());
        assertEquals(CustomerType.INDIVIDUAL, c.getType());
        id = c.getId();
    }

    @Test
    @Order(2)
    public void testFindCustomer() {
        assumeTrue(id != null, "testAddCustomer must have succeeded for id to be available");
        Optional<Customer> optCus = repository.findById(id);
        assertTrue(optCus.isPresent());
        assertEquals("Test1", optCus.get().getName());
        assertEquals("1234567890", optCus.get().getPesel());
    }

    @Test
    @Order(3)
    public void testFindCustomerByPesel() {
        Customer c = repository.findByPesel("1234567890");
        assertNotNull(c);
        assertNotNull(c.getId());
        assertEquals("Test1", c.getName());
    }

    @Test
    @Order(4)
    public void testFindAll() {
        List<Customer> customers = repository.findAll();
        assertNotNull(customers);
        assertFalse(customers.isEmpty());
    }

    @Test
    @Order(5)
    public void testAddAnotherCustomerType() {
        Customer c = new Customer();
        c.setName("Company1");
        c.setPesel("9876543210");
        c.setType(CustomerType.BUSINESS);
        c = repository.save(c);
        assertNotNull(c);
        assertNotNull(c.getId());
        assertEquals(CustomerType.BUSINESS, c.getType());
    }

    @Test
    @Order(6)
    public void testFindCustomerByPeselNotFound() {
        Customer c = repository.findByPesel("nonexistent");
        assertNull(c);
    }

    @Test
    @Order(7)
    public void testUpdateCustomer() {
        Customer c = repository.findByPesel("1234567890");
        assertNotNull(c);
        c.setName("UpdatedName");
        Customer updated = repository.save(c);
        assertEquals("UpdatedName", updated.getName());
        assertEquals(c.getId(), updated.getId());
    }

}
