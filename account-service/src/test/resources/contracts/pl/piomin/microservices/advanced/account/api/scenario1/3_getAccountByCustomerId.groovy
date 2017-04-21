org.springframework.cloud.contract.spec.Contract.make {
  request {
    method 'GET'
    url $(consumer(regex('^/accounts/customer/[0-9]{9}$')), producer('/accounts/customer/123456789'))
  }
response {
  status 200
  body([
    id: $(regex('[a-z0-9]{24}')),
    number: "12345678909",
    balance: 1234,
    customerId: "123456789"
  ])
  headers {
    contentType('application/json')
  }
 }
}