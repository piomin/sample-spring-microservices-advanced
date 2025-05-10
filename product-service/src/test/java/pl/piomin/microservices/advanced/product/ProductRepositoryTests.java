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

import static org.junit.jupiter.api.Assertions.*;

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

    @Autowired
    ProductRepository repository;

    @Test
    @Order(1)
    public void testAddProduct() {
        Product p = new Product();
        p.setAccountId("123");
        p.setCustomerId("123");
        p.setType(ProductType.CREDIT);
        p.setBalance(10000);
        p.setDateOfStart(LocalDate.now());
        p = repository.save(p);
        assertNotNull(p, "Product should not be null after saving");
        assertNotNull(p.getId(), "Product ID should not be null after saving");
        id = p.getId();
    }

    @Test
    @Order(2)
    public void testFindProduct() {
        Optional<Product> optProd = repository.findById(id);
        assertTrue(optProd.isPresent(), "Product should be found by ID");
        assertEquals("123", optProd.get().getAccountId(), "Product account ID should match");
    }

    @Test
    @Order(3)
    public void testFindProductByAccountId() {
        Product p = repository.findByAccountId("123");
        assertNotNull(p, "Product should not be null when found by account ID");
        assertEquals("123", p.getAccountId(), "Product account ID should match");
    }

    @Test
    @Order(4)
    public void testFindNonExistentProduct() {
        Optional<Product> optProd = repository.findById("non-existent-id");
        assertFalse(optProd.isPresent(), "Product should not be found for a non-existent ID");
    }
}
