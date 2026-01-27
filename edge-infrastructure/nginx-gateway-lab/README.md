# NGINX Gateway Lab

A containerized NGINX reverse proxy gateway in front of a FastAPI backend, demonstrating modern edge infrastructure patterns, request routing, load balancing, and security headers.

## Overview

```
┌─────────────────────────────────────────────────────────────┐
│                      Host (Linux)                           │
├─────────────────────────────────────────────────────────────┤
│                                                               │
│  Port 8081 (configurable via .env)                           │
│       │                                                       │
│       ▼                                                       │
│  ┌──────────────────────────────────────────────────┐       │
│  │  Docker Network: infra_default                   │       │
│  │                                                   │       │
│  │  ┌──────────────────┐      ┌──────────────────┐ │       │
│  │  │   NGINX (1.25)   │      │  FastAPI (api-N) │ │       │
│  │  │  Port 80         │◄────►│  Port 8000       │ │       │
│  │  │  (reverse proxy)  │      │  (Uvicorn)       │ │       │
│  │  └──────────────────┘      └──────────────────┘ │       │
│  │         │                          ▲             │       │
│  │         │ Route /api/* → upstream │             │       │
│  │         └──────────────────────────┘             │       │
│  │                                                   │       │
│  │  Bind-Mounted Volumes:                            │       │
│  │  • nginx.conf, sites/*, includes/*→ /etc/nginx  │       │
│  │  • logs/ → /var/log/nginx                       │       │
│  └──────────────────────────────────────────────────┘       │
│                                                               │
└─────────────────────────────────────────────────────────────┘
```

**Key Features:**

- ✅ NGINX reverse proxy with JSON structured logging
- ✅ FastAPI backend with health & endpoint tests
- ✅ Horizontal scaling (Docker Compose `--scale api=N`)
- ✅ Hot-reload NGINX configs (bind-mounted, zero downtime)
- ✅ Security headers (X-Frame-Options, X-Content-Type-Options, X-XSS-Protection)
- ✅ Proxy header forwarding (X-Real-IP, X-Forwarded-For, X-Forwarded-Proto)
- ✅ Structured JSON access logs for debugging & monitoring

## Directory Structure

````
nginx-gateway-lab/
├── README.md                          ← This file
├── nginx-gateway-lab-v1/
│   ├── infra/
### 2. Test the Gateway & API

In another terminal, test the endpoints:

```bash
# Gateway root endpoint
curl -v http://localhost:8081/

# API health check (routed through NGINX)
curl -v http://localhost:8081/api/health

# API root
curl -v http://localhost:8081/api/

# API with query params
curl "http://localhost:8081/api/slow?ms=500"

# Check proxy headers
curl -v http://localhost:8081/api/headers
````

**Expected Responses:**

- `GET /` → 200 OK, text: `"nginx gateway is up"`
- `GET /api/health` → 200 OK, JSON: `{"status":"ok","instance":"<container_id>"}`
- `GET /api/` → 200 OK, JSON: `{"message":"Hello from API","instance":"<container_id>"}`
- `GET /api/slow?ms=1000` → 200 OK (after 1 second)

### 3. View Logs (Optional)

```bash
# If running detached, follow NGINX logs
sudo docker compose logs -f nginx

# Or API logs
sudo docker compose logs -f api
```

## Configuration

### Environment Variables (`.env`)

Edit `infra/.env`:

```env
NGINX_IMAGE=nginx:1.25-alpine
NGINX_PORT=8081                 # Host port exposed to localhost
API_PORT=8000                   # Internal container port (Uvicorn)
```

### NGINX Config Files

All NGINX configs are **bind-mounted**—edit them and reload without restarting containers.

| File                          | Purpose                                            |
| ----------------------------- | -------------------------------------------------- |
| `nginx/nginx.conf`            | Worker processes, http block, log format           |
| `nginx/sites/gateway.conf`    | Server blocks, upstream definition, location rules |
| `nginx/includes/logging.conf` | JSON access log configuration                      |
| `nginx/includes/proxy.conf`   | Proxy headers & upstream directives                |
| `nginx/includes/headers.conf` | Security headers (X-Frame-Options, etc.)           |

## Common Tasks

### Scale to Multiple API Replicas

Test load-balancing with 2 API containers:

```bash
cd infra

