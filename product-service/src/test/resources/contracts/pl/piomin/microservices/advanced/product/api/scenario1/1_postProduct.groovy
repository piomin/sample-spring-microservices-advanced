org.springframework.cloud.contract.spec.Contract.make {
  request {
    method 'POST'
    url '/products'
	body([
      balance: 1234,
	  customerId: "123456789",
	  accountId: "123456789",
	  type: 'INVESTMENT',
	  dateOfStart: '2017-01-01'
	])
	headers {
	  contentType('application/json')
	}
  }
response {
  status 200
  body([
    id: $(regex('[a-z0-9]{24}')),
    balance: 1234,
	customerId: "123456789",
	accountId: "123456789",
	type: 'INVESTMENT',
	dateOfStart: '2017-01-01'
  ])
  headers {
    contentType('application/json')
  }
 }
}