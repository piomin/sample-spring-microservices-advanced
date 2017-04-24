org.springframework.cloud.contract.spec.Contract.make {
  request {
    method 'PUT'
    url '/accounts'
	body([
	  id: "1234567891",
      number: "12345678910",
      balance: $(regex('[0-9]{4}')),
	  customerId: "123456780"
	])
	headers {
	  contentType('application/json')
	}
  }
response {
  status 200
  body([
    id: "1234567891",
    number: "12345678910",
    balance: $(regex('[0-9]{4}')),
    customerId: "123456780"
  ])
  headers {
    contentType('application/json')
  }
 }
}