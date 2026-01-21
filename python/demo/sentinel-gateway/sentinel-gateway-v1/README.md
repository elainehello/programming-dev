# Sentinel Gateway

A FastAPI-based API gateway that authenticates requests with JWT, proxies to a downstream data service, and caches responses in Redis.

## Prerequisites

- Python 3.11+
- Redis (via Docker: `docker compose up -d`)
- Downstream data service running at `DATA_SERVICE_URL` from .env

## Setup

1. Install deps: `pip install -r requirements.txt`
2. Run Redis: `docker compose up -d`
3. Start gateway: `uvicorn app.main:app --reload --port 8000`

## Configuration

Environment variables are loaded from [.env](.env):

- `REDIS_URL` (e.g., redis://localhost:6379)
- `DATA_SERVICE_URL` (e.g., http://localhost:8001)
- `JWT_SECRET`
- `ALGORITHM` (default HS256)

## API

- `GET /health` — returns Redis status
- `GET /api/v1/{path}` — proxied request with auth, cache, and forward headers
- `GET /dev/generate-token` — dev-only JWT generator

## Auth

Send `Authorization: Bearer <token>`. Tokens must be signed with `JWT_SECRET` and include `sub`.

## Caching

Per-user cache key includes path and query params; entries stored for 60s in Redis.

## Notes

- A `/favicon.ico` handler returns 204 to silence browser requests.
- In production, remove or secure `/dev/generate-token`.
