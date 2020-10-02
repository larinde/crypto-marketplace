

| Action                     | HTTP Method   | URI  Cool                |
|:---------------------------|:-------------:|:-------------------------|
| Place an order             |  POST         | /api/orders              |
| Cancel an order            |  DELETE       | /api/orders/{orderId}    |
| Get Summary of SELL orders |  GET          | /api/orders/summary/SELL |
| Get Summary of BUY orders  |  GET          | /api/orders/summary/BUY  |


##### Building and deploying the app
This implementation is based on a Spring Boot application that runs in an embedded Tomcat container.
It uses default 8080 port.

For the sake of brevity all the code was implemented in a single class. Ideally, the business-logic should be refactored
and encapsulated in a service layer.



```bash
 mvn clean package
 java -jar target/credsuisse-challenge.jar
```

##### Place an Order using curl

```bash
 curl --header "Content-Type: application/json"  --request POST   --data '{"userId":"777","orderType":"SELL","coinType":"ETHERIUM","quantity": "99", "price":"9999.99"}'  http://localhost:8080/api/orders -v
```

##### Cancel an Order using curl
```bash
  curl   --request DELETE   http://localhost:8080/api/orders/9999 -v
```

##### Retrieve live Order using curl
```bash
  curl   --request DELETE   http://localhost:8080/api/orders/summary/SELL -v
```
