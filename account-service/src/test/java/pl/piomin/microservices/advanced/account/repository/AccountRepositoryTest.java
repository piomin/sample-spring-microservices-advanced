
package pl.piomin.microservices.advanced.account.repository;

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
import pl.piomin.microservices.advanced.account.model.Account;

import java.util.Optional;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

@DataMongoTest
@Testcontainers
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class AccountRepositoryTest {

    @Container
    static MongoDBContainer mongoDBContainer = new MongoDBContainer("mongo:8.0");

    @DynamicPropertySource
    static void registerMongoProperties(DynamicPropertyRegistry registry) {
        String uri = mongoDBContainer.getConnectionString() + "/test";
        registry.add("spring.data.mongodb.uri", () -> uri);
    }

    static String id;
    static final String ACCOUNT_NUMBER = "12345678909";
    static final String CUSTOMER_ID = "234353464576586464";

    @Autowired
    AccountRepository repository;

    @Test
    @Order(1)
    public void testAddAccount() {
        Account a = new Account();
        a.setNumber(ACCOUNT_NUMBER);
        a.setBalance(1232);
        a.setCustomerId(CUSTOMER_ID);
        a = repository.save(a);
        assertNotNull(a);
        assertNotNull(a.getId());
        assertEquals(1232, a.getBalance());
        assertEquals(ACCOUNT_NUMBER, a.getNumber());
        assertEquals(CUSTOMER_ID, a.getCustomerId());
        id = a.getId();
    }

    @Test
    @Order(2)
    public void testFindAccount() {
        Optional<Account> optAcc = repository.findById(id);
        assertTrue(optAcc.isPresent());
        Account account = optAcc.get();
        assertEquals(id, account.getId());
        assertEquals(ACCOUNT_NUMBER, account.getNumber());
        assertEquals(CUSTOMER_ID, account.getCustomerId());
        assertEquals(1232, account.getBalance());
    }

    @Test
    @Order(2)
    public void testFindAccountByNumber() {
        Account a = repository.findByNumber(ACCOUNT_NUMBER);
        assertNotNull(a);
        assertNotNull(a.getId());
        assertEquals(id, a.getId());
        assertEquals(CUSTOMER_ID, a.getCustomerId());
    }

    @Test
    @Order(3)
    public void testFindAccountsByCustomerId() {
        List<Account> accounts = repository.findByCustomerId(CUSTOMER_ID);
        assertNotNull(accounts);
        assertFalse(accounts.isEmpty());
        assertEquals(1, accounts.size());
        assertEquals(id, accounts.get(0).getId());
        assertEquals(ACCOUNT_NUMBER, accounts.get(0).getNumber());
    }

    @Test
    @Order(4)
    public void testUpdateAccountBalance() {
        Optional<Account> optAcc = repository.findById(id);
        assertTrue(optAcc.isPresent());
        Account account = optAcc.get();
        int newBalance = 2500;
        account.setBalance(newBalance);
        Account updated = repository.save(account);
        assertEquals(newBalance, updated.getBalance());
        assertEquals(id, updated.getId());
        assertEquals(ACCOUNT_NUMBER, updated.getNumber());
    }

    @Test
    @Order(5)
    public void testFindAllAccounts() {
        List<Account> accounts = repository.findAll();
        assertNotNull(accounts);
        assertFalse(accounts.isEmpty());
    }

    @Test
    @Order(6)
    public void testFindNonExistentAccount() {
        String randomId = UUID.randomUUID().toString();
        Optional<Account> optAccount = repository.findById(randomId);
        assertFalse(optAccount.isPresent());
        Account nonExistentAccount = repository.findByNumber("non-existent-number");
        assertEquals(null, nonExistentAccount);
    }

}
