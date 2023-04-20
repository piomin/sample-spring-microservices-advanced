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

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

@DataMongoTest
@Testcontainers
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class AccountRepositoryTest {

    @Container
    static MongoDBContainer mongoDBContainer = new MongoDBContainer("mongo:4.4");

    @DynamicPropertySource
    static void registerMongoProperties(DynamicPropertyRegistry registry) {
        String uri = mongoDBContainer.getConnectionString() + "/test";
        registry.add("spring.data.mongodb.uri", () -> uri);
    }

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
        id = a.getId();
    }

    @Test
    @Order(2)
    public void testFindAccount() {
        Optional<Account> optAcc = repository.findById(id);
        assertTrue(optAcc.isPresent());
    }

    @Test
    @Order(2)
    public void testFindAccountByNumber() {
        Account a = repository.findByNumber("12345678909");
        assertNotNull(a);
        assertNotNull(a.getId());
    }

}
