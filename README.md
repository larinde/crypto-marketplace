
| Action                     | HTTP Method   | URI                      |
|:---------------------------|:-------------:|:-------------------------|
| Place an order             |  POST         | /api/orders              |
| Cancel an order            |  DELETE       | /api/orders/{orderId}    |
| Get Summary of SELL orders |  GET          | /api/orders/summary/SELL |
| Get Summary of BUY orders  |  GET          | /api/orders/summary/BUY  |


#### Building and deploying the app
This implementation is based on a Spring Boot application that runs in an embedded Tomcat container.
It uses the default 8080 port.

```bash
 mvn clean package
 java -jar target/crypto-marketplace.jar
```

The running application instance is initialised with:
 - 2 SELL orders @ £111.11  for 74.25 and 25.75 Etherium  coins respectively.
 - 1 BUY 0rder @ £7.00 for 77.75 Litecoins


#### Placing an Order using curl

```bash
 curl --header "Content-Type: application/json"  --request POST   --data '{"userId":"777","orderType":"SELL","coinType":"ETHERIUM","quantity": "99", "price":"9999.99"}'  http://localhost:8080/api/orders -v
```
The response is a JSON payload containing the created order resource id

```json
{
 "orderId":"12"
}
```

#### Cancelling an Order using curl
```bash
  curl   --request DELETE   http://localhost:8080/api/orders/9999 -v
```

#### Retrieving live orders using curl

```bash
  curl   --request GET  http://localhost:8080/api/orders/summary/SELL -v
```

Response (an array of summarised orders):

```json
[
  {
    "quantity": 100.0,
    "price": 111.11
  }
]

```