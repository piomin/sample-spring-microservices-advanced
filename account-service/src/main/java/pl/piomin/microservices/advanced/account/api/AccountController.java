package pl.piomin.microservices.advanced.account.api;

import java.util.List;
import java.util.logging.Logger;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import pl.piomin.microservices.advanced.account.model.Account;
import pl.piomin.microservices.advanced.account.repository.AccountRepository;
import pl.piomin.microservices.advanced.account.repository.TestAccountRepository;

@RestController
@RefreshScope
public class AccountController {

	@Autowired
	AccountRepository repository;
	@Autowired
	TestAccountRepository testRepository;

	protected Logger logger = Logger.getLogger(AccountController.class.getName());

	@RequestMapping(value = "/accounts/{number}/{test}", method = RequestMethod.GET)
	public Account findByNumber(@PathVariable("number") String number, @PathVariable(value = "test", required = false) boolean test) {
		logger.info(String.format("Account.findByNumber(%s)", number));
		if (test)
			return testRepository.findByNumber(number);
		return repository.findByNumber(number);
	}

	@RequestMapping(value = "/accounts/customer/{customer}", method = RequestMethod.GET)
	public List<Account> findByCustomer(@PathVariable("customer") String customerId) {
		logger.info(String.format("Account.findByCustomer(%s)", customerId));
		return repository.findByCustomerId(customerId);
	}

	@RequestMapping(value = "/accounts", method = RequestMethod.GET)
	public List<Account> findAll() {
		logger.info("Account.findAll()");
		return repository.findAll();
	}

	@RequestMapping(value = "/accounts", method = RequestMethod.POST)
	public Account add(@RequestBody Account account) {
		logger.info(String.format("Account.add(%s)", account));
		return repository.save(account);
	}
	
	@RequestMapping(value = "/accounts", method = RequestMethod.PUT)
	public Account update(@RequestBody Account account) {
		logger.info(String.format("Account.update(%s)", account));
		return repository.save(account);
	}

}
