# Tinder Clone Project

## Overview

This project is a Tinder-like application that allows users to register, create and manage profiles, swipe on other user profiles (like or dislike), form mutual matches, and communicate via real-time or asynchronous messaging. It consists of:

- **Backend**: Java Spring Boot REST API
- **Frontend**: Angular SPA (Single-Page Application)

---

## Features

- **User Registration & Authentication**: Users can sign up with email and password, log in, and log out.
- **Profile Management**: Create, view, and update a user profile containing name, age, gender, bio, and location.
- **Swiping & Matching**: Browse other users’ profiles by swiping “like” or “dislike.” A match is formed when two users mutually “like” each other.
- **Messaging**: Once matched, users can send and receive text messages, with chat history persisted in the backend.
- **Likes**: Users can “like” other profiles to express interest. The backend tracks likes to compute matches.
- **Basic Validation & Security**: Passwords are hashed before storing. DTOs and input validation ensure data integrity.

---

## Technology Stack

- **Backend**:
  - Java 17+
  - Spring Boot 3
  - Spring Data JPA (Hibernate)
  - H2 (in-memory) or configurable relational database (PostgreSQL, MySQL, etc.)
  - Spring Security (for future enhancements)
  - Lombok (to reduce boilerplate)
  - MapStruct or custom mappers for DTO conversion
  - JUnit 5 + Mockito (unit and controller tests)

- **Frontend**:
  - Angular 15+
  - TypeScript
  - Angular Router (SPA navigation)
  - Angular HttpClient (REST calls)
  - RxJS (observables, BehaviorSubject)
  - Tailwind CSS (styling)
  - Optional: Material or other UI libraries

---

## Prerequisites

- **Backend**:
  - Java SDK 17+
  - Maven or Gradle
- **Frontend**:
  - Node.js 16+
  - npm or Yarn

---

## Getting Started

### 1. Clone the Repository

```bash
git clone https://github.com/yourusername/tinder-clone.git
cd tinder-clone
```

---

## Backend Setup

1. Navigate to the `backend` directory:
   ```bash
   cd backend
   ```

2. Configure the database:
   - By default, the application uses an in-memory H2 database.
   - To switch to another database (e.g., PostgreSQL), update `application.properties` or `application.yml` with the correct JDBC URL, username, and password.

3. Build and run the Spring Boot application:
   ```bash
   # Using Maven
   mvn clean install
   mvn spring-boot:run

   # Or using Gradle
   ./gradlew clean build
   ./gradlew bootRun
   ```

4. The backend server will start on: `http://localhost:8080/`

---

### Backend API Endpoints

- **User Endpoints**:
  - `POST /user/signup`  
    - Request: JSON body `{ "email": "...", "password": "...", "profile": { ... } }`  
    - Response: `201 Created` + User (password omitted)
  - `POST /user/login`  
    - Request: JSON body `{ "email": "...", "password": "..." }`  
    - Response: `200 OK` + User (password omitted) or `400 Bad Request`
  - `GET /user/all`  
    - Response: `200 OK` + List of all users (DTOs)
  - `GET /user/findbyid/{id}`  
    - Response: `200 OK` + User DTO or `404 Not Found`
  - `GET /user/findbyemail/{email}`  
    - Response: `200 OK` + User DTO or `404 Not Found`
  - `PUT /user/update/{id}`  
    - Request: JSON body updated UserDTO  
    - Response: `200 OK` + User DTO or `404 Not Found`

- **Profile Endpoints** (if separated):
  - `GET /profile/{id}`, `POST /profile`, `PUT /profile/{id}`, etc.

- **Like Endpoints**:
  - `POST /like/addlike`  
    - Request: JSON LikeDTO  
    - Response: `201 Created` + LikeDTO

- **Match Endpoints**:
  - `POST /match/add`  
    - Request: JSON MatchDTO `{ "user1Id": ..., "user2Id": ... }`  
    - Response: `201 Created` + MatchDTO
  - `GET /match/findbyid/{userId}`  
    - Response: `200 OK` + List of MatchDTO
  - `DELETE /match/deletebyid/{matchId}`  
    - Response: `204 No Content`

- **Message Endpoints**:
  - `POST /messages/send`  
    - Request: JSON MessageDTO `{ "content": "...", "senderId": ..., "receiverId": ..., "matchId": ... }`  
    - Response: `200 OK` + MessageDTO
  - `GET /messages/allbymatch/{matchId}`  
    - Response: `200 OK` + List of MessageDTO

