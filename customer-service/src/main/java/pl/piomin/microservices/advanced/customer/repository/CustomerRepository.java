package pl.piomin.microservices.advanced.customer.repository;

import org.springframework.data.mongodb.repository.MongoRepository;

import pl.piomin.microservices.advanced.customer.model.Customer;

public interface CustomerRepository extends MongoRepository<Customer, String> {

    public Customer findByPesel(String pesel);

}
