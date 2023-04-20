package pl.piomin.microservices.advanced.transfer.api;

import java.util.List;
import java.util.logging.Logger;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import pl.piomin.microservices.advanced.transfer.contract.AccountClient;
import pl.piomin.microservices.advanced.transfer.model.Transfer;
import pl.piomin.microservices.advanced.transfer.repository.TransferRepository;

@RestController
public class TransferController {

	@Autowired
	private AccountClient accountClient;
	
	@Autowired
	TransferRepository repository;
	
	protected Logger logger = Logger.getLogger(TransferController.class.getName());
	
	@RequestMapping("/transfers/sender/{sender}")
	public List<Transfer> findBySender(@PathVariable("sender") String sender) {
		logger.info(String.format("Transfer.findBySender(%s)", sender));
		return repository.findBySender(sender);
	}
	
	@RequestMapping("/transfers/recipient/{recipient}")
	public List<Transfer> findByRecipient(@PathVariable("recipient") String recipient) {
		logger.info(String.format("Transfer.findByRecipient(%s)", recipient));
		return repository.findByRecipient(recipient);
	}
	
	@RequestMapping("/transfers")
	public List<Transfer> findAll() {
		logger.info("Transfer.findAll()");
		return repository.findAll();
	}
	
	@RequestMapping("/transfers/{id}")
	public Transfer findById(@PathVariable("id") String id) {
		logger.info(String.format("Transfer.findById(%s)", id));
		Transfer transfer = repository.findById(id).orElseThrow();
		//Account account =  accountClient.getAccount(id);
		//transfer.setCustomerId(account.getCustomerId());
		return transfer;
	}
	
	@RequestMapping(method = RequestMethod.POST, value = "/transfers")
	public Transfer add(@RequestBody Transfer transfer) {
		logger.info(String.format("Transfer.add(%s)", transfer));
		return repository.save(transfer);
	}
	
}
