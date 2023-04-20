package pl.piomin.microservices.advanced.transfer.repository;

import org.springframework.data.mongodb.repository.MongoRepository;
import pl.piomin.microservices.advanced.transfer.model.Transfer;

import java.util.List;

public interface TransferRepository extends MongoRepository<Transfer, String> {

	public List<Transfer> findByRecipient(String recipient);
	public List<Transfer> findBySender(String sender);

}
