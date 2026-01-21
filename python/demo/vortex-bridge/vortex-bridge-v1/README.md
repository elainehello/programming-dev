# Vortex Bridge

## What it does
Redis worker that pops jobs, validates them with Pydantic, retries with exponential backoff, and moves exhausted jobs to a failed queue.

## Prerequisites
- Python 3.11+
- Docker + Docker Compose
- Access to Redis (Sentinel stack exposes `redis-sentinel:6379`; this stack also ships a local Redis on 6380:6379 if you need it).

## Environment
Create `.env` in the project root:
```
REDIS_URL=redis://redis-sentinel:6379/0
JOBS_QUEUE_NAME=jobs:queue
FAILED_QUEUE_NAME=jobs:failed
```

## Setup

- Install deps:

```bash
pip install -r [requirements.txt](http://_vscodecontentref_/0)
```


## Root directory
```bash
python -m app.worker
```

## Generate Jobs
```bash
cd vortex-bridge-v1
python -m app.producer_test
``