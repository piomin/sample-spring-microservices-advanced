org.springframework.cloud.contract.spec.Contract.make {
  request {
    method 'GET'
    url $(consumer(regex('^/products/account/[0-9]{9}')), producer('/products/account/123456789'))
  }
response {
  status 200
  body([
    [
      id: $(regex('[a-z0-9]{24}')),
      balance: 1234,
	  customerId: "123456789",
	  accountId: "123456789",
	  type: 'INVESTMENT',
	  dateOfStart: '2017-01-01'
    ]
  ])
  headers {
    contentType('application/json')
  }
 }
}