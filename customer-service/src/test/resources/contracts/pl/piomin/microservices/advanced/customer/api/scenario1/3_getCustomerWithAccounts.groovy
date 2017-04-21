org.springframework.cloud.contract.spec.Contract.make {
  request {
    method 'GET'
    url '/customers/123456789'
  }
response {
  status 200
  body([
    id: $(regex('[a-z0-9]{24}')),
    pesel: "12345678909",
    name: "Jan Testowy",
	type: "INDIVIDUAL"
  ])
  headers {
    contentType('application/json')
  }
 }
}