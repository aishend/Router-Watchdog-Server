# Router Watchdog Server

Small Spring Boot service for receiving router heartbeat events and showing the latest status of each registered device.

The application keeps heartbeat state in memory. Each device is identified by `deviceId`; when a new heartbeat arrives for the same device, the previous state is replaced.

## Features

- Receives heartbeat events through a REST API.
- Tracks multiple devices by `deviceId`.
- Marks devices as `UP` or `DOWN` based on the latest heartbeat time.
- Serves a simple web dashboard from `/`.
- Shows the last heartbeat age in minutes.

## Tech Stack

- Java 21
- Spring Boot 4
- Maven
- Static HTML, CSS and JavaScript

## Project Structure

```text
api/
  src/main/java/com/routerwatchdog/
    heartbeat/
      controller/        REST endpoints
      dto/               API request models
      HeartbeatState.java
  src/main/resources/
    application.yml
    static/
      index.html
      css/app.css
      js/app.js
```

## Running Locally

From the API directory:

```bash
cd api
./mvnw spring-boot:run
```

By default, the server starts on port `8080`.

Open the dashboard:

```text
http://localhost:8080
```

To use a different port:

```bash
PORT=10000 ./mvnw spring-boot:run
```

## API

### Send Heartbeat

```http
POST /api/v1/heartbeat
Content-Type: application/json
```

Request body:

```json
{
  "deviceId": "router-main",
  "ip": "192.168.1.1",
  "gateway": "192.168.1.254",
  "failures": 0,
  "uptime": 3600
}
```

Response:

```json
{
  "success": true
}
```

### List Device Statuses

```http
GET /api/v1/heartbeat/latest
```

Response:

```json
{
  "devices": [
    {
      "deviceId": "router-main",
      "deviceStatus": "UP",
      "lastReceivedAt": "2026-07-03T10:30:00Z",
      "secondsSinceLastHeartbeat": 12,
      "minutesSinceLastHeartbeat": 1
    }
  ],
  "serverTime": "2026-07-03T10:30:12Z"
}
```

## Status Rules

- `UP`: latest heartbeat was received within the last 30 seconds.
- `DOWN`: latest heartbeat is older than 30 seconds.

## Tests

Run the test suite from the API directory:

```bash
cd api
./mvnw test
```

## Docker

Build the image:

```bash
cd api
docker build -t router-watchdog-api .
```

Run the container:

```bash
docker run --rm -p 10000:10000 -e PORT=10000 router-watchdog-api
```
