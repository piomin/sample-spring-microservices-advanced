package pl.piomin.microservices.advanced.customer.contract;

import java.util.List;

import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@FeignClient("account-service")
public interface AccountClient {

	@RequestMapping(method = RequestMethod.GET, value = "/accounts/customer/{customerId}")
	List<Account> getAccounts(@PathVariable("customerId") String customerId);

}
