package pl.piomin.microservices.advanced.product.api;

import java.util.List;
import java.util.logging.Logger;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import pl.piomin.microservices.advanced.product.contract.Account;
import pl.piomin.microservices.advanced.product.contract.AccountClient;
import pl.piomin.microservices.advanced.product.model.Product;
import pl.piomin.microservices.advanced.product.repository.ProductRepository;

@RestController
public class ProductController {


    public ProductController(AccountClient accountClient, ProductRepository repository) {
        this.accountClient = accountClient;
        this.repository = repository;
    }

    private AccountClient accountClient;
    private ProductRepository repository;

    protected Logger logger = Logger.getLogger(ProductController.class.getName());

    @RequestMapping("/products/account/{accountId}")
    public Product findByPesel(@PathVariable String accountId) {
        logger.info(String.format("Product.findByAccountId(%s)", accountId));
        return repository.findByAccountId(accountId);
    }

    @RequestMapping("/products")
    public List<Product> findAll() {
        logger.info("Product.findAll()");
        return repository.findAll();
    }

    @RequestMapping("/products/{id}")
    public Product findById(@PathVariable String id) {
        logger.info(String.format("Product.findById(%s)", id));
        Product product = repository.findById(id).orElseThrow();
        Account account = accountClient.getAccount(id);
        product.setCustomerId(account.getCustomerId());
        return product;
    }

    @RequestMapping(method = RequestMethod.POST, value = "/products")
    public Product add(@RequestBody Product product) {
        logger.info(String.format("Product.add(%s)", product));
        return repository.save(product);
    }

}
