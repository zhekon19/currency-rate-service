## Currency Rate Service

This Java application provides an API to fetch fiat currency and cryptocurrency rates.

### Endpoints

#### GET `/currency-rates"`

- **Response:**
  - **Success (200):** JSON object with fiat and cryptocurrency rates
    ```json
    {
      "fiat": [
        {
          "currency": "USD",
          "rate": 123.33
        },
        {
          "currency": "EUR",
          "rate": 1232.22
        }
      ],
      "crypto": [
        {
          "currency": "BTC",
          "rate": 1232.22
        },
        {
          "currency": "ETH",
          "rate": 234.56
        }
      ]
    }
    ```
Before running the main application, you need to run the mock API from this repository: [Currencies Mocks API](https://github.com/illenko/currencies-mocks/blob/main/README.md).
### Running the Application

1. **Build the application:**

```sh
mvn clean install
```

2. **Run the application:**

```sh 
mvn spring-boot:run
```

3. **Build and run with docker-compose:**

```sh
docker-compose up --build
```

### Example Requests
**Fetch Currency Rates:**

 ```sh
curl http://localhost:8081/currency-rates
```
