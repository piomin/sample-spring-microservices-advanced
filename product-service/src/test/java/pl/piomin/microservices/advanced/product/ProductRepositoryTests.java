package pl.piomin.microservices.advanced.product;

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
import pl.piomin.microservices.advanced.product.model.Product;
import pl.piomin.microservices.advanced.product.model.ProductType;
import pl.piomin.microservices.advanced.product.repository.ProductRepository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assumptions.assumeTrue;

@DataMongoTest
@Testcontainers
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class ProductRepositoryTests {

    @Container
    @ServiceConnection
    static MongoDBContainer mongoDBContainer = new MongoDBContainer("mongo:8.0");

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
        p.setDateOfEnd(LocalDate.now().plusYears(5));
        p = repository.save(p);
        assertNotNull(p);
        assertNotNull(p.getId());
        assertEquals("123", p.getAccountId());
        assertEquals(ProductType.CREDIT, p.getType());
        assertEquals(10000, p.getBalance());
        assertNotNull(p.getDateOfEnd());
        id = p.getId();
    }

    @Test
    @Order(2)
    public void testFindProduct() {
        assumeTrue(id != null, "testAddProduct must have succeeded for id to be available");
        Optional<Product> optProduct = repository.findById(id);
        assertTrue(optProduct.isPresent());
        assertEquals("123", optProduct.get().getAccountId());
    }

    @Test
    @Order(3)
    public void testFindProductByAccountId() {
        Product p = repository.findByAccountId("123");
        assertNotNull(p);
        assertNotNull(p.getId());
        assertEquals(ProductType.CREDIT, p.getType());
    }

    @Test
    @Order(4)
    public void testFindAll() {
        List<Product> products = repository.findAll();
        assertNotNull(products);
        assertFalse(products.isEmpty());
    }

    @Test
    @Order(5)
    public void testAddInvestmentProduct() {
        Product p = new Product();
        p.setAccountId("456");
        p.setCustomerId("456");
        p.setType(ProductType.INVESTMENT);
        p.setBalance(50000);
        p.setDateOfStart(LocalDate.now());
        p = repository.save(p);
        assertNotNull(p);
        assertNotNull(p.getId());
        assertEquals(ProductType.INVESTMENT, p.getType());
        assertEquals("456", p.getAccountId());
    }

    @Test
    @Order(6)
    public void testUpdateProduct() {
        Product p = repository.findByAccountId("123");
        assertNotNull(p);
        p.setBalance(20000);
        Product updated = repository.save(p);
        assertEquals(20000, updated.getBalance());
        assertEquals(p.getId(), updated.getId());
    }

    @Test
    @Order(7)
    public void testFindProductByAccountIdNotFound() {
        Product p = repository.findByAccountId("nonexistent");
        assertNull(p);
    }

}
