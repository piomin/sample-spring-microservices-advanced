# Microservices with Spring Cloud Advanced Demo Project [![Twitter](https://img.shields.io/twitter/follow/piotr_minkowski.svg?style=social&logo=twitter&label=Follow%20Me)](https://twitter.com/piotr_minkowski)

[![CircleCI](https://circleci.com/gh/piomin/sample-spring-microservices-advanced.svg?style=svg)](https://circleci.com/gh/piomin/sample-spring-microservices-advanced)

[![SonarCloud](https://sonarcloud.io/images/project_badges/sonarcloud-black.svg)](https://sonarcloud.io/dashboard?id=piomin_sample-spring-microservices-advanced)
[![Bugs](https://sonarcloud.io/api/project_badges/measure?project=piomin_sample-spring-microservices-advanced&metric=bugs)](https://sonarcloud.io/dashboard?id=piomin_sample-spring-microservices-advanced)
[![Coverage](https://sonarcloud.io/api/project_badges/measure?project=piomin_sample-spring-microservices-advanced&metric=coverage)](https://sonarcloud.io/dashboard?id=piomin_sample-spring-microservices-advanced)
[![Lines of Code](https://sonarcloud.io/api/project_badges/measure?project=piomin_sample-spring-microservices-advanced&metric=ncloc)](https://sonarcloud.io/dashboard?id=piomin_sample-spring-microservices-advanced)

In this project I'm demonstrating you the most interesting features of [Spring Cloud Project](https://spring.io/projects/spring-cloud) for building microservice-based architecture to discuss some additional aspects like API documentation, configuration, testing or storing data. 

## Getting Started 
Currently you may find here some examples of microservices implementation using different projects from Spring Cloud. All the examples are divided into the branches and described in a separated articles on my blog. Here's a full list of available examples:
1. Using **Swagger2** for building API documentation and exposing it for all the microservices on API gateway, which is in that case Netlix **Zuul**. The example is available in the branch [master](https://github.com/piomin/sample-spring-microservices-advanced/tree/master). Detailed description can be found here: [Microservices API Documentation with Swagger2](https://piotrminkowski.com/2017/04/14/microservices-api-documentation-with-swagger2/)
2. Using **Spring Cloud Contract** for consumer-driven contract testing in microservices architecture and embedded **Mongo** simulating database in component testing. The example is available in the branch [testing](https://github.com/piomin/sample-spring-microservices-advanced/tree/testing). A detailed description can be found here: [Testing Java Microservices](https://piotrminkowski.com/2017/04/26/testing-java-microservices/). 
3. Using **Hoverfly** library for testing APIs and for simulating communication between microservices in component testing. The tests with Hoverfly will include examples with static configuration through IP address and dynamic with **Eureka** discovery. The example is available in the branch [hoverfly](https://github.com/piomin/sample-spring-microservices-advanced/tree/hoverfly). A detailed description can be found here: [Testing REST API with Hoverfly](https://piotrminkowski.com/2017/08/02/testing-rest-api-with-hoverfly/).
4. Using **Spring Cloud Config** on the client and server side for providing distributed configuration across microservices. These example will discuss some more advanced features. A detailed description can be found here: [Microservices Configuration With Spring Cloud Config](https://piotrminkowski.com/2017/07/17/microservices-configuration-with-spring-cloud-config/). 

### Usage

In the most cases you need to have Maven and JDK8+. The best way to run the sample applications is with IDEs like IntelliJ IDEA or Eclipse. For the some of examples you need to run **MongoDB** on **Docker** container, so you also need to have Docker installed on your local machine.

## Architecture

Our sample microservices-based system consists of the following modules:
- **gateway-service** - a module that Spring Cloud Netflix Zuul for running Spring Boot application that acts as a proxy/gateway in our architecture.
- **config-service** - a module that uses Spring Cloud Config Server for running configuration server.
- **discovery-service** - a module that depending on the example it uses Spring Cloud Netflix Eureka.
- **account-service** - a module containing the first of our sample microservices that allows to perform CRUD operations on MongoDB repository of customer's accounts
- **customer-service** - a module containing the second of our sample microservices that allows to perform CRUD operations on MongoDB repository of customers. It communicates with account-service and product-service.
- **product-service** - a module containing the third of our sample microservices that allows to perform CRUD operations on MongoDB repository of products. 
- **transfer-service** - a module containing the fourth of our sample microservices that allows to perform CRUD operations on MongoDB repository making cash transfers between customer accounts. It communicates with both account-service.

The following picture illustrates an approach to a contract and component testing in our sample architecture described above.

<img src="https://piotrminkowski.files.wordpress.com/2017/04/testingmicroservices2.png" title="Testing"/><br/>


The whole sample architecture is visible on the picture below. Each microservice has its own database as shown on the following picture. For simplification in my articles I'm using a single database run on Docker container and multiple collections.   

<img src="https://piotrminkowski.files.wordpress.com/2019/11/micro_begin-1.png" title="Architecture"/>
