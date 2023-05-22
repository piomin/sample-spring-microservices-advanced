package pl.piomin.microservices.advanced.account.config;

import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.testcontainers.service.connection.ServiceConnection;
import org.springframework.context.annotation.Bean;
import org.testcontainers.containers.MongoDBContainer;
import org.testcontainers.utility.DockerImageName;

@TestConfiguration(proxyBeanMethods = false)
public class AccountContainersConfiguration {

    @Bean
    @ServiceConnection
    public MongoDBContainer mongodbContainer() {
        return new MongoDBContainer(DockerImageName.parse("mongo:4.4"));
    }

}