# Start with 2 API replicas
sudo docker compose up --build --scale api=2 -d

# Round-robin check (should see alternating instance IDs)
for i in {1..6}; do curl -s http://localhost:8081/api/ | jq .instance; done
```

### Edit & Reload NGINX Config

No downtime needed—bind-mounted configs reload live:

```bash
# 1. Edit any config file (e.g., nginx/sites/gateway.conf)
# 2. Validate syntax
sudo docker compose exec nginx nginx -t

# 3. Reload gracefully
sudo docker compose exec nginx nginx -s reload

# 4. Verify
sudo docker compose logs nginx | tail -3
```

### View Access Logs

```bash
# Tail JSON access logs
sudo docker compose exec nginx tail -f /var/log/nginx/access.log

# Pretty-print JSON logs
sudo docker compose exec nginx tail -f /var/log/nginx/access.log | jq .

# Search for specific requests
sudo docker compose exec nginx grep "GET /api" /var/log/nginx/access.log
```

### Stop & Clean Up

```bash
# Stop containers (preserve volumes/images)
sudo docker compose stop

# Remove containers & networks
sudo docker compose down

# Full cleanup (remove everything)
sudo docker compose down -v --rmi all
```

## Troubleshooting

### Port 8081 Already in Use

**Symptom:** `failed to bind host port 0.0.0.0:8081`

**Fix:**

```bash
# Find what's using the port
sudo ss -ltnp | grep :8081

# Option A: Kill the process
sudo kill -9 <PID>

# Option B: Use a different port in infra/.env
# NGINX_PORT=18081
# Then: sudo docker compose down && sudo docker compose up --build
```

### "no configuration file provided: not found"

**Symptom:** `docker compose` command fails

**Fix:** Always run from the `infra/` folder:

```bash
cd nginx-gateway-lab/nginx-gateway-lab-v1/infra
sudo docker compose ps
```

### NGINX Config Syntax Error

**Symptom:** `nginx: [emerg] unknown directive ...`

**Fix:**

```bash
# Validate config
sudo docker compose exec nginx nginx -t

# Common issues:
# • log_format must be in http { } block (not server { })
# • listen [::]:80; (space before brackets)
# • All lines end with ;
```

### API Returns 502 Bad Gateway

**Symptom:** `curl http://localhost:8081/api/` → 502

**Debug:**

```bash
# Check if API container is running
sudo docker compose ps

# Check if API is listening
sudo docker compose logs api | grep Uvicorn

# Test connectivity from NGINX
sudo docker compose exec nginx wget -qO- http://api:8000/health

# Check upstream config
sudo docker compose exec nginx cat /etc/nginx/sites/gateway.conf | grep upstream -A5
```

### Empty or Malformed Logs

**Symptom:** Access logs are empty or plain text (not JSON)

**Fix:**

```bash
# Ensure log_format is in nginx.conf http block
sudo docker compose exec nginx grep "log_format" /etc/nginx/nginx.conf

# Ensure logging.conf includes the format
sudo docker compose exec nginx cat /etc/nginx/includes/logging.conf

# Reload NGINX
sudo docker compose exec nginx nginx -s reload
```

## Architecture

### Network Topology

```
Client
  │
  └─→ localhost:8081 (host)
       │
       └─→ Docker NAT (iptables)
            │
            └─→ infra-nginx-1 (container, 172.18.0.x)
                 │
                 └─→ Upstream lookup (Docker DNS)
                      │
                      └─→ infra-api-1, api-2, ... (containers)
```

