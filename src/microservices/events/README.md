# events-service (Java, Spring Boot, Kafka)

### Endpoints
- `GET /api/events/health` -> `{"status":"UP"}`
- `POST /api/events/user` (JSON body optional)
- `POST /api/events/payment`
- `POST /api/events/movie`

Each POST publishes a message to Kafka (topic `events`). The built-in consumer reads from the same topic and logs it.

### Compose snippet
```yaml
events-service:
  build:
    context: ./src/microservices/events
    dockerfile: Dockerfile
  container_name: cinemaabyss-events-service
  depends_on:
    - kafka
  ports:
    - "8082:8082"
  networks:
    - cinemaabyss-network
```
