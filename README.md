# Sales-statistics
microservice to calculate real time statistics of sales orders

# REST endpoints details :
```
1. Swagger URL : http://localhost:8080/swagger-ui.html#/
2. Record sales order - POST

URL: /sales
Method: POST
Content-Type: application/x-www-form-urlencoded
parameters - sales_amount (string) - required
Return HTTP Code: 202 Accepted
Return Body: blank

Example : http://localhost:8080/sales?sales_amount=10

3. Get sales statistics

URL: /statistics
Method: GET
Parameters: none
Return HTTP Code: 200 OK
Return Body:
{
total_sales_amount: “1000000.00”,
average_amount_per_order: “45.04”
}

Example : http://localhost:8080/statistics
Response :
  {
      "total_sales_amount": "1000000.00",
      "average_amount_per_order": "45.04"
  }
```
# How to build & start the application ?

1. Please unzip the file.(following files should be available)
```
README.md
mvnw
pom.xml
sales-statistics.iml
src
manifest.yml
mvnw.cmd
postman-collections
sales-statistics.png
startserver.sh
```

2. ./startserver.sh (executing this script will build and start the application)

# How to verify the REST endpoints ?

There is a postman collection provided in 'postman-collections' directory. Import it to the postman tool and execute.
```
filename : mglvm.postman_collection.json
```
# Instant access to this application :
This application is also deployed in a cloud environment for accessing immediately.Following are the links to access different endpoints.
```
Swagger URL : http://sales-statistics-cloud-app.cfapps.io/swagger-ui.html#/
POST : https://sales-statistics-cloud-app.cfapps.io/sales?sales_amount=100
GET  : https://sales-statistics-cloud-app.cfapps.io/statistics

```
# How to run test cases ?
To run the test suit, execute
```
mvn test
```


