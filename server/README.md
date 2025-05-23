# PayMe Backend - Microservices Architecture

This repository contains the backend codebase for **PayMe**, structured as a microservices system.  
Each microservice is designed to perform a specific role and communicates with others through RESTful APIs.  
With plans to adopt more appropriate API architectures (e.g., gRPC, GraphQL, or event-driven patterns) as the application evolves.

---

## Overview

The system is composed of the following core microservices:

- **Authentication Service**: Handles user registration, login, delegating token requests, and cache tokens to be used in the future (avoid calling TokenProvider).
- **Token Provider** *(name subject to change)*: Responsible for creating and signing access and refresh tokens used system-wide (both user and inter-service).
- **User Service**: Manages user profile data and preferences.
- **Account Service**: Manages user accounts, balances, and account types.
- **Transaction Service**: Processes and tracks transactions between accounts.
- **Email Service** *(planned)*: Will send verification emails, notifications, and alerts.

Each service is self-contained, owns its own database, and currently communicates via HTTP. Transition to HTTPS is planned.

---

## Authentication Flow

### 1. **User Registration**
- **Endpoint**: `POST /auth/register` *(subject to final routing)*
- **Flow**:
    - User submits `username`, `email`, and `password`.
    - Authentication Service creates the user and returns `201 Created`.
    - Publishes `USER_REGISTERED` event to Kafka.
    - Email Service and others can subscribe to trigger welcome workflows.

### 2. **User Login**
- **Endpoint**: `POST /auth/login` *(subject to final routing)*
- **Flow**:
    - User submits credentials.
    - Authentication Service validates credentials, role, account status (non-locked, not expired, etc.).
    - If valid:
        - Request access and refresh tokens from the **Token Provider**.
        - Returns tokens and user ID.
        - Publishes `USER_LOGGED_IN` event for logging/auditing.
        - Gateway **caches access token** (protects client from directly handling it—mitigates XSS).
        - Client stores the user ID (used in future requests to map to a cached token).
    - Note: First login may be slower due to downstream service initialization.

---

## First Login Flow

### Initial User Provisioning

When a user logs in for the first time:

- Authentication Service is the only service that initially knows about the user.
- After successful login:
    - Gateway makes requests to all necessary services using the user ID.
    - Each service handles provisioning as needed:
        - **User Service** creates a user profile if one doesn’t exist.
        - **Account Service** creates one or more default accounts for the user.
        - **Transaction Service** returns an empty list (no transactions yet).
- This "lazy initialization" ensures all services are consistent without relying on synchronous orchestration.

---

## Inter-Service Communication

### Kafka Topics
| Topic              | Publisher              | Subscribers                    | Description                             |
|-------------------|------------------------|--------------------------------|-----------------------------------------|
| `USER_REGISTERED` | Authentication Service | Email Service, User Service    | Triggered after successful registration |
| `TRANSACTION_MADE`| Transaction Service    | Email Service, Activity Service| Triggered when a transaction is complete|

*More events may be added as the system grows.*

---

## Security

- Services verify JWTs using a shared **public key** (asymmetric signing).
- Tokens carry necessary claims (user ID, roles, expiry, etc.).
- Role-based access control is enforced per service.
- Tokens are issued through **Token Provider**, ensuring a centralized, secure source of truth.

---

## Future Enhancements

- Support for **OAuth 2.0** and third-party identity providers.
- Rate limiting and abuse detection mechanisms.
- Admin dashboard for system observability and health monitoring.
- HTTPS communication.

---

## Development

- Environment setup and configuration lives in the **`INFRASTRUCTURE/`** directory.


## Links demonstrating flow of other scenarios. 

- Issuing a new AccessToken asynchronously
  https://app.eraser.io/workspace/Zf1kHpWcbAMD4flpnLxE?origin=share