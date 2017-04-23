package pl.piomin.microservices.advanced.product.repository;

import java.util.List;

import org.springframework.data.mongodb.repository.MongoRepository;

import pl.piomin.microservices.advanced.product.model.Product;

public interface ProductRepository extends MongoRepository<Product, String> {

	public List<Product> findByAccountId(String accountId);
	public List<Product> findByCustomerId(String customerId);
	public Product findById(String id);

}
