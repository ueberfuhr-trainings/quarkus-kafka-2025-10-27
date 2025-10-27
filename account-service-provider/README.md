# Account Service Provider

## Run the sample

To run and use the app, we can use these commands:

```bash
# run the Main class in your IDE or use Maven
cd .. && ./mvnw -pl account-service-provider -am quarkus:dev
# Open http://localhost:8081 in the browser
# Invoke the API to read and create customers
curl \
  -X 'GET' \
  'http://localhost:8081/customers' \
  -H 'accept: application/json' \
  -i
curl \
  -X 'POST' \
  'http://localhost:8081/customers' \
  -H 'accept: application/json' \
  -H 'Content-Type: application/json' \
  -d '{
    "name": "Tom Mayer",
    "birthdate": "2020-04-25",
    "state": "active"
  }' \
  -i
```
