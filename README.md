# Aha! Quiz App

A real-time multiplayer quiz application built with Spring Boot, React, Redis, and WebSockets (STOMP). A host creates a room, adds custom questions, and players compete in real time with a live leaderboard.

## Features

- **Room management** — create password-protected rooms with configurable player limits (2–10)
- **Host controls** — add questions, start the game, end the game early
- **Real-time gameplay** — questions delivered and answers collected over WebSocket/STOMP
- **Live leaderboard** — scores calculated and broadcast to all players after each question
- **Cookie-based sessions** — lightweight `ROOM_SESSION` cookie ties players to rooms without requiring login
- **Swagger UI** — interactive API documentation at `/docs`
- **Dockerized** — one-command deployment with Docker Compose (Redis + backend + frontend)

## Tech Stack

| Layer        | Technologies                                                                 |
|--------------|------------------------------------------------------------------------------|
| Backend      | Java 17, Spring Boot 4, Spring Data Redis, Spring WebSocket (STOMP), SpringDoc OpenAPI 3 |
| Frontend     | React 19, TypeScript, Vite, @stomp/stompjs, React Router                    |
| Database     | Redis (in-memory, with TTLs)                                                |
| Infrastructure | Docker, Docker Compose, Nginx (frontend serving)                          |
| Testing      | JUnit 5, Mockito, Testcontainers (Redis), Vitest, React Testing Library     |

## Architecture

```
┌──────────────────┐        REST + Cookie         ┌───────────────────┐         ┌───────────┐
│                  │  ──────────────────────────▶  │                   │ ──────▶ │           │
│   React Client   │                              │   Spring Boot     │         │   Redis   │
│   (Vite + Nginx) │  ◀─────── WebSocket ───────▶ │   Backend         │ ◀────── │           │
│                  │        (STOMP over WS)        │                   │         │           │
└──────────────────┘                               └───────────────────┘         └───────────┘
```

- **REST API** handles room creation, joining, adding questions, and game start/end
- **WebSocket (STOMP)** handles real-time answer submission, question delivery, leaderboard broadcasts, and game events
- **Redis** stores rooms, users, questions, and sessions as hashes (with TTLs), and leaderboards as sorted sets

## Getting Started

### Prerequisites

- **Docker & Docker Compose** (recommended), or
- Java 17+, Node.js 18+, and a running Redis instance

### Run with Docker Compose

```bash
cd aha
docker-compose up --build
```

| Service  | URL                        |
|----------|----------------------------|
| Frontend | http://localhost:5173       |
| Backend  | http://localhost:8080       |
| Swagger  | http://localhost:8080/docs  |

### Run Locally (without Docker)

**1. Start Redis**

```bash
redis-server
```

**2. Start the backend**

```bash
cd aha/backend
./mvnw spring-boot:run
```

**3. Start the frontend**

```bash
cd aha/frontend
npm install
npm run dev
```

The frontend dev server runs at http://localhost:5173 and the backend at http://localhost:8080.

### Environment Variables

| Variable                     | Default              | Description                      |
|------------------------------|----------------------|----------------------------------|
| `SERVER_PORT`                | `8080`               | Backend server port              |
| `SPRING_DATA_REDIS_HOST`     | `localhost`          | Redis host                       |
| `SPRING_DATA_REDIS_PORT`     | `6379`               | Redis port                       |
| `SPRING_DATA_REDIS_PASSWORD` | *(empty)*            | Redis password                   |
| `SPRING_DATA_REDIS_SSL`      | `false`              | Enable Redis SSL                 |
| `ALLOWED_ORIGINS`            | `http://localhost:5173` | CORS / WebSocket allowed origins |
| `COOKIE_SECURE`              | `false`              | Set `Secure` flag on cookies     |
| `SPRINGDOC_SWAGGER_UI_PATH`  | `/docs`              | Swagger UI path                  |

## How It Works

1. **Host creates a room** — picks a topic, sets max players, gets back a room ID and password
2. **Host adds questions** — each question has text, four options, and the index of the correct answer
3. **Players join** — enter the room ID, password, and a display name
4. **Host starts the game** — triggers a countdown; questions are sent one at a time via WebSocket
5. **Players answer** — submit answers over WebSocket within the time limit
6. **Leaderboard updates** — scores are calculated and broadcast after each question
7. **Game ends** — after the final question, or when the host manually ends it

## API Reference

### REST Endpoints

| Method | Endpoint              | Description                   | Auth                   |
|--------|-----------------------|-------------------------------|------------------------|
| GET    | `/`                   | Health check                  | None                   |
| POST   | `/api/rooms`          | Create a room                 | None                   |
| POST   | `/api/rooms/join`     | Join a room                   | None                   |
| POST   | `/api/rooms/questions`| Add questions to a room       | `ROOM_SESSION` cookie (host only) |
| POST   | `/api/rooms/start`    | Start the quiz                | `ROOM_SESSION` cookie (host only) |
| POST   | `/api/rooms/end`      | End the quiz                  | `ROOM_SESSION` cookie (host only) |

