package pl.piomin.microservices.advanced.transfer;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;
import pl.piomin.microservices.advanced.transfer.contract.AccountClient;

@SpringBootApplication
@EnableFeignClients(clients = { AccountClient.class })
public class Application {

	public static void main(String[] args) {
		SpringApplication.run(Application.class, args);
	}
    
}
