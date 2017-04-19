org.springframework.cloud.contract.spec.Contract.make {
  request {
    method 'POST'
    url '/accounts'
	body([
      number: "12345678909",
      balance: 1234,
	  customerId: "1"
	])
	headers {
	  contentType('application/json')
	}
  }
response {
  status 200
  body([
    id: $(regex('[a-z0-9]{24}')),
    number: "12345678909",
    balance: 1234,
    customerId: "1"
  ])
  headers {
    contentType('application/json')
  }
 }
}