### Request / Response Examples

**Create Room** — `POST /api/rooms`

```json
// Request
{ "hostName": "Alice", "topic": "Science", "maxPlayers": 6 }

// Response (201 Created) — also sets ROOM_SESSION cookie
{ "roomId": "abc123", "topic": "Science", "maxPlayers": 6, "password": "x7k9", "hostId": "user-uuid" }
```

**Join Room** — `POST /api/rooms/join`

```json
// Request
{ "roomId": "abc123", "password": "x7k9", "playerName": "Bob" }

// Response (200 OK) — also sets ROOM_SESSION cookie
{ "roomId": "abc123", "topic": "Science", "maxPlayers": 6, "password": "x7k9", "hostId": "user-uuid" }
```

**Add Questions** — `POST /api/rooms/questions`

```json
// Request (requires ROOM_SESSION cookie)
{
  "questions": [
    {
      "questionText": "What is the speed of light?",
      "options": ["300,000 km/s", "150,000 km/s", "450,000 km/s", "600,000 km/s"],
      "correctOptionIndex": 0
    }
  ]
}
```

Full interactive docs are available at `/docs` (Swagger UI) when the backend is running.

### WebSocket (STOMP)

**Connection endpoint:** `ws://localhost:8080/ws-aha`

| Direction | Destination                               | Purpose                    |
|-----------|-------------------------------------------|----------------------------|
| Send      | `/app/room/{roomId}/question`             | Submit an answer           |
| Subscribe | `/topic/room/{roomId}`                    | Room updates (player joined, etc.) |
| Subscribe | `/topic/room/{roomId}/question`           | Receive next question      |
| Subscribe | `/topic/room/{roomId}/game-started`       | Game started notification  |
| Subscribe | `/topic/room/{roomId}/leaderboard`        | Leaderboard after each question |
| Subscribe | `/topic/room/{roomId}/game-ended`         | Game ended notification    |
| Subscribe | `/user/queue/room/{roomId}/answer`        | Per-user answer feedback   |

## Project Structure

```
aha/
├── docker-compose.yml
├── backend/
│   ├── pom.xml
│   ├── dockerfile
│   ├── .env.docker
│   └── src/main/java/com/aha/aha/
│       ├── AhaApplication.java
│       ├── config/
│       │   ├── CorsConfig.java
│       │   ├── RedisConfig.java
│       │   ├── SwaggerConfig.java
│       │   └── websocket/          # WebSocket + STOMP configuration
│       ├── controller/
│       │   ├── Controller.java              # REST endpoints
│       │   └── RoomWebSocketController.java # STOMP message handlers
│       ├── entity/                  # Redis hash entities
│       │   ├── Room.java
│       │   ├── User.java
│       │   ├── Question.java
│       │   └── RoomSession.java
│       ├── service/
│       │   ├── RoomService.java
│       │   ├── UserService.java
│       │   └── websocket/
│       │       ├── GameService.java         # Quiz orchestration
│       │       ├── GameScheduler.java       # Timed question delivery
│       │       └── RoomEventService.java    # Broadcasts room events
│       ├── repository/              # Spring Data Redis repositories
│       ├── request/                 # Request DTOs with validation
│       ├── response/                # Response DTOs
│       └── exception/               # Global error handling
└── frontend/
    ├── package.json
    ├── Dockerfile
    ├── nginx.conf
    └── src/
        ├── config.ts
        ├── components/
        │   ├── StompProvider.tsx     # WebSocket connection manager
        │   ├── RoomCreateForm.tsx
        │   ├── JoinRoomForm.tsx
        │   └── AddQuestionSet.tsx
        ├── pages/
        │   ├── RoomCreationPage.tsx
        │   └── JoinRoomPage.tsx
        └── types/
            └── Question.ts
```

## Redis Data Model

| Entity      | Redis Type  | TTL  | Key Pattern                      |
|-------------|-------------|------|----------------------------------|
| Room        | Hash        | 2h   | `room:{roomId}`                  |
| User        | Hash        | 2h   | `user:{userId}`                  |
| Question    | Hash        | 2h   | `question:{questionId}`          |
| RoomSession | Hash        | 2h   | `room_session:{roomSessionId}`   |
| Leaderboard | Sorted Set  | —    | `room:{roomId}:leaderboard`      |

## Running Tests

**Backend**

```bash
cd aha/backend
./mvnw test
```

**Frontend**

```bash
cd aha/frontend
npm test
```

## Future Enhancements

- AI-generated questions (OpenAI / Anthropic integration)
- User authentication and persistent profiles
- Multiple question types (true/false, image-based)
- Game history and analytics dashboard
- Team / tournament mode
