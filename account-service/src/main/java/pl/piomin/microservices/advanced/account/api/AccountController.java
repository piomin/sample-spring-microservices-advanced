package pl.piomin.microservices.advanced.account.api;

import org.springframework.web.bind.annotation.*;
import pl.piomin.microservices.advanced.account.model.Account;
import pl.piomin.microservices.advanced.account.repository.AccountRepository;

import java.util.List;
import java.util.logging.Logger;

@RestController
public class AccountController {

    public AccountController(AccountRepository repository) {
        this.repository = repository;
    }

    AccountRepository repository;
    protected Logger logger = Logger.getLogger(AccountController.class.getName());

    @RequestMapping(value = "/accounts/{number}", method = RequestMethod.GET)
    public Account findByNumber(@PathVariable String number) {
        logger.info(String.format("Account.findByNumber(%s)", number));
        return repository.findByNumber(number);
    }

    @RequestMapping(value = "/accounts/customer/{customerId}", method = RequestMethod.GET)
    public List<Account> findByCustomer(@PathVariable String customerId) {
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
