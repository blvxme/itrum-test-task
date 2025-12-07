# ITRUM Test Task

To run the application, follow these instructions:

1. Clone this repository:

```shell
git clone git@github.com:blvxme/itrum-test-task.git
```

2. Rename `example.env` to `.env` and modify its content if needed:

```shell
mv example.env .env
```

3. Make sure nothing is running on ports `5432` and `8080`:

```shell
netstat -tuln | grep 5432
netstat -tuln | grep 8080
```

4. Build and start the Docker containers:

```shell
docker compose up --build
```

5. Now you can interact with the application:

- `GET /api/v1/wallets/<UUID>` - get the wallet balance
- `POST /api/v1/wallets` - deposit or withdraw from the wallet

To run the tests, simply execute:

```shell
./mvnw clean verify
```
