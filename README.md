# Payment Transactions API - Red LINK

This API allows users to create, retrieve, and list payment transactions. It integrates with an external exchange rate API to convert transaction amounts into USD when needed.

## Features

- **Create a transaction:**
    - Supports different transaction types: `CARD`, `BANK_TRANSFER`, and `P2P`.
    - Validates idempotency via the `Idempotency-Key` header.
    - Converts amounts to USD using [Exchange Rate API](https://www.exchangerate-api.com/docs/pair-conversion-requests).
- **Retrieve a transaction:**
    - Get detailed information about a specific transaction.
- **List transactions:**
    - Filter transactions by status, currency, or date range.

# API Endpoints

## Create Transaction

**POST** `/api/v1/transaction`

#### Request Headers
- `Idempotency-Key`: Unique key to ensure the transaction is not processed multiple times.

### Card Transaction

#### Request Body
```json
{
  "user_id": "user123",
  "amount": 1000,
  "currency": "BRL",
  "type": "CARD",
  "card_details": {
    "card_id": "4111111111111111",
    "merchant_name": "Merchant Name",
    "merchant_id": "merchant123",
    "mcc_code": 5411
  }
}
```

#### Response
```json
{
  "id": 1,
  "user_id": "user123",
  "amount": 1000,
  "usd_amount": 166.4,
  "currency": "BRL",
  "type": "CARD",
  "status": "COMPLETED",
  "created_at": "2024-12-08T13:29:01.454514"
}
```

### Bank Transfer

#### Request Body
```json
{
  "user_id": "user123",
  "amount": 1000,
  "currency": "BRL",
  "type": "BANK_TRANSFER",
  "bank_details": {
    "bank_code": "1111",
    "recipient_account": "534536346"
  }
}
```

#### Response
```json
{
  "id": 1,
  "user_id": "user123",
  "amount": 1000,
  "usd_amount": 166.4,
  "currency": "BRL",
  "type": "BANK_TRANSFER",
  "status": "COMPLETED",
  "created_at": "2024-12-08T13:46:12.074605"
}
```

### P2P Transfer

#### Request Body
```json
{
  "user_id": "user123",
  "amount": 1000,
  "currency": "EUR",
  "type": "P2P",
  "p2p_details": {
    "sender_id": "1111",
    "recipient_id": "222222",
    "note": "P2P Transfer"
  }
}
```

#### Response
```json
{
  "id": 15,
  "user_id": "user123",
  "amount": 1000,
  "usd_amount": 1056.7,
  "currency": "EUR",
  "type": "P2P",
  "status": "COMPLETED",
  "created_at": "2024-12-08T13:48:53.558247"
}
```

---

### Get Transaction by ID

**GET** `/api/v1/transaction/{id}`

#### Response
```json
{
  "id": 1,
  "user_id": "string",
  "amount": 100.50,
  "usd_amount": 100.50,
  "currency": "USD",
  "type": "CARD",
  "status": "COMPLETED",
  "created_at": "2024-12-08T10:00:00"
}
```

---

### List User Transactions

**GET** `/api/v1/transaction/user/{userId}`

#### Query Parameters
- `status`: Filter by transaction status (e.g., `PENDING`, `COMPLETED`).
- `currency`: Filter by currency ISO code.
- `startDate`: Start of the date range (ISO 8601 format).
- `endDate`: End of the date range (ISO 8601 format).

#### Response
```json
[
  {
    "id": 1,
    "user_id": "string",
    "amount": 100.50,
    "usd_amount": 100.50,
    "currency": "USD",
    "type": "CARD",
    "status": "COMPLETED",
    "created_at": "2024-12-08T10:00:00"
  }
]
```

## External Dependencies

This API integrates with the following external service:

- **[Exchange Rate API](https://www.exchangerate-api.com/docs/pair-conversion-requests):**
  Used to fetch exchange rates for currency conversions.

## Running the Application

1. Clone the repository:
   ```bash
   git clone <repository-url>
   cd <repository-folder>
   ```

2. Configure the application:
    - Obtain an API key from [Exchange Rate API](https://app.exchangerate-api.com/sign-up).
    - Replace the API key in the `application.yml` file:
      ```yaml
      exchange-rate-api:
        api-key: your_api_key_here
        base-url: https://v6.exchangerate-api.com/v6
      ```
    - Configure the database connection in `application.yml`:
      ```yaml
      spring:
        datasource:
          url: jdbc:mysql://localhost:3306/payments?useSSL=false&serverTimezone=UTC&allowPublicKeyRetrieval=true
          username: root
          password: 123456
          driver-class-name: com.mysql.cj.jdbc.Driver
      ```

3. Build and run the application:
   ```bash
   ./mvnw spring-boot:run
   ```

4. Test the API using tools like [Postman](https://www.postman.com/) or [cURL](https://curl.se/).

## Testing the API

### Prerequisites
- Install [Postman](https://www.postman.com/) or use [cURL](https://curl.se/) for testing.

### Steps

1. **Create a transaction:**
   ```bash
   curl -X POST http://localhost:8080/api/v1/transaction \
   -H "Content-Type: application/json" \
   -H "Idempotency-Key: unique-key" \
   -d '{
     "user_id": "123",
     "amount": 200.75,
     "currency": "EUR",
     "type": "BANK_TRANSFER"
   }'
   ```

2. **Get a transaction by ID:**
   ```bash
   curl -X GET http://localhost:8080/api/v1/transaction/1
   ```

3. **List user transactions:**
   ```bash
   curl -X GET "http://localhost:8080/api/v1/transaction/user/123?status=COMPLETED&currency=USD&startDate=2024-01-01T00:00:00&endDate=2024-12-31T23:59:59"
   ```

## Error Handling

- `400 Bad Request`: Invalid request parameters.
- `404 Not Found`: Transaction not found.
- `409 Conflict`: Duplicate idempotency key.
- `500 Internal Server Error`: Unexpected server error.
