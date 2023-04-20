package pl.piomin.microservices.advanced.customer.api;

import java.util.List;
import java.util.logging.Logger;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import pl.piomin.microservices.advanced.customer.contract.Account;
import pl.piomin.microservices.advanced.customer.contract.AccountClient;
import pl.piomin.microservices.advanced.customer.model.Customer;
import pl.piomin.microservices.advanced.customer.repository.CustomerRepository;

@RestController
public class CustomerController {

    @Autowired
    private AccountClient accountClient;

    @Autowired
    CustomerRepository repository;

    protected Logger logger = Logger.getLogger(CustomerController.class.getName());

    @RequestMapping(value = "/customers/pesel/{pesel}", method = RequestMethod.GET)
    public Customer findByPesel(@PathVariable("pesel") String pesel) {
        logger.info(String.format("Customer.findByPesel(%s)", pesel));
        return repository.findByPesel(pesel);
    }

    @RequestMapping(value = "/customers", method = RequestMethod.GET)
    public List<Customer> findAll() {
        logger.info("Customer.findAll()");
        return repository.findAll();
    }

    @RequestMapping(value = "/customers/{id}", method = RequestMethod.GET)
    public Customer findById(@PathVariable("id") String id) {
        logger.info(String.format("Customer.findById(%s)", id));
        Customer customer = repository.findById(id).orElseThrow();
        List<Account> accounts = accountClient.getAccounts(id);
        customer.setAccounts(accounts);
        return customer;
    }

    @RequestMapping(value = "/customers", method = RequestMethod.POST)
    public Customer add(@RequestBody Customer customer) {
        logger.info(String.format("Customer.add(%s)", customer));
        return repository.save(customer);
    }

}
