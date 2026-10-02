package pl.piomin.microservices.advanced.transfer;

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
import pl.piomin.microservices.advanced.transfer.model.Transfer;
import pl.piomin.microservices.advanced.transfer.repository.TransferRepository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@DataMongoTest
@Testcontainers
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class TransferRepositoryTests {

    @Container
    @ServiceConnection
    static MongoDBContainer mongoDBContainer = new MongoDBContainer("mongo:8.0");

    static String id;

    @Autowired
    TransferRepository repository;

    @Test
    @Order(1)
    public void testAddTransfer() {
        Transfer t = new Transfer();
        t.setSender("ACC-001");
        t.setRecipient("ACC-002");
        t.setAmount(500);
        t.setCreateAt(LocalDateTime.now());
        t = repository.save(t);
        assertNotNull(t);
        assertNotNull(t.getId());
        assertEquals("ACC-001", t.getSender());
        assertEquals("ACC-002", t.getRecipient());
        assertEquals(500, t.getAmount());
        id = t.getId();
    }

    @Test
    @Order(2)
    public void testFindTransferById() {
        Optional<Transfer> optTransfer = repository.findById(id);
        assertTrue(optTransfer.isPresent());
        assertEquals("ACC-001", optTransfer.get().getSender());
        assertEquals("ACC-002", optTransfer.get().getRecipient());
    }

    @Test
    @Order(3)
    public void testFindTransfersBySender() {
        List<Transfer> transfers = repository.findBySender("ACC-001");
        assertNotNull(transfers);
        assertFalse(transfers.isEmpty());
        assertEquals("ACC-001", transfers.get(0).getSender());
    }

    @Test
    @Order(4)
    public void testFindTransfersByRecipient() {
        List<Transfer> transfers = repository.findByRecipient("ACC-002");
        assertNotNull(transfers);
        assertFalse(transfers.isEmpty());
        assertEquals("ACC-002", transfers.get(0).getRecipient());
    }

    @Test
    @Order(5)
    public void testFindAll() {
        List<Transfer> transfers = repository.findAll();
        assertNotNull(transfers);
        assertFalse(transfers.isEmpty());
    }

    @Test
    @Order(6)
    public void testAddMultipleTransfers() {
        Transfer t2 = new Transfer();
        t2.setSender("ACC-001");
        t2.setRecipient("ACC-003");
        t2.setAmount(1000);
        t2.setCreateAt(LocalDateTime.now());
        t2 = repository.save(t2);
        assertNotNull(t2.getId());

        List<Transfer> senderTransfers = repository.findBySender("ACC-001");
        assertEquals(2, senderTransfers.size());
    }

    @Test
    @Order(7)
    public void testFindTransfersBySenderNotFound() {
        List<Transfer> transfers = repository.findBySender("nonexistent");
        assertNotNull(transfers);
        assertTrue(transfers.isEmpty());
    }

    @Test
    @Order(8)
    public void testFindTransfersByRecipientNotFound() {
        List<Transfer> transfers = repository.findByRecipient("nonexistent");
        assertNotNull(transfers);
        assertTrue(transfers.isEmpty());
    }

}
