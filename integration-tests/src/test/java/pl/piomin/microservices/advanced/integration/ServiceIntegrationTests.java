
package pl.piomin.microservices.advanced.integration;

import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.context.annotation.ContextConfiguration;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(classes = TestApplication.class, webEnvironment = WebEnvironment.RANDOM_PORT)
@ContextConfiguration(classes = {
        pl.piomin.microservices.advanced.account.config.AccountContainersConfiguration.class,
        pl.piomin.microservices.advanced.customer.config.CustomerContainersConfiguration.class,
        pl.piomin.microservices.advanced.product.config.ProductContainersConfiguration.class
})
@Testcontainers
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class ServiceIntegrationTests {

    @Autowired
    private TestRestTemplate restTemplate;

    private static String accountId;
    private static String customerId;
    private static String productId;

    @Test
    @Order(1)
    void testAccountCreation() {
        Map<String, Object> payload = new HashMap<>();
        payload.put("name", "Test Account");
        ResponseEntity<Map> response = restTemplate.postForEntity("/accounts", payload, Map.class);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        Map body = response.getBody();
        assertNotNull(body);
        assertNotNull(body.get("id"));
        accountId = body.get("id").toString();
    }

    @Test
    @Order(2)
    void testCustomerCreation() {
        Map<String, Object> payload = new HashMap<>();
        payload.put("firstName", "John");
        payload.put("lastName", "Doe");
        ResponseEntity<Map> response = restTemplate.postForEntity("/customers", payload, Map.class);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        Map body = response.getBody();
        assertNotNull(body);
        assertNotNull(body.get("id"));
        customerId = body.get("id").toString();
    }

    @Test
    @Order(3)
    void testProductCreation() {
        Map<String, Object> payload = new HashMap<>();
        payload.put("name", "Test Product");
        payload.put("accountId", accountId);
        payload.put("customerId", customerId);
        ResponseEntity<Map> response = restTemplate.postForEntity("/products", payload, Map.class);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        Map body = response.getBody();
        assertNotNull(body);
        assertNotNull(body.get("id"));
        productId = body.get("id").toString();
    }

    @Test
    @Order(4)
    void testServiceToServiceFlow() {
        ResponseEntity<Map> response = restTemplate.getForEntity("/products/{id}", Map.class, productId);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        Map product = response.getBody();
        assertNotNull(product);
        assertEquals("Test Product", product.get("name"));
        // Verify that customer details are included
        Map customer = (Map) product.get("customer");
        assertNotNull(customer);
        assertEquals("John", customer.get("firstName"));
        assertEquals("Doe", customer.get("lastName"));
    }

    @Test
    @Order(5)
    void testFailureScenario() {
        String randomId = "nonexistent-id";
        ResponseEntity<Map> response = restTemplate.getForEntity("/products/{id}", Map.class, randomId);
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertTrue(response.getBody() == null || response.getBody().isEmpty());
    }
}
