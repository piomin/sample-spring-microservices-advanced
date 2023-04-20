package pl.piomin.microservices.advanced.customer;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;
import pl.piomin.microservices.advanced.customer.contract.AccountClient;

@SpringBootApplication
@EnableFeignClients(clients = {AccountClient.class})
public class Application {

    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }

}
