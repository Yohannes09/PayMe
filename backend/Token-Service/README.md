# TokenService

## Purpose

This service is responsible for:

- Generating **JWT tokens** used to access internal or external resources.
- Providing the **signing public key** used to validate those tokens.

It does **not** store user state or role-based access. Instead, it focuses on stateless token issuance and signing key management.

---

## Overview

### Authentication Flow

- **Users** receive their JWTs via the **Authentication Service**, which:
    - Requests tokens on the user's behalf.
    - Retains responsibility for revoking access (e.g., deactivating users).
- **Internal services** authenticate using a **bootstrap token** — a long-lived token used to fetch their initial access and refresh tokens.

Token lifetimes and mechanisms vary depending on the recipient:
- **User tokens** are shorter-lived, with roles embedded by the Authentication Service.
- **Internal service tokens** are long-lived, under the assumption of mutual trust.

---

## Token Recipients

- Users (via Authentication Service)
- Internal services (via bootstrap flow)
- (Pluggable for future recipient types)

---

## API Endpoints

### Public Key

- `GET /api/v1/public-key`  
  Returns current and rotated signing public keys, token issuer, and signing algorithm.

### User Token Endpoints

- `POST /api/v1/user/access-refresh-token`
- `POST /api/v1/user/access-token`
- `POST /api/v1/user/refresh-token`

### Service Token Endpoints

- `POST /api/v1/service/access-refresh-token`
- `POST /api/v1/service/access-token`
- `POST /api/v1/service/refresh-token`

---

## Key Components

### `SigningKeyManager`
- Generates and manages signing key pairs (private/public).
- Supports seamless key rotation.

### `PublicKeyController`
- Exposes the current and previous public signing keys.
- Includes metadata: signing algorithm and token issuer.

### `TokenProvider`
- Issues signed JWTs with customizable claims.
- Uses `SigningKeyManager` for cryptographic operations.

### `UserTokenService` & `InternalTokenService`
- Define token claims and structure for their respective recipients.
- User roles and identity claims are injected by the **Authentication Service**.

---

## Notes

- This service does not manage user identity or session state.
- It is designed for **stateless token generation** and **public key distribution**.
- Decoupling user state allows centralized access control through the **Authentication Service**.
- Bootstrap tokens for services are long-lived and used during initialization or recovery scenarios.