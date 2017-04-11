package pl.piomin.microservices.advanced.gateway;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.zuul.EnableZuulProxy;
import org.springframework.context.annotation.Bean;

import springfox.documentation.swagger.web.UiConfiguration;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

@SpringBootApplication
@EnableZuulProxy
@EnableSwagger2
public class Application {

	public static void main(String[] args) {
		SpringApplication.run(Application.class, args);
	}
	
	  @Bean
	  UiConfiguration uiConfig() {
	    return new UiConfiguration(
	        "validatorUrl",
	        "list",       // docExpansion          => none | list
	        "alpha",      // apiSorter             => alpha
	        "schema",     // defaultModelRendering => schema
	        UiConfiguration.Constants.DEFAULT_SUBMIT_METHODS,
	        false,        // enableJsonEditor      => true | false
	        true,         // showRequestHeaders    => true | false
	        60000L);      // requestTimeout => in milliseconds, defaults to null (uses jquery xh timeout)
	  }
}
