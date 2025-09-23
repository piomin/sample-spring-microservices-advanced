
package pl.piomin.microservices.advanced.account.api;

import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
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
import pl.piomin.microservices.advanced.account.model.Account;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT,
    properties = {"spring.cloud.discovery.enabled=false"})
@Testcontainers
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class AccountControllerTest {

    @Container
    static MongoDBContainer mongoDBContainer = new MongoDBContainer("mongo:8.0");

    @DynamicPropertySource
    static void registerMongoProperties(DynamicPropertyRegistry registry) {
        registry.add("spring.data.mongodb.uri", () -> mongoDBContainer.getConnectionString() + "/test");
    }

    @Autowired
    TestRestTemplate restTemplate;

    private static String accountId;
    private static final String ACCOUNT_NUMBER = "ACC12345678";
    private static final String CUSTOMER_ID    = "CUST12345";

    @Test
    @Order(1)
    public void testAddAccount() {
        Account account = new Account();
        account.setNumber(ACCOUNT_NUMBER);
        account.setCustomerId(CUSTOMER_ID);
        account.setBalance(5000);

        ResponseEntity<Account> response = restTemplate.postForEntity("/accounts", account, Account.class);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        Account created = response.getBody();
        assertNotNull(created);
        assertNotNull(created.getId());
        assertEquals(ACCOUNT_NUMBER, created.getNumber());
        assertEquals(CUSTOMER_ID, created.getCustomerId());
        assertEquals(5000, created.getBalance());
        accountId = created.getId();
    }

    @Test
    @Order(2)
    public void testFindByNumber() {
        ResponseEntity<Account> response = restTemplate.getForEntity("/accounts/{number}", Account.class, ACCOUNT_NUMBER);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        Account found = response.getBody();
        assertNotNull(found);
        assertEquals(accountId, found.getId());
        assertEquals(ACCOUNT_NUMBER, found.getNumber());
        assertEquals(CUSTOMER_ID, found.getCustomerId());
    }

    @Test
    @Order(3)
    public void testFindByCustomer() {
        ResponseEntity<List<Account>> response = restTemplate.exchange(
            "/accounts/customer/{customerId}",
            HttpMethod.GET,
            null,
            new ParameterizedTypeReference<List<Account>>() {},
            CUSTOMER_ID);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        List<Account> list = response.getBody();
        assertNotNull(list);
        assertFalse(list.isEmpty());
        assertEquals(1, list.size());
        assertEquals(accountId, list.get(0).getId());
        assertEquals(ACCOUNT_NUMBER, list.get(0).getNumber());
    }

    @Test
    @Order(4)
    public void testFindAll() {
        ResponseEntity<List<Account>> response = restTemplate.exchange(
            "/accounts",
            HttpMethod.GET,
            null,
            new ParameterizedTypeReference<List<Account>>() {});
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertFalse(response.getBody().isEmpty());
    }

    @Test
    @Order(5)
    public void testUpdateAccount() {
        ResponseEntity<Account> getResp = restTemplate.getForEntity("/accounts/{number}", Account.class, ACCOUNT_NUMBER);
        Account account = getResp.getBody();
        assertNotNull(account);
        int newBalance = 7500;
        account.setBalance(newBalance);
        restTemplate.put("/accounts", account);
        ResponseEntity<Account> updatedResp = restTemplate.getForEntity("/accounts/{number}", Account.class, ACCOUNT_NUMBER);
        assertEquals(newBalance, updatedResp.getBody().getBalance());
        assertEquals(accountId, updatedResp.getBody().getId());
    }
}

package pl.piomin.microservices.advanced.account.api;

import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
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
import pl.piomin.microservices.advanced.account.model.Account;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT,
    properties = {"spring.cloud.discovery.enabled=false"})
@Testcontainers
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class AccountControllerTest {

    @Container
    static MongoDBContainer mongoDB = new MongoDBContainer("mongo:8.0");

    @DynamicPropertySource
    static void setProps(DynamicPropertyRegistry registry) {
        registry.add("spring.data.mongodb.uri", () -> mongoDB.getConnectionString() + "/test");
    }

    @Autowired
    TestRestTemplate restTemplate;

    private static String accountId;
    private static final String NUMBER = "ACC12345678";
    private static final String CUSTOMER = "CUST12345";

    @Test @Order(1)
    public void testAddAccount() {
        Account a = new Account();
        a.setNumber(NUMBER);
        a.setCustomerId(CUSTOMER);
        a.setBalance(5000);
        ResponseEntity<Account> r = restTemplate.postForEntity("/accounts", a, Account.class);
        assertEquals(HttpStatus.OK, r.getStatusCode());
        assertNotNull(r.getBody().getId());
        accountId = r.getBody().getId();
        assertEquals(NUMBER, r.getBody().getNumber());
    }

    @Test @Order(2)
    public void testFindByNumber() {
        ResponseEntity<Account> r = restTemplate.getForEntity("/accounts/{number}", Account.class, NUMBER);
        assertEquals(HttpStatus.OK, r.getStatusCode());
        assertEquals(accountId, r.getBody().getId());
    }

    @Test @Order(3)
    public void testFindByCustomer() {
        ResponseEntity<List<Account>> r = restTemplate.exchange(
            "/accounts/customer/{cust}", HttpMethod.GET, null,
            new ParameterizedTypeReference<List<Account>>() {}, CUSTOMER);
        assertFalse(r.getBody().isEmpty());
    }

    @Test @Order(4)
    public void testFindAll() {
        ResponseEntity<List<Account>> r = restTemplate.exchange(
            "/accounts", HttpMethod.GET, null,
            new ParameterizedTypeReference<List<Account>>() {});
        assertTrue(r.getBody().size() >= 1);
    }

    @Test @Order(5)
    public void testUpdateAccount() {
        Account a = restTemplate.getForEntity("/accounts/{number}", Account.class, NUMBER).getBody();
        a.setBalance(7500);
        restTemplate.put("/accounts", a);
        assertEquals(7500, restTemplate.getForEntity("/accounts/{number}", Account.class, NUMBER).getBody().getBalance());
    }
}
