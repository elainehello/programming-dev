import time
import logging
import redis
from app.schemas.job import Job
from app.broker.connection import settings

log = logging.getLogger(__name__)

def backoff_seconds(attempt: int) -> int:
    s = 2**attempt
    return 16 if s > 16 else s

def retry_or_fail(r: redis.Redis, job: Job, error: Exception) -> None:
    if job.retry_count < job.max_retries:
        delay = backoff_seconds(job.retry_count)
        time.sleep(delay)
        updated = job.bump_retry()
        r.lpush(settings.JOBS_QUEUE_NAME, updated.to_bytes())
        log.warning(f"Requeued job{updated.job_id} (retry {updated.retry_count})")
    else:
        payload = job.model_dump()
        payload["error"] = str(error)
        try:
            import orjson
            raw = orjson.dumps(payload)
        except Exception:
            import json
            raw = json.dumps(payload, separators=(",",":")).encode()
        r.lpush(settings.FAILED_QUEUE_NAME, raw)
        log.error(f"Job {job.job_id} failed after {job.retry_count} retries")