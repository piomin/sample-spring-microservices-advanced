org.springframework.cloud.contract.spec.Contract.make {
  request {
    method 'GET'
    url '/accounts/1234567891'
  }
response {
  status 200
  body([
    id: $(regex('[a-z0-9]{10}')),
    number: "12345678910",
    balance: 4675,
    customerId: "123456780"
  ])
  headers {
    contentType('application/json')
  }
 }
}