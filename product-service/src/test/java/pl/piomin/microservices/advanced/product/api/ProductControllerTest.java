
package pl.piomin.microservices.advanced.product.api;

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
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.MongoDBContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import pl.piomin.microservices.advanced.product.model.Product;
import pl.piomin.microservices.advanced.product.model.ProductType;

import java.time.LocalDate;
import java.util.List;

import static io.specto.hoverfly.junit.core.SimulationSource.dsl;
import static io.specto.hoverfly.junit.dsl.HoverflyDsl.service;
import static io.specto.hoverfly.junit.dsl.ResponseCreators.success;
import static io.specto.hoverfly.junit.dsl.matchers.HoverflyMatchers.startsWith;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT,
    properties = {"spring.cloud.discovery.enabled=false"})
@Testcontainers
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@HoverflyCore(config = @HoverflyConfig(logLevel = LogLevel.DEBUG))
@ExtendWith(HoverflyExtension.class)
public class ProductControllerTest {

    @Container
    static MongoDBContainer mongoDBContainer = new MongoDBContainer("mongo:8.0");

    @DynamicPropertySource
    static void registerMongoProperties(DynamicPropertyRegistry registry) {
        registry.add("spring.data.mongodb.uri", () -> mongoDBContainer.getConnectionString() + "/test");
    }

    @Autowired
    TestRestTemplate template;

    private static String productId;
    private static final String ACCOUNT_ID       = "ACC987654";
    private static final String CUSTOMER_ID      = "CUST987654";
    private static final int    INITIAL_BALANCE  = 10000;

    @Test
    @Order(1)
    public void testAddProduct() {
        Product product = new Product();
        product.setAccountId(ACCOUNT_ID);
        product.setCustomerId(CUSTOMER_ID);
        product.setType(ProductType.CREDIT);
        product.setBalance(INITIAL_BALANCE);
        product.setDateOfStart(LocalDate.now());

        ResponseEntity<Product> response = template.postForEntity("/products", product, Product.class);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        Product created = response.getBody();
        assertNotNull(created);
        assertNotNull(created.getId());
        assertEquals(ACCOUNT_ID, created.getAccountId());
        assertEquals(CUSTOMER_ID, created.getCustomerId());
        assertEquals(ProductType.CREDIT, created.getType());
        assertEquals(INITIAL_BALANCE, created.getBalance());
        assertNotNull(created.getDateOfStart());
        productId = created.getId();
    }

    @Test
    @Order(2)
    public void testFindByAccountId() {
        ResponseEntity<Product> response = template.getForEntity("/products/account/{accountId}", Product.class, ACCOUNT_ID);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        Product found = response.getBody();
        assertNotNull(found);
        assertEquals(productId, found.getId());
        assertEquals(ACCOUNT_ID, found.getAccountId());
        assertEquals(CUSTOMER_ID, found.getCustomerId());
        assertEquals(ProductType.CREDIT, found.getType());
    }

    @Test
    @Order(3)
    public void testFindById(Hoverfly hoverfly) {
        hoverfly.simulate(
            dsl(service("http://account-service")
                .get(startsWith("/accounts/" + productId))
                .willReturn(success("{\"id\":\"" + productId + "\",\"customerId\":\"" + CUSTOMER_ID + "\"}", "application/json"))));
        ResponseEntity<Product> response = template.getForEntity("/products/{id}", Product.class, productId);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        Product found = response.getBody();
        assertNotNull(found);
        assertEquals(productId, found.getId());
        assertEquals(ACCOUNT_ID, found.getAccountId());
        assertEquals(CUSTOMER_ID, found.getCustomerId());
    }

    @Test
    @Order(4)
    public void testFindAll() {
        ResponseEntity<List<Product>> response = template.exchange(
            "/products", HttpMethod.GET, null, new ParameterizedTypeReference<List<Product>>() {});
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertFalse(response.getBody().isEmpty());
    }

    @Test
    @Order(5)
    public void testFindByNonExistentAccountId() {
        ResponseEntity<Product> response = template.getForEntity(
            "/products/account/{accountId}", Product.class, "non-existent-account");
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNull(response.getBody());
    }
}

package pl.piomin.microservices.advanced.product.api;

import io.specto.hoverfly.junit.core.SimulationSource;
import io.specto.hoverfly.junit.dsl.HoverflyDsl;
import io.specto.hoverfly.junit.dsl.ResponseCreators;
import io.specto.hoverfly.junit.dsl.matchers.HoverflyMatchers;
import io.specto.hoverfly.junit5.HoverflyExtension;
import io.specto.hoverfly.junit5.api.HoverflyConfig;
import io.specto.hoverfly.junit5.api.HoverflyCore;
import io.specto.hoverfly.junit.core.config.LogLevel;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import org.testcontainers.containers.MongoDBContainer;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import pl.piomin.microservices.advanced.product.model.Product;
import pl.piomin.microservices.advanced.product.model.ProductType;

import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT,
    properties = {"spring.cloud.discovery.enabled=false"})
@Testcontainers
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@HoverflyCore(config = @HoverflyConfig(logLevel = LogLevel.DEBUG))
@ExtendWith(HoverflyExtension.class)
public class ProductControllerTest {

    @Container
    static MongoDBContainer mongoDB = new MongoDBContainer("mongo:8.0");

    @DynamicPropertySource
    static void setProps(DynamicPropertyRegistry registry) {
        registry.add("spring.data.mongodb.uri", () -> mongoDB.getConnectionString() + "/test");
    }

    @Autowired
    TestRestTemplate restTemplate;

    private static String id;
    private static final String ACCOUNT = "ACC987654";
    private static final String CUSTOMER = "CUST987654";

    @Test @Order(1)
    public void testAddProduct() {
        Product p = new Product();
        p.setAccountId(ACCOUNT);
        p.setCustomerId(CUSTOMER);
        p.setType(ProductType.CREDIT);
        p.setBalance(10000);
        p.setDateOfStart(LocalDate.now());
        ResponseEntity<Product> r = restTemplate.postForEntity("/products", p, Product.class);
        assertEquals(HttpStatus.OK, r.getStatusCode());
        id = r.getBody().getId();
    }

    @Test @Order(2)
    public void testFindByAccountId() {
        ResponseEntity<Product> r = restTemplate.getForEntity("/products/account/{acct}", Product.class, ACCOUNT);
        assertEquals(id, r.getBody().getId());
    }

    @Test @Order(3)
    public void testFindById() {
        // mock account-service response
        HoverflyDsl.service("http://account-service")
            .get(HoverflyMatchers.startsWith("/accounts/" + id))
            .willReturn(ResponseCreators.success("{\"id\":\""+id+"\",\"customerId\":\""+CUSTOMER+"\"}", "application/json"));
        ResponseEntity<Product> r = restTemplate.getForEntity("/products/{id}", Product.class, id);
        assertEquals(CUSTOMER, r.getBody().getCustomerId());
    }

    @Test @Order(4)
    public void testFindAll() {
        ResponseEntity<List<Product>> r = restTemplate.exchange(
            "/products", HttpMethod.GET, null,
            new ParameterizedTypeReference<List<Product>>(){});
        assertFalse(r.getBody().isEmpty());
    }

    @Test @Order(5)
    public void testNonExistentAccount() {
        ResponseEntity<Product> r = restTemplate.getForEntity("/products/account/{acct}", Product.class, "no-such");
        assertEquals(null, r.getBody());
    }
}
