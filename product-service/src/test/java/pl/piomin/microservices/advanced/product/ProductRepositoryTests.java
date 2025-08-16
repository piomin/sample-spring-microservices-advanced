
package pl.piomin.microservices.advanced.product;

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
import pl.piomin.microservices.advanced.product.model.Product;
import pl.piomin.microservices.advanced.product.model.ProductType;
import pl.piomin.microservices.advanced.product.repository.ProductRepository;

import java.time.LocalDate;
import java.util.Optional;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNull;

@DataMongoTest
@Testcontainers
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class ProductRepositoryTests {

    @Container
    static MongoDBContainer mongoDBContainer = new MongoDBContainer("mongo:8.0");

    @DynamicPropertySource
    static void registerMongoProperties(DynamicPropertyRegistry registry) {
        String uri = mongoDBContainer.getConnectionString() + "/test";
        registry.add("spring.data.mongodb.uri", () -> uri);
    }

    static String id;

    static final String ACCOUNT_ID       = "123";
    static final String CUSTOMER_ID      = "123";
    static final int    INITIAL_BALANCE  = 10000;

    @Autowired
    ProductRepository repository;

    @Test
    @Order(1)
    public void testAddProduct() {
        Product p = new Product();
        p.setAccountId(ACCOUNT_ID);
        p.setCustomerId(CUSTOMER_ID);
        p.setType(ProductType.CREDIT);
        p.setBalance(INITIAL_BALANCE);
        p.setDateOfStart(LocalDate.now());
        p = repository.save(p);
        assertNotNull(p);
        assertNotNull(p.getId());
        assertEquals(ACCOUNT_ID, p.getAccountId());
        assertEquals(CUSTOMER_ID, p.getCustomerId());
        assertEquals(ProductType.CREDIT, p.getType());
        assertEquals(INITIAL_BALANCE, p.getBalance());
        assertNotNull(p.getDateOfStart());
        id = p.getId();
    }

    @Test
    @Order(2)
    public void testFindProduct() {
        Optional<Product> optCus = repository.findById(id);
        assertTrue(optCus.isPresent());
        Product product = optCus.get();
        assertEquals(id, product.getId());
        assertEquals(ACCOUNT_ID, product.getAccountId());
        assertEquals(CUSTOMER_ID, product.getCustomerId());
        assertEquals(ProductType.CREDIT, product.getType());
        assertEquals(INITIAL_BALANCE, product.getBalance());
    }

    @Test
    @Order(2)
    public void testFindProductByAccountId() {
        Product p = repository.findByAccountId(ACCOUNT_ID);
        assertNotNull(p);
        assertNotNull(p.getId());
        assertEquals(id, p.getId());
        assertEquals(CUSTOMER_ID, p.getCustomerId());
        assertEquals(ProductType.CREDIT, p.getType());
        assertEquals(INITIAL_BALANCE, p.getBalance());
    }

    @Test
    @Order(3)
    public void testUpdateProductBalance() {
        Product product = repository.findById(id).get();
        int newBalance = 15000;
        product.setBalance(newBalance);
        Product updated = repository.save(product);
        assertEquals(newBalance, updated.getBalance());
        assertEquals(id, updated.getId());
        assertEquals(ACCOUNT_ID, updated.getAccountId());
        assertEquals(ProductType.CREDIT, updated.getType());
    }

    @Test
    @Order(4)
    public void testFindAllProducts() {
        List<Product> products = repository.findAll();
        assertNotNull(products);
        assertFalse(products.isEmpty());
        assertTrue(products.size() >= 1);
    }

    @Test
    @Order(5)
    public void testAddDebitProduct() {
        Product p = new Product();
        p.setAccountId("456");
        p.setCustomerId("456");
        p.setType(ProductType.DEBIT);
        p.setBalance(5000);
        p.setDateOfStart(LocalDate.now());
        p = repository.save(p);
        assertNotNull(p);
        assertNotNull(p.getId());
        assertEquals("456", p.getAccountId());
        assertEquals("456", p.getCustomerId());
        assertEquals(ProductType.DEBIT, p.getType());
        assertEquals(5000, p.getBalance());
    }

    @Test
    @Order(6)
    public void testFindNonExistentProduct() {
        String randomId = UUID.randomUUID().toString();
        Optional<Product> optProduct = repository.findById(randomId);
        assertFalse(optProduct.isPresent());
        Product nonExistentProduct = repository.findByAccountId("non-existent-account");
        assertNull(nonExistentProduct);
    }

}
