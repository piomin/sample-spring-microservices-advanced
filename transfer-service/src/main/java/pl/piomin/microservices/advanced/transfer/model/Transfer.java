package pl.piomin.microservices.advanced.transfer.model;

import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "transfer")
public class Transfer {

	private Integer id;

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

}
