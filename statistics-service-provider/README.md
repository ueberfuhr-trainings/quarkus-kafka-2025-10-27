# Statistics Service Provider

## Run the sample

To run and use the app, we can use these commands:

```bash
# run the Main class in your IDE or use Maven
cd .. && ./mvnw -pl statistics-service-provider -am quarkus:dev
# Open http://localhost:8082 in the browser
# Invoke the API to read the statistics
curl \
  -X 'GET' \
  'http://localhost:8082/statistics/customers' \
  -H 'accept: application/json' \
  -i
```
