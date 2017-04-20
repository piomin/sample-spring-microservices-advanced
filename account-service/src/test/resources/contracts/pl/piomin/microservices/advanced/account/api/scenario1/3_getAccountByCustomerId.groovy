org.springframework.cloud.contract.spec.Contract.make {
  request {
    method 'GET'
    url $(consumer(regex('/accounts/customer/[a-z0-9]{24}')))
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