---

## Frontend Setup

1. Navigate to the `frontend` directory (Angular project):
   ```bash
   cd frontend
   ```

2. Install dependencies:
   ```bash
   npm install
   # or
   yarn install
   ```

3. Update environment variables (e.g., API base URL) in `src/environments/environment.ts`:
   ```ts
   export const environment = {
     production: false,
     apiUrl: 'http://localhost:8080/user'
   };
   ```

4. Run the Angular development server:
   ```bash
   npm start
   # or
   ng serve
   ```

5. The application will be available at: `http://localhost:4200/`

---

## Angular Services & Models

- **UserService**:  
  Provides methods to sign up, log in, log out, fetch all users, fetch by ID or email, and update user.  
  Stores the current authenticated user in memory using a `BehaviorSubject`.

- **LikeService**:  
  Provides method to send a like to the backend (`/like/addlike`).

- **MatchService**:  
  Provides methods to add a match (`/match/add`), fetch matches for a user (`/match/findbyid/{userId}`), and delete matches.

- **MessageService**:  
  Provides methods to send a message (`/messages/send`) and fetch all messages for a match (`/messages/allbymatch/{matchId}`).

- **Models (TypeScript Interfaces)**:
  - `UserDTO`: `{ id: number; email: string; password?: string; profileName: string; profileAge: number; profileGender: string; profileBio?: string; profileLocation: string; }`
  - `LikeDTO`: `{ id?: number; userId: number; targetUserId: number; }`
  - `MatchDTO`: `{ id?: number; user1Id: number; user2Id: number; }`
  - `MessageDTO`: `{ id?: number; content: string; senderId: number; receiverId: number; matchId: number; timestamp?: string; }`

---

## Database & Entities

- **User**:
  - Fields: `id`, `email` (unique), `password` (hashed), one-to-one `Profile`.
  - Validations: `@NotBlank` on email/password, custom `@ValidPassword` for password strength.

- **Profile**:
  - Fields: `id`, `name`, `age`, `gender`, `bio`, `location`.

- **Like**:
  - Fields: `id`, `sourceUser` (ManyToOne → User), `targetUser` (ManyToOne → User), `timestamp`.

- **Match**:
  - Fields: `id`, `user1` (ManyToOne → User), `user2` (ManyToOne → User), `createdAt`.

- **Message**:
  - Fields: `id`, `content`, `sender` (ManyToOne → User), `receiver` (ManyToOne → User), `match` (ManyToOne → Match), `timestamp`.

Relationships are managed via JPA annotations; DTOs decouple the internal models from API.

---

## Running Tests

### Backend

- **Unit & Controller Tests**:  
  Located under `backend/src/test/java/...`  
  Run with:
  ```bash
  # Maven
  mvn test

  # or Gradle
  ./gradlew test
  ```

Tests cover:
- `UserControllerTest` (GET all, GET by id/email, POST signup/login, PUT update)
- `LikeControllerTest` (POST addLike scenarios)
- `MatchControllerTest` (POST add, GET findById, DELETE delete)
- `MessageControllerTest` (POST send, GET allByMatch)

### Frontend

- **Angular Tests** (unit & integration):  
  Run:
  ```bash
  ng test
  ```

---

## Future Improvements

- **WebSocket Chat**: Implement real-time messaging using STOMP over WebSocket.
- **Third-Party OAuth**: Sign in via Google, Facebook, etc.
- **Image Uploads**: Allow users to upload profile photos (Firebase Storage or AWS S3).
- **Push Notifications**: Notify users of new matches and messages.
- **Enhanced Security**: Integrate JWT tokens, refresh tokens, and role-based access.
- **Pagination & Filtering**: For swiping interface, load profiles in pages and filter by preferences.
- **Mobile-Friendly UI / PWA**: Improve responsive design or convert frontend into a Progressive Web App.
- **Deployment**: Dockerize both backend and frontend, configure CI/CD pipelines.

---

## Contributing

1. Fork the repository.
2. Create a feature branch: `git checkout -b feature/my-feature`.
3. Commit your changes: `git commit -m "Add some feature"`.
4. Push to the branch: `git push origin feature/my-feature`.
5. Submit a Pull Request describing your changes.

---

## Contact

- **Author**: Francesco Maxim  
- **Email**: maaximfrancesco@gmail.com
