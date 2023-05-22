package pl.piomin.microservices.advanced.account;

import org.springframework.boot.SpringApplication;
import pl.piomin.microservices.advanced.account.config.AccountContainersConfiguration;

public class TestApplication {

    public static void main(String[] args) {
        SpringApplication.from(Application::main)
                .with(AccountContainersConfiguration.class)
                .run(args);
    }

}