- **Bridge Network:** `infra_default`
- **DNS:** Docker embedded resolver; `api` resolves to all API containers
- **Port Binding:** Only NGINX 8081 exposed; API is internal-only
- **Load Balancing:** NGINX round-robins requests to API upstream

### Request Flow

1. Client → `http://localhost:8081/api/health`
2. Host port 8081 → Docker NAT → NGINX container port 80
3. NGINX upstream lookup: `api:8000` (Docker DNS)
4. NGINX proxy_pass → API container (round-robin)
5. Response flows back through NGINX → NAT → Host → Client

### Security Headers

Sent by NGINX on every response:

| Header                   | Value           | Purpose               |
| ------------------------ | --------------- | --------------------- |
| `X-Content-Type-Options` | `nosniff`       | Prevent MIME sniffing |
| `X-Frame-Options`        | `DENY`          | Prevent clickjacking  |
| `X-XSS-Protection`       | `1; mode=block` | Legacy XSS filter     |

### Proxy Headers

NGINX adds headers so API knows the original client:

| Header              | Value                        | Purpose                        |
| ------------------- | ---------------------------- | ------------------------------ |
| `X-Real-IP`         | `$remote_addr`               | Original client IP             |
| `X-Forwarded-For`   | `$proxy_add_x_forwarded_for` | Proxy chain                    |
| `X-Forwarded-Proto` | `$scheme`                    | Original protocol (http/https) |
| `Host`              | `$host`                      | Original Host header           |

### JSON Logging

**Format:**

```json
{
  "time": "2026-01-27T09:40:15+00:00",
  "remote_addr": "172.18.0.1",
  "method": "GET",
  "uri": "/api/health",
  "status": "200",
  "bytes": 41,
  "request_time": 0.001
}
```

**Location:** `infra/logs/access.log` (bind-mounted from container)

## Next Steps

- **Resilience:** Add `proxy_next_upstream` to retry failed backends
- **Health Checks:** Configure NGINX upstream health checks
- **Rate Limiting:** Add `limit_req_zone` for request throttling
- **TLS/SSL:** Enable HTTPS with Let's Encrypt or self-signed certs
- **Monitoring:** Collect metrics with Prometheus; visualize with Grafana
- **CI/CD:** Automate builds with GitHub Actions / GitLab CI
- **Orchestration:** Scale to Docker Swarm or Kubernetes

## References

- [NGINX Docs](https://nginx.org/en/docs/)
- [FastAPI Docs](https://fastapi.tiangolo.com/)
- [Docker Compose Docs](https://docs.docker.com/compose/)
- [Docker Networking](https://docs.docker.com/network/)
- [Uvicorn Server](https://www.uvicorn.org/)
  │ │ │ └── headers.conf ← Security headers (X-\*)
  │ │ └── sites/
  │ │ └── gateway.conf ← Server block, upstreams, locations
  │ ├── apps/
  │ │ └── api/
  │ │ ├── app.py ← FastAPI application
  │ │ ├── Dockerfile ← Python 3.11 + Uvicorn
  │ │ └── requirements.txt ← Dependencies (fastapi, uvicorn)
  │ ├── scripts/ ← Utility scripts (future)
  │ └── docs/ ← Architecture guides (future)

````

## Quick Start

### Prerequisites

- **Docker & Docker Compose v2+** (bundled in Docker Desktop)
- **Linux host** with `sudo` access
- **Port 8081** available (or configure in `.env`)

### 1. Start the Stack

```bash
# Navigate to the infra folder
cd nginx-gateway-lab/nginx-gateway-lab-v1/infra

# Build images and start containers
sudo docker compose up --build

# Or run detached (background):
sudo docker compose up --build -d
sudo docker compose up --build

# Or run detached
sudo docker compose up --build -d

# View logs (optional)
sudo docker compose logs -f
````
