org.springframework.cloud.contract.spec.Contract.make {
  request {
    method 'GET'
    url '/accounts'
  }
response {
  status 200
  body([
    [
      id: $(regex('[0-9]{10}')),
      number: "12345678909",
      balance: 1234,
      customerId: "123456789"
    ], [
      id: $(regex('[0-9]{10}')),
      number: "12345678909",
      balance: 1234,
      customerId: "123456789"    
    ]
  ])
  headers {
    contentType('application/json')
  }
 }
}