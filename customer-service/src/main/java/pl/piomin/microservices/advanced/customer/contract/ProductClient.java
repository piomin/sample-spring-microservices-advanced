package pl.piomin.microservices.advanced.customer.contract;

import java.util.List;

import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@FeignClient("product-service")
public interface ProductClient {

	@RequestMapping(method = RequestMethod.GET, value = "/products/customer/{customerId}", consumes = {MediaType.APPLICATION_JSON_VALUE})
	List<Product> getProducts(@PathVariable("customerId") String customerId);
	
}
