package pl.piomin.microservices.advanced.transfer.repository;

import java.util.List;

import org.springframework.data.mongodb.repository.MongoRepository;

import pl.piomin.microservices.advanced.transfer.model.Transfer;

public interface TransferRepository extends MongoRepository<Transfer, String> {

	public List<Transfer> findByRecipient(String recipient);
	public List<Transfer> findBySender(String sender);
	public Transfer findById(String id);

}
