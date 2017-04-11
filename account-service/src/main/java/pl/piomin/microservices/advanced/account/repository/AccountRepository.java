package pl.piomin.microservices.advanced.account.repository;

import java.util.List;

import org.springframework.data.mongodb.repository.MongoRepository;

import pl.piomin.microservices.advanced.account.model.Account;

public interface AccountRepository extends MongoRepository<Account, String> {

    public Account findByNumber(String number);
    public List<Account> findByCustomerId(String customerId);
    
}
