package pl.piomin.microservices.advanced.account.repository;

import pl.piomin.microservices.advanced.account.model.Account;

//@RunWith(SpringRunner.class)
//@DataMongoTest
public class AccountRepositoryTest {

//	@Autowired
	AccountRepository repository;
	
//	@Test
	public void testAddAccount() {
		Account a = new Account();
		a.setNumber("12345678909");
		a.setBalance(1232);
		a.setCustomerId("234353464576586464");
		repository.save(a);
	}
	
}
