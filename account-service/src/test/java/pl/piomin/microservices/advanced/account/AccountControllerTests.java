package pl.piomin.microservices.advanced.account;

import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.resttestclient.TestRestTemplate;
import org.springframework.boot.resttestclient.autoconfigure.AutoConfigureTestRestTemplate;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.testcontainers.service.connection.ServiceConnection;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.testcontainers.containers.MongoDBContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import pl.piomin.microservices.advanced.account.model.Account;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT,
        properties = {
                "spring.cloud.discovery.enabled=false"})
@Testcontainers
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@AutoConfigureTestRestTemplate
public class AccountControllerTests {

    @Container
    @ServiceConnection
    static MongoDBContainer mongoDBContainer = new MongoDBContainer("mongo:8.0");

    static String id;
    static final String CUSTOMER_ID = "cust-789";

    @Autowired
    TestRestTemplate template;

    @Test
    @Order(1)
    public void addAccountTest() {
        Account a = new Account();
        a.setNumber("PL1234567890");
        a.setBalance(5000);
        a.setCustomerId(CUSTOMER_ID);
        Account created = template.postForObject("/accounts", a, Account.class);
        assertNotNull(created);
        assertNotNull(created.getId());
        assertEquals("PL1234567890", created.getNumber());
        assertEquals(5000, created.getBalance());
        assertEquals(CUSTOMER_ID, created.getCustomerId());
        id = created.getId();
    }

    @Test
    @Order(2)
    public void findAllAccountsTest() {
        ResponseEntity<List> response = template.getForEntity("/accounts", List.class);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertFalse(response.getBody().isEmpty());
    }

    @Test
    @Order(3)
    public void findAccountByNumberTest() {
        Account a = template.getForObject("/accounts/{number}", Account.class, "PL1234567890");
        assertNotNull(a);
        assertEquals("PL1234567890", a.getNumber());
        assertEquals(CUSTOMER_ID, a.getCustomerId());
    }

    @Test
    @Order(4)
    public void findAccountByCustomerIdTest() {
        ResponseEntity<List> response = template.getForEntity(
                "/accounts/customer/{customerId}", List.class, CUSTOMER_ID);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertFalse(response.getBody().isEmpty());
    }

    @Test
    @Order(5)
    public void updateAccountTest() {
        Account a = new Account();
        a.setId(id);
        a.setNumber("PL1234567890");
        a.setBalance(9999);
        a.setCustomerId(CUSTOMER_ID);
        ResponseEntity<Account> response = template.exchange(
                "/accounts", HttpMethod.PUT, new HttpEntity<>(a), Account.class);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(9999, response.getBody().getBalance());
        assertEquals(id, response.getBody().getId());
    }

    @Test
    @Order(6)
    public void addSecondAccountForSameCustomerTest() {
        Account a = new Account();
        a.setNumber("PL0987654321");
        a.setBalance(3000);
        a.setCustomerId(CUSTOMER_ID);
        Account created = template.postForObject("/accounts", a, Account.class);
        assertNotNull(created);
        assertNotNull(created.getId());

        ResponseEntity<List> customerAccounts = template.getForEntity(
                "/accounts/customer/{customerId}", List.class, CUSTOMER_ID);
        assertEquals(HttpStatus.OK, customerAccounts.getStatusCode());
        assertEquals(2, customerAccounts.getBody().size());
    }

}
