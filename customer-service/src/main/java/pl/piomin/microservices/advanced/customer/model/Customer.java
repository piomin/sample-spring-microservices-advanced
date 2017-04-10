package pl.piomin.microservices.advanced.customer.model;

import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "customer")
public class Customer {

	private Integer id;

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

}
