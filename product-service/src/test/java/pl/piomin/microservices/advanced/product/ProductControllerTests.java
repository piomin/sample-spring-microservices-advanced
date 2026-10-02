package pl.piomin.microservices.advanced.product;

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
import pl.piomin.microservices.advanced.product.model.Product;
import pl.piomin.microservices.advanced.product.model.ProductType;

import java.time.LocalDate;
import java.util.List;

import static io.specto.hoverfly.junit.core.SimulationSource.dsl;
import static io.specto.hoverfly.junit.dsl.HoverflyDsl.service;
import static io.specto.hoverfly.junit.dsl.ResponseCreators.success;
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
public class ProductControllerTests {

    @Container
    @ServiceConnection
    static MongoDBContainer mongoDBContainer = new MongoDBContainer("mongo:8.0");

    static String id;

    @Autowired
    TestRestTemplate template;

    @Test
    @Order(1)
    public void addProductTest() {
        Product p = new Product();
        p.setAccountId("acc-123");
        p.setCustomerId("cust-123");
        p.setType(ProductType.CREDIT);
        p.setBalance(15000);
        p.setDateOfStart(LocalDate.now());
        Product created = template.postForObject("/products", p, Product.class);
        assertNotNull(created);
        assertNotNull(created.getId());
        assertEquals("acc-123", created.getAccountId());
        assertEquals(ProductType.CREDIT, created.getType());
        assertEquals(15000, created.getBalance());
        id = created.getId();
    }

    @Test
    @Order(2)
    public void findAllProductsTest() {
        ResponseEntity<List> response = template.getForEntity("/products", List.class);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertFalse(response.getBody().isEmpty());
    }

    @Test
    @Order(3)
    public void findProductByAccountIdTest() {
        Product p = template.getForObject("/products/account/{accountId}", Product.class, "acc-123");
        assertNotNull(p);
        assertEquals("acc-123", p.getAccountId());
        assertEquals(ProductType.CREDIT, p.getType());
    }

    @Test
    @Order(4)
    public void findProductByIdWithAccountDetailsTest(Hoverfly hoverfly) {
        assumeTrue(id != null, "addProductTest must have succeeded for id to be available");
        // ProductController.findById calls accountClient.getAccount(productId) — it passes the product's
        // own MongoDB id as the account lookup key. The Hoverfly stub intercepts that exact path.
        // The stub response's customerId ("cust-456") overwrites the product's persisted customerId ("cust-123"),
        // which is intentional controller behavior being verified here.
        hoverfly.simulate(
                dsl(service("http://account-service")
                        .get("/accounts/" + id)
                        .willReturn(success(
                                "{\"id\":\"acc-123\",\"number\":\"PL1234567890\",\"balance\":5000,\"customerId\":\"cust-456\"}",
                                "application/json"))));

        Product p = template.getForObject("/products/{id}", Product.class, id);
        assertNotNull(p);
        assertEquals(id, p.getId());
        // customerId is overwritten by the account stub response (see ProductController.findById)
        assertEquals("cust-456", p.getCustomerId());
        // accountId is unchanged from persisted value
        assertEquals("acc-123", p.getAccountId());
    }

    @Test
    @Order(5)
    public void addInvestmentProductTest() {
        Product p = new Product();
        p.setAccountId("acc-456");
        p.setCustomerId("cust-456");
        p.setType(ProductType.INVESTMENT);
        p.setBalance(100000);
        p.setDateOfStart(LocalDate.now());
        p.setDateOfEnd(LocalDate.now().plusYears(10));
        Product created = template.postForObject("/products", p, Product.class);
        assertNotNull(created);
        assertNotNull(created.getId());
        assertEquals(ProductType.INVESTMENT, created.getType());
        assertEquals(100000, created.getBalance());
    }

}
