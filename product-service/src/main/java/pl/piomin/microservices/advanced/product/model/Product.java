package pl.piomin.microservices.advanced.product.model;

import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "product")
public class Product {

	private Integer id;

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

}
