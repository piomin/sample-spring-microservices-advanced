
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
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;

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
    static final String CUSTOMER_NAME = "Test1";
    static final String CUSTOMER_PESEL = "1234567890";

    @Autowired
    CustomerRepository repository;

    @Test
    @Order(1)
    public void testAddCustomer() {
        Customer c = new Customer();
        c.setName(CUSTOMER_NAME);
        c.setPesel(CUSTOMER_PESEL);
        c.setType(CustomerType.INDIVIDUAL);
        c = repository.save(c);
        assertNotNull(c);
        assertNotNull(c.getId());
        id = c.getId();
        assertEquals(CUSTOMER_NAME, c.getName());
        assertEquals(CUSTOMER_PESEL, c.getPesel());
        assertEquals(CustomerType.INDIVIDUAL, c.getType());
    }

    @Test
    @Order(2)
    public void testFindCustomer() {
        Optional<Customer> optCus = repository.findById(id);
        assertTrue(optCus.isPresent());
        Customer customer = optCus.get();
        assertEquals(id, customer.getId());
        assertEquals(CUSTOMER_NAME, customer.getName());
        assertEquals(CUSTOMER_PESEL, customer.getPesel());
        assertEquals(CustomerType.INDIVIDUAL, customer.getType());
    }

    @Test
    @Order(2)
    public void testFindCustomerByPesel() {
        Customer c = repository.findByPesel(CUSTOMER_PESEL);
        assertNotNull(c);
        assertNotNull(c.getId());
        assertEquals(id, c.getId());
        assertEquals(CUSTOMER_NAME, c.getName());
        assertEquals(CustomerType.INDIVIDUAL, c.getType());
    }

    @Test
    @Order(3)
    public void testUpdateCustomer() {
        Optional<Customer> optCus = repository.findById(id);
        assertTrue(optCus.isPresent());
        Customer customer = optCus.get();
        // Update name
        String newName = "Updated Name";
        customer.setName(newName);
        Customer updated = repository.save(customer);
        assertEquals(newName, updated.getName());
        assertEquals(id, updated.getId());
        assertEquals(CUSTOMER_PESEL, updated.getPesel());
        assertEquals(CustomerType.INDIVIDUAL, updated.getType());
    }

    @Test
    @Order(4)
    public void testFindAllCustomers() {
        List<Customer> customers = repository.findAll();
        assertNotNull(customers);
        assertFalse(customers.isEmpty());
        assertTrue(customers.size() >= 1);
    }

    @Test
    @Order(5)
    public void testAddCorporateCustomer() {
        Customer c = new Customer();
        c.setName("Corporate Inc.");
        c.setPesel("9876543210");
        c.setType(CustomerType.CORPORATE);
        c = repository.save(c);
        assertNotNull(c);
        assertNotNull(c.getId());
        assertEquals("Corporate Inc.", c.getName());
        assertEquals("9876543210", c.getPesel());
        assertEquals(CustomerType.CORPORATE, c.getType());
    }

    @Test
    @Order(6)
    public void testFindNonExistentCustomer() {
        String randomId = UUID.randomUUID().toString();
        Optional<Customer> optCustomer = repository.findById(randomId);
        assertFalse(optCustomer.isPresent());
        Customer nonExistentCustomer = repository.findByPesel("non-existent-pesel");
        assertEquals(null, nonExistentCustomer);
    }

}
