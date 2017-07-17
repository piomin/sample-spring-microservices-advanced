package pl.piomin.microservices.advanced.config;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.config.monitor.GithubPropertyPathNotificationExtractor;
import org.springframework.cloud.config.server.EnableConfigServer;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
@EnableConfigServer
@EnableDiscoveryClient
public class ConfigServer {

	public static void main(String[] args) {
		SpringApplication.run(ConfigServer.class, args);
	}

	@Bean
	public GithubPropertyPathNotificationExtractor githubPropertyPathNotificationExtractor() {
	  return new GithubPropertyPathNotificationExtractor();
	}

}
