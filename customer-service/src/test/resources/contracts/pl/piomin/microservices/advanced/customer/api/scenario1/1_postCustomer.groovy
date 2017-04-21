org.springframework.cloud.contract.spec.Contract.make {
  request {
    method 'POST'
    url '/customers'
	body([
	  id: "123456789",
      pesel: "12345678909",
      name: "Jan Testowy",
	  type: "INDIVIDUAL"
	])
	headers {
	  contentType('application/json')
	}
  }
response {
  status 200
  body([
	id: "123456789",
    pesel: "12345678909",
    name: "Jan Testowy",
	type: "INDIVIDUAL"
  ])
  headers {
    contentType('application/json')
  }
 }
}