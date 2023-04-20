package pl.piomin.microservices.advanced.product.repository;

import org.springframework.data.mongodb.repository.MongoRepository;

import pl.piomin.microservices.advanced.product.model.Product;

public interface ProductRepository extends MongoRepository<Product, String> {

    public Product findByAccountId(String accountId);

}
