package pl.piomin.microservices.advanced.account.repository;

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
import pl.piomin.microservices.advanced.account.model.Account;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assumptions.assumeTrue;

@DataMongoTest
@Testcontainers
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class AccountRepositoryTest {

    @Container
    @ServiceConnection
    static MongoDBContainer mongoDBContainer = new MongoDBContainer("mongo:8.0");

    static String id;

    @Autowired
    AccountRepository repository;

    @Test
    @Order(1)
    public void testAddAccount() {
        Account a = new Account();
        a.setNumber("12345678909");
        a.setBalance(1232);
        a.setCustomerId("234353464576586464");
        a = repository.save(a);
        assertNotNull(a);
        assertNotNull(a.getId());
        assertEquals("12345678909", a.getNumber());
        assertEquals(1232, a.getBalance());
        id = a.getId();
    }

    @Test
    @Order(2)
    public void testFindAccount() {
        assumeTrue(id != null, "testAddAccount must have succeeded for id to be available");
        Optional<Account> optAcc = repository.findById(id);
        assertTrue(optAcc.isPresent());
        assertEquals("12345678909", optAcc.get().getNumber());
    }

    @Test
    @Order(3)
    public void testFindAccountByNumber() {
        Account a = repository.findByNumber("12345678909");
        assertNotNull(a);
        assertNotNull(a.getId());
        assertEquals("234353464576586464", a.getCustomerId());
    }

    @Test
    @Order(4)
    public void testFindAccountByCustomerId() {
        List<Account> accounts = repository.findByCustomerId("234353464576586464");
        assertNotNull(accounts);
        assertFalse(accounts.isEmpty());
        assertEquals("12345678909", accounts.get(0).getNumber());
    }

    @Test
    @Order(5)
    public void testFindAll() {
        List<Account> accounts = repository.findAll();
        assertNotNull(accounts);
        assertFalse(accounts.isEmpty());
    }

    @Test
    @Order(6)
    public void testUpdateAccount() {
        Account a = repository.findByNumber("12345678909");
        assertNotNull(a);
        a.setBalance(9999);
        Account updated = repository.save(a);
        assertEquals(9999, updated.getBalance());
        assertEquals(a.getId(), updated.getId());
    }

    @Test
    @Order(7)
    public void testFindAccountByNumberNotFound() {
        Account a = repository.findByNumber("nonexistent");
        assertNull(a);
    }

    @Test
    @Order(8)
    public void testFindAccountByCustomerIdNotFound() {
        List<Account> accounts = repository.findByCustomerId("nonexistent");
        assertNotNull(accounts);
        assertTrue(accounts.isEmpty());
    }

}
