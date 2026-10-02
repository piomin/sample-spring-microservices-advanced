package pl.piomin.microservices.advanced.transfer;

import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
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
import pl.piomin.microservices.advanced.transfer.model.Transfer;

import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assumptions.assumeTrue;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT,
        properties = {
                "spring.cloud.discovery.enabled=false"})
@Testcontainers
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@AutoConfigureTestRestTemplate
public class TransferControllerTests {

    @Container
    @ServiceConnection
    static MongoDBContainer mongoDBContainer = new MongoDBContainer("mongo:8.0");

    static String id;

    @Autowired
    TestRestTemplate template;

    @Test
    @Order(1)
    public void addTransferTest() {
        Transfer t = new Transfer();
        t.setSender("ACC-001");
        t.setRecipient("ACC-002");
        t.setAmount(750);
        t.setCreateAt(LocalDateTime.now());
        Transfer created = template.postForObject("/transfers", t, Transfer.class);
        assertNotNull(created);
        assertNotNull(created.getId());
        assertEquals("ACC-001", created.getSender());
        assertEquals("ACC-002", created.getRecipient());
        assertEquals(750, created.getAmount());
        id = created.getId();
    }

    @Test
    @Order(2)
    public void findAllTransfersTest() {
        ResponseEntity<List> response = template.getForEntity("/transfers", List.class);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertFalse(response.getBody().isEmpty());
    }

    @Test
    @Order(3)
    public void findTransferByIdTest() {
        assumeTrue(id != null, "addTransferTest must have succeeded for id to be available");
        Transfer t = template.getForObject("/transfers/{id}", Transfer.class, id);
        assertNotNull(t);
        assertEquals(id, t.getId());
        assertEquals("ACC-001", t.getSender());
        assertEquals("ACC-002", t.getRecipient());
        assertEquals(750, t.getAmount());
    }

    @Test
    @Order(4)
    public void findTransfersBySenderTest() {
        ResponseEntity<List> response = template.getForEntity("/transfers/sender/{sender}", List.class, "ACC-001");
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertFalse(response.getBody().isEmpty());
    }

    @Test
    @Order(5)
    public void findTransfersByRecipientTest() {
        ResponseEntity<List> response = template.getForEntity("/transfers/recipient/{recipient}", List.class, "ACC-002");
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertFalse(response.getBody().isEmpty());
    }

    @Test
    @Order(6)
    public void addSecondTransferAndVerifyListTest() {
        Transfer t = new Transfer();
        t.setSender("ACC-003");
        t.setRecipient("ACC-004");
        t.setAmount(2000);
        t.setCreateAt(LocalDateTime.now());
        Transfer created = template.postForObject("/transfers", t, Transfer.class);
        assertNotNull(created);
        assertNotNull(created.getId());

        ResponseEntity<List> allResponse = template.getForEntity("/transfers", List.class);
        assertEquals(HttpStatus.OK, allResponse.getStatusCode());
        assertNotNull(allResponse.getBody());
        assertTrue(allResponse.getBody().size() >= 2);
    }

    @Test
    @Order(7)
    public void findTransfersBySenderNotFoundTest() {
        ResponseEntity<List> response = template.getForEntity("/transfers/sender/{sender}", List.class, "nonexistent");
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertTrue(response.getBody().isEmpty());
    }

}
