package pl.piomin.microservices.advanced.transfer.contract;

import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@FeignClient("account-service")
public interface AccountClient {

	@RequestMapping(method = RequestMethod.PUT, value = "/accounts")
	Account putAccount(Account account);

	@RequestMapping(method = RequestMethod.GET, value = "/accounts/{id}")
	Account getAccount(@PathVariable("id") String id);
	
}
